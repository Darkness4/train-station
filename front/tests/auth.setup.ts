import { test } from '@playwright/test';

const authFile = 'playwright/.auth/user.json';

if (!process.env.AUTH_USERNAME) throw new Error('AUTH_USERNAME env var not defined');
if (!process.env.AUTH_PASSWORD) throw new Error('AUTH_PASSWORD env var not defined');

test('can login', async ({ page }) => {
	await page.goto('./');

	await page.getByRole('button', { name: 'Sign In with GitHub' }).click();
	await page.getByLabel('Username or email address').click();
	await page.getByLabel('Username or email address').fill(process.env.AUTH_USERNAME ?? '');
	await page.getByLabel('Password').click();
	await page.getByLabel('Password').fill(process.env.AUTH_PASSWORD ?? '');
	await page.getByRole('button', { name: 'Sign in' }).click();
	await page.waitForURL('./stations');

	await page.context().storageState({ path: authFile });
});
