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
		this.from('type', new TypeFormatter());
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
	 * class TypeFormatter
	 */

	function TypeFormatter() {
	}

	mc.class(function(cc) {
		cc.type(TypeFormatter);
	});

	TypeFormatter.prototype = {

		getDisplayNames : function() {
			var tab = this._table;
			if (tab == null) {

				tab = {};
				var context = Snowflake.getContext();
				var i18n = context.getBean('i18n');

				tab.application = i18n.getString('file');
				tab.directory = i18n.getString('directory');
				tab.image = i18n.getString('image');
				tab.text = i18n.getString('text');
				tab.audio = i18n.getString('audio');
				tab.video = i18n.getString('video');
				tab.file = i18n.getString('file');

				this._table = tab;
			}
			return tab;
		},

		toString : function(data) {

			var displayNames = this.getDisplayNames();
			var prefix = null;
			var index = data.indexOf('/');
			if (index < 0) {
				prefix = data;
			} else {
				prefix = data.substring(0, index);
			}

			if (prefix == null) {
				return displayNames.file;

			} else if (prefix == 'application') {
				return displayNames.application;

			} else if (prefix == 'image') {
				return displayNames.image;

			} else if (prefix == 'text') {
				return displayNames.text;

			} else if (prefix == 'audio') {
				return displayNames.audio;

			} else if (prefix == 'video') {
				return displayNames.video;

			} else if (prefix == 'directory') {
				return displayNames.directory;

			} else {
				return displayNames.file;
			}

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
