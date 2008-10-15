/**
 * Mail folder tree
 */
Mail.Components.folderTree = Ext.extend(Ext.tree.TreePanel, {
	constructor: function(config) {
		config.title = 'Folders';
		config.autoScroll = true;
		config.root = {
			nodeType: 'async',
			text: 'Local Folders',
			expanded: true,
			id: 'root'
		};
		config.dataUrl = Mail.CONTEXT_PATH + 'folders/ajax_getFolderList';
		config.contextMenu = new Ext.menu.Menu({
			items: [
				{ id: 'new-folder', text: 'New folder here', icon: Mail.CONTEXT_PATH + 'img/new-folder.png' },
				{ id: 'delete-folder', text: 'Delete folder', icon: Mail.CONTEXT_PATH + 'img/delete.png' }
			],
			listeners: {
				itemclick: function(item) {
					switch(item.id) {
						case 'new-folder':
							Mail.Events.newFolder(item.parentMenu.contextNode);
							break;
						case 'delete-folder':
							Mail.Events.deleteFolder(item.parentMenu.contextNode);
							break;
					}
				}
			}
		});
		config.listeners = {
			'beforenodedrop': function(dropEvent) {
				var data = dropEvent.data;
				var node = dropEvent.target;
				
				for (var i=0; i < data.selections.length; i++) {
					var selection = data.selections[i];
					if (selection.data.folder_id == node.id) {
						return false;
					}
					
					Mail.Events.moveMessage(selection.data.id, node.id);
				}
			},
			'contextmenu': function(node, e) {
				node.select();
				if (node.id == 'root' || node.text == 'Inbox') {
					Ext.getCmp('delete-folder').disable();
				}
				else {
					Ext.getCmp('delete-folder').enable();
				}
				var c = node.getOwnerTree().contextMenu;
				c.contextNode = node;
				c.showAt(e.getXY());
			},
			'load': function(node) {
				if (node.text == 'Inbox') {
					this.selectInbox();
				}
				
				if (node.attributes.numUnread && node.attributes.numUnread > 0) {
					this.updateNodeUnread(node, parseInt(node.attributes.numUnread));
				}
			}
		};
		config.enableDrop = true;
		config.dropConfig = { appendOnly: true, ddGroup: 'messageList' };
		
		Mail.Components.folderTree.superclass.constructor.apply(this, arguments);
		
		// The message list should be refreshed when the folder selection changes.
		this.getSelectionModel().on('selectionchange', function(selectionModel, node) {
			Mail.Events.selectFolder(node.id);
		});
	},
	
	selectInbox: function() {
		var node = this.getRootNode().findChild('text', 'Inbox');
		if (node !== null) {
			node.select();
		}
	},
	
	/**
	 * Updates the 'unread' count for a folder node. Folders with unread messages
	 * are bold with the number of unread messages in brackets next to the text.
	 * The unreadDelta is the amount to change it by.
	 */
	updateNodeUnread: function(node, unreadDelta) {
		var unread = parseInt(node.attributes.numUnread);
		node.attributes.numUnread = unread += unreadDelta;
		
		var textel = node.getUI().getTextEl();
		
		node.getUI().removeClass('folder-unread');
		$(textel).empty().append(node.text);
		
		if (node.attributes.numUnread > 0) {
			$(textel).append(' (' + node.attributes.numUnread + ')');
			node.getUI().addClass('folder-unread');
		}
	},
	
	updateCurrentNodeUnread: function(unreadDelta) {
		// Change the number of unread items on the selected node by the unreadDelta parameter
		var node = this.getSelectionModel().getSelectedNode();
		this.updateNodeUnread(node, unreadDelta);
	},
	
	refresh: function() {
		var path = this.getSelectionModel().getSelectedNode().getPath();
		this.getRootNode().reload();
		this.expandPath(path);
	}
});