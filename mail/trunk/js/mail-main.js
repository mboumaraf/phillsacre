Ext.namespace('Mail', 'Mail.Events', 'Mail.Components');

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
	    }, {
	        region: 'west',
	        collapsible: true,
	        title: 'Folders',
	        xtype: 'treepanel',
	        width: 200,
	        autoScroll: true,
	        split: true,
	        loader: new Ext.tree.TreeLoader(),
	        root: new Ext.tree.AsyncTreeNode({
	            expanded: true,
	            children: [{
	                text: 'Menu Option 1',
	                leaf: true
	            }, {
	                text: 'Menu Option 2',
	                leaf: true
	            }, {
	                text: 'Menu Option 3',
	                leaf: true
	            }]
	        }),
	        rootVisible: false,
	        listeners: {
	            click: function(n) {
	                Ext.Msg.alert('Navigation Tree Click', 'You clicked: "' + n.attributes.text + '"');
	            }
	        }
	    },
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
			header += '<table><tr><td class="label">From:</td><td>' + record.get('sender') + '</td></tr>';
			header += '<tr><td class="label">Subject:</td><td>' + record.get('subject') + '</td></tr>';
			
			if (result.attachments.length > 0) {
				header += '<tr><td class="label">Attachments:</td><td>';
				
				for (var i=0; i < result.attachments.length; i++) {
					header += '<a href="' + Mail.CONTEXT_PATH + 'attachments/download/' + id + '/' + result.attachments[i].id + '">' + result.attachments[i].name + '</a>';
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
	
	$.get(Mail.CONTEXT_PATH + 'mail/ajax_receiveMail', null, function(result) {
		Ext.getCmp('statusBar').reset();
	});
	
	Ext.getCmp('messageList').refresh();
};

Mail.Events.deleteMessage = function() {
	Ext.getCmp('messageList').getSelectionModel().getSelectedRow();
};

/** 
 * The panel in which a message is displayed. This includes toolbar buttons such as "Reply".
 */
Mail.Components.messagePane = Ext.extend(Ext.Panel, {
	constructor: function(config) {
		config.tbar = [
			this.replyBtn = new Ext.Button({text: 'Reply', cls: 'x-btn-text-icon', icon: Mail.CONTEXT_PATH + 'img/reply.png', tooltip: 'Reply to this message'}),
			this.replyAllBtn = new Ext.Button({text: 'Reply to All', cls: 'x-btn-text-icon', icon: Mail.CONTEXT_PATH + 'img/replyall.png', tooltip: 'Reply to all recipients of this message'}),
			this.deleteBtn = new Ext.Button({text: 'Delete', cls: 'x-btn-text-icon', icon: Mail.CONTEXT_PATH + 'img/delete.png', tooltip: 'Delete this message'})
		];
		Mail.Components.messagePane.superclass.constructor.apply(this, arguments);
	}
});

/**
 * The Message List data grid.
 */
Mail.Components.messageList = Ext.extend(Ext.grid.GridPanel, {
	_store: null,
	
	constructor: function(config) {
		this._store = new Ext.data.JsonStore({
			url: Mail.CONTEXT_PATH + '/mail/ajax_getMessages',
			root: 'messages',
			fields: ['id', 'account_id', 'sender', 'sender_email', {name: 'received_date', type: 'date', dateFormat: 'Y-m-d H:i:s'}, 'subject', 'read', {name: 'attachments', type: 'int'}]
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
			new Ext.Button({text: 'Check for New Mail', cls: 'x-btn-text-icon', icon: Mail.CONTEXT_PATH + 'img/sendreceive.png', listeners: { click: this.receiveMail } }), 
			new Ext.Button({ text: 'Refresh', tooltip: 'Refresh message list from the server', cls: 'x-btn-text-icon', icon: Mail.CONTEXT_PATH + 'img/refresh.png', listeners: { click: this.refresh } }) 
		];
		config.autoScroll = true;
		config.columns = [
			{header:'', sortable: false, fixed: true, width: 18, dataIndex: 'attachments', renderer: function(value) { if (value > 0) return '<img src="' + Mail.CONTEXT_PATH + 'img/attachment.png"/>'; else return ''; } },
			{header:'Subject', sortable: true, dataIndex: 'subject'},
			{header:'Sender', sortable: true, dataIndex: 'sender'},
			{header:'Sent Date', sortable: true, dataIndex: 'received_date', renderer: Ext.util.Format.dateRenderer('d-M-Y H:i:s')}
		];
		config.sm = new Ext.grid.RowSelectionModel({singleSelect:true});
		config.store = this._store;
		
		Mail.Components.messageList.superclass.constructor.apply(this, arguments);
		
		this._store.load();
	},
	
	receiveMail: function () {
		Mail.Events.receiveMail();
	},
	
	refresh: function() {
		this._store.reload();
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
	
	reset: function() {
		this.clearStatus({useDefaults: true});
	}
});