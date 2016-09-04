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

	var FileListCtrl = mc.import('com.boluozhai.h2o.widget.FileListCtrl');
	var PathBarCtrl = mc.import('com.boluozhai.h2o.widget.PathBarCtrl');
	var DirDataCtrl = mc.import('com.boluozhai.h2o.widget.DirDataCtrl');

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

		init : function() {

			var context = this._context;

			var dir_data_ctrl = new DirDataCtrl(context);
			var filelist_ctrl = new FileListCtrl(context);
			var path_bar_ctrl = new PathBarCtrl(context);

			path_bar_ctrl.dataSource(dir_data_ctrl);
			path_bar_ctrl.parent(this._jq_pathlist);

			filelist_ctrl.dataSource(dir_data_ctrl);
			filelist_ctrl.parent(this._jq_filelist);

			dir_data_ctrl.init();
			filelist_ctrl.init();
			path_bar_ctrl.init();

			this._dir_data_ctrl = dir_data_ctrl;
		},

		load : function(base_path, offset_elements) {
			this._dir_data_ctrl.load(base_path, offset_elements);
		},

	};

});

/*******************************************************************************
 * EOF
 */
