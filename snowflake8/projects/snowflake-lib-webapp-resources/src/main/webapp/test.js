JS.module(function(mc) {

	mc.package('test');

	var Class = mc.import('js.lang.Class');
	var Object = mc.import('js.lang.Object');
	var System = mc.import('js.lang.System');
	var Context = mc.import('snowflake.context.Context');

	/***************************************************************************
	 * class DebugOutput
	 */

	function DebugOutput() {
	}

	mc.class(function(cc) {
		cc.type(DebugOutput);
	});

	DebugOutput.prototype = {

		println : function(s) {
			alert(s);
		},

	};

	System.setOut(new DebugOutput());

	/***************************************************************************
	 * class Tester
	 */

	function Tester() {
	}

	mc.class(function(cc) {
		cc.type(Tester);
	});

	Tester.prototype = {

		test : function() {

			var obj = new Object();
			var clz = obj.getClass();

			var i = 0;
			var res = [];
			res[i++] = clz.equals();
			res[i++] = clz.getClass();
			res[i++] = clz.getName();
			res[i++] = clz.hashCode();
			res[i++] = clz.newInstance();
			res[i++] = clz.toString();

			i += 5;

			res[i++] = Class.forName('js.lang.Object');

			System.out.println("ok");

			var cl = clz.getClassLoader();
			var list = cl.listClassNames();

			var sb = "";
			for ( var i in list) {
				sb += (list[i] + '\n');
			}
			System.out.println(sb);

			// alert(obj.toString());
			// alert(clz.toString());

			var REST = snowflake.rest.REST;
			var context = Context.getInstance();

			var rest_client = REST.getClient(context);

		},

	};

});