/*******************************************************************************
 * 
 * app-index.html.js
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
	var FootCtrl = mc.import(widget_x + '.foot.FootCtrl');

	var Viewport = mc.import('com.boluozhai.snowflake.web.Viewport');
	var WebPageController = mc
			.import('com.boluozhai.snowflake.web.WebPageController');

	/***************************************************************************
	 * class AppHomeHtml
	 */

	function AppHomeHtml(context) {
		this.WebPageController(context);
	}

	mc.class(function(cc) {
		cc.type(AppHomeHtml);
		cc.extends(WebPageController);
	});

	var is_head_visible = false;

	AppHomeHtml.prototype = {

		onCreate : function() {

			var context = this._context;
			var self = this;

			// head
			var head_ctrl = new HeadCtrl(context);
			this._head_ctrl = head_ctrl;
			head_ctrl.binder().parent('#page-head');
			head_ctrl.init();

			var panel_a = new HeadPanel(context, 'Panel_A');
			var panel_b = new HeadPanel(context, 'Panel_B');
			// head_ctrl.addPanel(panel_a);
			// head_ctrl.addPanel(panel_b);
			head_ctrl.setCurrentPanel(panel_b.name());

			// foot
			var foot_ctrl = new FootCtrl(context);
			this._foot_ctrl = foot_ctrl;
			foot_ctrl.binder().parent('#page-foot');
			foot_ctrl.init();

		},

	};

	/***************************************************************************
	 * main
	 */

	$(document).ready(function() {

		var ctrl = new AppHomeHtml();
		ctrl.init();

	});

});

/*******************************************************************************
 * EOF
 */
