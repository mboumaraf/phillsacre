<?php

class Message_Model extends ORM {
	public $has_one = array('account');
	public $has_many = array('attachments');
}

?>