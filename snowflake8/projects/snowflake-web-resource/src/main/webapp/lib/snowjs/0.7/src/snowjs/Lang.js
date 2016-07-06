/*******************************************************************************
 * Lang.js
 */

function CoreJS(base_package) {
	this.m_base_package = base_package;
}

this.JS = CoreJS;

/*******************************************************************************
 * class CoreJS
 */

CoreJS.module_ = function(name, callback) {
	var core = CoreJS.getInstance();
	var mc = core.newModuleContext();
	mc.name_(name);
	{
		callback(mc);
	}
	mc.load();
};

CoreJS._newInstance_ = function(base) {

	// define core classes

	/***************************************************************************
	 * Core functions
	 */

	function core_copy_key_and_values(src, dst) {
		for ( var key in src) {
			dst[key] = src[key];
		}
	}

	function core_make_extends(self_class, super_class) {

		var mid = {};

		// add constructor
		var func_name = core_get_function_name(self_class);
		mid[func_name] = self_class;

		// normal methods
		var src = super_class.prototype;
		var dst = self_class.prototype;
		core_copy_key_and_values(src, mid);
		core_copy_key_and_values(dst, mid);

		// add 'class_' prefix
		var prefix = func_name + "_";
		var tmp = mid;
		mid = {};
		for ( var key in tmp) {
			var func = tmp[key];
			mid[key] = func;
			mid[prefix + key] = func;
		}

		// flush to new class
		core_copy_key_and_values(mid, dst);
	}

	function core_get_function_name(fun) {
		var ret = fun.toString();
		ret = ret.substr('function '.length);
		ret = ret.substr(0, ret.indexOf('('));
		return ret.trim();
	}

	function core_elements_from_class_qname(name) {
		var elements = [];
		var array = name.split('.');
		for ( var i in array) {
			var value = array[i].trim();
			if (value.length > 0) {
				elements.push(value);
			}
		}
		return elements;
	}

	function core_elements_to_class_qname(elements) {
		var out = "";
		for ( var i in elements) {
			var value = elements[i].trim();
			if (value.length > 0) {
				out = ((out.length > 0) ? (out + ".") : out);
				out += value;
			}
		}
		return out;
	}

	function core_normal_class_qname(qname) {
		var array = core_elements_from_class_qname(qname);
		return core_elements_to_class_qname(array);
	}

	function core_init_class_dot_class(class_context) {
		var name = class_context.getFullName();
		var res = null;
		if (name == null) {
		} else if (name == 'js.lang.Object') {
		} else if (name == 'js.lang.Class') {
		} else {
			res = class_context.getJsLangClass();
		}
		return res;
	}

	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}

	/***************************************************************************
	 * class CoreClassContext
	 */

	function CoreClassContext(mc) {
		this.m_module_context = mc;
	}

	CoreClassContext.prototype = {

		name_ : function(name) {
			var old = this.m_name;
			if (old == null) {
				this.m_name = name;
			} else {
				throw ('reset_class_name: ' + name);
			}
		},

		extends_ : function(super_class) {
			if (super_class == null) {
				throw Exception('null pointor: super_class');
			}
			var old = this.m_super_class;
			if (old == null) {
				this.m_super_class = super_class;
			} else {
				throw Exception('reset_super_class: ' + super_class);
			}
		},

		constructor_ : function(func) {
			this.m_constructor = func;
		},

		load : function() {

			// TODO
			// class_context.load

			var class_context = this;
			var module_context = this.m_module_context;
			var super_class = this.m_super_class;
			var constructor = this.m_constructor;
			var name = this.m_name;
			var core_js = module_context.getCoreJS();
			var class_manager = core_js.getCoreClassManager();

			// set default value
			if (super_class == null) {
				super_class = core_js.getBasePackage().js.lang.Object;
				this.m_super_class = super_class;
			}

			// extends
			core_make_extends(constructor, super_class);

			// implements

			// the 'Class' object
			var getClass = function() {
				return class_context.getJsLangClass();
			};
			constructor.prototype.getClass = getClass;
			constructor.class = core_init_class_dot_class(class_context);

			// register to manager
			class_manager.register(class_context);
		},

		getPackageName : function() {
			var pn = this.m_package_name;
			if (pn == null) {
				pn = this.m_module_context.getPackageName();
				this.m_package_name = pn;
			}
			return pn;
		},

		getSimpleName : function() {
			var sn = this.m_simple_name;
			if (sn == null) {
				var con = this.m_constructor;
				sn = core_get_function_name(con);
				this.m_simple_name = sn;
			}
			return sn;
		},

		getFullName : function() {
			var fn = this.m_full_name;
			if (fn == null) {
				var pn = this.getPackageName();
				var sn = this.getSimpleName();
				this.m_full_name = fn = (pn + '.' + sn);
			}
			return fn;
		},

		getConstructor : function() {
			return this.m_constructor;
		},

		getJsLangClass : function() {
			var out_class = this.m_js_lang_class;
			if (out_class == null) {
				out_class = new js.lang.Class(this);
				this.m_js_lang_class = out_class;
			}
			return out_class;
		},

		getClassLoader : function() {
			return this.m_module_context.getClassLoader();
		},

		getSuperConstructor : function() {
			return this.m_super_class;
		},

	};

	/***************************************************************************
	 * class CoreClassManager
	 */

	function CoreClassManager(core) {
		this.m_core = core;
		this.m_table = {};
	}

	CoreClassManager.prototype = {

		register : function(class_context) {
			var simple_name = class_context.getSimpleName();
			var full_name = class_context.getFullName();
			var pack_name = class_context.getPackageName();
			var constructor = class_context.getConstructor();
			var pkg = this.getPackage(pack_name, true);
			pkg[simple_name] = constructor;
			this.m_table[full_name] = class_context;
		},

		getPackage : function(pack_name, make_packages) {
			var array0 = [];
			var array1 = core_elements_from_class_qname(pack_name);
			var pkg = this.m_core.getBasePackage();
			for ( var i in array1) {
				var name = array1[i];
				var sub = pkg[name];
				array0.push(name);
				var path = core_elements_to_class_qname(array0);
				if (sub == null) {
					if (!make_packages) {
						return null;
					}
					sub = {
						__name__ : name,
						__package__ : path
					};
					pkg[name] = sub;
				}
				pkg = sub;
			}
			return pkg;
		},

		getCoreClassContext : function(name) {
			return this.m_table[name];
		},

	};

	/***************************************************************************
	 * class CoreModuleContext
	 */

	function CoreModuleContext(core) {
		this.m_core_js = core;
	}

	CoreModuleContext.prototype = {

		name_ : function(name) {
			var old = this.m_module_name;
			if (old == null) {
				this.m_module_name = name;
			} else {
				throw ('reset_module_name: ' + name);
			}
		},

		class_ : function(constructor, callback) {

			var pkg_name = this.m_package_name;
			if (pkg_name == null) {
				throw new Exception('the_package_name_not_define');
			}

			var cc = new CoreClassContext(this);
			cc.constructor_(constructor);
			{
				callback(cc);
			}
			cc.load();
		},

		package_ : function(name) {
			var old = this.m_package_name;
			if (old == null) {
				name = core_normal_class_qname(name);
				this.m_package_name = name;
			} else {
				throw ('reset_package_name: ' + name);
			}
		},

		load : function() {
			// TODO
			// module_context.load

			var abc = this;

		},

		getCoreJS : function() {
			return this.m_core_js;
		},

		getPackageName : function() {
			return this.m_package_name;
		},

		getClassLoader : function() {
			return this.m_core_js.getClassLoader();
		},

	};

	/***************************************************************************
	 * class CoreModuleManager
	 */

	function CoreModuleManager() {
	}

	CoreModuleManager.prototype = {};

	/***************************************************************************
	 * methods of CoreJS
	 */

	CoreJS.prototype = {

		newModuleContext : function() {
			return new CoreModuleContext(this);
		},

		getCoreModuleManager : function() {
			var mm = this.m_core_module_manager;
			if (mm == null) {
				this.m_core_module_manager = mm = new CoreModuleManager(this);
			}
			return mm;
		},

		getCoreClassManager : function() {
			var cm = this.m_core_class_manager;
			if (cm == null) {
				this.m_core_class_manager = cm = new CoreClassManager(this);
			}
			return cm;
		},

		getBasePackage : function() {
			return this.m_base_package;
		},

		getClassLoader : function() {
			var loader = this.m_class_loader;
			if (loader == null) {
				var base = this.getBasePackage();
				loader = new base.js.lang.ClassLoader(this);
				this.m_class_loader = loader;
			}
			return loader;
		},

	};

	// create instance
	return new CoreJS(base);

};

