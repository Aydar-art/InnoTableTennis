declare module '$env/static/public' {
	export const PUBLIC_DEV_SERVER_URL: string;
	export const PUBLIC_PROD_SERVER_URL: string;
}

declare module '$env/static/private' {
	export const PROD_SERVER_URL: string;
	export const DEV_SERVER_URL: string;
}
