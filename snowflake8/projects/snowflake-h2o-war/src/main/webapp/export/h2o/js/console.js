/*******************************************************************************
 * 
 * console.js
 * 
 * @Copyright (c) 2016 boluozhai.com
 * @License MIT License
 * @Author xukun<xukun17@sina.com>
 * 
 */

JS.module(function(mc) {

	mc.package('com.boluozhai.h2o.widget.console');

	var System = mc.import('js.lang.System');
	// var ListBuilder = mc.import('snowflake.view.list.ListBuilder');
	// var ListModel = mc.import('snowflake.view.list.ListModel');
	var Attributes = mc.import('js.lang.Attributes');
	// var RESTClient = mc.import('snowflake.rest.RESTClient');
	// var Event = mc.import('js.event.Event');
	// var EventDispatcher = mc.import('js.event.EventDispatcher');
	var ResourceLoader = mc.import('com.boluozhai.h2o.widget.ResourceLoader');

	/***************************************************************************
	 * class ConsoleCtrl
	 */

	function ConsoleCtrl(context) {
		this._context = context;
	}

	mc.class(function(cc) {
		cc.type(ConsoleCtrl);
		cc.extends(Attributes);
	});

	ConsoleCtrl.prototype = {

		parent : function(query) {
			return this.attr('parent_view', query);
		},

		init : function() {

			// init view
			var context = this._context;
			var loader = new ResourceLoader(context);
			var self = this;
			loader.loadHTML('~/export/h2o/html/Console.html', function(query) {
				self.onHtmlReday(query.find('.console'));
			});

		},

		setupSystemOutput : function() {
			System.setOut(new ConsoleOutputStream(this));
		},

		setupViewListener : function() {

			var self = this;
			var view = this.parent();
			var input = view.find('.console-input');
			var enter = view.find('.console-enter');

			enter.click(function() {
				self.onEnter();
			});

			input.keyup(function(e) {
				var key = e.which;
				if (key == 13) {
					self.onEnter();
				}
			});

			this._input = input;

		},

		onEnter : function() {
			var input = this._input;
			var line = input.val();
			input.val('');
			System.out.println('> ' + line);
		},

		onHtmlReday : function(query) {

			var parent = this.parent();
			var child = query;
			parent.append(child);
			this._jq_view = child;

			this.setupViewListener();
			this.setupSystemOutput();

			System.out.println('hello, snowflake');

		},

	};

	/***************************************************************************
	 * class ConsoleOutputStream
	 */

	function ConsoleOutputStream(ctrl) {
		var parent = ctrl.parent();
		this._output = parent.find('.console-output');
		this._input = parent.find('.console-input');
	}

	mc.class(function(cc) {
		cc.type(ConsoleOutputStream);
	});

	ConsoleOutputStream.prototype = {

		println : function(s) {
			this._output.append(s + '\n');
			this.scroll();
		},

		scroll : function() {

			window.location = '#input-point';
			this._input.focus();

		},

	};

});

/*******************************************************************************
 * EOF
 */
