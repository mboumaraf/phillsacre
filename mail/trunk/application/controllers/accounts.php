<?php

class Accounts_Controller extends Controller {
	public function __construct() {
		parent::__construct();
		
		if ( ! $this->isLoggedIn() ) {
			url::redirect('auth/login');
			die();
		}
	}
	
	public function add() {
		
	}
	
	public function view() {
	
	}
}

?>