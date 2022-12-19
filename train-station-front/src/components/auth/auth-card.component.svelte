<script lang="ts">
	export let onLoginGithub: () => Promise<unknown>;
</script>

<article class="grid">
	<div>
		<hgroup>
			<h1>Sign in</h1>
			<h2>Get access to the list of train stations.</h2>
		</hgroup>
		<button on:click={onLoginGithub}>Sign In with GitHub</button>
	</div>
	<div style="background-image: '/alessio-soggetti-8jeWeKdygfk-unsplash-1000x1200.jpg;' " />
</article>
