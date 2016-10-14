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
	var FootCtrl = mc.import(widget_x + '.foot.FootCtrl');

	var Viewport = mc.import('com.boluozhai.snowflake.web.Viewport');
	var HtmlCtrl = mc.import('snowflake.html.HtmlCtrl');

	var I18nResManager = mc.use(snowflake.I18nResManager);
	var I18n = mc.use(snowflake.I18n);
	var JSONRestRequest = mc.use(snowflake.JSONRestRequest);

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

			var foot_ctrl = new FootCtrl(context);
			this._foot_ctrl = foot_ctrl;
			foot_ctrl.binder().parent('#page-foot');
			foot_ctrl.init();

			// this.setupViewportInfo();
			// this.setupRepositoryList();

			this.setupLangListView($('.lang-list'), $('.lang-item'));
			this.setupCurLangInfo(context);

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

		setupCurLangInfo : function(context) {
			var lang = context.attr('session-lang');
			var i18n = context.getBean('i18n');
			var name = i18n.getString(lang);
			if (name == null) {
				name = lang;
			}
			$('.cur-lang-name').text(name);
		},

		setupLangListView : function(qParent, qItem) {

			var model = this.resSetList();
			var qDefault = null;
			var i18n = new I18n();

			qParent.empty();

			for ( var i in model) {

				// model
				var res_set = model[i];

				var lang = res_set.lang();
				var img_url = res_set.getResPath('flag.svg');
				var label = i18n.getString(lang);

				if (label == null) {
					label = '$' + lang;
				}

				// view
				var it = qItem.clone();

				var img = it.find('.lang-item-image');
				var txt = it.find('.lang-item-text');
				var btn = it.find('.btn');

				img.attr('src', img_url);
				txt.text(label);

				if (lang == 'default') {
					qDefault = it;
				} else {
					qParent.append(it);
				}

				this.setupLangButtonOnClick(btn, lang);

			}

			if (qDefault != null) {
				qParent.append(qDefault);
			}

		},

		setupLangButtonOnClick : function(btn, lang) {
			var self = this;
			btn.click(function() {
				self.onClickLangBtn(lang);
			});
		},

		onClickLangBtn : function(lang) {

			var self = this;
			var context = this._context;
			var jrr = new JSONRestRequest(context);
			var tx = jrr.open('PUT', {
				uid : 'u',
				repo : 'r',
				api : 'system-api',
				type : 'session',
				id : 'x',
			});

			tx.f_session().f_language(lang);

			jrr.onResult(function() {

				alert('set lang as : ' + lang);
				self.refresh();

			});
			jrr.send(tx);

		},

		refresh : function() {
			var url = window.location.href;
			window.location = url;
		},

		resSetList : function() {
			var context = this._context;
			var res_man = new I18nResManager(context);
			var list = this.langList();
			var li2 = [];
			for ( var i in list) {
				var lang = list[i];
				if (lang == null) {
					continue;
				}
				var res_set = res_man.forLang(lang);
				li2.push(res_set);
			}
			return li2;
		},

		langList : function() {
			var list = [ 'default', 'de_DE', 'en_GB', 'en_US', 'es_ES',
					'fr_FR', 'it_IT', 'ja_JP', 'ko_KR', 'ru_RU', 'zh_CN', ];
			return list.sort();
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
