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

	mc.package('com.boluozhai.h2o.webui.auth');

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
	}

	mc.class(function(cc) {
		cc.type(AuthCtrl);
		cc.extends(Attributes);
	});

	AuthCtrl.prototype = {

		login : function() {
			return new DoLogin(this._context);
		},

		register : function() {
			return new DoRegister(this._context);
		},

	};

	/***************************************************************************
	 * inner class AuthTask
	 */

	function AuthTask() {
	}

	mc.class(function(cc) {
		cc.type(AuthTask);
		cc.extends(Attributes);
	});

	AuthTask.prototype = {

		message : function(value) {
			return this.attr('message', value);
		},

		success : function(value) {
			return this.attr('success', value);
		},

		status : function(value) {
			return this.attr('status', value);
		},

		code : function(value) {
			return this.attr('code', value);
		},

	};

	/***************************************************************************
	 * inner class DoRegister
	 */

	function DoRegister(ctrl) {
		this._context = context;
		this._helper = new InnerHelper(context);
	}

	mc.class(function(cc) {
		cc.type(DoRegister);
		cc.extends(AuthTask);
	});

	DoRegister.prototype = {

		execute : function(fn) {

			not_impl();

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

	};

	/***************************************************************************
	 * inner class DoLogin
	 */

	function DoLogin(context) {
		this._context = context;
		this._helper = new InnerHelper(context);
	}

	mc.class(function(cc) {
		cc.type(DoLogin);
		cc.extends(AuthTask);
	});

	DoLogin.prototype = {

		email : function(value) {
			return this.attr('email', value);
		},

		password : function(value) {
			return this.attr('password', value);
		},

		execute : function(fn) {

			var self = this;
			var user = this.email();
			var pass = this.password();

			var helper = this._helper;
			var user_hash = helper.hash(user);
			var pass_hash = helper.hash(pass);

			if (!helper.check_email(user)) {
				var msg = 'bad email: ' + user;
				return helper.show_error(this, msg, fn);
			}
			if (!helper.check_password(pass)) {
				var msg = 'bad password !';
				return helper.show_error(this, msg, fn);
			}

			var client = RESTClient.getInstance(this._context);
			var app = client.getApplication();
			var api = app.getAPI('rest');
			var type = api.getType('auth');
			var res = type.getResource('email+password');
			var request = res.post();

			var entity = request.entity();
			entity.json({
				request : {
					method : 'login',
					name : user,
					key : pass_hash,
				}
			});

			request.execute(function(response) {
				helper.process_response(self, response);
				fn();
			});

		},

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

		process_response : function(task, response) {
			var entity = response.entity();
			var js = entity.toJSON();
			task.success(js.response.success);
			task.message(js.response.message);
			task.status(js.response.status);
		},

		show_error : function(task, msg, fn) {
			task.message(msg);
			task.success(false);
			fn();
		},

		check_email : function(user) {
			if (user.length < 3) {
				return false;
			} else if (user.indexOf('@') < 1) {
				return false;
			} else {
				return true;
			}
		},

		check_password : function(pass) {
			if (pass.length < 4) {
				return false;
			} else {
				return true;
			}
		},

	};

});

/*******************************************************************************
 * EOF
 */
