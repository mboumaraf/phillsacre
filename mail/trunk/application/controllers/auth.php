<?php

/**
 * Class to handle authentication.
 */
class Auth_Controller extends Controller {
	public function login() {
		$form = array('username' => '', 'password' => '');
		$errors = array();
		
		if ($_POST) {
			$post = new Validation($_POST);
			$form = arr::overwrite($form, $post->as_array());
			
			$post->add_rules('username', 'required');
			$post->add_rules('password', 'required');
			
			if ($post->validate()) {
				$auth = new AuthService();
				try {
					$user = $auth->authenticateUser($form['username'], $form['password']);
					
					$this->setLoggedIn($user);
					
					url::redirect('mail/home');
					return;
				}
				catch (Exception $e) {
					$post->add_error('username', 'invalid');
				}
			}
			
			$errors = $post->errors('validation');
		}
		
		$this->sendToView('auth/login', 'Login', array('form' => $form, 'errors' => $errors));
	}
	
	public function logout() {
		$this->session->destroy();
		
		url::redirect('auth/login');
	}
}

?>