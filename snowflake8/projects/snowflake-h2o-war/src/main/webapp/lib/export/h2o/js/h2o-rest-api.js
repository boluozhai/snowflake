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

		// String hashId;
		// String uid;
		// String nickname;
		// String email;
		// String description;
		// String language;
		// String location;
		// GitObjectDescriptor avatar;

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

		// String name; // the repo id
		// String owner; // the uid of owner
		// String description;
		// GitObjectDescriptor icon;

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
	 * class AuthProfile
	 */

	function AuthProfile(init) {

		this.BaseElement(init);

		// String mechanism; // password|email|sms|...
		// String method; // login|register|forget-pass|...
		// String name; // user|email
		// String key; // password|...
		// String key2; // for changed passwd
		// String thread;
		// String step;
		// String status; // [ok|error|continue|...]
		// String code; // status-code
		// String message; // status-message
		// boolean done;
		// boolean success;

	}

	mc.class(function(cc) {
		cc.type(AuthProfile);
		cc.extends(BaseElement);
	});

	AuthProfile.prototype = {

		f_mechanism : function(v) {
			return this.__field__('mechanism', v);
		},

		f_method : function(v) {
			return this.__field__('method', v);
		},

		f_name : function(v) {
			return this.__field__('name', v);
		},

		f_key : function(v) {
			return this.__field__('key', v);
		},

		f_key2 : function(v) {
			return this.__field__('key2', v);
		},

		f_thread : function(v) {
			return this.__field__('thread', v);
		},

		f_step : function(v) {
			return this.__field__('step', v);
		},

		f_status : function(v) {
			return this.__field__('status', v);
		},

		f_code : function(v) {
			return this.__field__('code', v);
		},

		f_message : function(v) {
			return this.__field__('message', v);
		},

		f_success : function(v) {
			return this.__field__('success', v);
		},

		f_done : function(v) {
			return this.__field__('done', v);
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

	var AuthProfile = mc.import(element_pkg + '.AuthProfile');
	var ViewportProfile = mc.import(element_pkg + '.ViewportProfile');

	/***************************************************************************
	 * class AuthModel
	 */

	function AuthModel(init) {

		this.BaseModel(init);

		this.request = new AuthProfile(this.request);
		this.response = new AuthProfile(this.response);

	}

	mc.class(function(cc) {
		cc.type(AuthModel);
		cc.extends(BaseModel);
	});

	AuthModel.prototype = {

		f_request : function(v) {
			return this.__field__('request', v);
		},

		f_response : function(v) {
			return this.__field__('response', v);
		},

	};

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

	/***************************************************************************
	 * class LanguageModel
	 */

	function LanguageModel(init) {
	}

	mc.class(function(cc) {
		cc.type(LanguageModel);
		cc.extends(BaseModel);
	});

	LanguageModel.prototype = {

		f_viewport : function(v) {
			return this.__field__('viewport', v);
		},

	};

});

