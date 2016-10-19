/*******************************************************************************
 * 
 * modal-dialog.js
 * 
 * @Copyright (c) 2016 boluozhai.com
 * @License MIT License
 * @Author xukun<xukun17@sina.com>
 * 
 */

JS.module(function(mc) {

	mc.package('com.boluozhai.h2o.widget.modal');

	var System = mc.import('js.lang.System');
	var Attributes = mc.import('js.lang.Attributes');
	var ResourceLoader = mc.import('com.boluozhai.h2o.widget.ResourceLoader');

	/***************************************************************************
	 * class ModalDialogService
	 */

	function ModalDialogService(context, facade) {
		this._context = context;
		this._facade = facade;
	}

	ModalDialogService.prototype = {

		init : function() {

			var body = $('body');
			var agent = this._facade;
			var url = agent.htmlURL();
			var sel = agent.dialogSelector();
			var loader = new ResourceLoader(this._context);

			loader.loadHTML(url, function(query) {
				var dlg = query.find(sel);
				agent.dialogQuery(dlg);
				body.append(dlg);
				agent.onCreate();
			});
		},

		show : function() {
			var q = this._facade.dialogQuery();
			q.modal({
				keyboard : false
			});
		},

	};

	/***************************************************************************
	 * class ModalDialog
	 */

	function ModalDialog(context) {
		this.service = new ModalDialogService(context, this);
	}

	mc.class(function(cc) {
		cc.type(ModalDialog);
		cc.extends(Attributes);
	});

	ModalDialog.prototype = {

		init : function() {
			this.service.init();
		},

		show : function() {
			this.service.show();
		},

		htmlURL : function(v) {
			return this.attr('html_url', v);
		},

		dialogSelector : function(v) {
			return this.attr('dlg_sel', v);
		},

		dialogQuery : function(v) {
			return this.attr('dlg_query', v);
		},

		onCreate : function() {
		},

	};

});

this.snowflake.ModalDialog = this.com.boluozhai.h2o.widget.modal.ModalDialog;

/*******************************************************************************
 * EOF
 */
