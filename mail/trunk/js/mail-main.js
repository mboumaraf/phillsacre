Ext.namespace('Mail', 'Mail.Events', 'Mail.Components', 'Mail.Utils');

$(document).ready(function() {
	Mail.init();
});

/**
 * Initialisation function for the app.
 */
Mail.init = function() {
	new Ext.Viewport({
	    layout: 'border',
	    items: [{
	        region: 'north',
	        html: '<h1 class="x-panel-header">Mail</h1>',
	        autoHeight: true,
	        border: false,
	        margins: '0 0 5 0'
	    }, new Mail.Components.folderTree({
			id: 'folderTree',
			region: 'west',
			collapsible: 'true',
			split: true
		}),
		{
			region: 'center',
			layout: 'border',
			items: [
				new Mail.Components.messageList({id: 'messageList', region: 'north', split: 'true', height: 200 }),
				new Mail.Components.messagePane({id: 'messagePane', region: 'center', autoScroll: true })
			]
		},
		new Mail.Components.statusBar({id: 'statusBar', region: 'south'})]
	});
	
	Ext.getCmp('messageList').getSelectionModel().on({
		'rowselect': Mail.Events.selectMessage,
		'rowdeselect': Mail.Events.deselectMessage,
		scope: this
	});
}

/* === EVENTS ==================================================== */

/**
 * Handles when a message is Selected in the MessageList grid.
 */
Mail.Events.selectMessage = function(selectionModel, rowIndex, record) {
	var id = record.get('id');
	var read = record.get('read');
	
	Ext.getCmp('statusBar').loading('Retrieving message...');
	
	if (read == 0) {
		record.set('read', 1);
		$.get(Mail.CONTEXT_PATH + 'mail/ajax_setRead', {id: id, read: 1}, function(result) { });
	}
	
	$.getJSON(Mail.CONTEXT_PATH + 'mail/ajax_getMessageBody', {id: id}, function(result) {
		if (result.body) {
			var header = "<div class='msg-header'>";
			header += '<table><tr>';
			header += '<td class="label">To:</td><td>' + record.get('recipient') + '</td></tr>';
			header += '<td class="label">From:</td><td>' + record.get('sender') + '</td></tr>';
			header += '<tr><td class="label">Subject:</td><td>' + record.get('subject') + '</td></tr>';
			
			if (result.attachments.length > 0) {
				header += '<tr><td class="label">Attachments:</td><td>';
				
				for (var i=0; i < result.attachments.length; i++) {
					header += '<a href="' + Mail.CONTEXT_PATH + 'attachments/download/' + id + '/' + result.attachments[i].id + '">' + result.attachments[i].name + '</a>';
					header += ' (' + Mail.Utils.displaySize(result.attachments[i].size) + ')';
					if (i < result.attachments.length - 1) {
						header += '; ';
					}
				}
				
				header += '</td></tr>';
			}
			
			header += '</table></div>';
			
			Ext.getCmp('messagePane').body.update(header + '<div class="msg-body">' + result.body + '</div>');
			Ext.getCmp('statusBar').reset();
		}
		else {
			Ext.getCmp('messagePane').body.update('');
		}
	});
};

/** 
 * Handles when a message is deselected in the MessageList grid.
 */ 
Mail.Events.deselectMessage = function() {
	Ext.getCmp('messagePane').body.update('');
};

/**
 * Executes and "Send and Receive" function.
 */
Mail.Events.receiveMail = function() {
	Ext.getCmp('statusBar').loading('Checking for new mail...');
	
	$.getJSON(Mail.CONTEXT_PATH + 'mail/ajax_receiveMail', null, function(result) {
		Ext.getCmp('statusBar').reset();
		Ext.getCmp('messageList').refresh();
		
		Ext.getCmp('statusBar').tempMessage(result.msgCount + ' new messages');
	});
};

/**
 * Handles a message being deleted.
 */
Mail.Events.deleteMessage = function() {
	var row = Ext.getCmp('messageList').getSelectionModel().getSelected();
	
	if (row) {
		var id = row.get('id');
		$.get(Mail.CONTEXT_PATH + 'mail/ajax_deleteMessage', { id: id }, function(result) { 
			Ext.getCmp('messageList').refresh(); 
			Mail.Events.deselectMessage(); 
		});
	}
};

/**
 * Shows the 'New Message' window.
 */
Mail.Events.composeMail = function() {
	new Mail.Components.messageWindow({id: 'messageWindow'}).show();
};

/**
 * Handles a folder selection event. This should refresh
 * the message list.
 */
