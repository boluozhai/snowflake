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

	function InnerHolder() {
	}

	InnerHolder.getInstance = function() {
		var inst = InnerHolder.instance;
		if (inst == null) {
			inst = new InnerHolder();
			InnerHolder.instance = inst;
		}
		return inst;
	};

	InnerHolder.init = function() {
		var inst = InnerHolder.getInstance();
		inst.load();
	};

	InnerHolder.prototype = {

		load : function() {

			var url = window.location.href;

			var uid = this.getQueryParam(url, 'uid');
			var repoid = this.getQueryParam(url, 'repoid');

			this._uid = uid;
			this._repo_id = repoid;

		},

		getQueryParam : function(url, key) {

			var i0 = url.indexOf('#');
			if (i0 >= 0) {
				url = url.substring(0, i0);
			}

			i0 = url.indexOf('?');
			if (i0 < 0) {
				return this.onError('no param: ' + key);
			}

			var i1 = url.indexOf(key + '=', i0);
			if (i1 < 0) {
				return this.onError('no param: ' + key);
			}

			var i2 = url.indexOf('&', i1);
			if (i2 < 0) {
				return url.substring(i1 + key.length + 1);
			} else {
				return url.substring(i1 + key.length + 1, i2);
			}

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

	InnerHolder.init();

	/***************************************************************************
	 * class ViewportInfo
	 */

	function ViewportInfo() {

		var inst = InnerHolder.getInstance();

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
