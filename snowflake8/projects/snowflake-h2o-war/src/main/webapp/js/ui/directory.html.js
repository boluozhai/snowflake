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
	var DirDataCtrl = mc.import(widget_x + '.folder.DirDataCtrl');
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

			var context = this._context;

			var cl = new CurrentLocation(context);
			var console_ctrl = new ConsoleCtrl(context);
			var path_bar_ctrl = new PathBarCtrl(context);
			// var filelist_ctrl = new FileListCtrl(context);

			this._cur_location = cl;
			this._console_ctrl = console_ctrl;
			this._path_bar_ctrl = path_bar_ctrl;
			// this._filelist_ctrl = filelist_ctrl;

			console_ctrl.currentLocation(cl);
			path_bar_ctrl.currentLocation(cl);
			// filelist_ctrl.currentLocation(cl);

			console_ctrl.binder().parent('#console');
			path_bar_ctrl.binder().parent('#path-bar');
			// filelist_ctrl.binder().parent('#file-list');

			console_ctrl.init();
			path_bar_ctrl.init();
			// filelist_ctrl.init();

			// vfs
			var vfs_factory = new VFSFactory();
			vfs_factory.httpURI('~/rest/file');
			var vfs = vfs_factory.create(context);
			vfs.ready(function() {
				var root = vfs.root();
				cl.location(root);
			});

		},

	};

});

/*******************************************************************************
 * EOF
 */
