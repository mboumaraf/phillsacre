<?php

class Message_Model extends ORM {
	public $has_one = array('account');
	public $has_many = array('attachments');
	
	/** 
	 * Retrieve the number of attachments this message has, with the type
	 * 'attachment' (as opposed to 'inline')
	 */
	public function getAttachmentCount() {
		$count = 0;
		
		foreach($this->attachments as $att) {
			if ($att->type == 'attachment') {
				$count++;
			}
		}
		
		return $count;
	}
}

?>