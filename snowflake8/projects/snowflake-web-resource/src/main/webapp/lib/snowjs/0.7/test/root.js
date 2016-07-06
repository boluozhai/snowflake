Page.config({

	beans : {
		'js.web.jhr.JSONHttpRequestClients' : {
			'class' : 'js.web.jhr.DefaultJHRClientFactory',
		},

		'js.i18n.MessageSource' : {

			'class' : 'js.i18n.DefaultMessageSource',

		},

		'js.i18n.I18nManager' : {

			'class' : 'js.i18n.I18nManager',

		},

	}

});

Page.onload(function(context) {
	context.jsapiServiceURI('~/jsapi/package');
});

function getContext() {
	return Page.context();
}
