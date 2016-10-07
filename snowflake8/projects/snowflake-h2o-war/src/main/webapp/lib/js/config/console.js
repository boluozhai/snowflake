/*******************************************************************************
 * 
 * js/config/console.js
 * 
 */

__config__.console = {

	'com.boluozhai.h2o.cli.CLIClientFactory' : 'com.boluozhai.h2o.cli.CLIClientFactory',
	'com.boluozhai.h2o.cli.CLIServiceFactory' : 'com.boluozhai.h2o.cli.CLIServiceFactory',

	'com.boluozhai.h2o.widget.console.Console' : 'com.boluozhai.h2o.widget.console.Console',

};

snowflake.web.WebContextUtils.init(function(factory) {

	factory.beans(__config__.console);

	factory.onCreate(function(context) {

		// var bean1 = context.getBean('id1');
		// var bean2 = context.getBean('id2');
		// bean1.equals(bean2);

	});

});
