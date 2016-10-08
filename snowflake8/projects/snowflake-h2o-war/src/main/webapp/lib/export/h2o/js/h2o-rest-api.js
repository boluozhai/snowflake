/*******************************************************************************
 * 
 * h2o-rest-api.js
 * 
 * @Copyright (c) 2016 boluozhai.com
 * @License MIT License
 * @Author xukun<xukun17@sina.com>
 * 
 */

JS.module(function(mc) {

	// base

	mc.package("com.boluozhai.snowflake.rest.api");

	// var System = mc.import('js.lang.System');
	// var Attributes = mc.import('js.lang.Attributes');

	/***************************************************************************
	 * class BasePOJO
	 */

	function BasePOJO(init) {

		if (init != null) {
			for ( var k in init) {
				var v = init[k];
				this[k] = v;
			}
		}

	}

	mc.class(function(cc) {
		cc.type(BasePOJO);
	});

	BasePOJO.prototype = {

		__field__ : function(k, v) {
			if (v == null) {
				v = this[k];
			} else {
				this[k] = v;
			}
			return v;
		},

	};

	/***************************************************************************
	 * class BaseElement
	 */

	function BaseElement(init) {
		this.BasePOJO(init);
	}

	mc.class(function(cc) {
		cc.type(BaseElement);
		cc.extends(BasePOJO);
	});

	BaseElement.prototype = {

		f_type : function(v) {
			return this.__field__('type', v);
		},

	};

	/***************************************************************************
	 * class BaseModel ( a.k.a. RestDoc)
	 */

	function BaseModel(init) {
		this.BasePOJO(init);
	}

	mc.class(function(cc) {
		cc.type(BaseModel);
		cc.extends(BasePOJO);
	});

	BaseModel.prototype = {

		type : function(v) {
			return this.__field__('type', v);
		},

	};

});

JS.module(function(mc) {

	// element

	mc.package("com.boluozhai.snowflake.rest.element");

	var base_pkg = "com.boluozhai.snowflake.rest.api";
	var BaseElement = mc.import(base_pkg + '.BaseElement');

	/***************************************************************************
	 * class GitObjectDescriptor
	 */

	function GitObjectDescriptor(init) {
		this.BaseElement(init);
	}

	mc.class(function(cc) {
		cc.type(GitObjectDescriptor);
		cc.extends(BaseElement);
	});

	GitObjectDescriptor.prototype = {

		f_type : function(v) {
			return this.__field__('type', v);
		},

		f_id : function(v) {
			return this.__field__('id', v);
		},

		f_owner : function(v) {
			return this.__field__('owner', v);
		},

		f_repository : function(v) {
			return this.__field__('repository', v);
		},

		f_url : function(v) {
			return this.__field__('url', v);
		},

		f_length : function(v) {
			return this.__field__('length', v);
		},

	};

	/***************************************************************************
	 * class AccountProfile
	 */

	function AccountProfile(init) {
		this.BaseElement(init);

		// private String hashId;
		// private String uid;
		// private String nickname;
		// private String email;
		// private String description;
		// private String language;
		// private String location;
		// private GitObjectDescriptor avatar;

		this.avatar = new GitObjectDescriptor(this.avatar);

	}

	mc.class(function(cc) {
		cc.type(AccountProfile);
		cc.extends(BaseElement);
	});

	AccountProfile.prototype = {

		f_hashId : function(v) {
			return this.__field__('hashId', v);
		},

		f_uid : function(v) {
			return this.__field__('uid', v);
		},

		f_nickname : function(v) {
			return this.__field__('nickname', v);
		},

		f_email : function(v) {
			return this.__field__('email', v);
		},

		f_description : function(v) {
			return this.__field__('description', v);
		},

		f_language : function(v) {
			return this.__field__('language', v);
		},

		f_location : function(v) {
			return this.__field__('location', v);
		},

		f_avatar : function(v) {
			return this.__field__('avatar', v);
		},

	};

	/***************************************************************************
	 * class RepositoryProfile
	 */

	function RepositoryProfile(init) {
		this.BaseElement(init);

		// private String name; // the repo id
		// private String owner; // the uid of owner
		// private String description;
		// private GitObjectDescriptor icon;

		this.icon = new GitObjectDescriptor(this.icon);

	}

	mc.class(function(cc) {
		cc.type(RepositoryProfile);
		cc.extends(BaseElement);
	});

	RepositoryProfile.prototype = {

		f_name : function(v) {
			return this.__field__('name', v);
		},

		f_owner : function(v) {
			return this.__field__('owner', v);
		},

		f_description : function(v) {
			return this.__field__('description', v);
		},

		f_icon : function(v) {
			return this.__field__('icon', v);
		},

	};

	/***************************************************************************
	 * class ViewportProfile
	 */

	function ViewportProfile(init) {
		this.BaseElement(init);

		this.owner = new AccountProfile(this.owner);
		this.operator = new AccountProfile(this.operator);
		this.repository = new RepositoryProfile(this.repository);

	}

	mc.class(function(cc) {
		cc.type(ViewportProfile);
		cc.extends(BaseElement);
	});

	ViewportProfile.prototype = {

		f_operator : function(v) {
			return this.__field__('operator', v);
		},

		f_owner : function(v) {
			return this.__field__('owner', v);
		},

		f_repository : function(v) {
			return this.__field__('repository', v);
		},

		f_role : function(v) {
			return this.__field__('role', v);
		},

	};

});

