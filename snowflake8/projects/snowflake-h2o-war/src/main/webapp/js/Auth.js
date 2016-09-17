/*******************************************************************************
 * 
 * Auth.js
 * 
 * @Copyright (c) 2016 boluozhai.com
 * @License MIT License
 * @Author xukun<xukun17@sina.com>
 * 
 */

JS.module(function(mc) {

	mc.package('com.boluozhai.h2o.webui');

	var System = mc.import('js.lang.System');
	var Attributes = mc.import('js.lang.Attributes');
	var RESTClient = mc.import('snowflake.rest.RESTClient');
	var SHA1 = mc.import('snowflake.security.sha1.SHA1');

	var widget_x = 'com.boluozhai.h2o.widget';

	// var FileListCtrl = mc.import(widget_x + '.folder.FileListCtrl');
	// var PathBarCtrl = mc.import(widget_x + '.folder.PathBarCtrl');
	// var DirDataCtrl = mc.import(widget_x + '.folder.DirDataCtrl');
	// var ConsoleCtrl = mc.import(widget_x + '.console.ConsoleCtrl');

	/***************************************************************************
	 * class AuthCtrl
	 */

	function AuthCtrl(context) {
		this._context = context;
		this._helper = new InnerHelper(context);
	}

	mc.class(function(cc) {
		cc.type(AuthCtrl);
		cc.extends(Attributes);
	});

	AuthCtrl.prototype = {

		init : function() {

			var context = this._context;

		},

		login : function(param, fn) {

			var email = param.email;
			var psw = param.password;

			var helper = this._helper;
			var email_hash = helper.hash(email);
			var psw_hash = helper.hash(psw);
			if (!helper.check_password(psw)) {
				var msg = 'bad password !';
				return helper.show_error(msg, fn);
			}
			if (!helper.check_email(email)) {
				var msg = 'bad email: ' + email;
				return helper.show_error(msg, fn);
			}

			var client = RESTClient.getInstance(this._context);
			var app = client.getApplication();
			var api = app.getAPI('rest');
			var type = api.getType('Auth');
			var res = type.getResource(email_hash);
			var request = res.put();

			var tx = {
				auth : {
					type : 'email+password',
					email : email,
					uid : email_hash,
					password : psw_hash,
				}
			};
			var entity = request.entity();
			entity.json(tx);

			request.execute(function(response) {

				// alert('xxxx');

			});

		},

		register : function(param, fn) {

			var email = param.email;
			var psw = param.password;
			var psw2 = param.password2;

		},

	};

	/***************************************************************************
	 * inner class AuthTask
	 */

	function AuthTask(context) {
		this._context = context;
	}

	mc.class(function(cc) {
		cc.type(AuthTask);
		cc.extends(Attributes);
	});

	AuthTask.prototype = {

	};

	/***************************************************************************
	 * inner class InnerHelper
	 */

	function InnerHelper(context) {
		this._context = context;
	}

	InnerHelper.prototype = {

		hash : function(plain) {
			return SHA1.digest(plain);
		},

		show_error : function(msg, fn) {
		},

	};

});

/*******************************************************************************
 * EOF
 */
