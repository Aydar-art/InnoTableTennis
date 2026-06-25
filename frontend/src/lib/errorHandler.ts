import { getExpirationDate, getRoles } from '$lib/server/token';
import { error } from '@sveltejs/kit';

/**
 * This file contains error handling functions for handling API response errors.
 */

/**
 * Handles errors for GET requests.
 * @param response - The response object from the API.
 * @param token - The user token.
 * @throws An error if the response is not successful or unauthorized.
 */
export async function handleGetErrors(response: Response, token: string): Promise<void> {
	if (!response.ok) {
		handleCommonErrors(response, token);

		if (response.status == 403) {
			let errorMessage = 'You are not authorized to see the content of this page.';
			if (token == '') errorMessage += ' Please Log In first!';
			throw error(response.status, { message: errorMessage });
		}

		const errorResponse = await response.json();

		throw new Error(errorResponse.message);
	}
}

/**
 * Handles errors for modify requests.
 * @param response - The response object from the API.
 * @param token - The user token.
 * @throws An error if the response is not successful or unauthorized.
 */
export async function handleModifyErrors(response: Response, token: string): Promise<void> {
	if (!response.ok) {
		handleCommonErrors(response, token);

		if (response.status === 504) {
			throw error(response.status, { message: response.statusText });
		}

		if (response.status == 403) {
			let errorMessage = 'You do not have rights to perform this action.';
			if (token == '') errorMessage += ' Please Log In first!';
			else if (!getRoles(token).includes('LEADER'))
				errorMessage += ' Only club leaders may perform this action.';
			throw new Error(errorMessage);
		}

		const errorResponse = await response.json();
		throw new Error(errorResponse.message);
	}
}

/**
 * Checks the expiration status of the token and performs necessary actions.
 * @param response - The response object from the API.
 * @param token - The user token.
 * @throws An error if the token has expired.
 */
function checkExpiration(response: Response, token: string): void {
	if (response.status == 401) {
		const expDate = getExpirationDate(token);
		if (expDate && expDate < new Date()) {
			throw new Error('Your login session is over. Please log in again!');
		}
	}
}

function handleCommonErrors(response: Response, token: string) {
	checkExpiration(response, token);

	if (response.status == 404) {
		throw new Error('Entity is not found!');
	}
}
