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
		this._hp_man = new HeadPanelManager(context);
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
			this._hp_man.setParent(query.find('.head-panel'));

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
				nav2("~/user/login/");
			});

			reg.click(function() {
				nav2("~/user/register/");
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

		addPanel : function(panel) {
			this._hp_man.addPanel(panel);
		},

		setCurrentPanel : function(name) {
			this._hp_man.setCurrentPanel(name);
		},

	};

	/***************************************************************************
	 * class HeadPanelManager
	 */

	function HeadPanelManager(context) {

		this._context = context;

		this._panel_table = {};
		this._panel_list = [];
		this._parent = null;
		this._cur_ipw = null;

	}

	HeadPanelManager.prototype = {

		setParent : function(parent) {
			this._parent = parent;
			this.update();
		},

		setChild : function(child) {
			this._child = child;
			this.update();
		},

		setCurrentIPW : function(ipw) {

			var old = this._cur_ipw;
			this._cur_ipw = ipw;

			if (old != null) {
				old.onHide();
				this.setChild(null);
			}

			if (ipw != null) {
				ipw.onShow();
				this.setChild(ipw.ui());
			}

		},

		update : function() {

			var p = this._parent;
			var c = this._child;

			if (p == null) {
				return;
			} else {
				p.empty();
			}

			if (c == null) {
				return;
			} else {
				p.append(c);
			}

		},

		addPanel : function(panel) {

			var context = this._context;
			panel = new InnerPanelWrapper(context, panel);

			var table = this._panel_table;
			var list = this._panel_list;

			var key = panel.name();
			table[key] = panel;
			list.push(panel);

		},

		setCurrentPanel : function(name) {

			var self = this;
			var table = this._panel_table;
			var ipw = table[name]; // InnerPanelWrapper

			if (ipw == null) {
				return;
			}

			ipw.load(function() {
				self.setCurrentIPW(ipw);
			});

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

	/***************************************************************************
	 * class HeadPanel
	 */

	function HeadPanel(context) {

		this._context = context;

		this.domURL('~/lib/export/h2o/html/HeadPanel.html');
		this.domSelector('.panel');
		this.name('a-head-panel');
		this.title('a-head-panel');

	}

	mc.class(function(cc) {
		cc.type(HeadPanel);
		cc.extends(Attributes);
	});

	HeadPanel.prototype = {

		name : function(value) {
			return this.attr('name', value);
		},

		title : function(value) {
			return this.attr('title', value);
		},

		ui : function(value) {
			return this.attr('ui', value);
		},

		domURL : function(value) {
			return this.attr('dom_url', value);
		},

		domSelector : function(value) {
			return this.attr('dom_sel', value);
		},

		onShow : function() {
			// alert('on_show');
		},

		onHide : function() {
			// alert('on_hide');
		},

		onLoad : function() {
			// alert('on_load');
		},

	};

	/***************************************************************************
	 * class InnerPanelWrapper
	 */

	function InnerPanelWrapper(context, panel) {
		this._context = context;
		this._panel = panel;
	}

	InnerPanelWrapper.prototype = {

		name : function() {
			return this._panel.name();
		},

		ui : function() {
			return this._panel.ui();
		},

		load : function(fn /* () */) {

			if (fn == null) {
				fn = function() {
				};
			}

			var ui = this._panel.ui();
			var url = this._panel.domURL();
			var selector = this._panel.domSelector();
			var context = this._context;
			var self = this;

			if (ui == null) {

				var loader = new ResourceLoader(context);

				loader.loadHTML(url, function(query) {
					var q2 = query.find(selector);
					self._panel.ui(q2);
					self._panel.onLoad();
					fn();
				});

			} else {
				fn();
			}

		},

		onShow : function() {
			return this._panel.onShow();
		},

		onHide : function() {
			return this._panel.onHide();
		},

		onLoad : function() {
			return this._panel.onLoad();
		},

	};

});

/*******************************************************************************
 * EOF
 */
