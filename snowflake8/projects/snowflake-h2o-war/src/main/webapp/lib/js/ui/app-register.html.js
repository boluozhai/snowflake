/*******************************************************************************
 * 
 * app-register.html.js
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

	var AuthCtrl = mc.import('com.boluozhai.h2o.webui.auth.AuthCtrl');

	/***************************************************************************
	 * class RegisterHtml
	 */

	function RegisterHtml(context) {
		this.HtmlCtrl(context);
	}

	mc.class(function(cc) {
		cc.type(RegisterHtml);
		cc.extends(snowflake.html.HtmlCtrl);
	});

	RegisterHtml.prototype = {

		onCreate : function() {

			var ctrl = this;

			// in this file
			$('#btn-ok').click(function() {
				onClickOK(ctrl);
			});
			set_ui_mode(false);
			init_4_debug(this._context);

		},

		register : function() {

			var context = this._context;
			var ctrl = new AuthCtrl(context);
			return ctrl.register();

		},

	};

	/***************************************************************************
	 * main
	 */

	$(document).ready(function() {

		var context = Snowflake.getContext();
		var ctrl = new RegisterHtml(context);
		ctrl.init();

	});

	function init_4_debug(context) {

		var i18n = context.getBean('i18n');
		var testing_user = i18n.getString('test_uid');
		var testing_passwd = i18n.getString('test_psw');

		$('#edit-email').val(testing_user);
		$('#edit-passwd').val(testing_passwd);
		$('#edit-passwd-2').val(testing_passwd);

	}

	function set_ui_mode(working) {
	}

	function onClickOK(ctrl) {

		var email = $('#edit-email').val();
		var passwd = $('#edit-passwd').val();
		var passwd2 = $('#edit-passwd-2').val();

		if (passwd != passwd2) {
			alert('2 passwds not match.');
			return;
		}

		set_ui_mode(true);

		var task = ctrl.register();
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

	function onLoginError(task) {
		var msg = task.message();
		alert('Error: ' + msg);
	}

	function onLoginOK(task) {
		alert('OK');
	}

});

/*******************************************************************************
 * EOF
 */
