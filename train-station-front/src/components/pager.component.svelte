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

<nav aria-label="pagination">
	<ul>
		<button disabled={page <= startPage} on:click={goToPrevious}>Previous</button>
	</ul>

	<ul class="pagination-list">
		{#if page > startPage}
			<li>
				<button on:click={goToStart} aria-label="Goto page {startPage}">{startPage}</button>
			</li>
		{/if}
		{#if prevPage > startPage}
			<li>
				<span>&hellip;</span>
			</li>
			<li>
				<button on:click={goToPrevious} aria-label="Goto page {prevPage}">{prevPage}</button>
			</li>
		{/if}
		<li>
			<button on:click={goToCurrent} aria-label="Page {page}" aria-current="page">{page}</button>
		</li>
		{#if nextPage < pageCount}
			<li>
				<button on:click={goToNext} aria-label="Goto page {nextPage}">{nextPage}</button>
			</li>
			<li>
				<span>&hellip;</span>
			</li>
		{/if}
		{#if page < pageCount}
			<li>
				<button on:click={goToLast} aria-label="Goto page {pageCount}">{pageCount}</button>
			</li>
		{/if}
	</ul>
	<ul>
		<button disabled={page >= pageCount} on:click={goToNext}>Next page</button>
	</ul>
</nav>
