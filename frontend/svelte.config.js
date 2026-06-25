import adapter from '@sveltejs/adapter-node';
import sveltePreprocess from 'svelte-preprocess';

const dev = process.env.NODE_ENV === 'development';

/** @type {import('@sveltejs/kit').Config} */
const config = {
	// Consult https://kit.svelte.dev/docs/integrations#preprocessors
	// for more information about preprocessors
	preprocess: sveltePreprocess(),

	kit: {
		adapter: adapter(),
	},
};

export default config;
