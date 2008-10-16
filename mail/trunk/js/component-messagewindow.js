/**
 * Compose Message window.
 */
Mail.Components.messageWindow = Ext.extend(Ext.Window, {
	format: 'html',
	
	constructor: function(config) {
		
		var accStore = new Ext.data.JsonStore({
			url: Mail.CONTEXT_PATH + 'accounts/ajax_getAccounts',
			root: 'accounts',
			fields: ['id', 'name', 'email_address']
		});
		
		this.form = new Ext.form.FormPanel({
			frame: true,
			labelWidth: 55,
			bodyStyle: 'padding: 5px',
			url: Mail.CONTEXT_PATH + 'mail/ajax_sendMail',
			items: [
				this.accountCombo = new Ext.form.ComboBox({
					fieldLabel: 'Account',
					tpl: '<tpl for="."><div class="x-combo-list-item">{email_address} - {name}</div></tpl>',
					anchor: '100%',
					displayField: 'email_address',
					editable: false,
					forceSelection: true,
					valueField: 'id',
					//mode: 'local',
					hiddenName: 'account',
					triggerAction: 'all',
					lazyInit: false,
					store: accStore
				}),
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
					xtype: 'textfield',
					fieldLabel: 'Subject',
					name: 'subject',
					anchor: '100%'
				},
				{
					xtype: 'htmleditor',
					id: 'msg-editor-html',
					hideLabel: true,
					name: 'message',
					anchor: '100% -53'
				}
			]
		});
		
		var menu = new Ext.menu.Menu({
			items: [
				{ 
					text: 'Format', 
					menu: {
						items: [
							{
								text: 'HTML',
								checked: true,
								group: 'format',
								checkHandler: this.changeFormat
							},
							{
								text: 'Plain Text',
								checked: false,
								group: 'format',
								checkHandler: this.changeFormat
							}
						]
					}
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
			new Ext.Button({text: 'Send', cls: 'x-btn-text-icon', icon: Mail.CONTEXT_PATH + 'img/send.png', handler: this.sendMail.createDelegate(this) }),
			new Ext.Button({text: 'Add Attachment', cls: 'x-btn-text-icon', icon: Mail.CONTEXT_PATH + 'img/add_attachment.png'}),
			new Ext.Button({text: 'Options', menu: menu })
		];
		config.items = [
			this.form
		];
		
		Mail.Components.messageWindow.superclass.constructor.apply(this, arguments);
		
		accStore.load();
		accStore.on('load', function(store, records, options) { console.log(this.accountCombo); this.accountCombo.setValue(records[0].get('id')); }, this);
	},
	
	sendMail: function() {
		if (this.validateFields()) {
			this.form.getForm().submit({ 
				params: { format: this.format },
				success: function(form, action) {
					alert('Success');
				}
			});
		}
	},
	
	/**
	 * Validates fields before sending an email, i.e. checks that the "To" address has been populated.
	 * Returns true if all fields are OK, false if a field does not validate.
	 */
	validateFields: function() {
		var vals = this.form.getForm().getValues();
		if (vals.to == '') {
			Mail.Utils.showError('You must specify a to address!');
			return false;
		}
		
		// Perform a very basic email syntax check - email must have an '@' and a . after the '@'
		if (! vals.to.match(/^.+@.+\..+$/)) {
			Mail.Utils.showError('The "to" email address appears to be invalid');
			return false;
		}
		
		return true;
	},
	
	changeFormat: function(item, checked) {
		if (checked) {
			if (item.text == 'HTML') {
				this.format = 'html';
			}
			else {
				this.format = 'text';
			}
		}
	}
});