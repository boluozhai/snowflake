/*******************************************************************************
 * Context.js
 */

JS.module_('ContextModule', function(mc) {

	mc.package_('js.context');

	var Properties = js.lang.Properties;

	function Context() {
		this.Properties();
	}

	mc.class_(Context, function(cc) {

		cc.extends_(Properties);

		Context.prototype = {

			getBean : function(name) {
				throw new Exception('implements_in_sub_class');
			},

			appTitle : function(title) {
				return this.property('appTitle', title);
			},

			subTitle : function(title) {
				return this.property('subTitle', title);
			},

			getMessage : function(code, args, defaultMessage, locale) {
				var bean_id = 'js.i18n.MessageSource';
				var i18n = this.getBean(bean_id);
				return i18n.getMessage(code, args, defaultMessage, locale);
			},

		};

	});

});

/*******************************************************************************
 * EOF
 */
