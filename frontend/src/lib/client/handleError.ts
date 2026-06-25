import { errors } from '$lib/client/stores/stores';
import type { Error } from '$lib/types/types';
// import type { Error } from '$lib/types/types';

export function handleError(errorMessage: string) {
	const error: Error = { message: errorMessage };

	if (error.message === 'Validation failed. Constraints: Score is not specified in format x:x') {
		error.message = 'Score is not specified in format number:number. Please rewrite score format!';
	} else if (
		error.message ===
		'Validation failed. Constraints: The match date must be within the tournament`s start and end dates'
	) {
		error.message = 'The match date must be within the tournament`s start and end dates';
	} else if (
		error.message ===
		'Validation failed. Constraints: No tournament with such title was found or it is finished'
	) {
		error.message = 'No tournament with such title was found or it is finished';
	} else if (
		error.message === 'Validation failed. Constraints: Scores of match players must be different'
	) {
		error.message = 'Scores of match players must be different';
	} else if (
		error.message === 'Validation failed. Constraints: Players` names in match must be different'
	) {
		error.message = 'Players` names in match must be different';
	} else if (
		error.message ===
		'Validation failed. The following errors occurred: Date must be in format dd.MM.yyyy'
	) {
		error.message = 'Date must be in format dd.mm.yyyy';
	}

	errors.update((errors) => [...errors, error]);

	setTimeout(() => {
		errors.update((errors) => errors.slice(0, errors.length - 1));
	}, 5000);
}
