/**
 * The edit / update accounts window.
 */
Mail.Components.accountsWindow = Ext.extend(Ext.Window, {
	constructor: function(config) {
		this.store = new Ext.data.JsonStore({
			url: Mail.CONTEXT_PATH + 'accounts/ajax_getAccounts',
			root: 'accounts',
			fields: [
				'id', 'name', 'email_address', 'host_name', 'port', 'username', 'password'
			]
		});
		
		config.modal = true;
		config.title = 'Manage Accounts';
		config.layout = 'border';
		config.width = 500;
		config.height = 400;
		config.items = [
			this.grid = new Ext.grid.GridPanel({ 
				store: this.store,
				region: 'center',
				columns: [
					{header: 'Name', sortable: true, dataIndex: 'name'},
					{header: 'Email Address', sortable: true, dataIndex: 'email_address'},
					{header: 'Host Name', sortable: true, dataIndex: 'host_name'},
					{header: 'Username', sortable: true, dataIndex: 'username'}
				],
				viewConfig: { forceFit: true },
				stripeRows: true,
				autoScroll: true,
				sm: new Ext.grid.RowSelectionModel({ singleSelect: true }),
				listeners: { 
					rowclick: this.editAccount.createDelegate(this),
					rowcontextmenu: function(grid, rowIndex, e) {
						var c = grid.contextMenu;
						grid.getSelectionModel().selectRow(rowIndex);
						
						c.grid = grid;
						c.showAt(e.getXY());
						e.stopEvent();
					}
				},
				contextMenu: new Ext.menu.Menu({
					items: [
						{ id: 'delete-account', text: 'Delete', icon: Mail.CONTEXT_PATH + 'img/delete.png' }
					],
					listeners: {
						itemclick: function(item) {
							var grid = this.grid;
							
							switch (item.id) {
								case 'delete-account':
									var record = grid.getSelectionModel().getSelected();
									Mail.Events.deleteAccount(record.get('id'));
									grid.getStore().reload();
									break;
							}
						}
					}
				})
			}),
			this.form = new Ext.form.FormPanel({
				region: 'south',
				labelWidth: 100,
				bodyStyle: 'padding: 5px;',
				height: 220,
				frame: true,
				url: Mail.CONTEXT_PATH + 'accounts/ajax_saveAccount',
				buttons: [
					new Ext.Button({ text: 'Save', handler: this.saveAccount.createDelegate(this) }),
					new Ext.Button({ text: 'New Account', handler: this.newAccount.createDelegate(this) })
				],
				items: [
					{
						xtype: 'hidden',
						name: 'id'
					}, {
						xtype: 'textfield',
						fieldLabel: 'Name',
						name: 'name',
						anchor: '100%',
						allowBlank: false
					},
					{
						xtype: 'textfield',
						fieldLabel: 'Email Address',
						name: 'email_address',
						anchor: '100%',
						regex: /.+@.+\..+/,
						regexText: 'Invalid email address',
						allowBlank: false
					},
					{
						xtype: 'textfield',
						fieldLabel: 'Host Name',
						name: 'host_name',
						anchor: '100%',
						allowBlank: false
					}, {
						xtype: 'textfield',
						fieldLabel: 'Port',
						name: 'port',
						anchor: '100%',
						value: '110',
						maskRe: /[0-9]/,
						allowBlank: false
					}, {
						xtype: 'textfield',
						fieldLabel: 'Username',
						name: 'username',
						anchor: '100%'
					}, {
						xtype: 'textfield',
						fieldLabel: 'Password',
						name: 'password',
						inputType: 'password',
						anchor: '100%'
					}
				]
			})
		];
		
		Mail.Components.accountsWindow.superclass.constructor.apply(this, arguments);
		
		this.store.load();
	},
	
	/**
	 * Saves the account.
	 */
	saveAccount: function() {
		this.form.getForm().submit({
			success: function(form, action) {
				if (action.result.success == true) {
					this.refresh();
				}
				else {
					Mail.utils.showError(action.result.errorMsg);
				}
			}.createDelegate(this),
			failure: function(form, action) {
				Mail.utils.showError(action.result.errorMsg);
			}
		});
	},
	
	/**
	 * Loads up the form with account details.
	 */
	editAccount: function(grid, rowIndex, e) {
		var record = this.store.getAt(rowIndex);
		
		this.form.getForm().setValues({
			id: record.get('id'),
			name: record.get('name'),
			email_address: record.get('email_address'),
			host_name: record.get('host_name'),
			port: record.get('port'),
			username: record.get('username'),
			password: record.get('password')
		});
	},
	
	/**
	 * Clears the form.
	 */
	newAccount: function() {
		this.grid.getSelectionModel().clearSelections();
		
		this.form.getForm().setValues({
			id: '',
			name: '',
			email_address: '',
			host_name: '',
			port: '110',
			username: '',
			password: ''
		});
	},
	
	/**
	 * Refreshes the accounts list
	 */
	refresh: function() {
		this.store.reload();
	}
});