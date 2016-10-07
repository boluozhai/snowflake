/*******************************************************************************
 * js/config/i18n-base.js
 */

snowflake.web.WebContextUtils.init(function(factory) {

	factory.onCreate(function(context) {

		var map = h2o.i18n.StringResTable.mapping;
		var i18n = context.i18n();
		i18n.appendMapping(map);

	});

});

JS.module(function(mc) {

	mc.package('h2o.i18n');

	/***************************************************************************
	 * class StringResTable
	 */

	function StringResTable() {
	}

	mc.class(function(cc) {
		cc.type(StringResTable);
	});

	StringResTable.prototype = {};

});
