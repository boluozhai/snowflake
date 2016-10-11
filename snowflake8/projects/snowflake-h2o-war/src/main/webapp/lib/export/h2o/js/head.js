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

	// var widget_x = 'com.boluozhai.h2o.widget';

	var System = mc.import('js.lang.System');
	var Attributes = mc.import('js.lang.Attributes');
	var RESTClient = mc.import('snowflake.rest.RESTClient');

	var DocumentBinder = mc.import('com.boluozhai.h2o.widget.DocumentBinder');
	var ResourceLoader = mc.import('com.boluozhai.h2o.widget.ResourceLoader');

	var Viewport = mc.import('com.boluozhai.snowflake.web.Viewport');

	var ListBuilder = mc.import('snowflake.view.list.ListBuilder');
	var ListModel = mc.import('snowflake.view.list.ListModel');

	/***************************************************************************
	 * class HeadCtrl
	 */

	function HeadCtrl(context) {
		this._context = context;
		this._binder = new HeadBinder();
		this._hp_man = new PanelManager(context);
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
			this.setupPanelList(query);

		},

		setupPanelList : function(query) {

			var hpm = this._hp_man;
			hpm.selectMenu('.panel-list-menu');
			hpm.selectMenuTitle('.menu-title');
			hpm.selectMenuList('.list');
			hpm.selectMenuItem('.list-item');

			hpm.init(query);
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
				nav2("~/login");
			});

			reg.click(function() {
				nav2("~/register");
			});

		},

		setupAccountInfo : function() {

			var context = this._context;

			var vpt = new Viewport();
			var signed = vpt.signed();
			var nickname = vpt.myNickname();

			var base = this.binder().parent();
			var view_login = base.find('.view-if-login');
			var view_login_not = base.find('.view-if-login-not');

			if (signed) {
				view_login_not.hide();
				view_login.show();
			} else {
				view_login.hide();
				view_login_not.show();
			}

			base.find('.string-account-nickname').text(nickname);

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
	 * inner class PanelListModel
	 */

	function PanelListModel(list) {

		if (list == null) {
			list = [];
		}

		this._list = list; // a list of IPW
	}

	PanelListModel.prototype = {

		size : function() {
			return this._list.length;
		},

		get : function(index) {
			return this._list[index];
		},

	};

	/***************************************************************************
	 * inner class PanelManager
	 */

	function PanelManager(context) {

		this._context = context;

		this._panel_table = {};
		this._panel_list = [];
		this._parent = null;
		this._cur_ipw = null;

		this._list_ctrl = null;
		this._panel_list_menu = null;

	}

	PanelManager.prototype = {

		attr : function(k, v) {
			k = '__attr_' + k;
			if (v == null) {
				v = this[k];
			} else {
				this[k] = v;
			}
			return v;
		},

		selectMenu : function(v) {
			return this.attr('menu_', v);
		},

		selectMenuTitle : function(v) {
			return this.attr('menu_title', v);
		},

		selectMenuList : function(v) {
			return this.attr('menu_list', v);
		},

		selectMenuItem : function(v) {
			return this.attr('menu_item', v);
		},

		init : function(query) {
			this.setupMenuList(query);
		},

		setupMenuList : function(query) {

			var self = this;

			var sel_menu = this.selectMenu();
			var sel_menu_title = this.selectMenuTitle();
			var sel_menu_list = this.selectMenuList();
			var sel_menu_item = this.selectMenuItem();

			query = query.find(sel_menu);
			var q_menu = query;
			var q_menu_title = query.find(sel_menu_title);
			var q_menu_list = query.find(sel_menu_list);
			var q_menu_item = query.find(sel_menu_item);

			var model = this.create_model();
			var builder = new ListBuilder();

			builder.view(q_menu_list);
			builder.model(model);
			builder.onSelectItem(function(model, index) {
				return sel_menu_item;
			});

			// items

			builder.addItem(sel_menu_item).onCreate(function(item) {
				var view = item.view();
				var btn = view.find('.li-label');
				btn.click(function() {
					self.fireOnClickItem(item);
				});
			}).onUpdate(function(item) {
				var view = item.view();
				var ipw = item.data();
				var title = ipw.title();
				var label = view.find('.li-label');
				label.text(title);
			});

			this._panel_list_menu = query;
			this._list_ctrl = builder.create();
			this.update();

		},

		fireOnClickItem : function(item) {
			var ipw = item.data();
			var name = ipw.name();
			this.setCurrentPanel(name);
		},

		create_model : function() {
			var list = this._panel_list;
			return new PanelListModel(list);
		},

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
				var title = ipw.title();
				this.setCurrentTitle(title);
			}

		},

		setCurrentTitle : function(title) {
			var menu = this._panel_list_menu;
			if (menu != null) {
				menu.find('.menu-title').text(title);
			}
		},

		update : function() {

			var p = this._parent;
			var c = this._child;
			if (p != null) {
				p.empty();
				if (c != null) {
					p.append(c);
				}
			}

			// menu model
			var ctrl = this._list_ctrl;
			var model = this.create_model();
			if (ctrl != null) {
				ctrl.model(model);
				ctrl.update(true);
			}

			// menu visible
			var menu = this._panel_list_menu;
			if (menu != null) {
				if (model.size() > 0) {
					menu.show();
				} else {
					menu.hide();
				}
			}

			// menu title
			var ipw = this._cur_ipw;
			if (ipw != null) {
				var title = ipw.title();
				this.setCurrentTitle(title);
			}

		},

		addPanel : function(panel) {

			var context = this._context;
			panel = new PanelWrapper(context, panel);

			var table = this._panel_table;
			var list = this._panel_list;

			var key = panel.name();
			table[key] = panel;
			list.push(panel);

		},

		setCurrentPanel : function(name) {

			var table = this._panel_table;
			var ipw = table[name]; // PanelWrapper

			if (ipw == null) {
				return;
			}

			ipw.load(function() {
			});

			this.setCurrentIPW(ipw);

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

	function HeadPanel(context, name) {

		this._context = context;

		if (name == null) {
			name = 'a-head-panel';
		}

		var ui = $('<div></div>');

		this.domURL('~/lib/export/h2o/html/HeadPanel.html');
		this.domSelector('.panel');
		this.name(name);
		this.title(name);
		this.ui(ui);

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

		loaded : function(value) {
			return this.attr('loaded', value);
		},

		onShow : function() {
			// alert('on_show');
		},

		onHide : function() {
			// alert('on_hide');
		},

		onLoad : function() {
			// alert('on_load');

			var ui = this.ui();
			var title = this.title();
			ui.find('.panel-title').text(title);

		},

	};

	/***************************************************************************
	 * inner class PanelWrapper
	 */

	function PanelWrapper(context, panel) {
		this._context = context;
		this._panel = panel;
	}

	PanelWrapper.prototype = {

		name : function() {
			return this._panel.name();
		},

		title : function() {
			return this._panel.title();
		},

		ui : function() {
			return this._panel.ui();
		},

		load : function(fn /* () */) {

			if (fn == null) {
				fn = function() {
				};
			}

			var panel = this._panel;

			var ui = panel.ui();
			var url = panel.domURL();
			var selector = panel.domSelector();
			var loaded = panel.loaded();
			var context = this._context;
			var self = this;

			if (loaded) {
				fn();
			} else {
				panel.loaded(true);
				var loader = new ResourceLoader(context);
				loader.loadHTML(url, function(query) {
					var q2 = query.find(selector);
					ui.empty();
					ui.append(q2);
					panel.onLoad();
					fn();
				});
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
