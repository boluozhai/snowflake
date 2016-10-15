/*******************************************************************************
 * 
 * app-login.html.js
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
	var SHA1 = mc.import('snowflake.security.sha1.SHA1');

	var widget_x = 'com.boluozhai.h2o.widget';

	// var FileListCtrl = mc.import(widget_x + '.folder.FileListCtrl');
	// var PathBarCtrl = mc.import(widget_x + '.folder.PathBarCtrl');
	// var DirDataCtrl = mc.import(widget_x + '.folder.DirDataCtrl');
	// var ConsoleCtrl = mc.import(widget_x + '.console.ConsoleCtrl');

	var HtmlCtrl = snowflake.html.HtmlCtrl;
	var AuthCtrl = mc.import('com.boluozhai.h2o.webui.auth.AuthCtrl');

	/***************************************************************************
	 * class LoginHtml
	 */

	function LoginHtml(context) {
		this.HtmlCtrl(context);
	}

	mc.class(function(cc) {
		cc.type(LoginHtml);
		cc.extends(HtmlCtrl);
	});

	LoginHtml.prototype = {

		onCreate : function() {

			init_4_debug(this._context);

		},

		login : function() {
			var context = this._context;
			var ctrl = new AuthCtrl(context);
			return ctrl.login();
		},

	};

	/***************************************************************************
	 * main
	 */

	function init_4_debug(context) {

		var i18n = context.getBean('i18n');
		var testing_user = i18n.getString('test_uid');
		var testing_passwd = i18n.getString('test_psw');

		$('#edit-email').val(testing_user);
		$('#edit-passwd').val(testing_passwd);

	}

	function set_ui_mode(in_working) {

		var w = $('.working');
		var nw = $('.no-work');

		if (in_working) {
			w.show();
			nw.hide();
		} else {
			nw.show();
			w.hide();
		}

	}

	function onClickOK(ctrl) {

		var email = $('#edit-email').val();
		var passwd = $('#edit-passwd').val();
		set_ui_mode(true);

		var task = ctrl.login();
		task.email(email);
		task.password(passwd);

		task.execute(function() {

			if (task.success()) {
				onLoginOK(task);
			} else {
				onLoginError(task);
			}

			set_ui_mode(false);

		});

	}

	function onLoginOK(task) {
		var url = '~/';
		var context = task._context;
		url = context.normalizeURL(url);
		window.location = url;
	}

	function onLoginError(task) {
		var msg = task.message();
		alert("登录失败！" + msg);
	}

	$(document).ready(function() {

		var context = Snowflake.getContext();
		var ctrl = new LoginHtml(context);
		ctrl.init();

		// in this file init
		$('#btn-ok').click(function() {
			onClickOK(ctrl);
		});
		set_ui_mode(false);

	});

});

/*******************************************************************************
 * EOF
 */
