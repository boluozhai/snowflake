/*******************************************************************************
 * 
 * core.js
 * 
 * @Copyright (c) 2016 boluozhai.com
 * @License MIT License
 * @Author xukun<xukun17@sina.com>
 * 
 */

function Snowflake() {
}

Snowflake.init = function(root) {

	/***************************************************************************
	 * type Snowflake
	 */

	var singleton = {

		sfcore : null,
		root : null,
		class_table : {},

	};

	singleton.root = root;

	Snowflake.getInstance = function() {
		var inst = singleton.sfcore;
		if (inst == null) {
			inst = new Snowflake();
			singleton.sfcore = inst;
		}
		return inst;
	};

	Snowflake.prototype = {

		newModuleContext : function() {
			var t_mc = ModuleContext;
			return new t_mc(this);
		},

		registerClass : function(cc) {
			var key = cc._this_class_name;
			var tab = singleton.class_table;
			tab[key] = cc;
		},

		getClassTable : function() {
			var tab = singleton.class_table;
			return tab;
		},

		getClassLoader : function() {
			var cl = this._class_loader;
			if (cl == null) {
				cl = new singleton.root.js.lang.ClassLoader(this);
				this._class_loader = cl;
			}
			return cl;
		},

	};

	Snowflake.module = function(fn) {

		var sf = Snowflake.getInstance();
		var mc = sf.newModuleContext();

		fn(mc);

		mc.load();

	};

	/***************************************************************************
	 * type ModuleContext
	 */

	function ModuleContext(sfcore) {
		this._class_list = [];
		this._sfcore = sfcore;
	}

	ModuleContext.prototype = {

		package : function(pkgName) {
			var old = this._package_name;
			if (old != null) {
				throw new Exception("the package name override!");
			}
			this._package_name = pkgName;
		},

		class : function(fn) {
			var cc = new ClassContext(this, fn);
			this._class_list.push(cc);
		},

		load : function() {
			this.inner_load_1();
			this.inner_load_2();
			this.inner_load_3();
		},

		registerClass : function(cc) {
			var sfcore = this._sfcore;
			sfcore.registerClass(cc);
		},

		inner_load_1 : function() {
			var clist = this._class_list;
			for ( var index in clist) {
				clist[index].inner_load_begin();
			}
		},

		inner_load_2 : function() {
			var clist = this._class_list;
			for ( var index in clist) {
				clist[index].inner_do_load();
			}
		},

		inner_load_3 : function() {
			var clist = this._class_list;
			for ( var index in clist) {
				clist[index].inner_load_end();
			}
		},

		import : function(className) {
			var type = singleton.root.js.lang.Class.forName(className);
			return type._class_context._this_class;
		},

		getClassLoader : function() {
			return this._sfcore.getClassLoader();
		},

	};

	/***************************************************************************
	 * type ClassContext
	 */

	function ClassContext(mc, fn) {
		this._mc = mc;
		this._loader = fn;
	}

	ClassContext.byFn = function(t, cc) {
		var key = '__Class_Context__';
		if (cc == null) {
			// get
			cc = t[key];
		} else {
			// set
			t[key] = cc;
		}
		return cc;
	};

	ClassContext.prototype = {

		extends : function(t) {
			this._super_class = t;
		},

		type : function(t) {
			this._this_class = t;
		},

		inner_type_old_ : function() {

			var t = this._this_class;
			var ext = this._super_class;

			if (ext == null) {
				ext = singleton.root.js.lang.Object;
				this._super_class = ext;
			}

			var def_methods = {};
			this.inner_copy_methods(t.prototype, def_methods);
			this._defined_methods = def_methods;

			// this._this_class = t;
			this._this_package_name = this._mc._package_name;
			this._this_class_simple_name = this.inner_get_func_name(t);
			this._this_class_name = this._this_package_name + '.'
					+ this._this_class_simple_name;

			def_methods[this._this_class_simple_name] = t;

			ClassContext.byFn(t, this);

		},

		inner_get_func_name : function(fn) {
			var ret = fn.toString();
			ret = ret.substr('function '.length);
			ret = ret.substr(0, ret.indexOf('('));
			return ret.trim();
		},

		inner_load_begin : function() {

			var fn = this._loader;
			fn(this);

			this.inner_type_old_();

			this.inner_setup_methods();
			this.inner_setup_class_object();
			this.inner_setup_to_root();

		},

		inner_do_load : function() {

		},

		inner_load_end : function() {

			var mc = this._mc;
			mc.registerClass(this);

			// setup Func.class
			var fn = this._this_class;
			var clazz = this.inner_get_class();
			fn.class = clazz;

		},

		inner_setup_methods : function() {
			var pt = this.inner_make_prototype();
			this._this_class.prototype = pt;
		},

		inner_setup_class_object : function() {
			var cc = this;
			this._this_class.prototype.getClass = function() {
				return cc.inner_get_class();
			};
		},

		inner_setup_to_root : function() {

			var p_name = this._this_package_name;
			var s_name = this._this_class_simple_name;
			var array = p_name.split('.');
			var root = singleton.root;

			var p = root;
			for (var i = 0; i < array.length; i++) {
				var key = array[i];
				var next = p[key];
				if (next == null) {
					next = {};
					p[key] = next;
				}
				p = next;
			}
			p[s_name] = this._this_class;

		},

		inner_make_prototype : function() {
			var tab = {};
			var all = this.inner_all_methods();
			this.inner_copy_methods(all, tab);
			return tab;
		},

		inner_copy_methods : function(from, to) {
			for ( var key in from) {
				var fn = from[key];
				to[key] = fn;
			}
		},

		inner_all_methods : function() {
			var all = this._all_methods;
			if (all == null) {
				var def = this.inner_defined_methods();
				var inh = this.inner_inher_methods();
				all = {};
				this.inner_copy_methods(inh, all);
				this.inner_copy_methods(def, all);
				this._all_methods = all;
			}
			return all;
		},

		inner_inher_methods : function() {
			var tab = this._inherited_methods;
			if (tab == null) {
				var parent_fn = this.inner_get_super_class();
				var parent_cc = ClassContext.byFn(parent_fn);
				if (parent_cc == null) {
					tab = {};
				} else {
					tab = parent_cc.inner_all_methods();
				}
				this._inherited_methods = tab;
			}
			return tab;
		},

		inner_defined_methods : function() {
			return this._defined_methods;
		},

		inner_get_super_class : function() {
			return this._super_class;
		},

		inner_get_class : function() {
			var c = this._class;
			if (c == null) {
				c = new Snow_Object_Class(this);
				this._class = c;
			}
			return c;
		},

		getClassLoader : function() {
			return this._mc.getClassLoader();
		},

	};

};

