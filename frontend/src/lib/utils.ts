/**
 * This file contains helper functions for manipulating strings and dates.
 */

const special = [
	'zeroth',
	'first',
	'second',
	'third',
	'fourth',
	'fifth',
	'sixth',
	'seventh',
	'eighth',
	'ninth',
	'tenth',
	'eleventh',
	'twelfth',
	'thirteenth',
	'fourteenth',
	'fifteenth',
	'sixteenth',
	'seventeenth',
	'eighteenth',
	'nineteenth',
	'one hundredth',
];
const deca = ['twent', 'thirt', 'fort', 'fift', 'sixt', 'sevent', 'eight', 'ninet'];

/**
 * Converts a number to string representation (1 - first, 2 - second).
 * @param number - The input number to convert.
 * @returns The converted to string number.
 */
export function stringifyNumber(number: number): string {
	if (number < 20) return special[number];
	if (number === 100) return special[20];
	if (number % 10 === 0) return deca[Math.floor(number / 10) - 2] + 'ieth';
	return deca[Math.floor(number / 10) - 2] + 'y-' + special[number % 10];
}

/**
 * Converts a date to a formatted string in the format "dd.mm.yyyy".
 * @param date - The input date to convert.
 * @returns The formatted date string.
 */
export const convertDateToString = (date: Date): string => {
	date = new Date(date);
	const yyyy = date.getFullYear();
	let mm = String(date.getMonth() + 1); // Months start at 0!
	let dd = String(date.getDate());

	if (+dd < 10) dd = '0' + dd;
	if (+mm < 10) mm = '0' + mm;

	const formattedDate = dd + '.' + mm + '.' + yyyy;
	return formattedDate;
};

/**
 * Converts a date to a formatted string in the format "yyyy-mm-dd".
 * @param date - The input date to convert.
 * @returns The formatted date string.
 */
export const convertDateToStringDash = (date: Date): string => {
	date = new Date(date);
	const yyyy = date.getFullYear();
	let mm = String(date.getMonth() + 1); // Months start at 0!
	let dd = String(date.getDate());

	if (+dd < 10) dd = '0' + dd;
	if (+mm < 10) mm = '0' + mm;

	const formattedDate = yyyy + '-' + mm + '-' + dd;
	return formattedDate;
};

/**
 * Converts a string date in a format "dd-mm-yyyy" to a formatted string in the format"yyyy-mm-dd" .
 * @param initDate - The input date string to convert.
 * @returns The formatted date string.
 */
export function changeDateFormat(initDate: string) {
	if (initDate) {
		const year = Number(initDate.slice(6, 10));
		const month = Number(initDate.slice(3, 5)) - 1;
		const day = Number(initDate.slice(0, 2));
		return convertDateToStringDash(new Date(year, month, day));
	}
	return '';
}

/**
 * Converts a string date in a format "yyyy-mm-dd" to a formatted string in the format"dd.mm.yyyy" .
 * @param initDate - The input date string to convert.
 * @returns The formatted date string.
 */
export function changeDateDottedFormat(initDate: string) {
	if (initDate) {
		const year = Number(initDate.slice(0, 4));
		const month = Number(initDate.slice(5, 7)) - 1;
		const day = Number(initDate.slice(8, 10));
		return convertDateToString(new Date(year, month, day));
	}
	return '';
}

/**
 * Retrieves a shorter name by taking the first name and the last name initial.
 * @param name - The input name to shorten.
 * @returns The shorter version of the name.
 */
export const getShorterName = (name: string): string => {
	const arr = name.split(' ');
	const firstName = arr[0];
	const secondName = arr.splice(1).join(' ');
	return firstName[0] + '. ' + secondName;
};

export function objectToURLSearchParams(obj: {
	[key: string]: FormDataEntryValue | string | null;
}): string {
	const result: {
		[key: string]: string;
	} = {};
	for (const [key, value] of Object.entries(obj)) {
		if (value !== '' && value !== null) result[key] = String(value);
	}

	return new URLSearchParams(result).toString();
}
