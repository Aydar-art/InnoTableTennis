<script lang="ts">
	export let disabled = false;
	export let width = '3.75rem';
	export let height = '3.75rem';
	export let enabledColor = 'var(--secondary-color)';
	export let disabledColor = 'var(--secondary-bg-color)';
</script>

<svg
	style="width:{width}; height:{height}"
	viewBox="0 0 60 60"
	fill="none"
	xmlns="http://www.w3.org/2000/svg"
>
	<rect
		style="width:{width}; height:{height}"
		rx="5"
		fill={disabled ? disabledColor : enabledColor}
	/>
	<path
		fill-rule="evenodd"
		clip-rule="evenodd"
		d="M11.6667 20V16.6667H21.6667V20H11.6667ZM11.6667 43.3334V40H48.3334V43.3334H11.6667ZM11.6667 31.6667V28.3334H35.0001V31.6667H11.6667Z"
		fill="var(--main-color)"
	/>
</svg>
