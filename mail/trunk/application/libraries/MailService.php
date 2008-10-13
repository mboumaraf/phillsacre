<?php

require_once(dirname(__FILE__) . '/pop3.php');
require_once(dirname(__FILE__) . '/mime_parser.php');
require_once(dirname(__FILE__) . '/rfc822_addresses.php');

class MailService {
	/**
	 * Check mail servers for new mail for a particular user.
	 */
	public function receiveMail($user) {
		$accounts = $user->accounts;
		
		echo '<pre>';
		
		foreach ($accounts as $account) {
			echo '** Checking account: ' . $account->name. "\n";
			
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
			
			echo 'Connected as ' . $account->username . "\n";
			
			$error = $pop3->Statistics($messages, $size);
			
			if ($error != '') {
				throw new Exception($error);
			}
			
			echo "There are $messages messages in the mail box with a total of $size bytes\n";
			
			$result = $pop3->ListMessages("", 0);
			
			foreach ($result as $id => $size) {
				echo "Message: $id, size: $size bytes.\n";
				
				//TODO: At some point we may want to handle large messages differently!
				
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
				
				// Process the message here and now to get it out of memory and into the DB!
				$headers = $this->parseMessageHeaders($raw, $body);
				
				$subject = array_key_exists('subject', $headers) ? $headers['subject'] : '';
				$from = array_key_exists('from', $headers) ? $headers['from'] : '';
				
				$message = new Message_Model();
				$message->account_id = $account->id;
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
					
					if (array_key_exists('Attachments', $results)) {
						$attachments = array();
						
						// Handle and store attachments
						foreach ($results['Attachments'] as $info) {
							$attachment = new Attachment_Model();
							$attachment->name = $info['FileName'];
							$attachment->size = strlen($info['Data']);
							$attachment->data = $info['Data'];
							$attachment->mime_type = $info['content-type'];
							
							$attachments[] = $attachment;
						}
						
						echo "Attachments: " . sizeof($attachments) . "\n";
					}
				}
				else {
					// Just create the plaintext message.
					$message->text = $body;
					$message->type = 'text';
				}
				
				$message->save();
				
				echo "Message id is: ". $message->id . "\n";
				
				if (isset($attachments)) {
					foreach ($attachments as $attachment) {
						$attachment->message_id = $message->id;
						$attachment->save();
					}
					
					// Unset, otherwise attachments will remain defined and isset will return true above...
					unset($attachments);
				}
			}
			
			$pop3->Close();
			
			echo "\n";
		}
		
		echo '</pre>';
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