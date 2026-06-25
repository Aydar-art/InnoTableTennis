export async function load({ cookies }) {
	const token = cookies.get('userToken') || '';
	const isAuthorized = Boolean(token);

	return { isAuthorized };
}