Snowflake.init(this);

this.JS = this.Snowflake;

/*******************************************************************************
 * package js.lang.*
 */

JS.module(function(_mc_) {

	_mc_.package('js.lang');

	/***************************************************************************
	 * class BaseObject
	 */

	function BaseObject() {
	}

	BaseObject.hash_count = 0;

	function next_object_hash_code() {
		return (++BaseObject.hash_count);
	}

	/***************************************************************************
	 * class Object
	 */

	function Object() {
	}

	_mc_.class(function(_cc_) {

		_cc_.type(Object);
		_cc_.extends(BaseObject);

	});

	Object.prototype = {

		equals : function(obj) {
			return (this == obj);
		},

		getClass : function() {
			// override by classLoader
		},

		hashCode : function() {
			var code = this._hash_code;
			if (code == null) {
				code = next_object_hash_code();
				this._hash_code = code;
			}
			return code;
		},

		toString : function() {
			var hash = this.hashCode();
			var cn = this.getClass().getName();
			return (cn + '@' + hash);
		},

	};

	/***************************************************************************
	 * class Class
	 */

	function Class(cc) {
		this.Object();
		this._class_context = cc;
	}

	_mc_.class(function(_cc_) {

		_cc_.type(Class);
		_cc_.extends(Object);

	});

	Class.prototype = {

		getName : function() {
			return this._class_context._this_class_name;
		},

		newInstance : function(p1, p2, p3, p4, p5, p6, p7, p8) {
			var t = this._class_context._this_class;
			return new t(p1, p2, p3, p4, p5, p6, p7, p8);
		},

		getClassLoader : function() {
			var cc = this._class_context;
			return cc.getClassLoader();
		},

	};

	Class.forName = function(name) {
		var sf = Snowflake.getInstance();
		var table = sf.getClassTable();
		var cc = table[name];
		if (cc == null) {
			var msg = "class not found: " + name;
			throw new Exception(msg);
		}
		return cc.inner_get_class();
	};

	Snow_Object_Class = Class;

	/***************************************************************************
	 * class ClassLoader
	 */

	function ClassLoader(sfcore) {
		this._sfcore = sfcore;
	}

	_mc_.class(function(cc) {
		cc.type(ClassLoader);
	});

	ClassLoader.prototype = {

		listClassNames : function() {
			var sf = this._sfcore;
			var tab = sf.getClassTable();
			var array = [];
			for ( var key in tab) {
				array.push(key);
			}
			return array.sort();
		},

	};

});

