module.exports = {
	root: true,
	parser: '@typescript-eslint/parser',
	extends: [
		'prettier',
		'plugin:import/errors',
		'plugin:import/warnings',
		'plugin:import/typescript',
		'eslint:recommended',
		'plugin:@typescript-eslint/recommended',
		'plugin:prettier/recommended'
	],
	plugins: ['svelte3', '@typescript-eslint', 'prettier', 'import', 'simple-import-sort'],
	ignorePatterns: ['*.cjs'],
	overrides: [{ files: ['*.svelte'], processor: 'svelte3/svelte3' }],
	settings: {
		'svelte3/typescript': () => require('typescript'),
		'import/resolver': {
			typescript: {}
		}
	},
	parserOptions: {
		sourceType: 'module',
		ecmaVersion: 2019
	},
	env: {
		browser: true,
		es2017: true,
		node: true
	},
	rules: {
		'import/no-unresolved': 'error',
		'simple-import-sort/imports': 'error',
		'simple-import-sort/exports': 'error',
		'@typescript-eslint/consistent-type-imports': [
			'error',
			{
				prefer: 'type-imports'
			}
		],
		'prettier/prettier': 'error',
		'import/no-named-as-default-member': 'off'
	}
};
