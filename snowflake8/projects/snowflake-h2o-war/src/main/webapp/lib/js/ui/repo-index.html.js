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

	var Viewport = mc.import('com.boluozhai.snowflake.web.Viewport');
	var HtmlCtrl = mc.use(snowflake.HtmlCtrl);

	/***************************************************************************
	 * class RepoIndexHtml
	 */

	function RepoIndexHtml(context) {
		this.HtmlCtrl(context);
	}

	mc.class(function(cc) {
		cc.type(RepoIndexHtml);
		cc.extends(HtmlCtrl);
	});

	RepoIndexHtml.prototype = {

		onCreate : function() {

			var self = this;
			var context = this._context;

			var head_ctrl = new HeadCtrl(context);
			this._head_ctrl = head_ctrl;
			head_ctrl.binder().parent('#page-head');
			head_ctrl.init();

			// path-bar

			// file-list

		},

	};

	/***************************************************************************
	 * main
	 */

	$(document).ready(function() {

		var context = Snowflake.getContext();
		var ctrl = new RepoIndexHtml(context);
		ctrl.init();

	});

});

/*******************************************************************************
 * EOF
 */
