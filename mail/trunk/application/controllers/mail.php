<?php

define('JSON_MIME_TYPE', 'text/x-json');

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
		$result = ORM::factory('message')->orderby('received_date')->find_all();
		
		$results = array();
		
		foreach ($result as $message) {
			$results[] = array(
				'id' => $message->id,
				'account_id' => $message->account_id,
				'recipient' => htmlentities($message->recipient),
				'sender' => htmlentities($message->sender),
				'received_date' => $message->received_date,
				'subject' => htmlentities($message->subject),
				'read' => $message->read,
				'attachments' => $message->getAttachmentCount()
			);
		}
		
		header('Content-Type: ' . JSON_MIME_TYPE);
		echo json_encode(array('messages' => $results));
	}
	
	public function ajax_getMessageBody() {
		$msgid = $this->input->get('id');
		
		$message = ORM::factory('message')->find($msgid);
		
		$attachments = array();
		if (sizeof($message->attachments) > 0) {
			foreach ($message->attachments as $att) {
				if ($att->type == 'attachment') {
					$attachments[] = array(
						'id' => $att->id,
						'name' => $att->name,
						'size' => $att->size,
						'mime_type' => $att->mime_type
					);
				}
			}
		}
		
		$result = array(
			'body' => $message->type == 'html' ? $this->parseHTMLMessageBody($message) : nl2br(htmlentities($message->text)), 
			'attachments' => $attachments, 
			'type' => $message->type
		);
		
		header('Content-Type: ' . JSON_MIME_TYPE);
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
		$msgCount = $service->receiveMail($this->session->get('user'));
		
		header('Content-Type: ' . JSON_MIME_TYPE);
		echo '{msgCount:' . $msgCount . '}';
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
	
	/**
	 * Parses an HTML message body for links to attachments, and replaces them with the 
	 * appropriate URLs. Also blocks remote images.
	 */
	private function parseHTMLMessageBody($message) {
		$text = $message->text;
		
		// Search for images with external URLs
		if (preg_match_all('/<img[^>]+src=["\']?(.*?)["\' ]/i', $text, $matches) !== 0) {
			foreach($matches[1] as $match) {
				$url = $match;
				
				if (strpos($url, 'cid:') !== FALSE) {
					continue;
				}
				
				$text = str_replace($url, 'BLOCKED::' . $url, $text);
			}
		}
		
		// Search for cid: urls
		if (preg_match_all('/(?:[=" ])(cid:.+?)(?:[ \/>"])/i', $text, $matches) !== 0) {
			foreach($matches[1] as $match) {
				$cid = $match;
				$cid = substr($cid, strpos($cid, 'cid:') + 4);
				
				foreach ($message->attachments as $att) {
					if ($att->contentid == $cid) {
						$url = url::site('attachments/download/' . $message->id . '/' . $att->id);
						
						$text = str_replace($match, $url, $text);
					}
				}
			}
		}
		
		return $text;
	}
}

?>