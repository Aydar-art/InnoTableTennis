import type { Writable } from 'svelte/store';
import { writable } from 'svelte/store';
import type { Error } from '$lib/types/types';
import type { ProfileData } from '$lib/types/types.profile';
import type { OverlayInput, OverlayOutput } from '$lib/types/types.popup';

/**
 * Represents the user token store.
 * Stores the user token value.
 */
export const isLeader: Writable<boolean> = writable(false);
export const isAdmin: Writable<boolean> = writable(false);
export const username: Writable<string> = writable('');
export const playerInfo: Writable<ProfileData | undefined> = writable();
// export const isLeader = derived(userToken, ($userToken) => getRoles($userToken).includes('LEADER'));

/**
 * Represents the loaded page store.
 * Stores the title of the loaded page, if any page is loaded.
 */
export const loadedPage: Writable<string | null> = writable(null);

export const errors: Writable<Error[]> = writable([]);

/**
 * Represents the color-scheme store.
 * Stores the color-scheme value.
 */
export const overlayText: Writable<string | null> = writable(null);
export const inputOverlayText: Writable<OverlayInput | null> = writable(null);
export const outputOverlayText: Writable<OverlayOutput | null> = writable(null);
