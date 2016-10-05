/*******************************************************************************
 * 
 * viewport.js
 * 
 * @Copyright (c) 2016 boluozhai.com
 * @License MIT License
 * @Author xukun<xukun17@sina.com>
 * 
 */

JS.module(function(mc) {

	mc.package('com.boluozhai.snowflake.web');

	var System = mc.import('js.lang.System');
	var Attributes = mc.import('js.lang.Attributes');
	// var SHA1 = mc.import('snowflake.security.sha1.SHA1');

	// var widget_x = 'com.boluozhai.h2o.widget';

	// var FileListCtrl = mc.import(widget_x + '.folder.FileListCtrl');
	// var PathBarCtrl = mc.import(widget_x + '.folder.PathBarCtrl');
	// var DirDataCtrl = mc.import(widget_x + '.folder.DirDataCtrl');
	// var ConsoleCtrl = mc.import(widget_x + '.console.ConsoleCtrl');

	// var REST = mc.import('com.boluozhai.....REST');
	var REST = snowflake.rest.REST;

	function InnerHolder(context) {
		this._context = context;
	}

	InnerHolder.getInstance = function(context) {
		var inst = InnerHolder.instance;
		if (inst == null) {
			inst = new InnerHolder(context);
			inst.load();
			InnerHolder.instance = inst;
		}
		return inst;
	};

	// InnerHolder.init = function() {
	// var inst = InnerHolder.getInstance();
	// inst.load();
	// };

	InnerHolder.prototype = {

		load : function() {

			var path = window.location.pathname;
			var client = REST.getClient(this._context);

			var path_map = client.parsePath(path);
			var user = this.getPathPart('uid', path_map, true);
			var repo = this.getPathPart('repo', path_map, true);

			this._uid = user;
			this._repo_id = repo;

		},

		getPathPart : function(key, path_map, required) {
			var value = path_map[key];
			if (value == null) {
				if (required) {
					var msg = 'no field named[' + key + '] in the path.';
					this.onError(msg);
				}
			}
			return value;
		},

		onError : function(msg) {

			var debug_mode = true;

			this._error = msg;

			if (debug_mode) {
				throw new RuntimeException(msg);
			}

		},

		error : function(msg) {
			return this._error;
		},

		user : function() {
			return this._uid;
		},

		repo : function() {
			return this._repo_id;
		},

	};

	// InnerHolder.init();

	/***************************************************************************
	 * class ViewportInfo
	 */

	function ViewportInfo(context) {

		var inst = InnerHolder.getInstance(context);

		this._owner = inst.user();
		this._repo = inst.repo();
		this._err = inst.error();
		this._str = inst.user() + '/' + inst.repo();

	}

	mc.class(function(cc) {
		cc.type(ViewportInfo);
		// cc.extends(Attributes);
	});

	ViewportInfo.prototype = {

		owner : function() {
			return this._owner;
		},

		repository : function() {
			return this._repo;
		},

		toString : function() {
			return this._str;
		},

	};

});

/*******************************************************************************
 * EOF
 */
