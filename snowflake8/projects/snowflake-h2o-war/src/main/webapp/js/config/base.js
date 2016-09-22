/*******************************************************************************
 * 
 * js/config/base.js
 * 
 */

function __config__() {
}

__config__.base = {

	'snowflake.rest.RESTClientFactory' : 'snowflake.rest.RESTClientFactory',

};

snowflake.web.WebContextUtils.init(function(factory) {

	factory.beans(__config__.base);

	factory.onCreate(function(context) {

		// var bean1 = context.getBean('id1');
		// var bean2 = context.getBean('id2');
		// bean1.equals(bean2);

	});

});
