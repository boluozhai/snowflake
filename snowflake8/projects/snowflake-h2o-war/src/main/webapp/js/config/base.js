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

		var rest_factory = context.getBean('snowflake.rest.RESTClientFactory');

		rest_factory.pathPattern('~/api/uid/repo/type/id/*');

	});

});
