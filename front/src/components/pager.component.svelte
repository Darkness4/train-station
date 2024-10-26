<script lang="ts">
	interface Props {
		startPage: number;
		pageCount: number;
		page: number;
		goto: (page: number) => unknown;
	}
	let { startPage, pageCount, page, goto }: Props = $props();
	let nextPage = $derived(page + 1);
	let prevPage = $derived(page - 1);

	function goToStart() {
		goto(startPage);
	}

	function goToPrevious() {
		goto(prevPage);
	}

	function goToCurrent() {
		goto(page);
	}

	function goToNext() {
		goto(nextPage);
	}

	function goToLast() {
		goto(pageCount);
	}
</script>

<nav aria-label="pagination" class="flex justify-center px-4">
	<ul class="max-md:hidden">
		<li>
			<button
				class="flex h-12 w-12 items-center justify-center rounded-full"
				disabled={page <= startPage}
				onclick={goToPrevious}><span class="material-symbols-outlined">arrow_back</span></button
			>
		</li>
	</ul>

	<ul class="pagination-list flex grow justify-center">
		{#if page > startPage}
			<li>
				<button
					class="flex h-12 w-12 items-center justify-center rounded-full"
					onclick={goToStart}
					aria-label="Goto page {startPage}">{startPage}</button
				>
			</li>
		{/if}
		{#if prevPage > startPage}
			<li>
				<span>&hellip;</span>
			</li>
			<li>
				<button
					class="flex h-12 w-12 items-center justify-center rounded-full"
					onclick={goToPrevious}
					aria-label="Goto page {prevPage}">{prevPage}</button
				>
			</li>
		{/if}
		<li>
			<button
				class="flex h-12 w-12 items-center justify-center rounded-full"
				onclick={goToCurrent}
				aria-label="Page {page}"
				aria-current="page">{page}</button
			>
		</li>
		{#if nextPage < pageCount}
			<li>
				<button
					class="flex h-12 w-12 items-center justify-center rounded-full"
					onclick={goToNext}
					aria-label="Goto page {nextPage}">{nextPage}</button
				>
			</li>
			<li>
				<span>&hellip;</span>
			</li>
		{/if}
		{#if page < pageCount}
			<li>
				<button
					class="flex h-12 w-12 items-center justify-center rounded-full"
					onclick={goToLast}
					aria-label="Goto page {pageCount}">{pageCount}</button
				>
			</li>
		{/if}
	</ul>

	<ul class="max-md:hidden">
		<li>
			<button
				class="flex h-12 w-12 items-center justify-center rounded-full"
				disabled={page >= pageCount}
				onclick={goToNext}><span class="material-symbols-outlined">arrow_forward</span></button
			>
		</li>
	</ul>
</nav>
