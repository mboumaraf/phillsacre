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
			new Ext.Button({text: 'Check for New Mail', cls: 'x-btn-text-icon', icon: Mail.CONTEXT_PATH + 'img/sendreceive.png', listeners: { click: this.receiveMail } })
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
		config.contextMenu = new Ext.menu.Menu({
			items: [ { id: 'mark-as-unread', text: 'Mark as Unread', icon: Mail.CONTEXT_PATH + 'img/unread.png' } ],
			listeners: {
				itemclick: function(item) {
					var grid = this.grid;
					var rowIndex = this.rowIndex;
					switch(item.id) {
						case 'mark-as-unread':
							var record = grid.getStore().getAt(rowIndex);
							Mail.Events.markAsRead(record.get('id'), 0);
							record.set('read', 0);
							break;
					}
				}
			}
		});
		config.listeners = {
			'rowcontextmenu': function(grid, rowIndex, e) {
				var c = grid.contextMenu;
				grid.getSelectionModel().selectRow(rowIndex);
				
				c.rowIndex = rowIndex;
				c.grid = grid;
				c.showAt(e.getXY());
				e.stopEvent();
			}
		};
		
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
