var beans = {

	'id1' : 'js.lang.Object',
	'id2' : 'js.lang.Object',

	'snowflake.rest.RESTClientFactory' : 'snowflake.rest.RESTClientFactory',

};

JS.module(function(mc) {

	var WebContextFactory = snowflake.web.WebContextFactory;
	var factory = new WebContextFactory();
	WebContextFactory.setDefault(factory)

	factory.pathInWebapp('{webapp}/index.js');
	factory.beans(beans);

	factory.onCreate(function(context) {

		var bean1 = context.getBean('id1');
		var bean2 = context.getBean('id2');

		bean1.equals(bean2);

	});

});