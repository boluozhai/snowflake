/*******************************************************************************
 * 
 * format.js
 * 
 * @Copyright (c) 2016 boluozhai.com
 * @License MIT License
 * @Author xukun<xukun17@sina.com>
 * 
 */

JS.module(function(mc) {

	mc.package('h2o.utils');

	var System = mc.import('js.lang.System');
	var Attributes = mc.import('js.lang.Attributes');
	// var Event = mc.import('js.event.Event');
	// var EventDispatcher = mc.import('js.event.EventDispatcher');

	/***************************************************************************
	 * class Formatter
	 */

	function Formatter() {
		this.from('size', new SizeFormatter());
		this.from('time', new TimeFormatter());
	}

	mc.class(function(cc) {
		cc.type(Formatter);
		cc.extends(Attributes);
	});

	Formatter.prototype = {

		from : function(type_name /* type_name=[time|size|...] */, type_fmt) {
			return this.attr(type_name, type_fmt);
		},

	};

	/***************************************************************************
	 * class TimeFormatter
	 */

	function TimeFormatter() {
		this.date = new Date();
	}

	mc.class(function(cc) {
		cc.type(TimeFormatter);
	});

	TimeFormatter.prototype = {

		toString : function(data) {
			var date = this.date;
			date.setTime(data);
			return date.toLocaleString();
		},

	};

	/***************************************************************************
	 * class SizeFormatter
	 */

	function SizeFormatter() {
		this.array = [ 'B', 'K', 'M', 'G', 'T' ];
	}

	mc.class(function(cc) {
		cc.type(SizeFormatter);
	});

	SizeFormatter.prototype = {

		toString : function(data) {
			var array = this.array;
			var len = array.length;
			var unit = '';
			var value = '';
			var n = data;
			for (var i = 0; i < len; i++) {
				value = n;
				unit = array[i];
				n /= 1024;
				if (n < 1) {
					break;
				} else {
					continue;
				}
			}
			var x = 10;
			value *= x;
			value = Math.floor(value);
			value /= x;
			// return (value + unit + '//' + data);
			return (value + unit);
		},

	};

});

this.snowflake.Formatter = this.h2o.utils.Formatter;

/*******************************************************************************
 * EOF
 */
