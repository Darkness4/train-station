import { test } from '@playwright/test';

test.use({
	storageState: 'playwright/.auth/auth.json'
});

test('find a station', async ({ page }) => {
	await page.goto('./stations');
	await page.getByPlaceholder('Find a station').click();
	await page.getByPlaceholder('Find a station').fill('Gardanne');
	await page.getByPlaceholder('Find a station').press('Enter');
	await page
		.getByRole('article')
		.filter({ hasText: 'Gardanne' })
		.getByRole('button', { name: 'Details' })
		.click();
	page.getByRole('heading', { name: 'Gardanne' });
});

test('favorite a station', async ({ page }) => {
	await page.goto('./stations');
	await page.getByPlaceholder('Find a station').click();
	await page.getByPlaceholder('Find a station').fill('Abancourt');
	await page.getByPlaceholder('Find a station').press('Enter');
	await page
		.getByRole('article')
		.filter({ hasText: 'Abancourt a2d2ff1fa57a761dffcb461d3a0c9e3d55a57b14' })
		.getByRole('button', { name: 'Favorite' })
		.click();
});
