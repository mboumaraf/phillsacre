<?= form::open() ?>

<fieldset>
	<legend>Login</legend>
	
	<table>
		<tr>
			<td><?= form::label('username', 'Username') ?></td>
			<td><?= form::input('username', $form['username']) ?></td>
		</tr>
		<tr>
			<td><?= form::label('password', 'Password') ?></td>
			<td><?= form::password('password', '') ?></td>
		</tr>
		<tr>
			<td colspan="2" align="right">
				<?= form::submit('action', 'Login') ?>
			</td>
		</tr>
	</table>
	
</fieldset>

<?= form::close() ?>