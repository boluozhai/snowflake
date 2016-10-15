/*******************************************************************************
 * 
 * i18n.js
 * 
 * @Copyright (c) 2016 boluozhai.com
 * @License MIT License
 * @Author xukun<xukun17@sina.com>
 * 
 */

JS.module(function(mc) {

	mc.package('h2o.i18n');

	var Attributes = mc.import('js.lang.Attributes');

	/***************************************************************************
	 * class StringResTable
	 */

	function StringResTable() {
	}

	mc.class(function(cc) {
		cc.type(StringResTable);
	});

	StringResTable.prototype = {};

	StringResTable.mapping = {};

	/***************************************************************************
	 * inner class Internationalization ( I18nImpl )
	 */

	function Internationalization() {
		this._lang_table = {};
		this._word_table = {};
	}

	Internationalization.instance = null;

	Internationalization.getInstance = function() {
		var inst = Internationalization.instance;
		if (inst == null) {
			inst = new Internationalization();
			Internationalization.instance = inst;
		}
		return inst;
	};

	Internationalization.prototype = {

		load : function() {
			var map = h2o.i18n.StringResTable.mapping;
			this.appendMapping(map);
		},

		getString : function(key) {
			var table = this._word_table;
			var value = table[key];
			if (value == null) {
				value = 'i18n(' + key + ')';
			}
			return value;
		},

		getStringTable : function() {
			return this._word_table;
		},

		appendMapping : function(lang_tab) {
			var to = this._word_table;
			for ( var lang in lang_tab) {
				var words = lang_tab[lang];
				this._lang_table[lang] = words;
				for ( var key in words) {
					var value = words[key];
					to[key] = value;
				}
			}
		},

		lang : function(lang) {
			if (lang == null) {
				lang = this._current_language;
			} else {
				this._current_language = lang;
				this.inner_set_lang(lang);
			}
			return lang;
		},

		inner_set_lang : function(lang) {
			this.load(lang);
			var to = this._word_table;
			var words = this._lang_table[lang];
			if (words == null) {
				return;
			}
			for ( var key in words) {
				var value = words[key];
				to[key] = value;
			}
		},

		translate : function(context, query) {
			this.trans_elements(context, query);
			this.setup_no_impl(context, query);
		},

		setup_no_impl : function(context, query) {
			query.find('.no-impl').click(function() {
				alert('施工中...');
			});
		},

		trans_atts_in_string : function(context, str) {
			// like '{attr:xxxx}'
			var head = '{attr:';
			var tail = '}';
			var sb = '';
			var i = 0;
			for (;;) {
				var i0 = str.indexOf(head, i);
				if (i0 < 0) {
					break;
				}
				var i1 = str.indexOf(tail, i0);
				if (i1 < 0) {
					break;
				}
				sb += str.substring(i, i0);
				var key = str.substring(i0 + head.length, i1);
				var value = context.attr(key);
				if (value == null) {
					value = '(attr:' + key + ')';
				}
				sb += value;
				i = i1 + tail.length;
			}
			sb += str.substring(i);
			return sb;
		},

		trans_elements : function(context, query) {

			var list1 = query.find('.context');
			var list2 = query.find('.i18n');
			var list = [];
			for (var i = list1.length - 1; i >= 0; i--) {
				list.push(list1[i]);
			}
			for (var i = list2.length - 1; i >= 0; i--) {
				list.push(list2[i]);
			}

			var prefix_i18n = 'i18n-';
			var prefix_context = 'context-';

			var task = {};
			task.context = context;
			task.self = this;

			var fn_i18n = function(task) {
				task.self.trans_task_i18n(task);
			};

			var fn_i18n_x = function(task) {
				task.self.trans_task_i18n_x(task);
			};

			var fn_context_x = function(task) {
				task.self.trans_task_context_x(task);
			};

			for ( var i in list) {

				var ele = list[i];
				var atts = ele.attributes;
				var q = $(ele);

				for ( var i2 in atts) {

					var key = atts[i2].name;
					var prefix = null;

					if (key == null) {
						continue;

					} else if (key == 'i18n') {
						prefix = 'i18n';
						task.fn = fn_i18n;

					} else if (key.indexOf(prefix_i18n) == 0) {
						prefix = prefix_i18n;
						task.fn = fn_i18n_x;

					} else if (key.indexOf(prefix_context) == 0) {
						prefix = prefix_context;
						task.fn = fn_context_x;

					} else {
						continue;
					}

					var value = q.attr(key);
					var k2 = key.substring(prefix.length);

					task.query = q;
					task.prefix = prefix;
					task.key = k2;
					task.value = value;

					task.fn(task);

				}

			}

		},

		trans_task_i18n : function(task) {
			var key = task.value;
			var value = this.getString(key);
			if (value == null) {
				value = 'i18n(' + key + ')';
			}
			task.query.text(value);
		},

		trans_task_i18n_x : function(task) {
			var key = task.value;
			var value = this.getString(key);
			if (value == null) {
				value = 'i18n(' + key + ')';
			}
			task.query.attr(task.key, value);
		},

		trans_task_context_x : function(task) {
			var raw = task.value;
			var context = task.context;
			var value = context.normalizeURL(raw);
			value = this.trans_atts_in_string(context, value);
			task.query.attr(task.key, value);
		},

	};

	/***************************************************************************
	 * class I18nProxy
	 */

	function I18nProxy() {
		this._impl_ = null;
	}

	I18nProxy.prototype = {

		impl : function() {
			var i = this._impl_;
			if (i == null) {
				i = Internationalization.getInstance();
				this._impl_ = i;
			}
			return i;
		},

		appendMapping : function(lang_tab) {
			return this.impl().appendMapping(lang_tab);
		},

		getString : function(key) {
			return this.impl().getString(key);
		},

		getStringTable : function() {
			return this.impl().getStringTable();
		},

		lang : function(lang) {
			return this.impl().lang(lang);
		},

		load : function(context) {
			return this.impl().load(context);
		},

		translate : function(context, q) {
			return this.impl().translate(context, q);
		},

	};

	/***************************************************************************
	 * class I18nResManager
	 */

	function I18nResManager(context) {

		this._context = context;
		this.basePath('~/lib/i18n/');

	}

	mc.class(function(cc) {
		cc.type(I18nResManager);
		cc.extends(Attributes);
	});

	I18nResManager.prototype = {

		basePath : function(path) {
			return this.attr('base_path', path);
		},

		forLang : function(lang) {
			if (lang == null) {
				lang = 'default';
			}
			var context = this._context;
			var url = this.basePath();
			url = context.normalizeURL(url);
			if (url.lastIndexOf('/') == url.length - 1) {
				url += lang;
			} else {
				url += ('/' + lang);
			}
			return new I18nResSet(url, lang);
		},

	};

	/***************************************************************************
	 * class I18nResSet
	 */

	function I18nResSet(path, lang) {
		this._path = path;
		this._lang = lang;
	}

	mc.class(function(cc) {
		cc.type(I18nResSet);
		cc.extends(Attributes);
	});

	I18nResSet.prototype = {

		lang : function() {
			return this._lang;
		},

		getResPath : function(file_name) {
			return (this._path + '/' + this._lang + '.' + file_name);
		},

	};

	/***************************************************************************
	 * class I18n
	 */

	function I18n( /* BEAN */) {
		this.inner = new I18nProxy();
	}

	mc.class(function(cc) {
		cc.type(I18n);
	});

	I18n.getInstance = function() {
		return new I18n();
	};

	I18n.prototype = {

		translate : function(context, query) {
			return this.inner.translate(context, query);
		},

		getString : function(key) {
			return this.inner.getString(key);
		},

		getStringTable : function() {
			return this.inner.getStringTable();
		},

		lang : function(lang) {
			return this.inner.lang(lang);
		},

	};

});

this.snowflake.I18n = h2o.i18n.I18n;
this.snowflake.I18nResManager = h2o.i18n.I18nResManager;

/*******************************************************************************
 * EOF
 */
