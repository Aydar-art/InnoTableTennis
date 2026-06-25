import { inputOverlayText, outputOverlayText } from '$lib/client/stores/stores';
import type { OverlayOutput } from '$lib/types/types.popup';
import { get } from 'svelte/store';
async function waitFor<T extends HTMLElement>(selector: string): Promise<T> {
	return new Promise<T>((resolve) => {
		const element = document.querySelector(selector) as T;
		if (element) {
			resolve(element);
		} else {
			const observer = new MutationObserver((mutationsList, observer) => {
				const targetElement = document.querySelector(selector) as T;
				if (targetElement) {
					observer.disconnect();
					resolve(targetElement);
				}
			});
			observer.observe(document.documentElement, { childList: true, subtree: true });
		}
	});
}
function waitForAnswer(item: HTMLFormElement, event: string) {
	return new Promise<OverlayOutput | null>((resolve) => {
		const listener = () => {
			item.removeEventListener(event, listener);
			resolve(get(outputOverlayText));
		};
		item.addEventListener(event, listener);
	}).then((value: OverlayOutput | null) => {
		outputOverlayText.set(null);
		return value;
	});
}
function waitForBackgroundTriggered(item: HTMLButtonElement, event: string) {
	return new Promise<OverlayOutput | null>((resolve) => {
		const listener = () => {
			item.removeEventListener(event, listener);
			resolve(get(outputOverlayText));
		};
		item.addEventListener(event, listener);
	}).then(() => {
		inputOverlayText.set(null);
		outputOverlayText.set(null);
		return null;
	});
}
async function waitForClick() {
	const inputPopup: HTMLFormElement = await waitFor('.fullscreen-overlay');
	const background: HTMLButtonElement = await waitFor('.overlay-background');
	return await Promise.race([
		waitForBackgroundTriggered(background, 'click'),
		waitForAnswer(inputPopup, 'submit'),
	]);
}
export async function alertInputPopup(
	message: string,
	firstName: string,
	secondName: string,
	date?: string,
) {
	inputOverlayText.set({ message, firstName, secondName, date });
	return await waitForClick();
}