Mail.Events.selectFolder = function(folderId) {
	var ml = Ext.getCmp('messageList');
	
	ml.setFolderId(folderId);
	ml.refresh();
	
	Mail.Events.deselectMessage();
};

/**
 * Moves a message to another folder.
 */
Mail.Events.moveMessage = function(messageId, folderId) {
	$.post(Mail.CONTEXT_PATH + 'mail/ajax_moveMessage', { messageId: messageId, folderId: folderId }, function(result) {
		Ext.getCmp('messageList').refresh();
		Mail.Events.deselectMessage();
	});
};

/**
 * Prompts the user for a folder name and creates the folder.
 */
Mail.Events.newFolder = function(node) {
	Ext.MessageBox.prompt('New Folder', 'Please enter the new folder name:', function(btn, text) {
		if (btn == 'ok') {
			var parentId = (node.id == 'root') ? '' : node.id;
			
			$.post(Mail.CONTEXT_PATH + 'folders/ajax_createFolder', { name: text, parentId: parentId }, function(result) {
				node.reload();
			});
		}
	});
};

/** 
 * Deletes the folder
 */
Mail.Events.deleteFolder = function(node) {
	var id = node.id;
	var name = node.text;
	
	var error = false;
	
	if (name == 'Inbox') {
		error = 'You cannot delete the Inbox folder!';
	}
	else if (Ext.getCmp('messageList').getStore().getCount() > 0) {
		error = 'This folder contains messages. Please move them before deleting the folder.';
	}
	
	if (error) {
		Ext.MessageBox.show({
			buttons: Ext.MessageBox.OK,
			msg: error,
			title: 'Error',
			icon: Ext.MessageBox.ERROR
		});
		
		return;
	}
	
	Ext.MessageBox.confirm('Confirm delete', 'Are you sure you want to delete the folder "' + name + '"?', function(btn) {
		if (btn == 'yes') {
			$.post(Mail.CONTEXT_PATH + 'folders/ajax_deleteFolder', { id: id }, function(result) {
				if (result.success) {
					node.parentNode.select();
					node.remove();
				}
				else {
					Ext.MessageBox.show({
						buttons: Ext.MessageBox.OK,
						msg: 'Could not delete folder: ' + result.errorMsg,
						title: 'Error',
						icon: Ext.MessageBox.ERROR
					});
				}
			}, 'json');
		}
	});
};

/* === UTILS ====================================================== */

/** 
 * Utility function to convert an amount in bytes to a human-readable amount.
 */
Mail.Utils.displaySize = function(bytes) {
	var num = bytes;
	var units = "bytes";
	
	if (bytes > 1024 * 1024) { // MB
		num = bytes / (1024 * 1024);
		units = "MB";
	}
	else if (bytes > 1024) { // KB
		num = bytes / 1024;
		units = "KB";
	}
	
	return num.toFixed(2) + " " + units;
};

/* === COMPONENTS ================================================= */

/** 
 * The panel in which a message is displayed. This includes toolbar buttons such as "Reply".
 */
Mail.Components.messagePane = Ext.extend(Ext.Panel, {
	constructor: function(config) {
		config.tbar = [
			this.replyBtn = new Ext.Button({text: 'Reply', cls: 'x-btn-text-icon', icon: Mail.CONTEXT_PATH + 'img/reply.png', tooltip: 'Reply to this message'}),
			this.replyAllBtn = new Ext.Button({text: 'Reply to All', cls: 'x-btn-text-icon', icon: Mail.CONTEXT_PATH + 'img/replyall.png', tooltip: 'Reply to all recipients of this message'}),
			this.deleteBtn = new Ext.Button({text: 'Delete', cls: 'x-btn-text-icon', icon: Mail.CONTEXT_PATH + 'img/delete.png', tooltip: 'Delete this message', listeners: { click: Mail.Events.deleteMessage } })
		];
		Mail.Components.messagePane.superclass.constructor.apply(this, arguments);
	}
});

/**
 * The Message List data grid.
 */
