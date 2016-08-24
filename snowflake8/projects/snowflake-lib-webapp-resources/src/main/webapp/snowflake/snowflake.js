/*******************************************************************************
 * Snowflake-JS core
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

			var def_methods = {};
			this.inner_copy_methods(t.prototype, def_methods);
			this._defined_methods = def_methods;

			this._this_class = t;
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

			this._loader(this);

			this.inner_setup_methods();
			this.inner_setup_class_object();
			this.inner_setup_to_root();

		},

		inner_do_load : function() {

		},

		inner_load_end : function() {

			var mc = this._mc;
			mc.registerClass(this);

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
				var parent_fn = this._super_class;
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

		inner_get_class : function() {
			var c = this._class;
			if (c == null) {
				c = new Snow_Object_Class(this);
				this._class = c;
			}
			return c;
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

	/***************************************************************************
	 * class Object
	 */

	function Object() {
		this._hash_code = (++BaseObject.hash_count);
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
			return this._hash_code;
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

});

this.Object = this.js.lang.Object;
this.Class = this.js.lang.Class;

JS.module(function(_mc_) {

	_mc_.package('js.io');

	/***************************************************************************
	 * class PrintStream
	 */

	function PrintStream() {
	}

	_mc_.class(function(_cc_) {

		_cc_.type(PrintStream);
		_cc_.extends(Object);

	});

	PrintStream.prototype = {

		println : function() {
			// TODO
		},

	};

});

JS.module(function(_mc_) {

	_mc_.package('js.lang');

	var PrintStream = js.io.PrintStream;

	/***************************************************************************
	 * class System
	 */

	function System() {
	}

	_mc_.class(function(_cc_) {
		_cc_.type(System);
		_cc_.extends(Object);
	});

	System.prototype = {

		xyz : function() {
			// TODO
		},

	};

	System.currentTimeMillis = function() {
		// TODO
	};

	System.out = new PrintStream();

	System.err = new PrintStream();

	System.setOut = function(ps) {
		System.out = ps;
	};

	System.setErr = function(ps) {
		System.err = ps;
	};

});

/*******************************************************************************
 * EOF
 */
