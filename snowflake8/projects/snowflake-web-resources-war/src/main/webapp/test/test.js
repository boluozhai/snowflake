JS
		.module(function(mc) {

			mc.package('test');

			var Class = mc.import('js.lang.Class');
			var Object = mc.import('js.lang.Object');
			var System = mc.import('js.lang.System');
			var Context = mc.import('snowflake.context.Context');

			/*******************************************************************
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

			/*******************************************************************
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
					System.out.println("  (now: " + System.currentTimeMillis()
							+ ")");
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
					client.pathPattern('~/api/type/id/*');
					// client. pathPattern ( '' ) ;

					var res = client.getResource();
					res.part('api', 'RestAPI');
					res.part('type', 'RestType');
					res.part('id', 'RestId.json');

					// res1

					var request = res.get();
					request.parameter('service', 'a-user-define-service');
					request.execute(callback);

					// res2

					client.pathPattern('~/api/type/+/+/id/*/++');

					var res2 = client.getResource();
					res2.parts({
						'api' : 'RestAPI',
						'type' : 'bad_t/xxx/3',
						'id' : 'bad_id/xxx/yyy/zzz',
					});
					request = res2.post();
					request.entity().text('abc');
					request.execute(callback);

					// parse path
					var path2parse = '~/[a-api]/[a-type]/t0/t1/[a-id]/id1/id2/id3/id4/';
					var map = client.parsePath(path2parse);
					map.method = 'RESTClient.parsePath()';
					map.path2parse = path2parse;
					map.pattern = client.pathPattern();
					var str = JSON.stringify(map, null, 4);
					System.out.println(str);

				},

				testSha1 : function() {

					var tcase = {
						case1 : {
							text : 'Hello, World',
							hash : '907d14fb3af2b0d4f18c2d46abe8aedce17367bd'
						},
						case2 : {
							text : '世界，你好',
							hash : '0d5ee633034da601802188ebb03c805bd3993075-'
						},
						case3 : {
							text : 'http://hello.com/world#abc',
							hash : '757b081e68d36610216c2cee71fbf0932096531e'
						},
					};

					var SHA1 = snowflake.security.sha1.SHA1;

					for ( var key in tcase) {
						var tc = tcase[key];
						var text = tc.text;
						var hash = tc.hash;

						var hash_2 = SHA1.digest(text);
						var error = (hash != hash_2);

						System.out.println('TestSha1');
						System.out.println('  plain_text:[' + text + ']');
						System.out.println('   want_hash:' + hash);
						System.out.println('   calc_hash:' + hash_2);
						System.out.println();

						if (error) {
							var msg = 'the hash value not match!';
							System.out.println(msg);
							throw new RuntimeException(msg);
						}

					}

				},

				testBase64 : function() {

					var tcase = {
						case1 : {
							text : 'Hello, World',
						},
						case2 : {
							text : '世界，你好',
						},
						case3 : {
							text : 'http://hello.com/world#abc',
						},
					};

					var b64 = snowflake.encoding.base64.Base64;

					for ( var key in tcase) {
						var tc = tcase[key];

						var text1 = tc.text;
						var txt64 = b64.encode(text1);
						var text2 = b64.decode(txt64);

						var error = (text1 != text2);

						System.out.println('TestBase64');
						System.out.println('  plain_text_1:[' + text1 + ']');
						System.out.println('  plain_text_2:[' + text2 + ']');
						System.out.println('   base64_text:[' + txt64 + ']');
						System.out.println();

						if (error) {
							var msg = 'the hash value not match!';
							System.out.println(msg);
							throw new RuntimeException(msg);
						}

					}

				},

				testI18n : function() {

					var context = Context.getInstance();
					context.i18n($('body'));

					System.out.println('translate done.');

				},

				testNormalizeURL : function() {

					var context = Context.getInstance();
					var piw = context.pathInWebapp();
					System.out.println('Test WebContext.normalizeURL()');
					System.out.println('    PIW: ' + piw);
					System.out.println();

					var array = [ '~/file', '~/abc/file', '~/abc/defg/file',
							'a/b/c/d/e/file' ];
					for ( var i in array) {
						var path1 = array[i];
						var path2 = context.normalizeURL(path1);
						var path3 = context.getContextPath();

						System.out.println('    p1 = ' + path1);
						System.out.println('    p2 = ' + path2);
						System.out.println('    contextPath = ' + path3);
						System.out.println();
					}

					System.out.println();

				},

			};

		});