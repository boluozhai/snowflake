/*******************************************************************************
 * 
 * viewport.js
 * 
 * @Copyright (c) 2016 boluozhai.com
 * @License MIT License
 * @Author xukun<xukun17@sina.com>
 * 
 */

JS.module(function(mc) {

	mc.package('com.boluozhai.snowflake.web');

	// var System = mc.import('js.lang.System');
	var Attributes = mc.import('js.lang.Attributes');

	// var REST = snowflake.rest.REST;
	// var JSONRestRequest = mc
	// .import("com.boluozhai.snowflake.rest.api.JSONRestRequest");

	var ResourceLoader = snowflake.ResourceLoader;
	var I18nResManager = snowflake.I18nResManager;

	/***************************************************************************
	 * class InnerHolder
	 */

	function InnerHolder() {
	}

	InnerHolder.getInstance = function() {
		var inst = InnerHolder.instance;
		if (inst == null) {
			inst = new InnerHolder();
			inst.load();
			InnerHolder.instance = inst;
		}
		return inst;
	};

	InnerHolder.prototype = {

		attr : function(k, v, def) {

			k = '__attr__' + k;

			if (v == null) {
				v = this[k];
			} else {
				this[k] = v;
			}

			if (v == null) {
				v = def;
			}

			return v;
		},

		load : function() {

			var m = com.boluozhai.snowflake.web.Viewport.model;
			var vpt = m.viewport;

			// TODO

			var my = vpt.operator;
			var owner = vpt.owner;
			var repo = vpt.repository;

			if (my != null) {
				if (my.exists) {
					this.myLocation(my.location);
					this.mySigned(my.login);
					this.mySignTime(my.loginTimestamp);
					this.myUid(my.uid);
				}
				this.myLanguage(my.language);
				this.myNickname(my.nickname);
				this.myExists(my.exists);
			}

			if (owner != null) {
				this.ownerUid(owner.uid);
				this.ownerNickname(owner.nickname);
				this.ownerExists(owner.exists);
			}

			if (repo != null) {
				this.repositoryName(repo.name);
				this.repositoryDescription(repo.description);
				this.repositoryExists(repo.exists);
				this.repositoryURL(repo.url);
				this.repositoryDefault(repo.theDefault);
			}

			// xxxx.xx();

		},

		myLanguage : function(v, def) {
			return this.attr('my_language', v, def);
		},

		myLocation : function(v, def) {
			return this.attr('my_location', v, def);
		},

		myNickname : function(v, def) {
			return this.attr('my_nickname', v, def);
		},

		mySigned : function(v, def) {
			return this.attr('my_signed', v, def);
		},

		mySignTime : function(v, def) {
			return this.attr('my_sign_time', v, def);
		},

		myUid : function(v, def) {
			return this.attr('my_uid', v, def);
		},

		myExists : function(v, def) {
			return this.attr('my_exists', v, def);
		},

		ownerUid : function(v, def) {
			return this.attr('owner_uid', v, def);
		},

		ownerNickname : function(v, def) {
			return this.attr('owner_nickname', v, def);
		},

		ownerExists : function(v, def) {
			return this.attr('owner_exists', v, def);
		},

		repositoryName : function(v, def) {
			return this.attr('repo_name', v, def);
		},

		repositoryDescription : function(v, def) {
			return this.attr('repo_desc', v, def);
		},

		repositoryURL : function(v, def) {
			return this.attr('repo_url', v, def);
		},

		repositoryExists : function(v, def) {
			return this.attr('repo_exists', v, def);
		},

		repositoryDefault : function(v, def) {
			return this.attr('repo_default', v, def);
		},

	};

	// InnerHolder.init();

	/***************************************************************************
	 * class Viewport
	 */

	function Viewport() {

		var inst = InnerHolder.getInstance();
		this._inner = inst;

		var uid = this.ownerUid('null');
		var repo = this.repositoryName('null');
		this._str = (uid + '^' + repo);

	}

	mc.class(function(cc) {
		cc.type(Viewport);
	});

	Viewport.prototype = {

		myLanguage : function(def) {
			return this._inner.myLanguage(null, def);
		},

		myLocation : function(def) {
			return this._inner.myLocation(null, def);
		},

		myNickname : function(def) {
			return this._inner.myNickname(null, def);
		},

		myUid : function(def) {
			return this._inner.myUid(null, def);
		},

		ownerUid : function(def) {
			return this._inner.ownerUid(null, def);
		},

		ownerNickname : function(def) {
			return this._inner.ownerNickname(null, def);
		},

		repositoryName : function(def) {
			return this._inner.repositoryName(null, def);
		},

		signed : function(def) {
			return this._inner.mySigned(null, def);
		},

		toString : function() {
			return this._str;
		},

	};

	/***************************************************************************
	 * inner class WebPageController_inner
	 */

	function WebPageController_inner(context) {
		this._context = context;
	}

	WebPageController_inner.prototype = {

		load : function(fn) {

			var context = this._context;
			var self = this;

			this.load_context_atts(context);
			this.load_i18n_res(function() {
				self.do_i18n_processing(context);
				fn();
			});
		},

		load_i18n_res : function(fn) {

			var context = this._context;
			var loader = new ResourceLoader(context);
			var vpt = new Viewport();

			var lang = vpt.myLanguage();

			var res_man = new I18nResManager(context);
			var res_set = res_man.forLang(lang);
			var url = res_set.getResPath('strings.js');
			lang = res_set.lang();

			loader.loadJS(url, function() {
				var i18n = context.getBean('i18n');
				i18n.lang(lang);
				fn();
			});

		},

		do_i18n_processing : function(context) {

			var doc = $(document);
			var i18n = context.getBean('i18n');
			i18n.translate(context, doc);

		},

		load_context_atts : function(context) {

			var vpt = new Viewport();

			var ses_lang = vpt.myLanguage();
			var ses_uid = vpt.myUid();
			var ses_nickname = vpt.myNickname();
			var vpt_uid = vpt.ownerUid();
			var vpt_nickname = vpt.ownerNickname();
			var vpt_repo_id = vpt.repositoryName();

			if (ses_lang == null) {
				ses_lang = 'default';
			}

			context.attr('session-lang', ses_lang);
			context.attr('session-uid', ses_uid);
			context.attr('session-nickname', ses_nickname);
			context.attr('viewport-uid', vpt_uid);
			context.attr('viewport-nickname', vpt_nickname);
			context.attr('viewport-repo-name', vpt_repo_id);

		},

	};

	/***************************************************************************
	 * class WebPageController
	 */

	function WebPageController(context) {
		this.Attributes();

		if (context == null) {
			context = Snowflake.getContext();
		}

		this._context = context;
		this._inner = new WebPageController_inner(context);
	}

	mc.class(function(cc) {
		cc.type(WebPageController);
		cc.extends(Attributes);
	});

	WebPageController.prototype = {

		init : function() {
			var self = this;
			this._inner.load(function() {
				self.onCreate();
			});
		},

		onCreate : function() {
			throw new Exception('override this method in sub-class');
		},

	};

});

JS.module(function(mc) {

	mc.package('snowflake.html');

	var BASE = com.boluozhai.snowflake.web.WebPageController;

	/***************************************************************************
	 * class HtmlCtrl
	 */

	function HtmlCtrl(context) {
		this.WebPageController(context)
	}

	mc.class(function(cc) {
		cc.type(HtmlCtrl);
		cc.extends(BASE);
	});

	HtmlCtrl.prototype = {};

});

this.snowflake.Viewport = this.com.boluozhai.snowflake.web.Viewport;

/*******************************************************************************
 * EOF
 */
