<?php

class Folders_Controller extends Controller {
	public function __construct() {
		parent::__construct();
		
		if ( ! $this->isLoggedIn() ) {
			url::redirect('auth/login');
			die();
		}
	}
	
	/**
	 * Retrieve a folder list suitable for user in the ExtJS TreePanel.
	 */
	public function ajax_getFolderList() {
		$user = $this->session->get('user');
		
		$folderId = $this->input->post('node');
		if (empty($folderId) || $folderId == 'root') {
			$result = $user->where('parent_id IS NULL')->orderby('name')->folders;
		}
		else {
			$result = ORM::factory('folder', $folderId)->orderby('name')->children;
		}
		
		$folders = array();
		
		foreach ($result as $folder) {
			$numChildren = sizeof($folder->children->as_array());
			
			$db = new Database();
			$result = $db->select('count(*) num')->from('messages')->where(array('folder_id' => $folder->id, 'read' => 0))->get();
			$arr = $result->result_array();
			$numUnread = $arr[0]->num;
			
			$leaf = ($numChildren == 0 ? TRUE : FALSE);
			$data = array(
				'id' => $folder->id,
				'text' => $folder->name,
				'allowDrag' => ($folder->name == 'Inbox' ? FALSE : TRUE),
				'allowDrop' => TRUE,
				'numUnread' => $numUnread
			);
			
			// Setting a node as a leaf node will mean it cannot ever have children (without
			// even the option of adopting or surrogacy). So, simulate a leaf node by giving
			// it zero children initially.
			if ($leaf) {
				$data['children'] = array();
				$data['allowChildren'] = TRUE;
				$data['allowDrop'] = TRUE;
				$data['expanded'] = TRUE;
			}
			
			$folders[] = $data;
		}
		
		$this->outputJson($folders);
	}
	
	function ajax_createFolder() {
		$parentId = $this->input->post('parentId');
		$name = $this->input->post('name', FALSE);
		
		if (empty($name) || $name === FALSE) {
			$this->outputJson(array('result' => FALSE));
			return;
		}
		
		if (empty($parentId)) {
			$parentId = NULL;
		}
		
		$folder = new Folder_Model();
		$folder->name = $name;
		$folder->user_id = $this->session->get('user')->id;
		$folder->parent_id = $parentId;
		
		try {
			$folder->save();
			$this->outputJson(array('success' => TRUE, 'folderId' => $folder->id));
		}
		catch (Exception $e) {
			$this->outputJson(array('success' => FALSE, 'errorMsg' => 'Folder with that name already exists'));
		}
	}
	
	function ajax_deleteFolder() {
		$id = $this->input->post('id');
		
		if (! empty($id)) {
			$folder = ORM::factory('folder', $id);
			
			if (sizeof($folder->messages) > 0) {
				$this->outputJson(array('success' => FALSE, 'errorMsg' => 'Folder contains messages'));
			}
			else {
				try {
					$folder->delete();
					$this->outputJson(array('success' => TRUE));
				}
				catch (Exception $e) {
					$this->outputJson(array('success' => FALSE, 'errorMsg' => 'A subfolder has messages in'));
				}
			}
		}
	}
}

?>