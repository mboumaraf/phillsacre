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
		Mail.Events.markAsRead(id, 1);
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
 * Handles marking a message as read or unread.
 */
Mail.Events.markAsRead = function(messageId, read) {
	$.get(Mail.CONTEXT_PATH + 'mail/ajax_setRead', {id: messageId, read: read}, function(result) { });
	
	// Update the unread count of the current folder
	var tree = Ext.getCmp('folderTree');
	var delta = (read == 0) ? 1 : -1;
	tree.updateCurrentNodeUnread(delta);
};

/**
 * Downloads new mail from the server.
 */
Mail.Events.receiveMail = function() {
	Ext.getCmp('statusBar').loading('Checking for new mail...');
	
	$.getJSON(Mail.CONTEXT_PATH + 'mail/ajax_receiveMail', null, function(result) {
		Ext.getCmp('statusBar').reset();
		Ext.getCmp('messageList').refresh();
		
		var tree = Ext.getCmp('folderTree');
		var node = tree.getRootNode().findChild('text', 'Inbox');
		tree.updateNodeUnread(node, result.msgCount);
		
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
 * Compose Message window.
 */
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