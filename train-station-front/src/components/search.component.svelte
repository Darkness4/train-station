<script lang="ts">
	export let value: string;
	export let onConfirm: () => void;
</script>

<section id="search" class="section">
	<form on:submit|preventDefault={onConfirm}>
		<div class="field has-addons">
			<div class="control is-flex-grow-1">
				<input bind:value class="input" type="text" placeholder="Find a station" />
			</div>
			<div class="control">
				<button type="submit" class="button is-info">Search</button>
			</div>
		</div>
	</form>
</section>
