<?php

class Account_Model extends ORM {
	public $has_one = array('user');
	public $has_many = array('messages');
}

?>