CoreJS.isNil = function(value) {
	return ((value == null) || (value == undefined));
};

CoreJS.getInstance = function() {
	return CoreJS.__instance__;
};

CoreJS.init = function(base) {
	var inst = CoreJS.__instance__;
	if (inst == null) {
		inst = CoreJS._newInstance_(base);
		CoreJS.__instance__ = inst;
	}
	return inst;
};

CoreJS.init(this);

/*******************************************************************************
 * js.lang
 */

JS.module_('LangModule', function(mc) {

	mc.package_('js.lang');

	var the_object_target_id_gen = 10000;

	function ObjectBase() {
	}

	function Object() {
	}

	function Class(class_context) {
		this.m_class_context = class_context;
	}

	function ClassLoader(core) {
		this.m_core = core;
	}

	function ClassManager(core) {
		this.m_core = core;
	}

	function ModuleManager(core) {
		this.m_core = core;
	}

	function System() {
	}

	/***************************************************************************
	 * class ObjectBase
	 */

	ObjectBase.prototype = {
	// NONE
	};

	/***************************************************************************
	 * class Object
	 */

	mc.class_(Object, function(cc) {

		cc.extends_(ObjectBase);

		Object.prototype = {

			getClass : function() {
				throw ('this method will be override by class loader.');
			},

			toString : function() {
				var type = this.getClass().getName();
				var id = this.hashCode();
				return (type + '@' + id);
			},

			equals : function(other) {
				if (other == null) {
					return false;
				}
				var id1 = Object.targetId(this);
				var id2 = Object.targetId(other);
				return (id1 == id2);
			},

			hashCode : function() {
				return Object.targetId(this);
			},

		};

		Object.targetId = function(object) {
			if (JS.isNil(object)) {
				return null;
			}
			if ((typeof object) == 'object') {
				var id = object.m_object_target_id;
				if (id == null) {
					id = (the_object_target_id_gen++);
					object.m_object_target_id = id;
				}
				return id;
			}
			return null;
		};

	});

	/***************************************************************************
	 * class Class
	 */

	mc.class_(Class, function(cc) {

		Class.forName = function(name) {
			var loader = mc.getClassLoader();
			var cm = loader.getClassManager();
			return cm.getClassByName(name);
		};

		Class.prototype = {

			getName : function() {
				return this.m_class_context.getFullName();
			},

			getSimpleName : function() {
				return this.m_class_context.getSimpleName();
			},

			newInstance : function(a, b, c, d, e, f, g) {
				var con = this.m_class_context.getConstructor();
				return new con(a, b, c, d, e, f, g);
			},

			isInstance : function(object) {
				if (JS.isNil(object)) {
					return false;
				}
				var type = (typeof object);
				if (type == 'object') {
					var cls0 = this;
					var cls = object.getClass();
					for (; cls != null; cls = cls.getSuperClass()) {
						if (cls0.equals(cls)) {
							return true;
						}
					}
				}
				return false;
			},

			getConstructor : function() {
				return this.m_class_context.getConstructor();
			},

			getClassLoader : function() {
				return this.m_class_context.getClassLoader();
			},

			getSuperClass : function() {
				var this_name = this.getName();
				if (this_name == 'js.lang.Object') {
					return null;
				} else {
					var super_con = this.m_class_context.getSuperConstructor();
					return super_con.class;
				}
			},

			toString : function() {
				return ('class ' + this.getName());
			},

		};

	});

	/***************************************************************************
	 * class ClassLoader
	 */

	mc.class_(ClassLoader, function(cc) {

		ClassLoader.prototype = {

			getClassManager : function() {
				var cm = this.m_class_manager;
				if (cm == null) {
					var core = this.m_core;
					var base = core.getBasePackage();
					cm = new base.js.lang.ClassManager(core);
					this.m_class_manager = cm;
				}
				return cm;
			},

			getModuleManager : function() {
				var mm = this.m_module_manager;
				if (mm == null) {
					var core = this.m_core;
					var base = core.getBasePackage();
					mm = new base.js.lang.ModuleManager(core);
					this.m_module_manager = mm;
				}
				return mm;
			},

		};

	});

	/***************************************************************************
	 * class System
	 */

	mc.class_(System, function(cc) {

		System.out = null;
		System.err = null;

		System.prototype = {
		// NIL
		};

	});

	/***************************************************************************
	 * class Properties
	 */

	function Properties() {
		this.m_property_table = {};
	}

	mc.class_(Properties, function(cc) {

		Properties.prototype = {

			property : function(key, value) {
				var table = this.m_property_table;
				if (value == null) {
					value = table[key];
				} else {
					table[key] = value;
				}
				return value;
			},

		};

	});

	/***************************************************************************
	 * class Attributes
	 */

	function Attributes() {
		this.m_attr_table = {};
	}

	mc.class_(Attributes, function(cc) {

		Attributes.prototype = {

			attr : function(key, value) {
				var table = this.m_attr_table;
				if (value == null) {
					value = table[key];
				} else {
					table[key] = value;
				}
				return value;
			},

		};

	});

	/***************************************************************************
	 * class ClassManager
	 */

	mc.class_(ClassManager, function(cc) {

		ClassManager.prototype = {

			getClassByName : function(name) {
				var core = this.m_core;
				var core_cm = core.getCoreClassManager();
				var core_cc = core_cm.getCoreClassContext(name);
				if (core_cc == null) {
					throw new Exception('class_not_found_by_name:' + name);
				}
				return core_cc.getJsLangClass();
			},

		};

	}); // class end

	/***************************************************************************
	 * class ModuleManager
	 */

	mc.class_(ModuleManager, function(cc) {

		ModuleManager.prototype = {

		};

	}); // class end

	var obj = new Object();
	Object.class = obj.getClass();
	Class.class = obj.getClass().getClass();

}); // module end

/*******************************************************************************
 * js.io
 */

JS.module_('CoreIOModule', function(mc) {

	mc.package_('js.io');

	function InputStream() {
	}

	function OutputStream() {
	}

	function PrintStream() {
	}

	/***************************************************************************
	 * class OutputStream
	 */

	mc.class_(OutputStream, function(cc) {

		OutputStream.prototype = {

			close : function() {
			},

			flush : function() {
			},

			write : function(data) {
			},

		};

	});

	/***************************************************************************
	 * class InputStream
	 */

	mc.class_(InputStream, function(cc) {

		InputStream.prototype = {

			close : function() {
			},

		};

	});

	/***************************************************************************
	 * class PrintStream
	 */

	mc.class_(PrintStream, function(cc) {

		cc.extends_(OutputStream);

		PrintStream.prototype = {

			println : function(n) {
			},

		};

	});

});

/*******************************************************************************
 * global
 */

this.Class = js.lang.Class;
this.System = js.lang.System;
this.System.out = new js.io.PrintStream();
this.System.err = new js.io.PrintStream();

/*******************************************************************************
 * EOF
 */
