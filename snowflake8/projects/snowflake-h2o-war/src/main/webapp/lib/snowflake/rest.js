/*******************************************************************************
 * 
 * rest.js
 * 
 * @Copyright (c) 2016 boluozhai.com
 * @License MIT License
 * @Author xukun<xukun17@sina.com>
 * 
 */

JS.module(function(_mc_) {

	_mc_.package('snowflake.rest');

	var Object = _mc_.import('js.lang.Object');
	var Class = _mc_.import('js.lang.Class');
	var Attributes = _mc_.import('js.lang.Attributes');

	/***************************************************************************
	 * class REST
	 */

	function REST() {
	}

	_mc_.class(function(cc) {
		cc.type(REST);
	});

	REST.prototype = {};

	REST.getClient = function(context) {
		return RESTClient.getInstance(context);
	};

	/***************************************************************************
	 * class RESTClient
	 */

	function RESTClient(context) {
		this._context = context;
	}

	_mc_.class(function(cc) {
		cc.type(RESTClient);
	});

	RESTClient.prototype = {

		getApplication : function(app_name) {
			return new RestApplication(this, app_name);
		},

		getContextURL : function() {
			var context = this._context;
			var path = context.pathInWebapp();
			var sb = null;
			for (var i = path.length - 1; i >= 0; i--) {
				var ch = path.charAt(i);
				if (ch == '/') {
					if (sb == null) {
						sb = '.';
					} else {
						sb = sb + '/..';
					}
				}
			}
			if (sb == null) {
				sb = '[error]';
			}
			return sb;
		},

	};

	RESTClient.getInstance = function(context) {
		var key = RESTClientFactory.class.getName();
		var factory = context.getBean(key);
		return factory.create(context);
	};

	/***************************************************************************
	 * class RESTClientFactory
	 */

	function RESTClientFactory() {
	}

	_mc_.class(function(cc) {
		cc.type(RESTClientFactory);
	});

	RESTClientFactory.prototype = {

		create : function(context) {
			return new RESTClient(context);
		},

	};

	/***************************************************************************
	 * class RestApplication
	 */

	function RestApplication(client, name) {

		this._client = client;
		this._name = name;

		var context_url = client.getContextURL();

		if (name == null) {
			this._display_name = 'myself';
			this._url = context_url;
		} else {
			this._url = context_url + '/../' + name;
		}
	}

	_mc_.class(function(cc) {
		cc.type(RestApplication);
	});

	RestApplication.prototype = {

		getURL : function() {
			return this._url;
		},

		getAPI : function(name) {
			return new RestAPI(this, name);
		},

	};

	/***************************************************************************
	 * class RestAPI
	 */

	function RestAPI(app, name) {
		this._owner_app = app;
		this._name = name;
		this._url = app.getURL() + '/' + name;
	}

	_mc_.class(function(cc) {
		cc.type(RestAPI);
	});

	RestAPI.prototype = {

		getURL : function() {
			return this._url;
		},

		getType : function(name) {
			return new RestType(this, name);
		},

	};

	/***************************************************************************
	 * class RestType
	 */

	function RestType(api, name) {
		this._owner_api = api;
		this._name = name;
		this._url = api.getURL() + '/' + name;
	}

	_mc_.class(function(cc) {
		cc.type(RestType);
	});

	RestType.prototype = {

		getURL : function() {
			return this._url;
		},

		getResource : function(id) {
			return new RestResource(this, id);
		},

	};

	/***************************************************************************
	 * class RestResource
	 */

	function RestResource(type, id) {
		this._owner_type = type;
		this._res_id = id;
		this._url = type.getURL() + '/' + id;
	}

	_mc_.class(function(cc) {
		cc.type(RestResource);
	});

	RestResource.prototype = {

		getURL : function() {
			return this._url;
		},

		'post' : function() {
			return new RestRequest(this, 'POST');
		},

		'delete' : function() {
			return new RestRequest(this, 'DELETE');
		},

		'put' : function() {
			return new RestRequest(this, 'PUT');
		},

		'get' : function() {
			return new RestRequest(this, 'GET');
		},

	};

	/***************************************************************************
	 * class RestEntity
	 */

	function RestEntity() {
		this.Attributes();
	}

	_mc_.class(function(cc) {
		cc.type(RestEntity);
		cc.extends(Attributes);
	});

	RestEntity.prototype = {

		text : function(data) {
			return this.attr('text', data);
		},

		json : function(data) {
			return this.attr('json', data);
		},

		toString : function() {
			var txt = this.text();
			var js = this.json();
			if (txt != null) {
				return txt;
			} else if (js != null) {
				return JSON.stringify(js);
			} else {
				return null;
			}
		},

		toJSON : function() {
			var txt = this.text();
			var js = this.json();
			if (js != null) {
				return js;
			} else if (txt != null) {
				return JSON.parse(txt);
			} else {
				return null;
			}
		},

	};

	/***************************************************************************
	 * class RestMessage
	 */

	function RestMessage() {
		this.Attributes();
		this.entity(new RestEntity());
	}

	_mc_.class(function(cc) {
		cc.type(RestMessage);
		cc.extends(Attributes);
	});

	RestMessage.prototype = {

		entity : function(ent) {
			return this.attr('entity', ent);
		},

	};

	/***************************************************************************
	 * class RestRequest
	 */

	function RestRequest(res, method) {
		this.RestMessage();
		this._res = res;
		this._method = method;
	}

	_mc_.class(function(cc) {
		cc.type(RestRequest);
		cc.extends(RestMessage);
	});

	RestRequest.prototype = {

		execute : function(callback) {
			RestRequest_exec(this, callback);
		},

		getURL : function() {
			return this._res.getURL();
		},

	};

	function RestRequest_make_response(xhr, request) {
		var ready = xhr.readyState;
		if (ready != 4) {
			return null;
		}
		var res = new RestResponse();
		var status = xhr.status;
		if (status == 200) {
			res.ok(true);
		} else {
			res.ok(false);
		}
		res.code(status);
		res.message(xhr.statusText);
		res.entity().text(xhr.responseText);
		res.url(xhr.responseURL);
		return res;
	}

	function RestRequest_exec(request, callback) {
		var method = request._method;
		var url = request.getURL();
		var xhr = new XMLHttpRequest();
		xhr.open(method, url, true);
		xhr.onreadystatechange = function() {
			var res = RestRequest_make_response(xhr, request);
			if (res == null) {
				// not Ready
			} else {
				callback(res);
			}
		};

		var data = request.entity().toString();
		if (data == null) {
			xhr.send();
		} else {
			xhr.send(data);
		}
	}

	/***************************************************************************
	 * class RestResponse
	 */

	function RestResponse() {
		this.RestMessage();
	}

	_mc_.class(function(cc) {
		cc.type(RestResponse);
		cc.extends(RestMessage);
	});

	RestResponse.prototype = {

		ok : function(value) {
			return this.attr('ok', value);
		},

		message : function(value) {
			return this.attr('message', value);
		},

		code : function(value) {
			return this.attr('code', value);
		},

		url : function(value) {
			return this.attr('url', value);
		},

	};

});

/*******************************************************************************
 * EOF
 */
