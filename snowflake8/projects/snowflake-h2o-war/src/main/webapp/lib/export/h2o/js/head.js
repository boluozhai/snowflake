/*******************************************************************************
 * 
 * head.js
 * 
 * @Copyright (c) 2016 boluozhai.com
 * @License MIT License
 * @Author xukun<xukun17@sina.com>
 * 
 */

JS.module(function(mc) {

	mc.package('com.boluozhai.h2o.widget.head');

	var System = mc.import('js.lang.System');
	var Attributes = mc.import('js.lang.Attributes');
	var RESTClient = mc.import('snowflake.rest.RESTClient');
	// var SHA1 = mc.import('snowflake.security.sha1.SHA1');

	var widget_x = 'com.boluozhai.h2o.widget';

	// var FileListCtrl = mc.import(widget_x + '.folder.FileListCtrl');
	// var PathBarCtrl = mc.import(widget_x + '.folder.PathBarCtrl');
	// var DirDataCtrl = mc.import(widget_x + '.folder.DirDataCtrl');
	// var ConsoleCtrl = mc.import(widget_x + '.console.ConsoleCtrl');
	var DocumentBinder = mc.import('com.boluozhai.h2o.widget.DocumentBinder');
	var ResourceLoader = mc.import('com.boluozhai.h2o.widget.ResourceLoader');

	var SessionInfo = mc.import('com.boluozhai.h2o.web.SessionInfo');

	/***************************************************************************
	 * class HeadCtrl
	 */

	function HeadCtrl(context) {
		this._context = context;
		this._binder = new HeadBinder();
	}

	mc.class(function(cc) {
		cc.type(HeadCtrl);
		cc.extends(Attributes);
	});

	HeadCtrl.prototype = {

		init : function() {

			this.loadHtmlUI();

		},

		loadHtmlUI : function() {

			var context = this._context;
			// this._parent = this.binder().parent();
			var loader = new ResourceLoader(context);
			var self = this;
			loader.loadHTML('~/lib/export/h2o/html/Head.html', function(query) {
				self.onHtmlReday(query.find('.head'));
			});

		},

		onHtmlReday : function(query) {

			var parent = this.binder().parent();
			var child = query;
			parent.append(child);
			// this._jq_view = child;

			this.setupAuthButton(child);
			this.setupAccountInfo();

		},

		setupAuthButton : function(base) {

			var context = this._context;

			function nav2(url) {
				url = context.normalizeURL(url);
				window.location = url;
			}

			// var base = this.binder().parent();
			var login = base.find('.btn-auth-login');
			var reg = base.find('.btn-auth-reg');

			login.click(function() {
				nav2("~/login.html");
			});

			reg.click(function() {
				nav2("~/register.html");
			});

		},

		setupAccountInfo : function() {

			var info = new SessionInfo();
			var signed = info.signed();
			var nickname = info.nickname();

			var base = this.binder().parent();
			var view_login = base.find('.view-if-login');
			var view_login_not = base.find('.view-if-login-not');

			if (info.signed()) {
				view_login_not.hide();
				view_login.show();
			} else {
				view_login.hide();
				view_login_not.show();
			}

			base.find('.account-nickname').text(nickname);

		},

		binder : function() {
			return this._binder;
		},

		currentLocation : function(value) {
			return this.attr('current_location', value);
		},

	};

	/***************************************************************************
	 * class HeadBinder
	 */

	function HeadBinder() {
	}

	mc.class(function(cc) {
		cc.type(HeadBinder);
		cc.extends(DocumentBinder);
	});

	HeadBinder.prototype = {

		parent : function(value) {
			return this.bind('parent', value);
		},

	};

});

/*******************************************************************************
 * EOF
 */
