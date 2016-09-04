/*******************************************************************************
 * 
 * widget.js
 * 
 * @Copyright (c) 2016 boluozhai.com
 * @License MIT License
 * @Author xukun<xukun17@sina.com>
 * 
 */

JS.module(function(mc) {

	mc.package('com.boluozhai.h2o.widget');

	var System = mc.import('js.lang.System');
	var ListBuilder = mc.import('snowflake.view.list.ListBuilder');
	var ListModel = mc.import('snowflake.view.list.ListModel');
	var Attributes = mc.import('js.lang.Attributes');
	var RESTClient = mc.import('snowflake.rest.RESTClient');
	var Event = mc.import('js.event.Event');
	var EventDispatcher = mc.import('js.event.EventDispatcher');

	/***************************************************************************
	 * class ResourceLoader
	 */

	function ResourceLoader(context) {
		this._context = context;
	}

	mc.class(function(cc) {
		cc.type(ResourceLoader);
		cc.extends(Attributes);
	});

	ResourceLoader.prototype = {

		loadHTML : function(url, fn/* (query) */) {

			var context = this._context;
			url = context.normalizeURL(url);

			var xhr = new XMLHttpRequest();
			xhr.open('GET', url, true);
			xhr.onreadystatechange = function() {

				var state = xhr.readyState;
				if (state != 4) {
					return;
				}
				var status = xhr.status;
				if (status != 200) {
					throw new RuntimeException('HTTP ' + status);
				}
				var txt = xhr.responseText;
				var child = $(txt);
				var parent = $('<div></div>');
				parent.append(child);
				fn(parent);

			};
			xhr.send();

		},

	};

});

/*******************************************************************************
 * EOF
 */
