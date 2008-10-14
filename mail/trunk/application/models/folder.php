<?php

class Folder_Model extends ORM_Tree {
	protected $belongs_to = array('user');
	protected $has_many = array('messages');
	protected $children = 'folders';
}

?>