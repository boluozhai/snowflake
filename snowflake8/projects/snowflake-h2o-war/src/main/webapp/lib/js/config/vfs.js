/*******************************************************************************
 * 
 * js/config/vfs.js
 * 
 */

__config__.base = {

	'snowflake.rest.RESTClientFactory' : 'snowflake.rest.RESTClientFactory',
	'com.boluozhai.snowflake.vfs.VFSFactory' : 'com.boluozhai.snowflake.vfs.RestVFSFactory',

};

snowflake.web.WebContextUtils.init(function(factory) {

	factory.beans(__config__.base);

	factory.onCreate(function(context) {

		// var rest_factory =
		// context.getBean('snowflake.rest.RESTClientFactory');
		// rest_factory.pathPattern('~/uid/repo/api/type/id/*');

	});

});
