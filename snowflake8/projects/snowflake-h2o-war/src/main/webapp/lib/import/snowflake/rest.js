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
		this.baseURL('~');
	}

	_mc_.class(function(cc) {
		cc.type(RESTClient);
		cc.extends(Attributes);
	});

	RESTClient.prototype = {

		getResource : function() {
			return new RestResource(this);
		},

		baseURL : function(value) {
			return this.attr('base_url', value);
		},

		getURL : function() {
			var context = this._context;
			var base = this.baseURL();
			return context.normalizeURL(base);
		},

		pathPattern : function(value) {
			return this.attr('path_pattern', value);
		},

		parsePath : function(path) {
			var pb = new RestPathBuilder(this);
			return pb.parsePath(path);
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
		cc.extends(Attributes);
	});

	RESTClientFactory.prototype = {

		create : function(context) {
			var client = new RESTClient(context);
			client.pathPattern(this.pathPattern());
			return client;
		},

		pathPattern : function(value) {
			return this.attr('path_pattern', value);
		},

	};

	/***************************************************************************
	 * class RestResource
	 */

	function RestResource(client) {
		this._path_builder = new RestPathBuilder(client);
	}

	_mc_.class(function(cc) {
		cc.type(RestResource);
	});

	RestResource.prototype = {

		getURL : function() {
			return this._path_builder.create();
		},

		service : function(value) {
			return this._path_builder.service(value);
		},

		part : function(key, value) {
			return this._path_builder.part(key, value);
		},

		parts : function(table) {
			for ( var key in table) {
				var val = table[key];
				this.part(key, val);
			}
		},

		'post' : function() {
			return new RestRequest(this, 'POST');
		},

		'del' : function() {
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
	 * inner class RestPathReader
	 */

	function RestPathReader(path) {

		var array = path.split('/');
		var a2 = [];
		for ( var i in array) {
			var s = array[i];
			if (s == null) {
				// NOP
			} else if (s == '') {
				// NOP
			} else if (s == '.') {
				// NOP
			} else if (s == '..') {
				a2.pop();
			} else if (s == '~') {
				a2.empty();
			} else {
				a2.push(s);
			}
		}
		this._path_elements = a2;
		this._ptr = 0;
	}

	RestPathReader.prototype = {

		read : function(part) {

			var array = this._path_elements;
			var ptr = this._ptr;
			var sb = '';
			var end = ptr + part.length();

			if (part.mutableLength()) {
				end = array.length;
			}

			for (; ptr < end; ptr++) {
				var ele = array[ptr];
				if (sb.length > 0) {
					sb = sb + '/' + ele;
				} else {
					sb = ele;
				}
			}

			this._ptr = end;
			return sb;

		},

	};

	/***************************************************************************
	 * inner class RestPathPart
	 */

	function RestPathPart() {
		this._x_length = false; // the indeterminable length
		this._length = 1;
	}

	RestPathPart.prototype = {

		mark_as_x_length : function() {
			this._x_length = true;
		},

		attr : function(key, value) {
			key = '__attr_' + key + '__';
			if (value == null) {
				value = this[key];
			} else {
				this[key] = value;
			}
			return value;
		},

		name : function(value) {
			return this.attr('name', value);
		},

		value : function(value) {
			return this.attr('value', value);
		},

		to_regular_string : function() {

			var val = this.value();
			if (val == null) {
				var msg = 'the part is not set: ' + this.name();
				throw new Exception(msg);
			}

			var a1 = val.split('/');
			var a2 = [];
			for ( var i in a1) {
				var s = a1[i].trim();
				if (s == null) {
					// skip
				} else if (s == '') {
					// skip
				} else {
					a2.push(s);
				}
			}

			if (this._x_length) {
				// skip
			} else if (this._length != a2.length) {
				var want = this._length;
				var real = a2.length;
				var msg = 'bad path length, want:' + want + ', but:' + real;
				throw new Exception(msg);
			}

			var sb = '';
			for ( var i in a2) {
				var val = a2[i];
				if (sb.length == 0) {
					sb = val;
				} else {
					sb += ('/' + val);
				}
			}

			return sb;
		},

		inc_count : function() {
			this._length++;
		},

		length : function() {
			return this._length;
		},

		mutableLength : function() {
			return (this._x_length == true);
		},

	};

	/***************************************************************************
	 * inner class RestPathBuilder
	 */

	function RestPathBuilder(client) {
		this._context = client._context;
		this._pattern = client.pathPattern();
		this._base = client.getURL();
		this._part_list = null;
		this._part_table = null;
		this.parsePattern();
	}

	RestPathBuilder.prototype = {

		part : function(name, value) {
			var tab = this._part_table;
			var part = tab[name];
			if (part == null) {
				var msg = 'no part with name: ' + name;
				throw new RuntimeException(msg);
			}
			return part.value(value);
		},

		parsePath : function(path) {

			var context = this._context;
			var cp = context.getContextPath();

			// path = '/context/path' or '~/path'
			if (path == null) {
				return null;

			} else if (path.indexOf('~/') == 0) {
				path = path.substring(2);

			} else if (path.indexOf(cp) == 0) {
				path = path.substring(cp.length);

			} else {
				return null;
			}

			var reader = new RestPathReader(path);
			var tab = {};
			var plist = this._part_list;
			for ( var i in plist) {
				var part = plist[i];
				var s = reader.read(part);
				tab[part.name()] = s;
			}

			return tab;

		},

		parsePattern : function() {

			var pattern = this._pattern;
			if (pattern == null) {
				var msg = 'no pattern setted, in rest-client';
				throw new SnowflakeException(msg);
			}

			var parts = [];
			var cur_part = null;
			var array = pattern.split('/');
			for ( var i in array) {
				var s = array[i];
				if (s == null) {
					// skip
				} else if (s == '') {
					// skip
				} else if (s == '~') {
					// skip
				} else if (s == '+') {
					cur_part.inc_count();
				} else if (s == '*') {
					// end
					cur_part.mark_as_x_length();
					break;
				} else {
					if (cur_part != null) {
						parts.push(cur_part);
					}
					cur_part = new RestPathPart();
					cur_part.name(s);
				}
			}
			parts.push(cur_part);

			var table = {};
			for ( var i in parts) {
				var part = parts[i];
				var name = part.name();
				table[name] = part;
			}

			this._part_list = parts;
			this._part_table = table;

		},

		setPart : function(name, value) {
			var part = this._part_table[name];
			part.setValue(value);
		},

		service : function(value) {
			return this.attr('service', value);
		},

		attr : function(k, v) {
			k = '__attr_' + k;
			if (v == null) {
				v = this[k];
			} else {
				this[k] = v;
			}
			return v;
		},

		create : function() {
			var sb = this._base;
			var list = this._part_list;
			for ( var i in list) {
				var part = list[i];
				var s = part.to_regular_string();
				sb += ('/' + s);
			}

			var service = this.service();
			if (service == null) {
				// NOP
			} else {
				sb += ('?service=' + service);
			}

			var context = this._context;
			return context.normalizeURL(sb);
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
