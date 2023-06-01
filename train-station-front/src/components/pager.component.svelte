<script lang="ts">
	export let startPage: number;
	export let pageCount: number;
	export let page: number;
	export let goto: (page: number) => unknown;
	let nextPage: number;
	let prevPage: number;
	$: nextPage = page + 1;
	$: prevPage = page - 1;

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

<nav aria-label="pagination" class="px-4 flex justify-center">
	<ul class="max-md:hidden">
		<li>
			<button
				class="rounded-full w-12 h-12 flex items-center justify-center"
				disabled={page <= startPage}
				on:click={goToPrevious}><span class="material-symbols-outlined">arrow_back</span></button
			>
		</li>
	</ul>

	<ul class="pagination-list grow flex justify-center">
		{#if page > startPage}
			<li>
				<button
					class="rounded-full w-12 h-12 flex items-center justify-center"
					on:click={goToStart}
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
					class="rounded-full w-12 h-12 flex items-center justify-center"
					on:click={goToPrevious}
					aria-label="Goto page {prevPage}">{prevPage}</button
				>
			</li>
		{/if}
		<li>
			<button
				class="rounded-full w-12 h-12 flex items-center justify-center"
				on:click={goToCurrent}
				aria-label="Page {page}"
				aria-current="page">{page}</button
			>
		</li>
		{#if nextPage < pageCount}
			<li>
				<button
					class="rounded-full w-12 h-12 flex items-center justify-center"
					on:click={goToNext}
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
					class="rounded-full w-12 h-12 flex items-center justify-center"
					on:click={goToLast}
					aria-label="Goto page {pageCount}">{pageCount}</button
				>
			</li>
		{/if}
	</ul>

	<ul class="max-md:hidden">
		<li>
			<button
				class="rounded-full w-12 h-12 flex items-center justify-center"
				disabled={page >= pageCount}
				on:click={goToNext}><span class="material-symbols-outlined">arrow_forward</span></button
			>
		</li>
	</ul>
</nav>
