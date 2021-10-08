module.exports = {
	root: true,
	parser: '@typescript-eslint/parser',
	extends: [
		'plugin:import/errors',
		'plugin:import/warnings',
		'plugin:import/typescript',
		'eslint:recommended',
		'plugin:@typescript-eslint/recommended',
		'plugin:@typescript-eslint/recommended-requiring-type-checking'
	],
	plugins: ['svelte3', '@typescript-eslint', 'import'],
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
		ecmaVersion: 2019,
		tsconfigRootDir: __dirname,
		project: ['./tsconfig.json'],
		extraFileExtensions: ['.svelte']
	},
	env: {
		browser: true,
		es2017: true,
		node: true
	},
	rules: {
		'import/no-unresolved': 'error',
		'@typescript-eslint/consistent-type-imports': [
			'error',
			{
				prefer: 'type-imports'
			}
		],
		'import/no-named-as-default-member': 'off'
	}
};
