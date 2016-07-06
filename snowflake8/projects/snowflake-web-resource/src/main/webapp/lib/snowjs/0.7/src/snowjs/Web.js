/*******************************************************************************
 * Web.js
 */

JS.module_('WebModule', function(mc) {

	mc.package_('js.web');

	/***************************************************************************
	 * implements Page
	 */

	function Page() {
	}

	mc.class_(Page, function(cc) {

		// private

		var pc_builder = null;
		var page_context = null;

		function getPCBuilder() {
			var pcb = pc_builder;
			if (pcb == null) {
				pcb = new PageContextBuilder();
				pc_builder = pcb;
			}
			return pcb;
		}

		// public

		// Page.path = function(path) {
		// getPCBuilder().path(path);
		// };

		Page.config = function(conf) {
			getPCBuilder().config(conf);
		};

		Page.onload = function(func) {
			getPCBuilder().onload(func);
		};

		Page.context = function() {
			var pc = page_context;
			if (pc == null) {
				pc = getPCBuilder().create();
				page_context = pc;
			}
			return pc;
		};

		// Page.prototype = {};

	});

	/***************************************************************************
	 * implements PageContext
	 */

	var Context = js.context.Context;

	function PageContext(table) {
		this.Context();
		this.m_beans = table;
	}

	mc.class_(PageContext, function(cc) {

		cc.extends_(Context);

		PageContext.prototype = {

			getBean : function(name) {
				var bean = this.m_beans[name];
				if (bean == null) {
					throw new Exception('no_bean_named:' + name);
				}
				return bean;
			},

			jsapiServiceURI : function(value) {
				return this.property('jsapi_service_uri', value);
			},

			contextURI : function(value) {
				return this.property('context_uri', value);
			},

			normalizeURI : function(url) {
				if (url == null) {
					return null;
				} else if (url.indexOf('~/') == 0) {
					var base = this.contextURI();
					var offset = url;
					return inner_compute_context_uri(base, offset);
				} else if (url.indexOf('/') == 0) {
					return url;
				} else {
					return url;
				}
			},

		};

		function inner_compute_context_uri(base, offset) {
			var sb = offset.substring(2);
			var array = base.split('/');
			for (var i = array.length - 2; i > 0; i--) {
				sb = '../' + sb;
			}
			return sb;
		}

	});

	/***************************************************************************
	 * implements BeanLoader
	 */

	function BeanLoader(loader_table) {
		this.m_table = loader_table;
	}

	mc.class_(BeanLoader, function(cc) {

		BeanLoader.prototype = {

			load : function(name, param) {

				this.m_name = name;
				this.m_param = param;

				var class_name = param['class'];
				var clazz = js.lang.Class.forName(class_name);
				var bean = clazz.newInstance();

				this.m_bean = bean;
			},

			get : function() {
				return this.m_bean;
			},

			configure : function() {
			},

		};

	});

	/***************************************************************************
	 * implements PageContextBuilder
	 */

	function PageContextBuilder() {
		this.m_onload_func = [];
	}

	mc.class_(PageContextBuilder, function(cc) {

		PageContextBuilder.prototype = {

			config : function(conf, append) {
				this.m_config = conf;
			},

			path : function(page_path) {
				this.m_page_path = page_path;
			},

			onload : function(func) {
				this.m_onload_func.push(func);
			},

			create : function() {

				// TODO ...

				var conf = this.m_config;
				var path = this.m_page_path;

				// create beans
				var beans = conf.beans;
				var out_bean_loaders = {};
				for ( var name in beans) {
					var bean = beans[name];
					var loader = new BeanLoader(out_bean_loaders);
					loader.load(name, bean);
					out_bean_loaders[name] = loader;
				}

				// configure beans
				for ( var name in out_bean_loaders) {
					var loader = out_bean_loaders[name];
					loader.configure();
				}

				// create context
				var out_beans = {};
				for ( var name in out_bean_loaders) {
					var loader = out_bean_loaders[name];
					out_beans[name] = loader.get();
				}
				var context = new PageContext(out_beans);

				// onload
				var onload_list = this.m_onload_func;
				for ( var i in onload_list) {
					var onload = onload_list[i];
					onload(context);
				}

				return context;
			},

		};

	});

	/***************************************************************************
	 * implements AlertPrintStream
	 */

	function AlertPrintStream() {
		this.m_buffer = "";
	}

	mc.class_(AlertPrintStream, function(cc) {

		cc.extends_(js.io.PrintStream);

		AlertPrintStream.prototype = {

			reset : function() {
				this.m_buffer = "";
			},

			println : function(str) {
				if (str == null) {
					str = '\n';
				} else {
					str += '\n';
				}
				this.write(str);
			},

			print : function(str) {
				this.write(str);
			},

			write : function(str) {
				if (str == null) {
					return;
				}
				var str2 = this.m_buffer + str;
				this.m_buffer = str2;
				if (str2.length > 102400) {
					this.flush();
				}
			},

			flush : function(str) {
				alert(this.m_buffer);
				this.reset();
			},

		};

	});

}); // end of module

