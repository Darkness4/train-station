import fs from 'node:fs';
import { defineConfig, devices } from '@playwright/test';

const authFile = 'playwright/.auth/auth.json';

export default defineConfig({
	webServer: {
		command: 'bun run build && bun run preview',
		port: 4173,
	},
	testMatch: '**/*.e2e.{ts,js}',
	projects: [
		// Setup proect
		!fs.existsSync(authFile)
			? { name: 'setup', testMatch: /.*\.setup\.ts/ }
			: {},

		{
			name: 'chromium',
			use: {
				...devices['Desktop Chrome'],
				// Use prepared auth state.
				storageState: authFile,
			},
			dependencies: !fs.existsSync(authFile) ? ['setup'] : [],
		},

		{
			name: 'firefox',
			use: {
				...devices['Desktop Firefox'],
				// Use prepared auth state.
				storageState: authFile,
			},
			dependencies: !fs.existsSync(authFile) ? ['setup'] : [],
		},
	],
});
