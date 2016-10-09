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

	var JSONRestRequest = mc
			.import("com.boluozhai.snowflake.rest.api.JSONRestRequest");

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

	function DoRegister(context) {
		this._context = context;
		this._helper = new InnerHelper(context);
	}

	mc.class(function(cc) {
		cc.type(DoRegister);
		cc.extends(AuthTask);
	});

	DoRegister.prototype = {

		email : function(value) {
			return this.attr('email', value);
		},

		password : function(value) {
			return this.attr('password', value);
		},

		execute : function(fn) {

			var self = this;
			var context = this._context;
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

			var jrr = new JSONRestRequest(context);
			var req_ent = jrr.open('POST', {
				uid : 'u',
				repo : 'r',
				api : 'rest',
				type : 'auth',
				id : 'register',
			});

			var param = req_ent.f_request();
			param.f_mechanism('password');
			param.f_method('register');
			param.f_name(user);
			param.f_key(pass_hash);

			jrr.onResult(function() {
				helper.process_response(self, jrr);
				fn();
			});
			jrr.send(req_ent);

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
			var context = this._context;
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

			var jrr = new JSONRestRequest(context);
			var req_ent = jrr.open('POST', {
				uid : 'u',
				repo : 'r',
				api : 'rest',
				type : 'auth',
				id : 'login',
			});

			var param = req_ent.f_request();
			param.f_mechanism('password');
			param.f_method('login');
			param.f_name(user);
			param.f_key(pass_hash);

			jrr.onResult(function() {
				helper.process_response(self, jrr);
				fn();
			});
			jrr.send(req_ent);

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

		process_response : function(task, jrr) {

			var ok = false;
			var msg = null;
			var code = null;
			var status = null;

			if (jrr.ok()) {
				var ent = jrr.responseEntity();
				var res = ent.f_response();
				ok = res.f_success();
				msg = res.f_message();
				code = res.f_code();
				status = res.f_status();
			} else {
				ok = false;
				msg = jrr.responseMessage();
				code = jrr.responseCode();
			}

			task.code(code);
			task.success(ok);
			task.status(status);
			task.message(msg);

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