JS.module(function(mc) {

	// model

	mc.package("com.boluozhai.snowflake.rest.api.h2o");

	var base_pkg = "com.boluozhai.snowflake.rest.api";
	var element_pkg = "com.boluozhai.snowflake.rest.element";

	var BaseModel = mc.import(base_pkg + '.BaseModel');

	var ViewportProfile = mc.import(element_pkg + '.ViewportProfile');

	/***************************************************************************
	 * class ViewportModel
	 */

	function ViewportModel(init) {
		this.BaseModel(init);

		this.viewport = new ViewportProfile(this.viewport);

	}

	mc.class(function(cc) {
		cc.type(ViewportModel);
		cc.extends(BaseModel);
	});

	ViewportModel.prototype = {

		f_viewport : function(v) {
			return this.__field__('viewport', v);
		},

	};

});

JS.module(function(mc) {

	// api

	mc.package("com.boluozhai.snowflake.rest.api");

	// var System = mc.import('js.lang.System');
	// var Attributes = mc.import('js.lang.Attributes');
	var model_pkg = "com.boluozhai.snowflake.rest.api.h2o";

	var ViewportModel = mc.import(model_pkg + '.ViewportModel');

	/***************************************************************************
	 * class RestModelInfo
	 */

	function RestModelInfo(api_name, clazz) {
		this._api_name = api_name;
		this._class_name = clazz.getName();
		this._class = clazz;
	}

	mc.class(function(cc) {
		cc.type(RestModelInfo);
	});

	RestModelInfo.prototype = {

		apiName : function() {
			return this._api_name;
		},

		fullName : function() {
			return this._class_name;
		},

		type : function() {
			return this._class;
		},

	};

	/***************************************************************************
	 * class RestModelRegistrar
	 */

	function RestModelRegistrar(context) {
		this._context = context;
		this._table = {};
	}

	mc.class(function(cc) {
		cc.type(RestModelRegistrar);
		// cc.extends(Attributes);
	});

	RestModelRegistrar.prototype = {

		register : function(api_name, pojo) {

			var clazz = pojo.getClass();
			var n1 = api_name;
			var n2 = clazz.getName();

			var item = new RestModelInfo(api_name, clazz);
			this._table[n1] = item;
			this._table[n2] = item;

		},

		registerAll : function() {

			this.register('viewport', new ViewportModel());

		},

		init : function() {
			this.registerAll();
		},

		getType : function(name, required) {
			var item = this._table[name];
			if ((item == null) && required) {
				var msg = 'no type named: ' + name;
				throw new Exception(msg);
			}
			return item.type();
		},

	};

	var RestModelRegistrar_inst = null;

	RestModelRegistrar.getInstance = function() {
		var inst = RestModelRegistrar_inst;
		if (inst == null) {
			inst = new RestModelRegistrar();
			inst.init();
			RestModelRegistrar_inst = inst;
		}
		return inst;
	};

	/***************************************************************************
	 * class RestAPI
	 */

	function RestAPI() {
	}

	mc.class(function(cc) {
		cc.type(RestAPI);
	});

	RestAPI.prototype = {

		parseResponseOk : function(response) {

			var ent = response.entity();
			var pojo = ent.toJSON();
			var full_name = pojo.type;
			var reg = RestModelRegistrar.getInstance();
			var clazz = reg.getType(full_name, true);

			var model = clazz.newInstance(pojo);

			return model;

		},

		parseResponseError : function(response) {
			return 'error';
		},

		parseResponse : function(response) {
			if (response.ok()) {
				return this.parseResponseOk(response);
			} else {
				return this.parseResponseError(response);
			}
		},

	};

});

JS.module(function(mc) {

	// test/head

	var pkg = "com.boluozhai.snowflake.rest.api";
	mc.package(pkg + '.test');
	var RestModelRegistrar = mc.import(pkg + '.RestModelRegistrar');

	// test/body

	var reg = RestModelRegistrar.getInstance();
	var type = reg.getType('viewport');
	var model = type.newInstance();
	var vpt = model.f_viewport();
	var owner = vpt.f_owner();

});

/*******************************************************************************
 * EOF
 */
