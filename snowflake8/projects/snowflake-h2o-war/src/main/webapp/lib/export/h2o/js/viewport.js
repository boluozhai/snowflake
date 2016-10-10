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

	var SessionInfo = mc.import('com.boluozhai.h2o.web.SessionInfo');

	// var REST = mc.import('com.boluozhai.....REST');
	var REST = snowflake.rest.REST;
	var JSONRestRequest = mc
			.import("com.boluozhai.snowflake.rest.api.JSONRestRequest");

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
			var user = this.getPathPart('uid', path_map, false);
			var repo = this.getPathPart('repo', path_map, false);

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
		this._context = context;

	}

	mc.class(function(cc) {
		cc.type(ViewportInfo);
		// cc.extends(Attributes);
	});

	ViewportInfo.prototype = {

		owner : function(def) {
			var value = this._owner;
			if (value == null) {
				value = def;
			}
			return value;
		},

		uid : function(def) {
			return this.owner(def);
		},

		repository : function(def) {
			var value = this._repo;
			if (value == null) {
				value = def;
			}
			return value;
		},

		toString : function() {
			return this._str;
		},

		getDetailLoader : function() {
			return new ViewportDetailLoader(this);
		},

	};

	/***************************************************************************
	 * class ViewportDetailLoader
	 */

	function ViewportDetailLoader(info) {
		this._info = info;
		this._context = info._context;
	}

	ViewportDetailLoader.prototype = {

		load : function(fn) {

			var self = this;
			var context = this._context;
			var info = this._info;
			var user = info.owner('u');
			var repo = info.repository('r');

			var jrr = new JSONRestRequest(context);
			var tx_entity = jrr.open('GET', {
				uid : user,
				repo : repo,
				api : 'rest',
				type : 'viewport',
				id : 'a-id',
			});
			jrr.onResult(function() {
				if (jrr.ok()) {
					self.result(jrr.responseEntity());
				}
				fn();
			});
			jrr.send(tx_entity);

		},

		result : function(value) {
			if (value == null) {
				value = this._result;
			} else {
				this._result = value;
			}
			return value;
		},

	};

	/***************************************************************************
	 * class WebPageController
	 */

	function WebPageController(context) {
		this.Attributes();
		this._context = context;
		this.inner_load_context_atts(context);
		context.i18n($(document));
	}

	mc.class(function(cc) {
		cc.type(WebPageController);
		cc.extends(Attributes);
	});

	WebPageController.prototype = {

		init : function() {
			throw new Exception('override this method in sub-class');
		},

		inner_load_context_atts : function(context) {

			var session = new SessionInfo();
			var vpt = new ViewportInfo(context);

			var ses_uid = session.uid();
			var vpt_uid = vpt.uid();
			var vpt_repo_id = vpt.repository();

			context.attr('session-uid', ses_uid);
			context.attr('viewport-uid', vpt_uid);
			context.attr('viewport-repo-id', vpt_repo_id);

		},

	};

});

/*******************************************************************************
 * EOF
 */
