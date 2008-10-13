<?php

class Attachments_Controller extends Controller {
	public function __construct() {
		parent::__construct();
		
		if ( ! $this->isLoggedIn() ) {
			url::redirect('auth/login');
			die();
		}
	}
	
	public function download($messageId, $attachmentId) {
		$att = ORM::factory('attachment')->where('message_id', $messageId)->where('id', $attachmentId)->find();
		
		download::force($att->name, $att->data);
	}
}

?>