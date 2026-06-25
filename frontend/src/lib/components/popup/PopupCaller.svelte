<script lang="ts">
	import { overlayText } from '$lib/client/stores/stores';
	import Popup from '$lib/components/popup/Popup.svelte';
	import { inputOverlayText } from '$lib/client/stores/stores.js';
	import InputPopup from '$lib/components/popup/InputPopup.svelte';
</script>

<div class="popup-container">
	{#if $overlayText != null}
		<Popup popupText={$overlayText} />
	{/if}
	{#if $inputOverlayText != null}
		<InputPopup
			popupText={$inputOverlayText.message}
			firstPlayer={$inputOverlayText.firstName}
			secondPlayer={$inputOverlayText.secondName}
			date={$inputOverlayText.date}
		/>
	{/if}
</div>
