<script lang="ts">
	export let onLoginGithub: () => Promise<unknown>;
</script>

<article>
	<hgroup>
		<h1>Train Station Locator</h1>
		<h2>Get access to the list of train stations.</h2>
	</hgroup>
	<button id="github-sign-in" on:click={onLoginGithub}>Sign In with GitHub</button>
</article>
