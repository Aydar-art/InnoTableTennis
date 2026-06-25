<script lang="ts">
	import { enhance } from '$app/forms';
	import Button from '$lib/components/base/Button.svelte';
	import InputTemplate from '$lib/components/base/inputs/InputTemplate.svelte';
	import DeleteIcon from '$lib/components/icons/DeleteIcon.svelte';
	import { alertPopup } from '$lib/client/popup/popup.handler.js';
	import { handleError } from '$lib/client/handleError.js';

	export let form;
	export let data;

	$: if (form?.error) {
		handleError(form.error);
	}
	$: if (data.error) {
		handleError(data.error);
	}

	$: leaders = data.leaders;

	let telegramMessage = '';
	let leaderToPromote = '';
</script>

<svelte:head>
	<meta
		name="description"
		content="Manage admins and send messages to the InnoTableTennis group channel with the Admin Panel at InnoTableTennis Club. Grant administrative privileges and stay connected with the club community."
	/>
	<meta
		name="keywords"
		content="admin panel, InnoTableTennis Club, manage admins, group channel messages, administrative privileges, club community"
	/>
</svelte:head>

<h2>Admin panel</h2>

<div class="wrapper">
	<div class="side-wrapper left">
		<h3>Admins</h3>
		<div class="scrollable">
			{#each leaders as leader}
				<div class="leaders-row">
					<form
						method="POST"
						action="?/demoteLeader"
						use:enhance={async ({ cancel }) => {
							let isConfirmed = await alertPopup(
								`Are you sure that you want to demote leader ${leader.name}?`,
							);
							if (!isConfirmed) {
								cancel();
							}

							return async ({ update }) => {
								await update({ reset: false });
							};
						}}
					>
						<input type="hidden" value={leader.id} name="id" />
						<button aria-label="Demote" class="demote-btn"><DeleteIcon /></button>
					</form>
					<div class="people-list">{leader.name} - @{leader.telegramAlias}</div>
				</div>
			{/each}
		</div>
		<div class="form-holder">
			<h4>Add new admin</h4>
			<form
				class="add-admin"
				method="POST"
				action="?/promoteLeader"
				use:enhance={async ({ cancel }) => {
					let alias = leaderToPromote;
					if (leaderToPromote[0] == '@') alias = leaderToPromote.slice(1);
					let isConfirmed = await alertPopup(
						`Are you sure that you want to promote @${alias} to leaders?`,
					);
					if (!isConfirmed) {
						cancel();
					}

					return async ({ update }) => {
						await update({ reset: false });
						leaderToPromote = '';
					};
				}}
			>
				<InputTemplate
					type="text"
					name="telegramAlias"
					placeholder="Enter alias"
					required={true}
					bind:stringVal={leaderToPromote}
				/>
				<div>
					<input type="hidden" value={leaderToPromote} />
					<Button type="submit">Add</Button>
				</div>
			</form>
		</div>
	</div>
	<div class="side-wrapper right">
		<h3>Bot Message</h3>
		<p class="broadcast-description">
			Here you can write the message that bot will send to all players
		</p>
		<form
			class="broadcast-form"
			method="POST"
			action="?/broadcastMessage"
			use:enhance={async ({ cancel }) => {
				let isConfirmed = await alertPopup('Are you sure that you want to broadcast this message?');
				if (!isConfirmed) {
					cancel();
				}
				return async ({ update }) => {
					await update({ reset: false });
					telegramMessage = '';
				};
			}}
		>
			<div class="textarea-container">
				<InputTemplate
					type="textarea"
					name="message"
					placeholder="Write here the message"
					required={true}
					bind:stringVal={telegramMessage}
				/>
			</div>
			<div class="send-message">
				<div />
				<Button type="submit">Send</Button>
			</div>
		</form>
	</div>
</div>

<style>
	h2 {
		font-size: var(--fontsize-x-large);
		margin: 1.5rem 0 2rem 0;
	}

	h3 {
		font-size: var(--fontsize-large);
		margin-bottom: 1.5rem;
	}

	h4 {
		font-size: 1.2rem;
		margin: 0.75rem 0;
	}

	.wrapper {
		height: 30rem;
		gap: 1.2rem;
		display: grid;
		grid-template-columns: 1fr 1fr;
	}
	.side-wrapper {
		height: 30rem;
		box-shadow: 0px 0px 4px 2px rgba(0, 0, 0, 0.25);
		border-radius: 30px;
		padding: 1.5rem;
	}

	.side-wrapper.left {
		display: grid;
		grid-template-rows: auto minmax(0, 1fr) auto;
		height: 30rem;
	}

	.side-wrapper.right {
		display: grid;
		grid-template-rows: auto auto minmax(0, 1fr);
		height: 30rem;
	}

	.side-wrapper.right .textarea-container {
		margin-bottom: 0.75rem;
	}

	.side-wrapper.right .broadcast-description {
		margin-bottom: 1.5rem;
	}

	.leaders-row {
		display: flex;
		gap: 0.5rem;
		margin: 1rem 0;
	}

	.broadcast-form {
		display: grid;
		grid-template-rows: minmax(0, 1fr) auto;
	}

	.leaders-row:first-of-type {
		margin: 0 0 1rem;
	}
	.demote-btn {
		background: none;
		border: none;
		cursor: pointer;
		padding: 0;
		border: none;
		height: 1em;
		width: 1em;
	}

	.form-holder {
		height: 100%;
	}

	.add-admin {
		width: 100%;
		font-size: var(--fontsize-small);
		display: grid;
		grid-template-columns: minmax(0, 2fr) minmax(0, 1fr);
		gap: 2rem;
	}

	.send-message {
		width: 100%;
		font-size: var(--fontsize-small);
		display: grid;
		grid-template-columns: minmax(0, 2fr) minmax(0, 1fr);
		gap: 2rem;
	}

	@media (max-width: 480px) {
		.wrapper {
			margin: 1rem;
			grid-template-columns: 1fr;
		}
	}
</style>
