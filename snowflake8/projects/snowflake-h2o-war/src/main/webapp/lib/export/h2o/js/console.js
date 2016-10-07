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
	var FunctionAdapter = mc.import('js.event.FunctionAdapter');
	// var EventDispatcher = mc.import('js.event.EventDispatcher');
	var ResourceLoader = mc.import('com.boluozhai.h2o.widget.ResourceLoader');
	var DocumentBinder = mc.import('com.boluozhai.h2o.widget.DocumentBinder');

	/***************************************************************************
	 * class Console
	 */

	function Console() {
	}

	mc.class(function(cc) {
		cc.type(Console);
		cc.extends(Attributes);
	});

	Console.prototype = {

		xxxxxxxx : function(value) {
			// return this.attr('current_path_uri', value);
		},

	};

	Console.getInstance = function(context) {
		var key = Console.class.getName();
		return context.getBean(key);
	};

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

		binder : function() {
			var binder = this._ui_binder;
			if (binder == null) {
				binder = new ConsoleUIBinder();
				this._ui_binder = binder;
			}
			return binder;
		},

		init : function() {

			// init view
			var context = this._context;
			this._parent = this.binder().parent();
			var loader = new ResourceLoader(context);
			var self = this;
			loader.loadHTML('~/lib/export/h2o/html/Console.html', function(query) {
				self.onHtmlReday(query.find('.console'));
			});

			this.setupLocationListener();

		},

		setupLocationListener : function() {

			var self = this;
			var li = new FunctionAdapter(function(e) {

				self.updateCurrentPathView();

			});
			var cl = this.currentLocation();
			cl.addEventHandler(li);

		},

		setupSystemOutput : function() {
			System.setOut(new ConsoleOutputStream(this));
		},

		setupViewListener : function() {

			var self = this;
			var view = this._parent;
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

			// execute

			var context = this._context;
			var CLI = com.boluozhai.h2o.cli.CLI;
			var client = CLI.getClient(context);
			client.currentLocation(this.currentLocation());

			client.execute(line, function( /* DONE */) {
			});

		},

		onHtmlReday : function(query) {

			var parent = this.binder().parent();
			var child = query;
			parent.append(child);
			this._jq_view = child;

			this.setupViewListener();
			this.setupSystemOutput();

			System.out.println('hello, snowflake');

		},

		updateCurrentPathView : function() {

			var cl = this.currentLocation();
			var file = cl.location();
			var uri = file.toFileURI();

			var view = this._jq_view;
			view.find('.current-path').text(uri);

		},

		currentLocation : function(value) {
			return this.attr('currentLocation', value);
		},

	};

	/***************************************************************************
	 * class ConsoleUIBinder
	 */

	function ConsoleUIBinder() {
	}

	mc.class(function(cc) {
		cc.type(ConsoleUIBinder);
		cc.extends(DocumentBinder);
	});

	ConsoleUIBinder.prototype = {

		parent : function(value) {
			return this.bind('parent', value);
		},

	};

	/***************************************************************************
	 * class ConsoleOutputStream
	 */

	function ConsoleOutputStream(ctrl) {
		var parent = ctrl.binder().parent();
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

		reset : function() {
			this._output.text('');
		},

	};

});

/*******************************************************************************
 * EOF
 */
