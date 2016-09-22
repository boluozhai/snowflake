/*******************************************************************************
 * 
 * header.js
 * 
 * @Copyright (c) 2016 boluozhai.com
 * @License MIT License
 * @Author xukun<xukun17@sina.com>
 * 
 */

JS.module(function(mc) {

	mc.package('com.boluozhai.h2o.widget.head.ext');

	var System = mc.import('js.lang.System');
	var Attributes = mc.import('js.lang.Attributes');
	var RESTClient = mc.import('snowflake.rest.RESTClient');
	var SHA1 = mc.import('snowflake.security.sha1.SHA1');

	var widget_x = 'com.boluozhai.h2o.widget';

	// var FileListCtrl = mc.import(widget_x + '.folder.FileListCtrl');
	// var PathBarCtrl = mc.import(widget_x + '.folder.PathBarCtrl');
	// var DirDataCtrl = mc.import(widget_x + '.folder.DirDataCtrl');
	// var ConsoleCtrl = mc.import(widget_x + '.console.ConsoleCtrl');

	/***************************************************************************
	 * class HeaderCtrl
	 */

	function HeaderCtrl(context) {
		this._context = context;
	}

	mc.class(function(cc) {
		cc.type(HeaderCtrl);
		cc.extends(Attributes);
	});

	HeaderCtrl.prototype = {

		xxxxxx : function() {
			// return new DoRegister(this._context);
		},

	};

});

/*******************************************************************************
 * EOF
 */
