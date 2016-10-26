/*******************************************************************************
 * 
 * widget.js
 * 
 * @Copyright (c) 2016 boluozhai.com
 * @License MIT License
 * @Author xukun<xukun17@sina.com>
 * 
 */

JS.module(function(mc) {

	mc.package('com.boluozhai.h2o.widget');

	var System = mc.import('js.lang.System');
	var Attributes = mc.import('js.lang.Attributes');
	var Event = mc.import('js.event.Event');
	var EventDispatcher = mc.import('js.event.EventDispatcher');

	/***************************************************************************
	 * class ResourceLoader
	 */

	function ResourceLoader(context) {
		this._context = context;
	}

	mc.class(function(cc) {
		cc.type(ResourceLoader);
		cc.extends(Attributes);
	});

	ResourceLoader.prototype = {

		loadCSS : function(url, fn/* () */) {
			throw new Exception('no impl');
		},

		loadJS : function(url, fn/* () */) {

			var context = this._context;
			url = context.normalizeURL(url);

			var js = '<script></script>';
			var parent = $('body');
			var child = $(js);

			child[0].onload = (function() {
				fn();
			});

			parent.append(child);
			child.attr('src', url);

		},

		loadHTML : function(url, fn/* (query) */) {

			var context = this._context;
			url = context.normalizeURL(url);

			var xhr = new XMLHttpRequest();
			xhr.open('GET', url, true);
			xhr.onreadystatechange = function() {

				var state = xhr.readyState;
				if (state != 4) {
					return;
				}
				var status = xhr.status;
				if (status != 200) {
					throw new RuntimeException('HTTP ' + status);
				}
				var txt = xhr.responseText;
				var child = $(txt);
				var parent = $('<div></div>');
				parent.append(child);

				var i18n = context.getBean('i18n');
				i18n.translate(context, parent);

				fn(parent);

			};
			xhr.send();

		},

	};

	/***************************************************************************
	 * class DomBinding
	 */

	function DomBinding(binder, name) {
		this._binder = binder;
		this._name = name;
		this._value = null;
	}

	mc.class(function(cc) {
		cc.type(DomBinding);
	});

	DomBinding.prototype = {

		bind : function(value) {
			if (value == null) {
				value = this._value;
			} else {
				var dom = this._binder.document();
				value = DocumentBinder_get_required_query(dom, value);
				this._value = value;
			}
			return value;
		},

		name : function() {
			return this._name;
		},

	};

	/***************************************************************************
	 * class DocumentBinder
	 */

	function DocumentBinder() {
	}

	mc.class(function(cc) {
		cc.type(DocumentBinder);
		cc.extends(Attributes);
	});

	function DocumentBinder_get_required_query(dom, query_or_sel) {

		if (dom == null) {
			dom = $(document);
		}

		var query = null;
		var con = query_or_sel.context;
		if (con == null) {
			query = dom.find(query_or_sel);
		} else {
			query = query_or_sel;
		}

		if (query.length < 1) {
			var msg = 'cannot find query: ' + query_or_sel;
			throw new SnowflakeException(msg);
		}

		return query;
	}

	DocumentBinder.prototype = {

		bind : function(key, value) {
			var table = this.bindingTable();
			var binding = table[key];
			if (binding == null) {
				binding = new DomBinding(this, key);
				table[key] = binding;
			}
			return binding.bind(value);
		},

		bindingTable : function() {
			var table = this._table;
			if (table == null) {
				table = {};
				this._table = table;
			}
			return table;
		},

		document : function(value) {
			return this.attr('document', value);
		},

		example : function(value) {
			return this.bind('example', value);
		},

	};

});

this.snowflake.ResourceLoader = com.boluozhai.h2o.widget.ResourceLoader;

/*******************************************************************************
 * module 'snowflake.net'
 */

JS.module(function(mc) {

	mc.package('snowflake.net');

	var Attributes = mc.import('js.lang.Attributes');

	/***************************************************************************
	 * class URLElements
	 */

	function URLElements(init) {
		this.Attributes();

		this._param_ = {};

		if (init) {
			this.schema(init.schema());
			this.user(init.user());
			this.host(init.host());
			this.port(init.port());
			this.path(init.path());
			this.query(init.query());
			this.fragment(init.fragment());
		}

	}

	mc.class(function(cc) {
		cc.type(URLElements);
		cc.extends(Attributes);
	});

	URLElements.prototype = {

		schema : function(v) {
			return this.attr('schema', v);
		},

		user : function(v) {
			return this.attr('user', v);
		},

		host : function(v) {
			return this.attr('host', v);
		},

		port : function(v) {
			return this.attr('port', v);
		},

		path : function(v) {
			return this.attr('path', v);
		},

		query : function(v) {
			if (v == null) {
				// TODO to string
			} else {
				// TODO parse v
			}
			return v;
		},

		parameter : function(k, v) {
			var tab = this._param_;
			if (v == null) {
				v = tab[k];
			} else {
				tab[k] = v;
			}
			return v;
		},

		fragment : function(v) {
			return this.attr('fragment', v);
		},

		toString : function() {
			var builder = new URLBuilder(this, this._param_);
			return builder.create();
		},

	};

	/***************************************************************************
	 * class URLParser
	 */

	function URLParser() {
	}

	URLParser.prototype = {};

	/***************************************************************************
	 * class URLBuilder
	 */

	function URLBuilder(elements) {
	}

	URLBuilder.prototype = {};

});

/*******************************************************************************
 * EOF
 */
