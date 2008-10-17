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
				listeners: { rowclick: this.editAccount.createDelegate(this) }
			}),
			this.form = new Ext.form.FormPanel({
				region: 'south',
				labelWidth: 100,
				bodyStyle: 'padding: 5px;',
				height: 200,
				frame: true,
				tbar: [
					new Ext.Button({ text: 'New Account', handler: this.newAccount.createDelegate(this) }),
					new Ext.Button({ text: 'Save' })
				],
				items: [
					{
						xtype: 'hidden',
						name: 'id'
					}, {
						xtype: 'textfield',
						fieldLabel: 'Name',
						name: 'name',
						anchor: '100%'
					},
					{
						xtype: 'textfield',
						fieldLabel: 'Email Address',
						name: 'email_address',
						anchor: '100%'
					},
					{
						xtype: 'textfield',
						fieldLabel: 'Host Name',
						name: 'host_name',
						anchor: '100%'
					}, {
						xtype: 'textfield',
						fieldLabel: 'Port',
						name: 'port',
						anchor: '100%'
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
			port: '',
			username: '',
			password: ''
		});
	}
});