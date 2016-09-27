/*******************************************************************************
 * 
 * directory.html.js
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

	var FileListCtrl = mc.import(widget_x + '.folder.FileListCtrl');
	var PathBarCtrl = mc.import(widget_x + '.folder.PathBarCtrl');
	var ConsoleCtrl = mc.import(widget_x + '.console.ConsoleCtrl');

	var CurrentLocation = mc
			.import('com.boluozhai.snowflake.vfs.CurrentLocation');

	var vfs_x = 'com.boluozhai.snowflake.vfs';
	var CurrentLocation = mc.import(vfs_x + '.CurrentLocation');
	var VFSFactory = mc.import(vfs_x + '.VFSFactory');

	/***************************************************************************
	 * class DirectoryHtml
	 */

	function DirectoryHtml(context) {
		this._context = context;
	}

	mc.class(function(cc) {
		cc.type(DirectoryHtml);
		cc.extends(Attributes);
	});

	DirectoryHtml.prototype = {

		init : function() {

			var self = this;
			var context = this._context;

			var cl = new CurrentLocation(context);
			var console_ctrl = new ConsoleCtrl(context);
			var path_bar_ctrl = new PathBarCtrl(context);
			var filelist_ctrl = new FileListCtrl(context);

			this._cur_location = cl;
			this._console_ctrl = console_ctrl;
			this._path_bar_ctrl = path_bar_ctrl;
			this._filelist_ctrl = filelist_ctrl;

			console_ctrl.currentLocation(cl);
			path_bar_ctrl.currentLocation(cl);
			filelist_ctrl.currentLocation(cl);

			var path_bar_head = $('#directory-path-bar-head');
			this.setupFsRootButton(path_bar_head);

			console_ctrl.binder().parent('#console');
			path_bar_ctrl.binder().parent('#path-bar');
			path_bar_ctrl.binder().head(path_bar_head);
			filelist_ctrl.binder().parent('#file-list');

			console_ctrl.init();
			path_bar_ctrl.init();
			filelist_ctrl.init();

			// vfs
			var vfs_factory = new VFSFactory();
			vfs_factory.httpURI('~/rest/uid/repo/file');
			var vfs = vfs_factory.create(context);
			this._vfs = vfs;
			vfs.ready(function() {
				self.gotoRoot();
			});

		},

		setupFsRootButton : function(query) {
			var self = this;
			query.find('.btn').click(function() {
				self.gotoRoot();
			});
		},

		gotoRoot : function() {

			var cl = this._cur_location;
			var vfs = this._vfs;

			var root = vfs.root();
			cl.location(root);

		},

	};

});

/*******************************************************************************
 * EOF
 */
