<?php

/**
 * Abstract Controller class for all controllers to extend from.
 */
abstract class Controller extends Controller_Core {
	public function __construct() {
		parent::__construct();
		
		$this->session = Session::instance();
	}
	
	/**
	 * Redirects to a view.
	 */
	protected function sendToView($viewName, $title, $data = NULL) {
		if ($data == NULL) {
			$data = array();
		}
		
		$tpl = new View('tpl/main', $data);
		$tpl->title = $title;
		$tpl->header = new View('tpl/header');
		$tpl->footer = new View('tpl/footer');
		
		$tpl->content = new View($viewName, $data);
		
		$tpl->render(TRUE);
	}
	
	/**
	 * Sets the currently logged in user.
	 */
	protected function setLoggedIn($user) {
		$this->session->set('user', $user);
	}
	
	/**
	 * Returns a BOOL value of whether the current user is logged in or not.
	 */
	protected function isLoggedIn() {
		return $this->session->get('user') !== FALSE;
	}
	
	/**
	 * Encodes an object as JSON and outputs it to the browser.
	 */
	protected function outputJson($obj) {
		header('Content-Type: text/plain');
		echo json_encode($obj);
	}
	
	/**
	 * Outputs a JSON error and then stops the script.
	 */
	protected function jsonError($msg) {
		$this->outputJson(array('success' => FALSE, 'errorMsg' => $msg));
		die();
	}
}

?>