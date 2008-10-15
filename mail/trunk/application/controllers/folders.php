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
			$leaf = (sizeof($folder->children->as_array()) == 0 ? TRUE : FALSE);
			$data = array(
				'id' => $folder->id,
				'text' => $folder->name,
				'allowDrag' => ($folder->name == 'Inbox' ? FALSE : TRUE),
				'allowDrop' => TRUE
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
		
		$folder->save();
		
		$this->outputJson(array('folderId' => $folder->id));
	}
	
	function ajax_deleteFolder() {
		$id = $this->input->post('id');
		
		if (! empty($id)) {
			$folder = ORM::factory('folder', $id);
			
			if (sizeof($folder->messages) > 0) {
				$this->outputJson(array('success' => FALSE, 'errorMsg' => 'Folder contains children'));
			}
			else {
				$folder->delete();
				$this->outputJson(array('success' => TRUE));
			}
		}
	}
}

?>