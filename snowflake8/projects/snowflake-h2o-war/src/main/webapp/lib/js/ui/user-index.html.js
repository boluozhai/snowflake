/*******************************************************************************
 * 
 * user-index.html.js
 * 
 * @Copyright (c) 2016 boluozhai.com
 * @License MIT License
 * @Author xukun<xukun17@sina.com>
 * 
 */

JS.module(function(mc) {

	mc.package('com.boluozhai.h2o.html');

	var System = mc.import('js.lang.System');
	var ListBuilder = mc.import('snowflake.view.list.ListBuilder');
	var ListModel = mc.import('snowflake.view.list.ListModel');
	var Attributes = mc.import('js.lang.Attributes');
	var RESTClient = mc.import('snowflake.rest.RESTClient');

	var widget_x = 'com.boluozhai.h2o.widget';
	var HeadCtrl = mc.import(widget_x + '.head.HeadCtrl');
	var HeadPanel = mc.import(widget_x + '.head.HeadPanel');
	var FootCtrl = mc.import(widget_x + '.foot.FootCtrl');

	var Viewport = mc.import('com.boluozhai.snowflake.web.Viewport');
	var HtmlCtrl = mc.use(snowflake.html.HtmlCtrl);
	var JSONRestRequest = mc.use(snowflake.JSONRestRequest);

	/***************************************************************************
	 * class MyHtmlCtrl
	 */

	function MyHtmlCtrl(context) {
		this.HtmlCtrl(context);
	}

	mc.class(function(cc) {
		cc.type(MyHtmlCtrl);
		cc.extends(HtmlCtrl);
	});

	MyHtmlCtrl.prototype = {

		onCreate : function() {

			this.setupPageHead();
			this.setupPageFoot();

			this.setupRepoListHead();
			this.setupRepoList();

		},

		setupPageHead : function() {

			var context = this._context;
			var self = this;

			// head
			var head_ctrl = new HeadCtrl(context);
			this._head_ctrl = head_ctrl;
			head_ctrl.binder().parent('#page-head');
			head_ctrl.init();

			var panel_a = new HeadPanel(context, 'Panel_A');
			var panel_b = new HeadPanel(context, 'Panel_B');
			// head_ctrl.addPanel(panel_a);
			// head_ctrl.addPanel(panel_b);
			head_ctrl.setCurrentPanel(panel_b.name());
		},

		setupPageFoot : function() {

			var context = this._context;
			var self = this;

			// foot
			var foot_ctrl = new FootCtrl(context);
			this._foot_ctrl = foot_ctrl;
			foot_ctrl.binder().parent('#page-foot');
			foot_ctrl.init();
		},

		setupRepoListHead : function() {

			var vpt = new Viewport();
			var nickname = vpt.ownerNickname();
			var uid = vpt.ownerUid();

			$('.owner-nickname').text(nickname);
			$('.owner-uid').text(uid);

		},

		setupRepoList : function() {

			var context = this._context;
			var builder = new ListBuilder();

			builder.view($('#repo-list'));
			builder.model(new RepoListModel());
			builder.onSelectItem(function(model, i) {
				return '.list-item';
			});

			builder.addHead('.list-head').onCreate(function(item) {

			}).onUpdate(function(item) {

			});

			builder.addItem('.list-item').onCreate(function(item) {

				var data = item.data();
				var view = item.view();

				var owner = data.f_owner();
				var uid = owner.f_uid();
				var repo = data.f_name();

				var url = '~/{uid}/{rid}/'
				url = url.replace('{uid}', uid);
				url = url.replace('{rid}', repo);
				url = context.normalizeURL(url);

				var tag_a = view.find('.repo-href');
				tag_a.attr('href', url);

			}).onUpdate(function(item) {

				var data = item.data();
				var view = item.view();

				var name = data.f_name();
				var description = data.f_description();
				var is_default = data.f_theDefault();

				var text_default = null; // ? text_default : '';
				if (is_default) {
					var i18n = context.getBean('i18n');
					text_default = '(' + i18n.getString('default') + ')';
				} else {
					text_default = '';
				}

				view.find('.repo-name').text(name);
				view.find('.repo-description').text(description);
				view.find('.repo-is-default').text(text_default);

			});

			// builder. addFoot ( '' ) ;

			this._repo_list_view = builder.create();

			this.loadRepoList();

		},

		loadRepoList : function() {
			var self = this;
			var vpt = new Viewport();
			var uid = vpt.ownerUid();
			if (uid == null) {
				// 404
				return;
			}
			var jrr = new JSONRestRequest(this._context);
			var tx = jrr.open('GET', {
				uid : uid,
				repo : 'r',
				api : 'user-api',
				type : 'repository',
				id : '',
			});
			jrr.onResult(function() {
				// alert('load done');
				self.onRepoListDataReady(jrr);
			});
			jrr.send();
		},

		onRepoListDataReady : function(jrr) {
			var ent = jrr.responseEntity();
			var list = ent.f_list();
			var model = new RepoListModel(list);

			var listview = this._repo_list_view;
			listview.model(model);

			// listview.update();
			listview.update(true);
		},

	};

	/***************************************************************************
	 * class RepoListModel
	 */

	function RepoListModel(list) {
		if (list == null) {
			list = [];
		}
		this._list = list;
	}

	RepoListModel.prototype = {

		size : function() {
			return this._list.length;
		},

		get : function(index) {
			return this._list[index];
		},

	};

	/***************************************************************************
	 * main
	 */

	$(document).ready(function() {
		var ctrl = new MyHtmlCtrl();
		ctrl.init();
	});

});

/*******************************************************************************
 * EOF
 */
