JS.module(function(mc) {

	mc.package('test');

	var Class = mc.import('js.lang.Class');
	var Object = mc.import('js.lang.Object');
	var System = mc.import('js.lang.System');
	var Context = mc.import('snowflake.context.Context');

	/***************************************************************************
	 * class DebugOutput
	 */

	function DebugOutput(jq) {
		this._query = jq;
		this._sb = "";
	}

	mc.class(function(cc) {
		cc.type(DebugOutput);
	});

	DebugOutput.prototype = {

		println : function(s) {
			if (s == null) {
				s = "";
			}
			var s2 = this._sb + s + '\n';
			this._sb = s2;
			this._query.text(s2);
		},

		clean : function() {
			this._sb = "";
			this._query.text("");
		},

	};

	/***************************************************************************
	 * class Tester
	 */

	function Tester() {
	}

	mc.class(function(cc) {
		cc.type(Tester);
	});

	Tester.prototype = {

		setOutput : function(jq) {
			var out = new DebugOutput(jq);
			System.setOut(out);
			this._output = out;
		},

		resetOutput : function() {
			this._output.clean();
		},

		test : function() {
			this.testContext();
		},

		testCore : function() {

			var clazz = Object.class;
			var cl = clazz.getClassLoader();
			var list = cl.listClassNames();

			System.out.println("list classes@runtime");

			for ( var i in list) {
				var name = list[i];
				System.out.println("  " + name);
			}

			System.out.println("  (count: " + list.length + ")");
			System.out.println("  (now: " + System.currentTimeMillis() + ")");
			System.out.println();

		},

		testContext : function() {

			var context = Context.getInstance();

			var keys = context.getBeanDefinitionNames();
			var cnt = context.getBeanDefinitionCount();

			System.out.println("list beans@context");

			for ( var i in keys) {
				var key = keys[i];
				System.out.println("    " + key);
			}

			System.out.println("  count: " + cnt + " bean(s)");
			System.out.println();

		},

		testRest : function() {

			var callback = function(response) {

				var s = response.entity().toString();
				System.out.println("HttpResponse");
				System.out.println("      url : " + response.url());
				System.out.println("       ok : " + response.ok());
				System.out.println("     code : " + response.code());
				System.out.println("  message : " + response.message());
				System.out.println(s);
				System.out.println();

			};

			var REST = snowflake.rest.REST;
			var context = Context.getInstance();
			var client = REST.getClient(context);

			var app = client.getApplication(null);
			var api = app.getAPI('RestAPI');
			var type = api.getType('RestType');

			// res1

			var res = type.getResource('RestId.json');
			var request = res.get();
			request.execute(callback);

			// res2

			var res2 = api.getType('bad_t').getResource('bad_res');
			request = res2.post();
			request.entity().text('abc');
			request.execute(callback);

		},

	};

});