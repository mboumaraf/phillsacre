<?php

class Mail_Controller extends Controller {
	public function __construct() {
		parent::__construct();
		
		if ( ! $this->isLoggedIn() ) {
			url::redirect('auth/login');
			die();
		}
	}
	
	public function home() {
		$view = new View('mail/home');
		$view->render(TRUE);
	}
	
	public function ajax_getMessages() {
		$result = ORM::factory('message')->find_all();
		
		$results = array();
		
		foreach ($result as $message) {
			
			$results[] = array(
				'id' => $message->id,
				'account_id' => $message->account_id,
				'sender' => htmlentities($message->sender),
				'received_date' => $message->received_date,
				'subject' => htmlentities($message->subject),
				'read' => $message->read,
				'attachments' => sizeof($message->attachments)
			);
		}
		
		header('Content-Type: text/x-json');
		echo json_encode(array('messages' => $results));
	}
	
	public function ajax_getMessageBody() {
		$msgid = $this->input->get('id');
		
		$message = ORM::factory('message')->find($msgid);
		
		$attachments = array();
		if (sizeof($message->attachments) > 0) {
			foreach ($message->attachments as $att) {
				$attachments[] = array(
					'id' => $att->id,
					'name' => $att->name,
					'size' => $att->size,
					'mime_type' => $att->mime_type
				);
			}
		}
		
		$result = array(
			'body' => $message->type == 'html' ? $message->text : nl2br(htmlentities($message->text)), 
			'attachments' => $attachments, 
			'type' => $message->type
		);
		
		//header('Content-Type: text/x-json');
		echo json_encode($result);
	}
	
	public function ajax_setRead() {
		$msgid = $this->input->get('id');
		$read = $this->input->get('read');
		
		$message = ORM::factory('message')->find($msgid);
		$message->read = $read;
		
		$message->save();
	}
	
	/**
	 * Executes a send and receive for the current user.
	 */
	public function ajax_receiveMail() {
		$service = new MailService();
		$service->receiveMail($this->session->get('user'));
	}
	
	public function ajax_deleteMessage() {
		$id = $this->input->get('id');
		
		if ($id !== FALSE) {
			$message = ORM::factory('message')->find($id);
			
			if (sizeof($message->attachments) > 0) {
				foreach($message->attachments as $att) {
					$att->delete();
				}
			}
			
			$message->delete();
		}
		
		echo '{success: true}';
	}
}

?>