Mail.Components.messageList = Ext.extend(Ext.grid.GridPanel, {
	_store: null,
	_folderId: null,
	
	constructor: function(config) {
		this._store = new Ext.data.JsonStore({
			url: Mail.CONTEXT_PATH + '/mail/ajax_getMessages',
			root: 'messages',
			fields: [
				'id', 
				'account_id', 
				'folder_id',
				'recipient',
				'sender', 
				'sender_email', 
				{name: 'received_date', type: 'date', dateFormat: 'Y-m-d H:i:s'}, 
				'subject', 
				'read', {name: 'attachments', type: 'int'}
			]
		});
		
		config.view = new Ext.grid.GridView({
			forceFit: true,
			getRowClass: function(row, index) {
				if (row.data.read == 0) {
					return 'unread';
				}
				return '';
			}
		});
		config.stripeRows = true;
		config.tbar = [ 
			new Ext.Button({text: 'Compose', cls: 'x-btn-text-icon', icon: Mail.CONTEXT_PATH + 'img/compose.png', listeners: { click: this.compose } }),
			new Ext.Button({text: 'Check for New Mail', cls: 'x-btn-text-icon', icon: Mail.CONTEXT_PATH + 'img/sendreceive.png', listeners: { click: this.receiveMail } }), 
			new Ext.Button({ text: 'Refresh', tooltip: 'Refresh message list from the server', cls: 'x-btn-text-icon', icon: Mail.CONTEXT_PATH + 'img/refresh.png', listeners: { click: this.refresh } }) 
		];
		config.autoScroll = true;
		config.columns = [
			{header:'', sortable: false, fixed: true, width: 18, dataIndex: 'attachments', renderer: function(value) { if (value > 0) return '<img src="' + Mail.CONTEXT_PATH + 'img/attachment.png"/>'; else return ''; } },
			{header:'Subject', sortable: true, dataIndex: 'subject'},
			{header:'To', sortable: true, dataIndex: 'recipient', hidden: true},
			{header:'From', sortable: true, dataIndex: 'sender'},
			{header:'Sent Date', sortable: true, dataIndex: 'received_date', renderer: Ext.util.Format.dateRenderer('d-M-Y H:i:s')}
		];
		config.sm = new Ext.grid.RowSelectionModel({singleSelect:true});
		config.store = this._store;
		config.enableDrag = true;
		config.ddGroup = 'messageList';
		config.ddText = '{0} selected message(s)';
		
		Mail.Components.messageList.superclass.constructor.apply(this, arguments);
	},
	
	compose: function() {
		Mail.Events.composeMail();
	},
	
	receiveMail: function () {
		Mail.Events.receiveMail();
	},
	
	setFolderId: function(folderId) {
		this._folderId = folderId;
	},
	
	refresh: function() {
		var fid = this._folderId;
		this._store.reload({ params: { folderId: fid } });
	}
});

Mail.Components.messageWindow = Ext.extend(Ext.Window, {
	constructor: function(config) {
		
		var form = new Ext.form.FormPanel({
			frame: true,
			labelWidth: 30,
			bodyStyle: 'padding: 5px',
			items: [
				{
					xtype: 'textfield',
					fieldLabel: 'To',
					name: 'to',
					anchor: '100%'
				},
				{
					xtype: 'textfield',
					fieldLabel: 'CC',
					name: 'cc',
					anchor: '100%'
				},
				{
					xtype: 'htmleditor',
					hideLabel: true,
					name: 'message',
					anchor: '100% -53'
				}
			]
		});
		
		config.layout = 'fit';
		config.title = 'Compose';
		config.minWidth = 300;
		config.minHeight = 200;
		config.width = 600;
		config.height = 500;
		config.plain = true;
		config.tbar = [
			new Ext.Button({text: 'Send', cls: 'x-btn-text-icon', icon: Mail.CONTEXT_PATH + 'img/send.png'}),
			new Ext.Button({text: 'Add Attachment', cls: 'x-btn-text-icon', icon: Mail.CONTEXT_PATH + 'img/add_attachment.png'})
		];
		config.items = [
			form
		];
		
		Mail.Components.messageWindow.superclass.constructor.apply(this, arguments);
	}
});

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
			}
		};
		config.enableDrop = true;
		config.dropConfig = { appendOnly: true, ddGroup: 'messageList' };
		
		Mail.Components.statusBar.superclass.constructor.apply(this, arguments);
		
		this.getLoader().on('load', function() {
			this.selectInbox();
			this.getLoader().purgeListeners();
		}, this);
		
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
	
	refresh: function() {
		var path = this.getSelectionModel().getSelectedNode().getPath();
		this.getRootNode().reload();
		this.expandPath(path);
	}
});

/**
 * Status Panel
 */
Mail.Components.statusBar = Ext.extend(Ext.StatusBar, {
	constructor: function(config) {
		config.defaultText = 'Ready';
		
		Mail.Components.statusBar.superclass.constructor.apply(this, arguments);
	},
	
	loading: function(text) {
		this.showBusy({text: text});
	},
	
	tempMessage: function(text) {
		this.setStatus({ text: text, clear: 3000 });
	},
	
	reset: function() {
		this.clearStatus({useDefaults: true});
	}
});