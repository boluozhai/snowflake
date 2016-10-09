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

	var ViewportInfo = mc.import('com.boluozhai.snowflake.web.ViewportInfo');

	/***************************************************************************
	 * class UserHomeHtml
	 */

	function UserHomeHtml(context) {
		this._context = context;
		context.i18n($(document));
	}

	mc.class(function(cc) {
		cc.type(UserHomeHtml);
		cc.extends(Attributes);
	});

	var is_head_visible = false;

	UserHomeHtml.prototype = {

		init : function() {

			var context = this._context;
			var self = this;

			var head_ctrl = new HeadCtrl(context);
			this._head_ctrl = head_ctrl;
			head_ctrl.binder().parent('#page-head');
			head_ctrl.init();

			this.setupViewportInfo();
			this.setupRepositoryList();

		},

		setupViewportInfo : function() {

			var self = this;
			var context = this._context;
			var vpinfo = new ViewportInfo(context);
			var loader = vpinfo.getDetailLoader();
			loader.load(function() {
				self.onLoadViewportInfo(loader.result());
			});

		},

		setupRepositoryList : function() {
		},

		onLoadViewportInfo : function(model) {
			var vpt = model.f_viewport();
			var owner = vpt.f_owner();
			var nickname = owner.f_nickname();
			var uid = owner.f_uid();
			$('.nickname').text(nickname);
			$('.uid').text(uid);
		},

	};

});

/*******************************************************************************
 * EOF
 */
