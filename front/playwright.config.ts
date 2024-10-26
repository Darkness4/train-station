import { PlaywrightTestConfig, devices } from '@playwright/test';
import fs from 'fs';

const authFile = 'playwright/.auth/auth.json';

const config: PlaywrightTestConfig = {
	webServer: {
		command: 'bun run build && bun run preview',
		port: 4173
	},
	testDir: 'tests',
	projects: [
		// Setup proect
		!fs.existsSync(authFile) ? { name: 'setup', testMatch: /.*\.setup\.ts/ } : {},

		{
			name: 'chromium',
			use: {
				...devices['Desktop Chrome'],
				// Use prepared auth state.
				storageState: authFile
			},
			dependencies: !fs.existsSync(authFile) ? ['setup'] : []
		},

		{
			name: 'firefox',
			use: {
				...devices['Desktop Firefox'],
				// Use prepared auth state.
				storageState: authFile
			},
			dependencies: !fs.existsSync(authFile) ? ['setup'] : []
		}
	]
};

export default config;
