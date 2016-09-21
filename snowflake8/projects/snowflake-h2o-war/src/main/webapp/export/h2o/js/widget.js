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

/*******************************************************************************
 * EOF
 */
