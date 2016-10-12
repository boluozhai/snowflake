/*******************************************************************************
 * 
 * context.js
 * 
 * @Copyright (c) 2016 boluozhai.com
 * @License MIT License
 * @Author xukun<xukun17@sina.com>
 * 
 */

JS.module(function(mc) {

	mc.package('snowflake.context');

	var Object = mc.import('js.lang.Object');
	var Class = mc.import('js.lang.Class');
	var Attributes = mc.import('js.lang.Attributes');
	var RuntimeException = mc.import('js.lang.RuntimeException');

	var singleton = {

		context : null,
		factory : null,

	};

	/***************************************************************************
	 * class Context
	 */

	function Context(beans) {
		this._bean_table = beans;
	}

	mc.class(function(cc) {
		cc.type(Context);
		cc.extends(Attributes);
	});

	Context.prototype = {

		getBean : function(name) {
			var bean = this._bean_table[name];
			if (bean == null) {
				var msg = "no bean named: " + name;
				throw new RuntimeException(msg);
			}
			return bean;
		},

		getApplicationName : function() {
			return null;
		},

		getDisplayName : function() {
			return null;
		},

		getParent : function() {
			return null;
		},

		getBeanDefinitionCount : function() {
			var tab = this._bean_table;
			var cnt = 0;
			for ( var key in tab) {
				cnt++;
			}
			return cnt;
		},

		getBeanDefinitionNames : function() {
			var tab = this._bean_table;
			var array = [];
			for ( var key in tab) {
				array.push(key);
			}
			return array;
		},

	};

	Context.getInstance = function() {
		var inst = singleton.context;
		if (inst == null) {
			var factory = singleton.factory;
			inst = factory.create();
			singleton.context = inst;
		}
		return inst;
	};

	/***************************************************************************
	 * class ContextFactory
	 */

	function ContextFactory() {
		this._on_create_listeners = [];
		this._beans_namelist = {};
	}

	mc.class(function(cc) {
		cc.type(ContextFactory);
	});

	ContextFactory.prototype = {

		create : function() {
			var beans = this.inner_new_beans();
			var context = this.inner_new_context(beans);
			// dispatch onCreate(fn)
			var listeners = this._on_create_listeners;
			for ( var i in listeners) {
				var li = listeners[i];
				li(context);
			}
			return context;
		},

		inner_new_context : function(beans) {
			return new Context(beans);
		},

		inner_new_beans : function() {
			var beans = {};
			var nlist = this._beans_namelist;
			for ( var key in nlist) {
				var cn = nlist[key];
				var type = Class.forName(cn);
				var inst = type.newInstance();
				beans[key] = inst;
			}
			return beans;
		},

		beans : function(namelist) {
			for ( var key in namelist) {
				var val = namelist[key];
				this._beans_namelist[key] = val;
			}
		},

		onCreate : function(fn/* ( context ) */) {
			if (fn == null) {
				return;
			} else {
				this._on_create_listeners.push(fn);
			}
		},

	};

	ContextFactory.setDefault = function(factory) {
		singleton.factory = factory;
	};

	ContextFactory.getDefault = function() {
		return singleton.factory;
	};

});

JS.module(function(mc) {

	mc.package('snowflake.web');

	var Context = mc.import('snowflake.context.Context');
	var ContextFactory = mc.import('snowflake.context.ContextFactory');

	/***************************************************************************
	 * class WebContext
	 */

	function WebContext(beans) {
		this.Context(beans);
		this._path_in_webapp = null;
	}

	mc.class(function(cc) {
		cc.type(WebContext);
		cc.extends(Context);
	});

	WebContext.prototype = {

		pathInWebapp : function() {
			return this._path_in_webapp;
		},

		normalizeURL : function(path) {

			// https://host.com/app/api/type
			// http://host.com/app/api/type
			// /app/api/type
			// ~/api/type
			// api/type

			if (path.indexOf('~/') != 0) {
				return path;
			}
			path = path.substring(1);
			var piw = this.pathInWebapp();
			var sb = null;
			for (var index = 0;;) {
				var next = piw.indexOf('/', index);
				if (next < 0) {
					break;
				}
				index = next + 1;
				if (sb == null) {
					sb = '.';
				} else {
					sb = sb + '/..';
				}
			}
			if (sb == null) {
				return path;
			} else {
				return (sb + path);
			}

		},

		getContextPath : function() {

			var base = '~/.';
			base = this.normalizeURL(base);

			var page_dir = window.location.pathname;
			var i_last_slash = page_dir.lastIndexOf('/');
			if (i_last_slash >= 0) {
				page_dir = page_dir.substring(0, i_last_slash);
			}

			var path = page_dir + '/' + base;
			var array = path.split('/');
			var a2 = [];

			for (var i = 0; i < array.length; i++) {
				var ele = array[i];
				if (ele == null) {
					// skip
				} else if (ele == '') {
					// skip
				} else if (ele == '.') {
					// skip
				} else if (ele == '..') {
					a2.pop();
				} else {
					a2.push(ele);
				}
			}

			path = '/';
			for ( var i in a2) {
				var ele = a2[i];
				path += (ele + '/');
			}

			return path;
		},

	};

	/***************************************************************************
	 * class WebContextFactory
	 */

	function WebContextFactory() {
		this.ContextFactory();
		this._path_in_webapp = null;
	}

	mc.class(function(cc) {
		cc.type(WebContextFactory);
		cc.extends(ContextFactory);
	});

	WebContextFactory.prototype = {

		/***********************************************************************
		 * @param path
		 *            like '~/index.html'
		 */

		pathInWebapp : function(path) {
			if (path == null) {
				path = this._path_in_webapp;
			} else {
				this._path_in_webapp = path;
			}
			return path;
		},

		inner_new_context : function(beans) {
			var wc = new WebContext(beans);
			wc._path_in_webapp = this.pathInWebapp();
			return wc;
		},

	};

	WebContextFactory.setDefault = function(factory) {
		ContextFactory.setDefault(factory);
	};

	WebContextFactory.getDefault = function() {
		return ContextFactory.getDefault();
	};

	/***************************************************************************
	 * class WebContextUtils
	 */

	function WebContextUtils() {
	}

	mc.class(function(cc) {
		cc.type(WebContextUtils);
	});

	WebContextUtils.init = function(fn) {

		var factory = WebContextFactory.getDefault();
		if (factory == null) {
			factory = new WebContextFactory();
			WebContextFactory.setDefault(factory);
		}

		fn(factory);
	};

});

/*******************************************************************************
 * EOF
 */