// this.Object = this.js.lang.Object;
// this.Class = this.js.lang.Class;

JS.module(function(_mc_) {

	// TODO js.io.*
	_mc_.package('js.io');

	/***************************************************************************
	 * class PrintStream
	 */

	function PrintStream() {
	}

	_mc_.class(function(_cc_) {
		_cc_.type(PrintStream);
		// _cc_.extends(Object);
	});

	PrintStream.prototype = {

		println : function() {
			// TODO
		},

	};

});

JS.module(function(mc) {

	// TODO js.util.*
	mc.package('js.util');

	/***************************************************************************
	 * class Date
	 */

	function Calendar(time) {
		var date = new Date();
		if (time != null) {
			date.setTime(time);
		}
		this._date = date;
	}

	mc.class(function(cc) {
		cc.type(Calendar);
	});

	Calendar.prototype = {

		timeInMillis : function(value) {
			if (value == null) {
				value = this._date.getTime();
			} else {
				this._date.setTime(value);
			}
			return value;
		},

		toString : function() {
			return this._date.toLocaleString();
		},

	};

});

JS.module(function(_mc_) {

	// TODO js.lang.*
	_mc_.package('js.lang');

	var PrintStream = js.io.PrintStream;

	/***************************************************************************
	 * class Exception
	 */

	function Exception(message) {
		this.Object();
		this._message = message;
	}

	_mc_.class(function(cc) {
		cc.type(Exception);
	});

	Exception.prototype = {

		toString : function() {
			return this._message;
		},

	};

	/***************************************************************************
	 * class RuntimeException
	 */

	function RuntimeException(message) {
		this.Exception(message);
	}

	_mc_.class(function(cc) {
		cc.type(RuntimeException);
		cc.extends(Exception);
	});

	RuntimeException.prototype = {};

	/***************************************************************************
	 * class System
	 */

	function System() {
	}

	_mc_.class(function(_cc_) {
		_cc_.type(System);
		// _cc_.extends(Object);
	});

	System.prototype = {

	};

	System.currentTimeMillis = function() {
		var d = new Date();
		return d.getTime();
	};

	System.out = new PrintStream();

	System.err = new PrintStream();

	System.setOut = function(ps) {
		System.out = ps;
	};

	System.setErr = function(ps) {
		System.err = ps;
	};

	/***************************************************************************
	 * class Attributes
	 */

	function Attributes() {
	}

	_mc_.class(function(cc) {
		cc.type(Attributes);
	});

	Attributes.prototype = {

		attributes : function() {
			var tab = this.__attributes__;
			if (tab == null) {
				tab = {};
				this.__attributes__ = tab;
			}
			return tab;
		},

		attr : function(key, value) {
			var tab = this.attributes();
			if (value == null) {
				value = tab[key];
			} else {
				tab[key] = value;
			}
			return value;
		},

	};

});

/*******************************************************************************
 * EOF
 */
