/*******************************************************************************
 * 
 * app-logout.html.js
 * 
 * @Copyright (c) 2016 boluozhai.com
 * @License MIT License
 * @Author xukun<xukun17@sina.com>
 * 
 */

JS.module(function(mc) {

	mc.package('com.boluozhai.h2o.html');

	var System = mc.import('js.lang.System');
	var Attributes = mc.import('js.lang.Attributes');
	var RESTClient = mc.import('snowflake.rest.RESTClient');

	// var widget_x = 'com.boluozhai.h2o.widget';
	// var HeadCtrl = mc.import(widget_x + '.head.HeadCtrl');
	// var HeadPanel = mc.import(widget_x + '.head.HeadPanel');

	var Viewport = snowflake.Viewport;
	var HtmlCtrl = snowflake.html.HtmlCtrl;
	var AuthCtrl = snowflake.AuthCtrl;

	/***************************************************************************
	 * class AppLogoutHtml
	 */

	function AppLogoutHtml(context) {
		this.HtmlCtrl(context);
	}

	mc.class(function(cc) {
		cc.type(AppLogoutHtml);
		cc.extends(HtmlCtrl);
	});

	var is_head_visible = false;

	AppLogoutHtml.prototype = {

		onCreate : function() {
		},

		logout : function() {

			var context = this._context;
			var ctrl = new AuthCtrl(context);
			return ctrl.logout();

		},

	};

	/***************************************************************************
	 * main
	 */

	$(document).ready(function() {

		var context = Snowflake.getContext();
		var ctrl = new AppLogoutHtml(context);
		ctrl.init();

		$('.btn-ok').click(function() {
			onClickOK(ctrl);
		});

		$('.btn-back').click(function() {
			onClickBack(ctrl);
		});

		$('.btn-logout').click(function() {
			onClickLogout(ctrl);
		});

		set_ui_as_success(true);
		update_ui();
		$('.root-box').removeClass('hide');

	});

	function onClickOK(ctrl) {
		var url = '~/';
		var context = ctrl._context;
		url = context.normalizeURL(url);
		window.location = url;
	}

	function onClickBack(ctrl) {
		onClickOK(ctrl);
	}

	function onClickLogout(ctrl) {
		var task = ctrl.logout();
		task.execute(function() {
			refresh_page();
		});
	}

	function refresh_page() {

		var url = window.location.href;
		window.location = url;

	}

	function update_ui() {
		var vpt = new Viewport();
		if (vpt.signed()) {
			set_ui_as_success(false);
		} else {
			set_ui_as_success(true);
		}
	}

	function set_ui_as_success(ok) {
		var show = null;
		var hide = null;
		if (ok) {
			show = '.view-success';
			hide = '.view-error';
		} else {
			hide = '.view-success';
			show = '.view-error';
		}
		$(hide).hide();
		$(show).show();
	}

});

/*******************************************************************************
 * EOF
 */
