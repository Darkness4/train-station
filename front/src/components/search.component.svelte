<script lang="ts">
	import { preventDefault } from 'svelte/legacy';

	interface Props {
		value: string;
		onConfirm: () => void;
	}
	let { value = $bindable(), onConfirm }: Props = $props();

	function init(el: HTMLElement) {
		el.focus();
	}
</script>

<div id="search" class="max-w-md">
	<form onsubmit={preventDefault(onConfirm)}>
		<input use:init bind:value type="search" class="bg-white" placeholder="Find a station" />
	</form>
</div>
