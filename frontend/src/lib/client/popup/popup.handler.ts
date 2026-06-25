import { overlayText } from '$lib/client/stores/stores';

function getPromiseFromEvent(item: HTMLDivElement | HTMLButtonElement, event: string) {
	return new Promise<void>((resolve) => {
		const listener = () => {
			item.removeEventListener(event, listener);
			resolve();
		};
		item.addEventListener(event, listener);
	}).then(() => {
		return item.classList.item(0);
	});
}

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

async function waitForClick() {
	const yesButton: HTMLDivElement = await waitFor('.overlay-button-yes');
	const noButton: HTMLDivElement = await waitFor('.overlay-button-no');
	const background: HTMLButtonElement = await waitFor('.overlay-background');
	return await Promise.race([
		getPromiseFromEvent(yesButton, 'click'),
		getPromiseFromEvent(noButton, 'click'),
		getPromiseFromEvent(background, 'click'),
	]);
}

/**
 * Calls a popup for confirmation.
 * @param message text shown in popup.
 * @return true - user clicked 'Yes', otherwise - false.
 */
export async function alertPopup(message: string) {
	overlayText.set(message);
	return (await waitForClick()) == 'overlay-button-yes';
}
