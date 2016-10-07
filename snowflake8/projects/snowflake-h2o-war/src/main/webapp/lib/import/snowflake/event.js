/*******************************************************************************
 * 
 * event.js
 * 
 * @Copyright (c) 2016 boluozhai.com
 * @License MIT License
 * @Author xukun<xukun17@sina.com>
 * 
 */

JS.module(function(mc) {

	mc.package('js.event');

	var RuntimeException = mc.import('js.lang.RuntimeException');
	var Attributes = mc.import('js.lang.Attributes');

	/***************************************************************************
	 * class Event
	 */

	function Event() {
	}

	mc.class(function(cc) {
		cc.type(Event);
		cc.extends(Attributes);
	});

	Event.prototype = {

		code : function(value) {
			return this.attr('code', value);
		},

		message : function(value) {
			return this.attr('message', value);
		},

		source : function(value) {
			return this.attr('source', value);
		},

	};

	/***************************************************************************
	 * class EventHandler
	 */

	function EventHandler() {
	}

	mc.class(function(cc) {
		cc.type(EventHandler);
	});

	EventHandler.prototype = {

		onEvent : function(event) {
		},

	};

	/***************************************************************************
	 * class EventDispatcher
	 */

	function EventDispatcher(source) {
		this._source = source;
	}

	mc.class(function(cc) {
		cc.type(EventDispatcher);
	});

	EventDispatcher.prototype = {

		dispatchEvent : function(event, source) {

			if (source == null) {
				source = this._source;
			}
			event.source(source);

			var list = this.eventHandlers();
			for ( var key in list) {
				var h = list[key];
				if (h == null) {
					continue;
				}
				h.onEvent(event);
			}
		},

		addEventHandler : function(h) {

			// var clazz = h.getClass();
			// var fn = clazz.onEvent;
			// if (fn == null) {
			// var msg = 'not a EventHandler';
			// throw new RuntimeException(msg);
			// }

			var list = this.eventHandlers();
			var id = h.hashCode();
			list[id] = h;
		},

		removeEventHandler : function(h) {
			var list = this.eventHandlers();
			var id = h.hashCode();
			list[id] = null;
		},

		eventHandlers : function() {
			var list = this._handler_list;
			if (list == null) {
				list = {};
				this._handler_list = list;
			}
			return list;
		},

	};

	/***************************************************************************
	 * class FunctionAdapter
	 */

	function FunctionAdapter(fn) {
		this._fn = fn;
	}

	mc.class(function(cc) {
		cc.type(FunctionAdapter);
		cc.extends(EventHandler);
	});

	FunctionAdapter.prototype = {

		onEvent : function(event) {
			this._fn(event);
		},

	};

});

/*******************************************************************************
 * EOF
 */