JS.module_('WebJHRequestModule', function(mc) {

	mc.package_('js.web.jhr');

	var Attributes = js.lang.Attributes;
	var Base64 = js.util.base64.Base64;

	/***************************************************************************
	 * class JSONHttpRequestClients
	 */

	function JSONHttpRequestClients() {
		// JS.super_call( ) ;
	}

	mc.class_(JSONHttpRequestClients, function(cc) {

		JSONHttpRequestClients.getClient = function(context) {
			var name = JSONHttpRequestClients.class.getName();
			var factory = context.getBean(name);
			return factory.getClient(context);
		};

		JSONHttpRequestClients.prototype = {};

	});

	/***************************************************************************
	 * class DefaultJHRequest
	 */

	function DefaultJHRequest() {
		this.Attributes();
	}

	mc.class_(DefaultJHRequest, function(cc) {

		cc.extends_(Attributes);

		DefaultJHRequest.prototype = {

			action : function(value) {
				return this.attr('action', value);
			},

			httpMethod : function(value) {
				return this.attr('http_method', value);
			},

			id : function(value) {
				return this.attr('id', value);
			},

			token : function(value) {
				return this.attr('token', value);
			},

			type : function(value) {
				return this.attr('type', value);
			},

			data : function(value) {
				return this.attr('data', value);
			},

		};

	});

	/***************************************************************************
	 * class DefaultJHResponse
	 */

	function DefaultJHResponse(client, js) {
		this.Attributes();
		this.data(js);
		this.error(js.error);
		this.exception(js.exception);
		this.message(js.message);
		if (js.error == null && js.exception == null && js.ok) {
			this.ok(true);
		} else {
			this.ok(false);
		}
	}

	mc.class_(DefaultJHResponse, function(cc) {

		cc.extends_(Attributes);

		DefaultJHResponse.prototype = {

			ok : function(value) {
				return this.attr('ok', value);
			},

			error : function(value) {
				return this.attr('error', value);
			},

			exception : function(value) {
				return this.attr('exception', value);
			},

			message : function(value) {
				return this.attr('message', value);
			},

			data : function(value) {
				return this.attr('data', value);
			},

		};

	});

	/***************************************************************************
	 * class DefaultJHRClient
	 */

	function DefaultJHRClient(context) {
		this.m_context = context;
	}

	mc.class_(DefaultJHRClient, function(cc) {

		var isMimeTypeOf = function(str, array) {
			if (str == null) {
				return false;
			}
			var i = str.indexOf(';');
			if (i >= 0) {
				str = str.substring(0, i);
			}
			for ( var j in array) {
				var val = array[j];
				if (str == val) {
					return true;
				}
			}
			return false;
		};

		var on_response = function(xhr, client, request, func) {
			var ready_state = xhr.readyState;
			if (ready_state != 4) {
				return;
			}
			var js = null;
			var http_status = xhr.status;
			var http_status_text = xhr.statusText;
			if (http_status == 200) {
				var mime = xhr.getResponseHeader('content-type');
				var reg_types = [ 'application/json', 'text/json',
						'text/javascript' ];
				if (isMimeTypeOf(mime, reg_types)) {
					var text = xhr.responseText;
					js = JSON.parse(text);
				} else {
					js = {
						error : ('bad_content_type'),
						message : ('need json but rx a ' + mime),
					};
				}
			} else {
				js = {
					error : ('HTTP ' + http_status),
					message : http_status_text,
				};
			}
			var response = client.createResponse(js);
			func(response);
		};

		var do_get = function(client, request, func) {

			var self = client;
			var url = client.getServiceURI();

			// make data
			var token = request.token();
			var type = request.type();
			var id = request.id();
			var action = request.action();
			var data = request.data();
			if (data == null) {
				data = {};
			}
			data.token = Base64.encode(JSON.stringify(token));
			data.type = type;
			data.id = id;
			data.action = action;

			// make query
			var query = null;
			for ( var key in data) {
				var value = data[key];
				if (query == null) {
					query = '?' + key + '=' + value;
				} else {
					query += ('&' + key + '=' + value);
				}
			}
			if (query == null) {
				query = "";
			}

			// send by XHR
			var xhr = new XMLHttpRequest();
			xhr.open('get', url + query);
			xhr.onreadystatechange = function() {
				on_response(xhr, client, request, func);
			};
			xhr.send();
		}

		var do_post = function(client, request, func) {

			var self = client;
			var url = client.getServiceURI();

			// make data
			var token = request.token();
			var type = request.type();
			var id = request.id();
			var action = request.action();
			var data = request.data();
			if (data == null) {
				data = {};
			} else {
				var tmp = {};
				for ( var key in data) {
					tmp[key] = data[key];
				}
				data = tmp;
			}
			data.token = token;
			data.type = type;
			data.id = id;
			data.action = action;

			// send by XHR
			var xhr = new XMLHttpRequest();
			xhr.open('post', url);
			xhr.onreadystatechange = function() {
				on_response(xhr, client, request, func);
			};
			xhr.send(JSON.stringify(data));
		}

		DefaultJHRClient.prototype = {

			createRequest : function(http_method) {
				var request = new DefaultJHRequest(this);
				request.httpMethod(http_method);
				return request;
			},

			createResponse : function(js) {
				return new DefaultJHResponse(this, js);
			},

			getServiceURI : function() {
				var uri = this.m_service_uri;
				if (uri == null) {
					var context = this.m_context;
					uri = context.jsapiServiceURI();
					uri = context.normalizeURI(uri);
					this.m_service_uri = uri;
				}
				return uri;
			},

			execute : function(request, func) {
				var method = request.httpMethod();
				method = (method + "").toLowerCase();
				if (method == 'get') {
					do_get(this, request, func);
				} else if (method == 'post') {
					do_post(this, request, func);
				} else {
					var msg = 'unsupported method: ' + method;
					throw new RuntimeException(msg);
				}
			},

		};

	});

	/***************************************************************************
	 * class DefaultJHRClientFactory
	 */

	function DefaultJHRClientFactory() {
		// JS.super_call( ) ;
	}

	mc.class_(DefaultJHRClientFactory, function(cc) {

		DefaultJHRClientFactory.prototype = {

			getClient : function(context) {
				return new DefaultJHRClient(context);
			},

		};

	});

}); // end of module

this.Page = js.web.Page;
// this.LangObject = js.lang.Object;

this.System.out = new js.web.AlertPrintStream('out');
this.System.err = new js.web.AlertPrintStream('err');

/*******************************************************************************
 * EOF
 */
