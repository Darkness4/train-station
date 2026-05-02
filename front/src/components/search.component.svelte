<script lang="ts">
	interface Props {
		value: string;
		onConfirm: (event: SubmitEvent & { currentTarget: EventTarget & HTMLFormElement }) => void;
	}
	let { value = $bindable(), onConfirm }: Props = $props();

	function init(el: HTMLElement) {
		el.focus();
	}
</script>

<div id="search" class="max-w-md">
	<form onsubmit={onConfirm}>
		<input use:init bind:value type="search" class="bg-white" placeholder="Find a station" />
	</form>
</div>
