/*******************************************************************************
 * Web.js
 */

JS.module_('I18nModule', function(mc) {

	mc.package_('js.i18n');

	var Attributes = js.lang.Attributes;

	/***************************************************************************
	 * implements I18n
	 */

	function I18n() {
	}

	mc.class_(I18n, function(cc) {

		I18n.onload = function(fn) {
			var manager = I18nManager.getInstance();
			fn(manager);
		};

		I18n.localize = function(query) {
			var tool = new HtmlLocalizer();
			tool.localize(query);
		};

		I18n.getManager = function() {
			return I18nManager.getInstance();
		};

		I18n.prototype = {};

	});

	/***************************************************************************
	 * implements MessageSource
	 */

	function MessageSource() {
	}

	mc.class_(MessageSource, function(cc) {

		// private

		MessageSource.prototype = {

			getMessage : function(code, arg, defaultText, locale) {
				throw new RuntimeException('needs impl in subclass');
			},

		};

	});

	/***************************************************************************
	 * implements I18nManager
	 */

	function I18nManager() {
		this.Attributes();
		this.m_locale_table = {};
		this.defaultLocale('en_US');
	}

	mc.class_(I18nManager, function(cc) {

		cc.extends_(Attributes);

		// private

		var _static_inst_ = null;

		// public

		I18nManager.getInstance = function() {
			var inst = _static_inst_;
			if (inst == null) {
				inst = new I18nManager();
				_static_inst_ = inst;
			}
			return inst;
		},

		I18nManager.prototype = {

			defaultLocale : function(value) {
				return this.attr('defaultLocale', value);
			},

			register : function(name, mapping) {
				var ms = new LocalizedMessageSource(name, mapping);
				this.m_locale_table[name] = ms;
			},

			getMessageSource : function(locale_name) {
				if (locale_name == null) {
					locale_name = this.defaultLocale();
				}
				return this.m_locale_table[locale_name];
			},

		};

	});

	/***************************************************************************
	 * implements DefaultMessageSource
	 */

	function DefaultMessageSource() {
	}

	mc.class_(DefaultMessageSource, function(cc) {

		// private

		DefaultMessageSource.prototype = {

			getMessage : function(code, arg, defaultText, locale) {
				var manager = I18nManager.getInstance();
				var ms = manager.getMessageSource(locale);
				if (ms == null) {
					ms = manager.getMessageSource(null);
				}
				if (defaultText == null) {
					defaultText = '$(' + code + ')';
				}
				if (ms == null) {
					return defaultText;
				}
				return ms.getMessage(code, arg, defaultText, locale);
			},

		};

	});

	/***************************************************************************
	 * implements LocalizedMessageSource
	 */

	function LocalizedMessageSource(name, mapping) {
		this.m_name = name;
		this.m_mapping = mapping;
	}

	mc.class_(LocalizedMessageSource, function(cc) {

		// private

		LocalizedMessageSource.prototype = {

			getMessage : function(code, arg, defaultText, locale) {
				var value = this.m_mapping[code];
				if (value == null) {
					value = defaultText;
				}
				return value;
			},

		};

	});

	/***************************************************************************
	 * implements HtmlLocalizer
	 */

	function HtmlLocalizer(manager) {

		this.Attributes();

		if (manager == null) {
			manager = I18nManager.getInstance();
		}

		this.m_manager = manager;

		this.defaultRootSelector('body');
		this.i18nSelector('.i18n');
		this.i18nPrefix('i18n-');

	}

	mc.class_(HtmlLocalizer, function(cc) {

		cc.extends_(Attributes);

		// private

		function getClassByPrefix(query, prefix) {
			var clazz = query.attr('class');
			var i0 = clazz.indexOf(prefix);
			if (i0 < 0) {
				return null;
			}
			var i1 = clazz.indexOf(' ', i0);
			if (i1 < 0) {
				return clazz.substring(i0);
			} else {
				return clazz.substring(i0, i1);
			}
		}

		// public

		HtmlLocalizer.prototype = {

			defaultRootSelector : function(value) {
				return this.attr('default_root_sel', value);
			},

			i18nSelector : function(value) {
				return this.attr('i18n_sel', value);
			},

			i18nPrefix : function(value) {
				return this.attr('i18n_prefix', value);
			},

			localize : function(query) {

				var sel_body = this.defaultRootSelector();
				var sel_i18n = this.i18nSelector();
				var sel_prefix = this.i18nPrefix();

				if (query == null) {
					query = $(sel_body);
				}
				query = query.find(sel_i18n);

				var ms = new DefaultMessageSource();

				for (var i = query.size() - 1; i >= 0; i--) {
					var ele = query.get(i);
					var q2 = $(ele);
					var code = getClassByPrefix(q2, sel_prefix);
					var msg = ms.getMessage(code);
					q2.text(msg);
				}

			},

		};

	});

}); // end of module

/*******************************************************************************
 * EOF
 */