JS.module(function(mc) {

	// api

	mc.package("com.boluozhai.snowflake.rest.api");

	var model_pkg = "com.boluozhai.snowflake.rest.api.h2o";

	var System = mc.import('js.lang.System');
	var Attributes = mc.import('js.lang.Attributes');
	var REST = mc.import('snowflake.rest.REST');

	var AuthModel = mc.import(model_pkg + '.AuthModel');
	var ViewportModel = mc.import(model_pkg + '.ViewportModel');

	/***************************************************************************
	 * class RestModelInfo
	 */

	function RestModelInfo(service, tag_name, clazz) {
		this._service_name = service;
		this._tag_name = tag_name;
		this._full_name = clazz.getName();
		this._class = clazz;
	}

	mc.class(function(cc) {
		cc.type(RestModelInfo);
	});

	RestModelInfo.prototype = {

		service : function() {
			return this._service_name;
		},

		tag : function() {
			return this._tag_name;
		},

		name : function() {
			return this._full_name;
		},

		getModelClass : function() {
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
		cc.extends(Attributes);
	});

	RestModelRegistrar.prototype = {

		register : function(api_name, pojo) {

			var clazz = pojo.getClass();
			var n1 = api_name;
			var n2 = clazz.getName();
			var service = this.currentService();

			var item = new RestModelInfo(service, api_name, clazz);
			this._table[n1] = item;
			this._table[n2] = item;

		},

		currentService : function(v) {
			return this.attr('current_service', v);
		},

		registerAll : function() {

			// TODO reg types

			this.currentService('system-api');
			this.register('auth', new AuthModel());
			this.register('viewport', new ViewportModel());
			this.register('language', new LanguageModel());

			this.currentService('user-api');

			this.currentService('repo-api');

		},

		init : function() {
			this.registerAll();
		},

		getTypeInfo : function(name, required) {
			var item = this._table[name];
			if ((item == null) && required) {
				var msg = 'no type named: ' + name;
				throw new Exception(msg);
			}
			return item;
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
	 * inner class JSONRestRequestImpl
	 */

	function JSONRestRequestImpl(context, facade) {
		this._context = context;
		this._facade = facade;
		this._method = null;
		this._param = null;
		this._check_response_type = true;
	}

	JSONRestRequestImpl.prototype = {

		parse_response_ok : function(txt) {

			var pojo = JSON.parse(txt);
			var model_info = this._model_info;
			var check_response_type = this._check_response_type;

			var real_t_name = pojo.type;
			var reg_t_name = model_info.name();

			if (check_response_type) {
				if (reg_t_name != real_t_name) {
					throw new Exception('the 2 types not match.');
				}
			}

			var model_class = model_info.getModelClass();
			return model_class.newInstance(pojo);
		},

		parse_response_error : function(txt) {
			return txt;
		},

		open : function(method, param) {

			var type_reg = RestModelRegistrar.getInstance();
			var info = type_reg.getTypeInfo(param.type, true);
			var clazz = info.getModelClass();
			var service = info.service();
			var entity = clazz.newInstance();

			this._method = method.toLowerCase();
			this._param = param;
			this._model_info = info;
			this._service = service;

			return entity;

		},

		send : function(tx) {

			var context = this._context;
			var facade = this._facade;
			var method = this._method;
			var parts = this._param;
			var request = null;

			var fn = facade.onResult();
			if (fn == null) {
				fn = function() {
				};
			}

			var client = REST.getClient(context);
			var resource = client.getResource();
			resource.parts(parts);
			resource.service(this._service);

			if (method == null) {
				// NOP
			} else if (method == 'get') {
				request = resource.get();
				tx = null;
			} else if (method == 'post') {
				request = resource.post();
			} else if (method == 'put') {
				request = resource.put();
			} else if (method == 'delete') {
				request = resource.del();
			} else {
				// NOP
			}

			if (request == null) {
				throw new Exception('unsupported method: ' + method);
			}

			var self = this;
			var req_ent = request.entity();
			req_ent.json(tx);
			facade.requestEntity(tx);

			request.execute(function(response) {

				var ok = response.ok();
				var res_ent = response.entity();
				var rx = res_ent.toString();

				if (ok) {
					rx = self.parse_response_ok(rx);
				} else {
					rx = self.parse_response_error(rx);
				}

				facade.ok(ok);
				facade.responseEntity(rx);
				facade.responseMessage(response.message());
				facade.responseCode(response.code());
				fn();

			});

		},

	};

	/***************************************************************************
	 * class JSONRestRequest
	 */

	function JSONRestRequest(context) {
		this._impl = new JSONRestRequestImpl(context, this);
	}

	mc.class(function(cc) {
		cc.type(JSONRestRequest);
		cc.extends(Attributes);
	});

	JSONRestRequest.prototype = {

		open : function(method, param) {
			return this._impl.open(method, param);
		},

		send : function(entity) {
			return this._impl.send(entity);
		},

		onResult : function(fn) {
			return this.attr('on_result', fn);
		},

		ok : function(value) {
			return this.attr('ok', value);
		},

		requestEntity : function(value) {
			return this.attr('request_entity', value);
		},

		responseCode : function(value) {
			return this.attr('response_code', value);
		},

		responseMessage : function(value) {
			return this.attr('response_message', value);
		},

		responseEntity : function(value) {
			return this.attr('response_entity', value);
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
	var type = reg.getTypeInfo('viewport');
	var clazz = type.getModelClass();
	var model = clazz.newInstance();
	var vpt = model.f_viewport();
	var owner = vpt.f_owner();

});

this.snowflake.JSONRestRequest = this.com.boluozhai.snowflake.rest.api.JSONRestRequest;

/*******************************************************************************
 * EOF
 */
