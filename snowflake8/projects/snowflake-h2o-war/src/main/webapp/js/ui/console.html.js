/*******************************************************************************
 * 
 * console.html.js
 * 
 * @Copyright (c) 2016 boluozhai.com
 * @License MIT License
 * @Author xukun<xukun17@sina.com>
 * 
 */

JS.module(function(mc) {

	mc.package('com.boluozhai.h2o.html');

	var System = mc.import('js.lang.System');
	var Attributes = mc.import('js.lang.Attributes');
	var RESTClient = mc.import('snowflake.rest.RESTClient');

	var vfs_x = 'com.boluozhai.snowflake.vfs';
	var CurrentLocation = mc.import(vfs_x + '.CurrentLocation');
	var VFSFactory = mc.import(vfs_x + '.VFSFactory');

	var widget_x = 'com.boluozhai.h2o.widget';
	var ConsoleCtrl = mc.import(widget_x + '.console.ConsoleCtrl');

	/***************************************************************************
	 * class ConsoleHtml
	 */

	function ConsoleHtml(context) {
		this._context = context;
	}

	mc.class(function(cc) {
		cc.type(ConsoleHtml);
		cc.extends(Attributes);
	});

	ConsoleHtml.prototype = {

		init : function() {

			var context = this._context;
			var cl = new CurrentLocation(context);

			// load UI
			var ctrl = new ConsoleCtrl(context);
			var binder = ctrl.binder();
			binder.parent('#console');
			ctrl.currentLocation(cl);
			ctrl.init();

			// loader

			var vfs_factory = new VFSFactory();
			vfs_factory.httpURI('~/rest/file');
			var vfs = vfs_factory.create(context);
			vfs.ready(function() {

				var root = vfs.root();
				cl.location(root);

			});

		},

		onEvent : function(event) {

		},

	};

});

/*******************************************************************************
 * EOF
 */
