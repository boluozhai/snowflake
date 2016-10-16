/*******************************************************************************
 * 
 * app-login-success.html.js
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
	var Viewport = mc.use(snowflake.Viewport);
	var JSONRestRequest = mc.use(snowflake.JSONRestRequest);

	/***************************************************************************
	 * class LoginHtml
	 */

	function LoginSuccessHtml(context) {
		this.HtmlCtrl(context);
	}

	mc.class(function(cc) {
		cc.type(LoginSuccessHtml);
		cc.extends(HtmlCtrl);
	});

	LoginSuccessHtml.prototype = {

		onCreate : function() {

			this.loadRepoList();

		},

		loadRepoList : function() {

			var self = this;
			var vpt = new Viewport();
			var uid = vpt.myUid();
			var signed = vpt.signed();

			if (!signed) {
				this.onNotSigned();
			}

			var jrr = new JSONRestRequest(this._context);
			var tx = jrr.open('GET', {
				uid : uid,
				repo : 'r',
				api : 'system-api',
				type : 'repository',
				id : '',
			});
			jrr.onResult(function() {
				if (jrr.ok()) {
					var ent = jrr.responseEntity();
					self.onRepoListReady(ent);
				} else {
					self.onNotSigned();
				}
			});
			jrr.send();

		},

		onRepoListReady : function(model) {
			var repo = model.f_repository();
			if (repo.f_exists() && repo.f_theDefault()) {
				this.nav2defaultRepo(repo);
			} else {
				this.onNotSigned();
			}
		},

		onNotSigned : function() {
			this._context.navigate2('~/');
		},

		nav2defaultRepo : function(repo) {

			var user = repo.f_owner().f_uid();
			var repo = repo.f_name();

			var url = '~/{uid}/{rid}/';
			url = url.replace('{uid}', user);
			url = url.replace('{rid}', repo);

			this._context.navigate2(url);

		},

	};

	/***************************************************************************
	 * main
	 */

	$(document).ready(function() {

		var context = Snowflake.getContext();
		var ctrl = new LoginSuccessHtml(context);
		ctrl.init();

	});

});

/*******************************************************************************
 * EOF
 */
