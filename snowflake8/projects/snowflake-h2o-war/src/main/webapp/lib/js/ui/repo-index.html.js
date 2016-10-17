/*******************************************************************************
 * 
 * user-home.html.js
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

	var Viewport = mc.use(snowflake.Viewport);
	var HtmlCtrl = mc.use(snowflake.html.HtmlCtrl);

	var CurrentLocation = mc
			.import('com.boluozhai.snowflake.vfs.CurrentLocation');

	var PathBarCtrl = mc.import('com.boluozhai.h2o.widget.folder.PathBarCtrl');

	var FileListCtrl = mc
			.import('com.boluozhai.h2o.widget.folder.FileListCtrl');

	var VFS = mc.use(snowflake.vfs.VFS);

	/***************************************************************************
	 * class RepoIndexHtml
	 */

	function RepoIndexHtml(context) {
		this.HtmlCtrl(context);
	}

	mc.class(function(cc) {
		cc.type(RepoIndexHtml);
		cc.extends(HtmlCtrl);
	});

	RepoIndexHtml.prototype = {

		onCreate : function() {

			var self = this;
			var context = this._context;

			var head_ctrl = new HeadCtrl(context);
			this._head_ctrl = head_ctrl;
			head_ctrl.binder().parent('#page-head');
			head_ctrl.init();

			var cl = new CurrentLocation(context);
			this._cur_location = cl;

			// path-bar
			this.setupPathBar(cl);

			// file-list
			this.setupFileList(cl);

			// vfs
			this.setupVFS();

		},

		setupPathBar : function(location) {
			var context = this._context;
			var path_bar_ctrl = new PathBarCtrl(context);
			this._path_bar_ctrl = path_bar_ctrl;
			path_bar_ctrl.currentLocation(location);

			var path_bar_head = $('.path-bar-head');
			this.setupPathBarHead(path_bar_head);

			path_bar_ctrl.binder().parent('#path-bar');
			path_bar_ctrl.binder().head(path_bar_head);
			// path_bar_ctrl.binder().onCreateHead(function(item) {
			// self.setupMagicButton(item);
			// });

			path_bar_ctrl.init();

		},

		setupFileList : function(location) {
			var context = this._context;
			var filelist_ctrl = new FileListCtrl(context);
			this._filelist_ctrl = filelist_ctrl;
			filelist_ctrl.currentLocation(location);
			filelist_ctrl.binder().parent('#file-list');

			filelist_ctrl.init();

		},

		___init : function() {

			var context = this._context;
			var self = this;

			// head

			var head_ctrl = new HeadCtrl(context);
			this._head_ctrl = head_ctrl;
			head_ctrl.currentLocation(cl);
			head_ctrl.binder().parent('#page-head');
			head_ctrl.init();

			// console

			var console_ctrl = new ConsoleCtrl(context);
			this._console_ctrl = console_ctrl;
			console_ctrl.currentLocation(cl);
			console_ctrl.binder().parent('#console');
			console_ctrl.init();

			// /////////////////////////

		},

		setupVFS : function() {

			// vfs

			var context = this._context;
			var vfs = VFS.newInstance(context);

			// vfs_factory.httpURI(this.genWorkingBaseHttpURI());

			this._vfs = vfs;
			vfs.root().load(function() {
				self.fireOnClickRootBtn();
			});

		},

		genWorkingBaseHttpURI : function() {

			var temp = '~/{user}/{repo}/rest/working/';
			var info = new Viewport();
			var user = info.ownerUid();
			var repo = info.repositoryName();

			temp = temp.replace('{user}', user);
			temp = temp.replace('{repo}', repo);

			return temp;
		},

		setupPathBarHead : function(query) {
			this.setupRootButton(query);
			this.setupMagicButton(query);
		},

		setupRootButton : function(q) {

			var context = this._context;

			var info = new Viewport();
			var user = info.ownerUid();
			var repo = info.repositoryName();

			var self = this;
			var btn = q.find('.h2o-root-button');
			btn.text(user + '^' + repo);
			btn.click(function() {
				self.fireOnClickRootBtn();
			});

		},

		setupMagicButton : function(q) {

			var head = $('#page-head---');
			var speed = 200;

			var base = q;
			var btn = base.find('.btn-h2o-magic');
			var mark = base.find('.mark-h2o-plus');

			btn.click(function() {
				if (!is_head_visible) {
					mark.text('-');
					head.show(speed);
					is_head_visible = true;
				} else {
					mark.text('+');
					head.hide(speed);
					is_head_visible = false;
				}
			});

			head.hide();
			is_head_visible = false;

		},

		fireOnClickRootBtn : function() {

			var vfs = this._vfs;
			var cl = this._cur_location;
			var root = vfs.root();
			cl.location(root);

		},

	};

	/***************************************************************************
	 * main
	 */

	$(document).ready(function() {

		var context = Snowflake.getContext();
		var ctrl = new RepoIndexHtml(context);
		ctrl.init();

	});

});

/*******************************************************************************
 * EOF
 */
