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
				'email_address' => $account->email_address,
				'host_name' => $account->host_name,
				'port' => $account->port,
				'username' => $account->username,
				'password' => $account->password
			);
		}
		
		$this->outputJson(array('success' => TRUE, 'accounts' => $accounts));
	}
	
	/**
	 * Saves an account.
	 */
	public function ajax_saveAccount() {
		$user = $this->session->get('user');
		
		$id = $this->input->post('id');
		
		if (! empty($id) ) {
			$account = ORM::factory('account', $id);
			
			if ($user->id != $account->user_id) {
				$this->jsonError('Authorisation failure');
			}
		}
		else {
			$account = new Account_Model();
		}

		$account->user_id = $user->id;
		$account->name = $this->input->post('name');
		$account->email_address = $this->input->post('email_address');
		$account->host_name = $this->input->post('host_name');
		$account->port = $this->input->post('port');
		$account->username = $this->input->post('username');
		$account->password = $this->input->post('password');
		
		if (! is_numeric($account->port) ) {
			$account->port = 110;
		}
		
		try {
			$account->save();
			
			$this->outputJson(array('success' => TRUE));
		}
		catch (Exception $e) {
			$this->jsonError($e->getMessage());
		}
	}
	
	/**
	 * Deletes an account.
	 */
	public function ajax_deleteAccount() {
		$id = $this->input->post('id');
		
		$user = $this->session->get('user');
		
		if (! empty($id) ) {
			try {
				$account = ORM::factory('account', $id);
				
				if ($user->id != $account->user_id) {
					$this->jsonError('Authorisation failure');
				}
				
				$account->delete();
				
				$this->outputJson(array('success' => TRUE));
			}
			catch (Exception $e) {
				$this->jsonError($e->getMessage());
			}
		}
		else {
			$this->jsonError('No account ID specified');
		}
	}
}

?>