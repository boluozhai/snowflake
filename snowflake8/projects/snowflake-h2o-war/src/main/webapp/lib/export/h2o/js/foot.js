/*******************************************************************************
 * 
 * foot.js
 * 
 * @Copyright (c) 2016 boluozhai.com
 * @License MIT License
 * @Author xukun<xukun17@sina.com>
 * 
 */

JS.module(function(mc) {

	mc.package('com.boluozhai.h2o.widget.foot');

	var System = mc.import('js.lang.System');
	var Attributes = mc.import('js.lang.Attributes');
	var RESTClient = mc.import('snowflake.rest.RESTClient');

	var DocumentBinder = mc.import('com.boluozhai.h2o.widget.DocumentBinder');
	var ResourceLoader = mc.import('com.boluozhai.h2o.widget.ResourceLoader');

	var widget_x = 'com.boluozhai.h2o.widget';

	// var FileListCtrl = mc.import(widget_x + '.folder.FileListCtrl');
	// var PathBarCtrl = mc.import(widget_x + '.folder.PathBarCtrl');
	// var DirDataCtrl = mc.import(widget_x + '.folder.DirDataCtrl');
	// var ConsoleCtrl = mc.import(widget_x + '.console.ConsoleCtrl');

	/***************************************************************************
	 * class FootCtrl
	 */

	function FootCtrl(context) {
		this._context = context;
		this._binder = new FootBinder();
	}

	mc.class(function(cc) {
		cc.type(FootCtrl);
		cc.extends(Attributes);
	});

	FootCtrl.prototype = {

		init : function() {
			this.loadHtmlUI();
		},

		loadHtmlUI : function() {

			var context = this._context;
			var loader = new ResourceLoader(context);
			var self = this;
			loader.loadHTML('~/lib/export/h2o/html/Foot.html', function(query) {
				self.onHtmlReday(query.find('.foot'));
			});

		},

		onHtmlReday : function(query) {

			var parent = this.binder().parent();
			var child = query;
			parent.append(child);

			// this._hp_man.setParent(query.find('.head-panel'));
			// this.setupAuthButton(child);
			// this.setupAccountInfo();
			// this.setupPanelList(query);

		},

		binder : function() {
			return this._binder;
		},

	};

	/***************************************************************************
	 * class FootBinder
	 */

	function FootBinder() {
	}

	mc.class(function(cc) {
		cc.type(FootBinder);
		cc.extends(DocumentBinder);
	});

	FootBinder.prototype = {

		parent : function(value) {
			return this.bind('parent', value);
		},

	};

});

/*******************************************************************************
 * EOF
 */
