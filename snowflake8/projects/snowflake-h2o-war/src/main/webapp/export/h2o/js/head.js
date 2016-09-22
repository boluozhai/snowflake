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

	mc.package('com.boluozhai.h2o.widget.head');

	var System = mc.import('js.lang.System');
	var Attributes = mc.import('js.lang.Attributes');
	var RESTClient = mc.import('snowflake.rest.RESTClient');
	// var SHA1 = mc.import('snowflake.security.sha1.SHA1');

	var widget_x = 'com.boluozhai.h2o.widget';

	// var FileListCtrl = mc.import(widget_x + '.folder.FileListCtrl');
	// var PathBarCtrl = mc.import(widget_x + '.folder.PathBarCtrl');
	// var DirDataCtrl = mc.import(widget_x + '.folder.DirDataCtrl');
	// var ConsoleCtrl = mc.import(widget_x + '.console.ConsoleCtrl');
	var DocumentBinder = mc.import('com.boluozhai.h2o.widget.DocumentBinder');
	var ResourceLoader = mc.import('com.boluozhai.h2o.widget.ResourceLoader');

	/***************************************************************************
	 * class HeadCtrl
	 */

	function HeadCtrl(context) {
		this._context = context;
		this._binder = new HeadBinder();
	}

	mc.class(function(cc) {
		cc.type(HeadCtrl);
		cc.extends(Attributes);
	});

	HeadCtrl.prototype = {

		init : function() {

			this.loadHtmlUI();

		},

		loadHtmlUI : function() {

			var context = this._context;
			// this._parent = this.binder().parent();
			var loader = new ResourceLoader(context);
			var self = this;
			loader.loadHTML('~/export/h2o/html/Head.html', function(query) {
				self.onHtmlReday(query.find('.head'));
			});

		},

		onHtmlReday : function(query) {

			var parent = this.binder().parent();
			var child = query;
			parent.append(child);
			// this._jq_view = child;

			// this.setupViewListener();
			this.setupAccountInfo();

		},

		setupAccountInfo : function() {

			var signed = true;
			var nickname = 'ak';

			var base = this.binder().parent();

			if (signed) {
				// base.find('.account-info-not').hide();
				// base.find('.account-info').show();
			} else {
				// base.find('.account-info').hide();
				// base.find('.account-info-not').show();
			}

			base.find('.string-account-nickname').text(nickname);

		},

		binder : function() {
			return this._binder;
		},

		currentLocation : function(value) {
			return this.attr('current_location', value);
		},

	};

	/***************************************************************************
	 * class HeadBinder
	 */

	function HeadBinder() {
	}

	mc.class(function(cc) {
		cc.type(HeadBinder);
		cc.extends(DocumentBinder);
	});

	HeadBinder.prototype = {

		parent : function(value) {
			return this.bind('parent', value);
		},

	};

});

/*******************************************************************************
 * EOF
 */
