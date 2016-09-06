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
	 * class Console
	 */

	function Console() {
	}

	mc.class(function(cc) {
		cc.type(Console);
		cc.extends(Attributes);
	});

	Console.prototype = {

		currentPathURI : function(value) {
			return this.attr('current_path_uri', value);
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

			// execute

			var context = this._context;
			var CLI = com.boluozhai.h2o.cli.CLI;
			var client = CLI.getClient(context);

			client.execute(line, function( /* DONE */) {
			});

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

		currentPathURI : function(value) {

			var context = this._context;
			var console = Console.getInstance(context);
			if (value == null) {
				return console.currentPathURI();
			} else {
				console.currentPathURI(value);
				this.updateCurrentPathView(value);
				return value;
			}

		},

		updateCurrentPathView : function(newPath) {
			var view = this._jq_view;
			view.find('.current-path').text(newPath);
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

		reset : function() {
			this._output.text('');
		},

	};

});

JS.module(function(mc) {

	mc.package('com.boluozhai.h2o.cli');

	var System = mc.import('js.lang.System');
	// var ListBuilder = mc.import('snowflake.view.list.ListBuilder');
	// var ListModel = mc.import('snowflake.view.list.ListModel');
	var Attributes = mc.import('js.lang.Attributes');
	var RESTClient = mc.import('snowflake.rest.RESTClient');
	var RestEntity = mc.import('snowflake.rest.RestEntity');
	var Console = mc.import('com.boluozhai.h2o.widget.console.Console');
	// var Event = mc.import('js.event.Event');
	// var EventDispatcher = mc.import('js.event.EventDispatcher');
	// var ResourceLoader =
	// mc.import('com.boluozhai.h2o.widget.ResourceLoader');

	/***************************************************************************
	 * class CLI
	 */

	function CLI() {
	}

	mc.class(function(cc) {
		cc.type(CLI);
	});

	CLI.prototype = {};

	CLI.getClient = function(context) {
		var key = CLIClientFactory.class.getName();
		var factory = context.getBean(key);
		return factory.create(context);
	};

	CLI.getService = function(context) {
		var key = CLIServiceFactory.class.getName();
		var factory = context.getBean(key);
		return factory.create(context);
	};

	/***************************************************************************
	 * class CLIClient
	 */

	function CLIClient(context) {
		this._context = context;
	}

	mc.class(function(cc) {
		cc.type(CLIClient);
		cc.extends(Attributes);
	});

	CLIClient.prototype = {

		execute : function(cmd, fn) {

			var pkg_cli = com.boluozhai.h2o.cli;
			var CLI = pkg_cli.CLI;
			var CLICommandContext = pkg_cli.CLICommandContext;

			var context = this._context;
			var service = CLI.getService(context);

			// make command context
			cmd = cmd.trim();
			var args = this.inner_parse_arguments(cmd);
			var name = args[0];
			var cur_path = this.inner_get_current_path_uri(context);

			var cmd_context = new CLICommandContext();
			cmd_context.command(cmd);
			cmd_context.context(context);
			cmd_context.arguments(args);
			cmd_context.currentPathURI(cur_path);
			cmd_context.callback(function() {
				if (fn != null) {
					fn();
				}
			});

			// execute
			var handler = service.getHandler(name);
			handler.process(cmd_context);

		},

		currentPathURI : function(value) {
			return this.attr('current_path_uri', value);
		},

		inner_get_current_path_uri : function(context) {
			var console = Console.getInstance(context);
			return console.currentPathURI();
		},

		inner_parse_arguments : function(cmd) {
			var array = cmd.split(' ');
			var args = [];
			for ( var i in array) {
				var s = array[i];
				if (s.length > 0) {
					args.push(s);
				}
			}
			if (args.length == 0) {
				args.push('');
			}
			return args;
		},

	};

	/***************************************************************************
	 * class CLIClientFactory
	 */

	function CLIClientFactory() {
	}

	mc.class(function(cc) {
		cc.type(CLIClientFactory);
	});

	CLIClientFactory.prototype = {

		create : function(context) {
			return new CLIClient(context);
		},

	};

	/***************************************************************************
	 * class CLIService
	 */

	function CLIService() {
		this._handlers = {};
		this._default_handler = null;
	}

	mc.class(function(cc) {
		cc.type(CLIService);
	});

	CLIService.prototype = {

		getHandler : function(name) {
			var h = this._handlers[name];
			if (h == null) {
				h = this._default_handler;
			}
			if (h == null) {
				var msg = 'Cannot find handler for command:' + name;
				throw new RuntimeException(msg);
			}
			return h;
		},

		setHandler : function(name, handler) {
			this._handlers[name] = handler;
		},

		setDefaultHandler : function(handler) {
			this._default_handler = handler;
		},

	};

	/***************************************************************************
	 * class CLIServiceFactory
	 */

	function CLIServiceFactory() {
	}

	mc.class(function(cc) {
		cc.type(CLIServiceFactory);
	});

	var CLIServiceFactory_inst = null;

	CLIServiceFactory.prototype = {

		create : function(context) {
			var inst = CLIServiceFactory_inst;
			if (inst == null) {
				inst = new CLIService(context);
				this.inner_init_service(inst);
				CLIServiceFactory_inst = inst;
			}
			return inst;
		},

		inner_init_service : function(service) {

			service.setDefaultHandler(new RestCommandHandler());
			service.setHandler('clear', new CmdClearHandler());
			service.setHandler('', new CmdNilHandler());

		},

	};

	/***************************************************************************
	 * class CLIHandler
	 */

	function CLIHandler() {
	}

	mc.class(function(cc) {
		cc.type(CLIHandler);
	});

	CLIHandler.prototype = {

		process : function(cmd_context) {

		},

	};

	/***************************************************************************
	 * class RestCommandHandler
	 */

	function RestCommandHandler() {
	}

	mc.class(function(cc) {
		cc.type(RestCommandHandler);
		cc.extends(CLIHandler);
	});

	RestCommandHandler.prototype = {

		process : function(cmd_context) {

			var context = cmd_context.context();
			var args = cmd_context.arguments();
			var cmd_line = cmd_context.command();
			var cur_path = cmd_context.currentPathURI();

			var client = RESTClient.getInstance(context);
			var app = client.getApplication();
			var api = app.getAPI('rest');
			var type = api.getType('command');
			var res = type.getResource(args[0]);
			var request = res.post();

			var req_entity = new RestEntity();
			req_entity.json({
				argument : {
					command : cmd_line,
					pathURI : cur_path,
				}
			});

			request.entity(req_entity);
			request.execute(function(response) {

				if (response.ok()) {
					var res_entity = response.entity();
					var js = res_entity.toJSON();
					var msg = js.result.message;
					System.out.println(msg);
				} else {
					var msg = response.message();
					System.out.println(msg);
				}

			});

		},

	};

	/***************************************************************************
	 * class CmdNilHandler
	 */

	function CmdNilHandler() {
	}

	mc.class(function(cc) {
		cc.type(CmdNilHandler);
		cc.extends(CLIHandler);
	});

	CmdNilHandler.prototype = {

		process : function(cmd_context) {
			// NOP
		},

	};

	/***************************************************************************
	 * class CmdClearHandler
	 */

	function CmdClearHandler() {
	}

	mc.class(function(cc) {
		cc.type(CmdClearHandler);
		cc.extends(CLIHandler);
	});

	CmdClearHandler.prototype = {

		process : function(cmd_context) {
			System.out.reset();
		},

	};

	/***************************************************************************
	 * class CLICommandContext
	 */

	function CLICommandContext() {
	}

	mc.class(function(cc) {
		cc.type(CLICommandContext);
		cc.extends(Attributes);
	});

	CLICommandContext.prototype = {

		command : function(value) {
			return this.attr('command', value);
		},

		arguments : function(value) {
			return this.attr('arguments', value);
		},

		context : function(value) {
			return this.attr('context', value);
		},

		callback : function(value) {
			return this.attr('callback', value);
		},

		currentPathURI : function(value) {
			return this.attr('current_path_uri', value);
		},

	};

});

/*******************************************************************************
 * EOF
 */
