<script lang="ts">
	export let startPage: number;
	export let pageCount: number;
	export let page: number;
	export let goto: (page: number) => void;
	$: nextPage = page + 1;
	$: prevPage = page - 1;

	function goToStart() {
		goto(startPage);
		page = startPage;
	}

	function goToPrevious() {
		goto(prevPage);
		page = prevPage;
	}

	function goToCurrent() {
		goto(page);
		page = page;
	}

	function goToNext() {
		goto(nextPage);
		page = nextPage;
	}

	function goToLast() {
		goto(pageCount);
		page = pageCount;
	}
</script>

<nav class="pagination" role="navigation" aria-label="pagination">
	<button disabled={page <= startPage} on:click={goToPrevious} class="pagination-previous"
		>Previous</button
	>
	<button disabled={page >= pageCount} on:click={goToNext} class="pagination-next">Next page</button
	>
	<ul class="pagination-list">
		{#if page > startPage}
			<li>
				<button on:click={goToStart} class="pagination-link" aria-label="Goto page {startPage}"
					>{startPage}</button
				>
			</li>
		{/if}
		{#if prevPage > startPage}
			<li>
				<span class="pagination-ellipsis">&hellip;</span>
			</li>
			<li>
				<button on:click={goToPrevious} class="pagination-link" aria-label="Goto page {prevPage}"
					>{prevPage}</button
				>
			</li>
		{/if}
		<li>
			<button
				on:click={goToCurrent}
				class="pagination-link is-current"
				aria-label="Page {page}"
				aria-current="page">{page}</button
			>
		</li>
		{#if nextPage < pageCount}
			<li>
				<button on:click={goToNext} class="pagination-link" aria-label="Goto page {nextPage}"
					>{nextPage}</button
				>
			</li>
			<li>
				<span class="pagination-ellipsis">&hellip;</span>
			</li>
		{/if}
		{#if page < pageCount}
			<li>
				<button on:click={goToLast} class="pagination-link" aria-label="Goto page {pageCount}"
					>{pageCount}</button
				>
			</li>
		{/if}
	</ul>
</nav>
