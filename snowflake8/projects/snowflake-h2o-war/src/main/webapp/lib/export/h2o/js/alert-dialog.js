/*******************************************************************************
 * 
 * file-upload-dialog.js
 * 
 * @Copyright (c) 2016 boluozhai.com
 * @License MIT License
 * @Author xukun<xukun17@sina.com>
 * 
 */

JS.module(function(mc) {

	mc.package('com.boluozhai.h2o.widget');

	var System = mc.import('js.lang.System');
	var Attributes = mc.import('js.lang.Attributes');
	var ResourceLoader = mc.import('com.boluozhai.h2o.widget.ResourceLoader');

	/***************************************************************************
	 * class AlertDialogCore
	 */

	function AlertDialogCore(facade) {

		var service = AlertDialogService.getService();

		this._facade = facade;
		this._view = service.getView();
		this._service = service;

	}

	AlertDialogCore.prototype = {

		show : function(fn) {

			var self = this;

			if (fn == null) {
				fn = function() {
				};
			}

			var model = this._facade;
			var view = this._view;

			self.open(model, view, function() {
				self.close(model, view);
				fn();
			});

			view.modal({
				keyboard : false,
			});

		},

		open : function(model, view, fn) {

			var title = model.title();
			var message = model.message();
			var value = model.value();
			var buttons = model.buttons();
			var icon = model.icon;

			var alert_title = view.find('#alert-title');
			var alert_icon = view.find('#alert-icon');
			var alert_message = view.find('#alert-message');
			var alert_value = view.find('#alert-value');
			var alert_buttons = view.find('#alert-buttons');

			if (title == null) {
				title = 'H2O';
			} else {
				title = this.translate(title);
			}
			alert_title.text(title);

			if (icon == null) {
				alert_icon.hide();
			} else {
				alert_icon.show();
			}

			if (message == null) {
				message = '  ';
			} else {
				message = this.translate(message);
			}
			alert_message.text(message);

			if (value == null) {
				alert_value.hide();
			} else {
				alert_value.show();
				alert_value.val(value);
			}

			alert_buttons.empty();
			if (buttons != null) {
				for ( var k in buttons) {
					var v = buttons[k];
					this.setup_button(k, v, alert_buttons, fn);
				}
			}

		},

		setup_button : function(i18n, clazz, parent, fn) {
			var self = this;
			var btn = this._service.make_button(i18n, clazz);
			parent.append(btn);
			btn.click(function() {
				self.onClickButton(i18n);
				fn();
			});
		},

		onClickButton : function(btn_id) {
			this._facade.result(btn_id);
		},

		close : function() {
			var outer = this._facade;
			var input = this._view.find('#alert-value');
			outer.value(input.val());
		},

		translate : function(s) {
			return s;
		},

	};

	/***************************************************************************
	 * class AlertDialogService
	 */

	function AlertDialogService(context) {
		this._context = context;
	}

	AlertDialogService.prototype = {

		start : function() {

			if (this._has_init) {
				return;
			} else {
				this._has_init = true;
			}

			var self = this;
			var url = '~/lib/export/h2o/html/AlertDialog.html';
			var loader = new ResourceLoader(this._context);
			loader.loadHTML(url, function(query) {
				var child = query.find('.alert-dialog');
				var parent = $('body');
				parent.append(child);
				self.onLoad(child);
			});

		},

		onLoad : function(q) {
			this._view = q;
			this._btn_template = q.find('.btn-template');
			window.alert = AlertDialogService.alert;
		},

		getView : function() {
			return this._view;
		},

		make_button : function(i18n_id, clazz) {
			var i18n = this._context.getBean('i18n');
			var txt = i18n.getString(i18n_id);
			var btn = this._btn_template.clone();
			btn.text(txt);
			btn.addClass(clazz);
			return btn;
		},

	};

	AlertDialogService.getService = function(context) {
		var service = AlertDialogService.instance;
		if (service == null) {
			service = new AlertDialogService(context);
			AlertDialogService.instance = service;
		}
		return service;
	};

	AlertDialogService.startup = function(context) {
		var service = AlertDialogService.getService(context);
		service.start();
	};

	AlertDialogService.alert = function(str) {
		var dlg = new AlertDialog();
		dlg.title('H2O');
		dlg.message(str);
		dlg.value(null);
		dlg.buttons({
			'ok' : 'btn-primary',
		});
		dlg.show();
	};

	/***************************************************************************
	 * class AlertDialog (facade class)
	 */

	function AlertDialog() {
		this.core = new AlertDialogCore(this);
	}

	mc.class(function(cc) {
		cc.type(AlertDialog);
		cc.extends(Attributes);
	});

	AlertDialog.prototype = {

		title : function(v) {
			return this.attr('title', v);
		},

		message : function(v) {
			return this.attr('message', v);
		},

		value : function(v) {
			return this.attr('value', v);
		},

		result : function(v) {
			return this.attr('result', v);
		},

		buttons : function(map/* <i18n , class > */) {
			return this.attr('buttons', map);
		},

		show : function(fn) {
			this.core.show(fn);
		},

	};

	/***************************************************************************
	 * main
	 */

	$(document).ready(function() {
		var context = Snowflake.getContext();
		AlertDialogService.startup(context);
	});

});

this.snowflake.AlertDialog = this.com.boluozhai.h2o.widget.AlertDialog;

/*******************************************************************************
 * EOF
 */
