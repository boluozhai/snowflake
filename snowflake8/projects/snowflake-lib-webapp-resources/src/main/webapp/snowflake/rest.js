/*******************************************************************************
 * rest.js
 */

JS.module(function(_mc_) {

	_mc_.package('snowflake.rest');

	var Object = _mc_.import('js.lang.Object');
	var Class = _mc_.import('js.lang.Class');

	/***************************************************************************
	 * class REST
	 */

	function REST() {
	}

	_mc_.class(function(cc) {
		cc.type(REST);
	});

	REST.prototype = {};

	REST.getClient = function(context) {
		return RESTClient.getInstance(context);
	};

	/***************************************************************************
	 * class RESTClient
	 */

	function RESTClient(context) {
		this._context = context;
	}

	_mc_.class(function(cc) {
		cc.type(RESTClient);
	});

	RESTClient.prototype = {};

	RESTClient.getInstance = function(context) {
		var key = RESTClientFactory.class.getName();
		var factory = context.getBean(key);
		return factory.create(context);
	};

	/***************************************************************************
	 * class RESTClientFactory
	 */

	function RESTClientFactory() {
	}

	_mc_.class(function(cc) {
		cc.type(RESTClientFactory);
	});

	RESTClientFactory.prototype = {

		create : function(context) {
			return new RESTClient(context);
		},

	};

});

/*******************************************************************************
 * EOF
 */
