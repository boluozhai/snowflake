/*******************************************************************************
 * 
 * user-home.html.js
 * 
 * @Copyright (c) 2016 boluozhai.com
 * @License MIT License
 * @Author xukun<xukun17@sina.com>
 * 
 */

JS.module(function(mc) {

	mc.package('com.boluozhai.h2o.html');

	var System = mc.import('js.lang.System');
	var ListBuilder = mc.import('snowflake.view.list.ListBuilder');
	var ListModel = mc.import('snowflake.view.list.ListModel');
	var Attributes = mc.import('js.lang.Attributes');
	var RESTClient = mc.import('snowflake.rest.RESTClient');

	var widget_x = 'com.boluozhai.h2o.widget';
	var HeadCtrl = mc.import(widget_x + '.head.HeadCtrl');
	var HeadPanel = mc.import(widget_x + '.head.HeadPanel');

	var ViewportInfo = mc.import('com.boluozhai.snowflake.web.ViewportInfo');

	/***************************************************************************
	 * class AppHomeHtml
	 */

	function AppHomeHtml(context) {
		this._context = context;
		context.i18n($(document));
	}

	mc.class(function(cc) {
		cc.type(AppHomeHtml);
		cc.extends(Attributes);
	});

	var is_head_visible = false;

	AppHomeHtml.prototype = {

		init : function() {

			var context = this._context;
			var self = this;

			var panel = new HeadPanel(context);

			var head_ctrl = new HeadCtrl(context);
			this._head_ctrl = head_ctrl;
			head_ctrl.binder().parent('#page-head');
			head_ctrl.addPanel(panel);
			head_ctrl.init();

			head_ctrl.setCurrentPanel(panel.name());

		},

	};

});

/*******************************************************************************
 * EOF
 */
