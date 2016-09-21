/*******************************************************************************
 * 
 * Directory.js
 * 
 * @Copyright (c) 2016 boluozhai.com
 * @License MIT License
 * @Author xukun<xukun17@sina.com>
 * 
 */

JS.module(function(mc) {

	mc.package('com.boluozhai.h2o.webui');

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

	/***************************************************************************
	 * class DirectoryCtrl
	 */

	function DirectoryCtrl(context) {
		this._context = context;
	}

	mc.class(function(cc) {
		cc.type(DirectoryCtrl);
		cc.extends(Attributes);
	});

	DirectoryCtrl.prototype = {

		selectClient : function(sel) {
			this._jq_client = $(sel);
		},

		selectPathList : function(sel) {
			this._jq_pathlist = $(sel);
		},

		selectFileList : function(sel) {
			this._jq_filelist = $(sel);
		},

		selectConsole : function(sel) {
			this._jq_console = $(sel);
		},

		init : function() {

			var context = this._context;

			var dir_data_ctrl = new DirDataCtrl(context);
			var filelist_ctrl = new FileListCtrl(context);
			var path_bar_ctrl = new PathBarCtrl(context);
			var console_ctrl = new ConsoleCtrl(context);

			path_bar_ctrl.dataSource(dir_data_ctrl);
			path_bar_ctrl.parent(this._jq_pathlist);

			filelist_ctrl.dataSource(dir_data_ctrl);
			filelist_ctrl.parent(this._jq_filelist);

			console_ctrl.parent(this._jq_console);

			dir_data_ctrl.init();
			filelist_ctrl.init();
			path_bar_ctrl.init();
			console_ctrl.init();

			var dir_data_model = dir_data_ctrl.model();
			dir_data_model.addEventHandler(this);
			this._dir_data_ctrl = dir_data_ctrl;
			this._dir_data_model = dir_data_model;
			this._console_ctrl = console_ctrl;
		},

		load : function(base_path, offset_elements) {
			this._dir_data_ctrl.load(base_path, offset_elements);
		},

		onEvent : function(event) {
			// TODO alert(this.getClass().getName() + '.onEvent()');

			var dir_data_model = this._dir_data_model;
			var src = event.source();
			if (src == dir_data_model) {
				var fileURI = src.fileURI();
				var console_ctrl = this._console_ctrl;
				console_ctrl.currentPathURI(fileURI);
			}

		},

	};

});

/*******************************************************************************
 * EOF
 */
