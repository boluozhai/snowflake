/*******************************************************************************
 * 
 * app-language.html.js
 * 
 * @Copyright (c) 2016 boluozhai.com
 * @License MIT License
 * @Author xukun<xukun17@sina.com>
 * 
 */

JS.module(function(mc) {

	mc.package('com.boluozhai.h2o.html');

	var System = mc.import('js.lang.System');
	var ListBuilder = mc.import('snowflake.view.list.ListBuilder');
	var ListModel = mc.import('snowflake.view.list.ListModel');
	var Attributes = mc.import('js.lang.Attributes');
	var RESTClient = mc.import('snowflake.rest.RESTClient');

	var widget_x = 'com.boluozhai.h2o.widget';
	var HeadCtrl = mc.import(widget_x + '.head.HeadCtrl');

	var Viewport = mc.import('com.boluozhai.snowflake.web.Viewport');
	var HtmlCtrl = mc.import('snowflake.html.HtmlCtrl');

	/***************************************************************************
	 * class LanguageHtml
	 */

	function LanguageHtml(context) {
		this.HtmlCtrl(context);
	}

	mc.class(function(cc) {
		cc.type(LanguageHtml);
		cc.extends(HtmlCtrl);
	});

	var is_head_visible = false;

	LanguageHtml.prototype = {

		onCreate : function() {

			var context = this._context;
			var self = this;

			var head_ctrl = new HeadCtrl(context);
			this._head_ctrl = head_ctrl;
			head_ctrl.binder().parent('#page-head');
			head_ctrl.init();

			// this.setupViewportInfo();
			// this.setupRepositoryList();

			var input = $('.input-lang-name');
			input.val('zh_CN');
			$('.btn-setup').click(do_setup);

			// output i18n mapping
			var i18n = context.getBean('i18n');
			var js = i18n.getStringTable();
			js = this.sort_map(js);
			var str = JSON.stringify(js, null, 4);
			$('.i18n-out').text('i18n = ' + str);

		},

		sort_map : function(map) {
			var keys = [];
			for ( var key in map) {
				keys.push(key);
			}
			keys = keys.sort();
			var m2 = {};
			for ( var i in keys) {
				var key = keys[i];
				var value = map[key];
				m2[key] = value;
			}
			return m2;
		},

	};

	/***************************************************************************
	 * main
	 */

	$(document).ready(function() {

		var context = Snowflake.getContext();
		var ctrl = new LanguageHtml(context);
		ctrl.init();

	});

	function do_setup() {

		var input = $('.input-lang-name');
		var lang = input.val();
		send(lang);

	}

	function send(lang) {
		var url = './i18n.js?service=active-www&language=' + lang;
		var xhr = new XMLHttpRequest();
		xhr.open('POST', url, true);
		xhr.onreadystatechange = function() {

			var state = xhr.readyState;
			if (state != 4) {
				return;
			}

			var status = xhr.status;
			if (status == 200) {
				alert('setup language as : ' + lang);
			} else {
				alert('Error: HTTP ' + status);
			}

		};
		xhr.send();
	}

});

/*******************************************************************************
 * EOF
 */
