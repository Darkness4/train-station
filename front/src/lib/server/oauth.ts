import { CodeChallengeMethod, OAuth2Client, type OAuth2Tokens } from "arctic";
import jwt from "jsonwebtoken";
import jwksClient from "jwks-rsa";
import { env } from "$env/dynamic/private";

class OidcClient {
	constructor(
		readonly discoveryUrl: string,
		readonly oauth2Client: OAuth2Client,
		readonly scopes: string[],
		readonly issuer: string,
		readonly authorizationEndpoint: string,
		readonly tokenEndpoint: string,
		readonly revocationEndpoint: string | undefined,
		readonly jwksClient: jwksClient.JwksClient,
	) {}

	static async create(
		discoveryUrl: string,
		clientId: string,
		clientSecret: string,
		redirectUri: string,
		scopes: string[],
	): Promise<OidcClient> {
		const response = await fetch(discoveryUrl);

		if (!response.ok) {
			throw new Error(
				`Failed to fetch OIDC discovery document: ${response.statusText}`,
			);
		}

		const config = await response.json();

		if (
			config.grant_types_supported?.includes("authorization_code") === false
		) {
			throw new Error(
				"OIDC client does not support authorization_code grant type",
			);
		}

		if (config.grant_types_supported?.includes("refresh_token") === false) {
			throw new Error("OIDC client does not support refresh_token grant type");
		}

		if (config.code_challenge_methods_supported?.includes("S256") === false) {
			throw new Error(
				"OIDC client does not support S256 code challenge method",
			);
		}

		if (!config.issuer) {
			throw new Error("OIDC client missing issuer");
		}

		if (!config.authorization_endpoint) {
			throw new Error("OIDC client missing authorization_endpoint");
		}

		if (!config.token_endpoint) {
			throw new Error("OIDC client missing token_endpoint");
		}

		if (!config.jwks_uri) {
			throw new Error("OIDC client missing jwks_uri");
		}

		return new OidcClient(
			discoveryUrl,
			new OAuth2Client(clientId, clientSecret, redirectUri),
			scopes,
			config.issuer,
			config.authorization_endpoint,
			config.token_endpoint,
			config.revocation_endpoint,
			jwksClient({
				jwksUri: config.jwks_uri,
				timeout: 30000,
				cache: true,
				cacheMaxAge: 3600,
				rateLimit: true,
			}),
		);
	}

	createAuthorizationURLWithPKCE(state: string, codeVerifier: string): URL {
		return this.oauth2Client.createAuthorizationURLWithPKCE(
			this.authorizationEndpoint,
			state,
			CodeChallengeMethod.S256,
			codeVerifier,
			this.scopes,
		);
	}

	validateAuthorizationCode(
		code: string,
		codeVerifier: string,
	): Promise<OAuth2Tokens> {
		return this.oauth2Client.validateAuthorizationCode(
			this.tokenEndpoint,
			code,
			codeVerifier,
		);
	}

	refreshAccessToken(refreshToken: string): Promise<OAuth2Tokens> {
		return this.oauth2Client.refreshAccessToken(
			this.tokenEndpoint,
			refreshToken,
			this.scopes,
		);
	}

	async revokeToken(accessToken: string): Promise<void> {
		if (this.revocationEndpoint !== undefined) {
			return this.oauth2Client.revokeToken(
				this.revocationEndpoint,
				accessToken,
			);
		}
	}

	getKey: jwt.GetPublicKeyOrSecret = (header, callback) => {
		this.jwksClient.getSigningKey(header.kid, (_err, key) =>
			callback(null, key?.getPublicKey()),
		);
	};

	verifyIdToken(idToken: string): Promise<jwt.JwtPayload> {
		return new Promise((resolve, reject) => {
			jwt.verify(
				idToken,
				this.getKey,
				{
					issuer: this.issuer,
					audience: this.oauth2Client.clientId,
				},
				(err, decoded) => {
					if (err) {
						reject(err);
					} else {
						resolve(decoded as jwt.JwtPayload);
					}
				},
			);
		});
	}
}

let instance: OidcClient | null = null;

export const getOidcClient = async () => {
	if (instance) return instance;

	if (!env.OIDC_DISCOVERY_URL) {
		throw new Error("Missing OIDC_DISCOVERY_URL");
	}
	if (!env.CLIENT_ID) {
		throw new Error("Missing CLIENT_ID");
	}
	if (!env.CLIENT_SECRET) {
		throw new Error("Missing CLIENT_SECRET");
	}
	if (!env.OAUTH_REDIRECT_URI) {
		throw new Error("Missing OAUTH_REDIRECT_URI");
	}

	instance = await OidcClient.create(
		env.OIDC_DISCOVERY_URL ?? "",
		env.CLIENT_ID ?? "",
		env.CLIENT_SECRET ?? "",
		env.OAUTH_REDIRECT_URI ?? "",
		env.OAUTH_SCOPES?.split(" ") ?? ["openid", "profile", "offline_access"],
	);
	return instance;
};
