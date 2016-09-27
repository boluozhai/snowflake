/*******************************************************************************
 * 
 * cli.js
 * 
 * @Copyright (c) 2016 boluozhai.com
 * @License MIT License
 * @Author xukun<xukun17@sina.com>
 * 
 */

JS.module(function(mc) {

	mc.package('com.boluozhai.h2o.cli');

	var System = mc.import('js.lang.System');
	// var ListBuilder = mc.import('snowflake.view.list.ListBuilder');
	// var ListModel = mc.import('snowflake.view.list.ListModel');
	var Attributes = mc.import('js.lang.Attributes');
	var RESTClient = mc.import('snowflake.rest.RESTClient');
	var RestEntity = mc.import('snowflake.rest.RestEntity');
	// var Console = mc.import('com.boluozhai.h2o.widget.console.Console');
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
			var cur_loc = this.currentLocation();

			var cmd_context = new CLICommandContext();
			cmd_context.command(cmd);
			cmd_context.context(context);
			cmd_context.arguments(args);
			cmd_context.currentLocation(cur_loc);
			cmd_context.out(this.out());
			cmd_context.err(this.err());
			cmd_context.callback(function() {
				if (fn != null) {
					fn();
				}
			});

			// execute
			var handler = service.getHandler(name);
			handler.process(cmd_context);

		},

		currentLocation : function(value) {
			return this.attr('current_location', value);
		},

		out : function(value) {
			return this.attr('out', value);
		},

		err : function(value) {
			return this.attr('err', value);
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
			service.setHandler('cd', new CmdCdHandler());
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
			var cur_loc = cmd_context.currentLocation();
			var cur_file = cur_loc.location();
			var cur_uri = cur_file.toFileURI();

			var client = RESTClient.getInstance(context);
			var res = client.getResource();
			res.parts({
				api : 'rest',
				uid : 'null',
				repo : 'null',
				type : 'command',
				id : args[0],
			});
			var request = res.post();

			var req_entity = new RestEntity();
			req_entity.json({
				argument : {
					command : cmd_line,
					pathURI : cur_uri,
				}
			});

			request.entity(req_entity);
			request.execute(function(response) {

				var code = response.code();

				if (response.ok()) {
					var res_entity = response.entity();
					var js = res_entity.toJSON();
					var msg = js.result.message;
					System.out.println(msg);

				} else if (code == 500) {
					var res_entity = response.entity();
					var msg = res_entity.toString();
					System.out.println('Error: ' + msg);

				} else {
					var msg = 'HTTP ' + code + ' ';
					msg += response.message();
					System.out.println('Error: ' + msg);
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
	 * class CmdCdHandler
	 */

	function CmdCdHandler() {
	}

	mc.class(function(cc) {
		cc.type(CmdCdHandler);
		cc.extends(CLIHandler);
	});

	CmdCdHandler.prototype = {

		process : function(cmd_context) {

			var arg = cmd_context.arguments();
			var to = arg[1];

			var cur_loc = cmd_context.currentLocation();
			var file = cur_loc.location();
			if (to == '.') {
				return;
			} else if (to == '..') {
				var parent = file.getParentFile();
				if (parent == null) {
					return;
				}
				file = parent;
			} else {
				file = file.child(to);
			}
			cur_loc.location(file);

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

		currentLocation : function(value) {
			return this.attr('current_location', value);
		},

		err : function(value) {
			return this.attr('err', value);
		},

		out : function(value) {
			return this.attr('out', value);
		},

	};

});

/*******************************************************************************
 * EOF
 */
