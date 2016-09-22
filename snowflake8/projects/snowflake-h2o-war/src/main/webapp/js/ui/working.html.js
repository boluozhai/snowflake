/*******************************************************************************
 * 
 * working.html.js
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
	var HeadCtrl = mc.import(widget_x + '.head.HeadCtrl');

	var CurrentLocation = mc
			.import('com.boluozhai.snowflake.vfs.CurrentLocation');

	var vfs_x = 'com.boluozhai.snowflake.vfs';
	var CurrentLocation = mc.import(vfs_x + '.CurrentLocation');
	var VFSFactory = mc.import(vfs_x + '.VFSFactory');

	/***************************************************************************
	 * class WorkingHtml
	 */

	function WorkingHtml(context) {
		this._context = context;
	}

	mc.class(function(cc) {
		cc.type(WorkingHtml);
		cc.extends(Attributes);
	});

	var is_head_visible = false;

	WorkingHtml.prototype = {

		init : function() {

			var context = this._context;
			var self = this;

			var cl = new CurrentLocation(context);
			var head_ctrl = new HeadCtrl(context);
			var console_ctrl = new ConsoleCtrl(context);
			var path_bar_ctrl = new PathBarCtrl(context);
			var filelist_ctrl = new FileListCtrl(context);

			this._cur_location = cl;
			this._head_ctrl = head_ctrl;
			this._console_ctrl = console_ctrl;
			this._path_bar_ctrl = path_bar_ctrl;
			this._filelist_ctrl = filelist_ctrl;

			head_ctrl.currentLocation(cl);
			console_ctrl.currentLocation(cl);
			path_bar_ctrl.currentLocation(cl);
			filelist_ctrl.currentLocation(cl);

			head_ctrl.binder().parent('#page-head');
			// console_ctrl.binder().parent('#console');
			path_bar_ctrl.binder().parent('#path-bar');
			filelist_ctrl.binder().parent('#file-list');
			path_bar_ctrl.binder().head($('.path-bar-head'));
			path_bar_ctrl.binder().onCreateHead(function(item) {
				self.setupMagicButton(item);
			});

			head_ctrl.init();
			// console_ctrl.init();
			path_bar_ctrl.init();
			filelist_ctrl.init();

			// vfs
			var vfs_factory = new VFSFactory();
			vfs_factory.httpURI('~/rest/file/home/');
			var vfs = vfs_factory.create(context);
			vfs.ready(function() {
				var root = vfs.root();
				cl.location(root);
			});

			this.setupMagicButton(null);

		},

		setupMagicButton : function(item) {

			var head = $('#page-head');

			if (item == null) {

				head.hide();
				is_head_visible = false;

			} else {

				var speed = 50;

				var base = item.view();
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

			}

		},

	};

});

/*******************************************************************************
 * EOF
 */
