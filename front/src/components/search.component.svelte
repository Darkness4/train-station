<script lang="ts">
	export let value: string;
	export let onConfirm: () => void;

	function init(el: HTMLElement) {
		el.focus();
	}
</script>

<div id="search" class="max-w-md">
	<form on:submit|preventDefault={onConfirm}>
		<input use:init bind:value type="search" class="bg-white" placeholder="Find a station" />
	</form>
</div>
