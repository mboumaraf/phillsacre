<?php

require_once(dirname(__FILE__) . '/pop3.php');
require_once(dirname(__FILE__) . '/mime_parser.php');
require_once(dirname(__FILE__) . '/rfc822_addresses.php');

class MailService {
	/**
	 * Check mail servers for new mail for a particular user. Returns the number of
	 * new messages downloaded.
	 */
	public function receiveMail($user) {
		$accounts = $user->accounts;
		$msgCount = 0;
		
		foreach ($accounts as $account) {
			$pop3 = new pop3_class();
			$pop3->hostname = $account->host_name;
			$pop3->port = $account->port;
			
			$error = $pop3->open();
			
			if ($error != '') {
				throw new Exception($error);
			}
			
			$error = $pop3->login($account->username, $account->password);
			
			if ($error != '') {
				throw new Exception($error);
			}
			
			$error = $pop3->Statistics($messages, $size);
			
			if ($error != '') {
				throw new Exception($error);
			}
			
			$result = $pop3->ListMessages("", 0);
			
			foreach ($result as $id => $size) {
				//TODO: At some point we may want to handle large messages differently
				
				// Get the raw message, as RetrieveMessage parses the headers
				$raw = '';
				$pop3->OpenMessage($id);
				
				while (true) {
					$error = $pop3->GetMessage(1024, $part, $eom);
					
					if ($error != '') {
						throw new Exception($error);
					}
					
					$raw .= $part;
					
					if ($eom) {
						break;
					}
				}
				
				// Check whether a message already exists
				if ($this->messageExists($account->id, $id)) {
					continue;
				}
				
				// Process message during the loop so that, in the event of a large message,
				// it's not hanging around in memory.
				$this->processMessage($account, $id, $raw);
				$msgCount++;
			}
			
			$pop3->Close();
		}
		
		return $msgCount;
	}
	
	/** 
	 * checks whether a message with the specified server Id exists for the given account.
	 */
	private function messageExists($accountId, $messageId) {
		$message = ORM::factory('message')->where(array('account_id' => $accountId, 'server_index' => $messageId))->find();
		if ($message->loaded)
			return TRUE;
			
		return FALSE;
	}
	
	/**
	 * Process a message given the raw data.
	 */
	private function processMessage($account, $index, $raw) {
		$headers = $this->parseMessageHeaders($raw, $body);
				
		$subject = array_key_exists('subject', $headers) ? $headers['subject'] : '';
		$from = array_key_exists('from', $headers) ? $headers['from'] : '';
		
		$message = new Message_Model();
		$message->account_id = $account->id;
		$message->server_index = $index;
		$message->recipient = array_key_exists('to', $headers) ? $headers['to'] : '';
		$message->sender = $from;
		$message->subject = $subject;
		$message->read = 0;
		$message->received_date = date('Y-m-d H:i:s', strtotime($headers['date']));
		
		// If the message is MIME encoded, extract the actual (HTML) message body and store 
		// any other attachments.
		if (array_key_exists('mime-version', $headers)) {
			$parser = new mime_parser_class();
			$parser->decode_bodies = 1;
			$parser->ignore_synxtax_errors = 1;
			
			$params = array('Data' => $raw);
			
			if (! $parser->Decode($params, $decoded) ) {
				throw new Exception($parser->error . ' at ' . $parser->error_position);
			}
			
			$parser->Analyze($decoded[0], $results);
			
			$message->text = $results['Data'];
			if ($results['Type'] == 'html') {
				$message->type = 'html';
			}
			else {
				$message->type = 'text';
			}
			
			$attachments = array();
			
			if (array_key_exists('Attachments', $results)) {
				// Handle and store attachments
				$this->addAttachments($attachments, $results['Attachments']);
			}
			
			if (array_key_exists('Related', $results)) {
				// Handle and store inline attachments
				$this->addAttachments($attachments, $results['Related'], 'inline');
			}
		}
		else {
			// Just create the plaintext message.
			$message->text = $body;
			$message->type = 'text';
		}
		
		$message->save();
		
		if (isset($attachments)) {
			foreach ($attachments as $attachment) {
				$attachment->message_id = $message->id;
				$attachment->save();
			}
		}	
	}
	
	/**
	 * Adds attachments to a pre-existing array of attachments. The data
	 * parameter must be an array outputted by $mime->analyze(...), such as
	 * $result['Related'] (all related parts of a message). 
	 *
	 * @param  type  this can be 'inline' or 'attachment'
	 */
	private function addAttachments(& $attachments, $data, $type = 'attachment') {
		foreach ($data as $info) {
			$attachment = new Attachment_Model();
			$attachment->name = $info['FileName'];
			$attachment->size = strlen($info['Data']);
			$attachment->data = $info['Data'];
			$attachment->mime_type = $info['content-type'];
			$attachment->type = $type;
			if (array_key_exists('ContentID', $info)) {
				$attachment->contentid = $info['ContentID'];
			}
			
			$attachments[] = $attachment;
		}
	}
	
	/**
	 * Splits a raw message out into headers and the body. Returns the headers as an array. The body is passed
	 * as a parameter by reference.
	 */
	private function parseMessageHeaders($raw, & $body) {
		$headerlines = array();
		$buffer = '';
		
		for ($i=0; $i < strlen($raw); $i++) {
			$char = $raw{$i};
			if ($char == "\r") {
				// Discard return characters. Although these are specified by the RFC, they are not always sent!
				continue;
			}
			elseif ($char == "\n") {
				if ($buffer == '') {
					// We've reached the end of the headers
					$body = substr($raw, $i + 1);
					break;
				}
				else {
					// Continuation header, i.e. it starts with a tab or space
					if ($buffer{0} == "\t" || $buffer{0} == ' ') {
						$headerlines[sizeof($headerlines) - 1] .= $buffer;
					}
					else {
						$headerlines[] = $buffer;
					}
					$buffer = '';
				}
			}
			else {
				$buffer .= $char;
			}
		}
		
		$headers = array();
		foreach ($headerlines as $line) {
			if (strpos($line, ':') === FALSE) {
				continue;
			}
			
			$key = strtolower(trim(substr($line, 0, strpos($line, ':'))));
			$val = trim(substr($line, strpos($line, ':') + 1));
			
			$headers[$key] = $val;
		}
		
		return $headers;
	}
}

?>