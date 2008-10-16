<?php

class Accounts_Controller extends Controller {
	public function __construct() {
		parent::__construct();
		
		if ( ! $this->isLoggedIn() ) {
			url::redirect('auth/login');
			die();
		}
	}
	
	/**
	 * Retrieve all accounts for a user.
	 */
	public function ajax_getAccounts() {
		$user = $this->session->get('user');
		
		$accounts = array();
		
		foreach($user->orderby('name')->accounts as $account) {
			$accounts[] = array(
				'id' => $account->id,
				'name' => $account->name,
				'email_address' => $account->email_address
			);
		}
		
		$this->outputJson(array('accounts' => $accounts));
	}
}

?>