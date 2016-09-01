var __beans__ = {

	'id1' : 'js.lang.Object',
	'id2' : 'js.lang.Object',

	'snowflake.rest.RESTClientFactory' : 'snowflake.rest.RESTClientFactory',

};

snowflake.web.WebContextUtils.init(function(factory) {

	factory.beans(__beans__);
	factory.onCreate(function(context) {

		var bean1 = context.getBean('id1');
		var bean2 = context.getBean('id2');

		bean1.equals(bean2);

	});

});
