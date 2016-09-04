/*******************************************************************************
 * 
 * sha1.js
 * 
 * @Copyright (c) 2016 boluozhai.com
 * @License MIT License
 * @Author xukun<xukun17@sina.com>
 * 
 */

JS.module(function(mc) {

	mc.package('snowflake.security.sha1');

	var Object = mc.import('js.lang.Object');
	var Class = mc.import('js.lang.Class');
	var Attributes = mc.import('js.lang.Attributes');

	/***************************************************************************
	 * class SHA1
	 */

	function SHA1() {
	}

	mc.class(function(cc) {
		cc.type(SHA1);
	});

	SHA1.prototype = {

		digestText : function(data) {
			return hex_sha1 ( data ) ;
		},

	};

	SHA1.digest = function(data) {
		var inst = new SHA1();
		return inst.digestText(data);
	};

	/***************************************************************************
	 * implements
	 */

	var hex_sha1=function(b){function k(a){return g(h(f(a),a.length*8))}function d(c){var a='',b;for(var d in c)b=c.charCodeAt(d),a+=(b>>4&15).toString(16)+(b&15).toString(16);return a}function e(e){var c='',d=-1,a,f;while(++d<e.length)a=e.charCodeAt(d),f=d+1<e.length?e.charCodeAt(d+1):0,55296<=a&&a<=56319&&56320<=f&&f<=57343&&(a=65536+((a&1023)<<10)+(f&1023),d++),a<=127?c+=b(a):a<=2047?c+=b(192|a>>6&31,128|a&63):a<=65535?c+=b(224|a>>12&15,128|a>>6&63,128|a&63):a<=2097151&&(c+=b(240|a>>18&7,128|a>>12&63,128|a>>6&63,128|a&63));return c}function f(c){var b=[];for(var a=0;a<c.length*8;a+=8)b[a>>5]|=(c.charCodeAt(a/8)&255)<<24-a%32;return b}function g(d){var c='';for(var a=0;a<d.length*32;a+=8)c+=b(d[a>>5]>>24-a%32&255);return c}function h(m,l){m[l>>5]|=128<<24-l%32,m[(l+64>>9<<4)+15]=l;var d=[],e=1732584193,f=-271733879,g=-1732584194,h=271733878,k=-1009589776;for(var n=0;n<m.length;n+=16){var o=e,p=f,q=g,r=h,s=k;for(var b=0;b<80;b++){b<16?d[b]=m[n+b]:d[b]=c(d[b-3]^d[b-8]^d[b-14]^d[b-16],1);var t=a(a(c(e,5),i(b,f,g,h)),a(a(k,d[b]),j(b)));k=h,h=g,g=c(f,30),f=e,e=t}e=a(e,o),f=a(f,p),g=a(g,q),h=a(h,r),k=a(k,s)}return[e,f,g,h,k]}function i(d,a,b,c){return d<20?a&b|~a&c:d<40?a^b^c:d<60?a&b|a&c|b&c:a^b^c}function j(a){return a<20?1518500249:a<40?1859775393:a<60?-1894007588:-899497514}function a(b,c){var a=(b&65535)+(c&65535),d=(b>>16)+(c>>16)+(a>>16);return d<<16|a&65535}function c(a,b){return a<<b|a>>>32-b}return b=String.fromCharCode,function(a){return d(k(e(a)))}}()
	;
	
});

/*******************************************************************************
 * EOF
 */
