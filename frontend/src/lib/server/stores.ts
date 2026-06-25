import type { Writable } from 'svelte/store';
import { writable } from 'svelte/store';

/**
 * Represents the user token store.
 * Stores the user token value.
 */
export const userToken: Writable<string> = writable('');
