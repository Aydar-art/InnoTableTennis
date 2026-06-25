<script lang="ts">
	export let checked: true | false;
	export let label: string;
	export let chosenId;
	export let editData;
	export let mode;

	function changeState() {
		checked = checked ? false : true;
		chosenId = -1;
		if (checked) {
			mode = 'add';
		} else {
			mode = 'view';
		}
		editData = {};
	}
</script>

<label class="toggle">
	<input
		type="checkbox"
		id="check"
		class="toggle-checkbox"
		on:click={changeState}
		value="checkbox"
	/>
	<div class="toggle-switch" />
</label>
<span class="toggle-label">{label}</span>

<style>
	.toggle {
		cursor: pointer;
		display: inline-block;
	}

	.toggle-switch {
		display: inline-block;
		border: 4px solid var(--secondary-color);
		border-radius: 2em;
		width: 2.1em;
		height: 1em;
		position: relative;
		vertical-align: middle;
		transition: background 0.25s;
	}
	.toggle-switch:before,
	.toggle-switch:after {
		content: '';
	}
	.toggle-switch:before {
		display: block;
		background: var(--secondary-color);
		border-radius: 1em;
		width: 0.8em;
		height: 0.8em;
		position: absolute;
		top: 0.1em;
		left: 0.1em;
		transition: all 0.2s ease;
	}
	.toggle-checkbox:checked + .toggle-switch:before {
		left: 1.2em;
	}

	.toggle-checkbox {
		position: absolute;
		visibility: hidden;
	}

	.toggle-label {
		margin-left: 5px;
		position: relative;
		top: 2px;
	}
</style>
