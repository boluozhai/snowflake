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
			var owner = vpt.xxx;
			var repo = vpt.xxx;

			if (my != null) {
				if (my.exists) {
					this.myNickname(my.nickname);
					this.mySigned(my.login);
					this.mySignTime(my.loginTimestamp);
					this.myUid(my.uid);
				}
			}

			if (owner != null) {
				this.myNickname(owner.xxx);
			}

			if (repo != null) {
				this.myNickname(repo.xxx);
			}

			xxxx.xx();

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

		ownerUid : function(v, def) {
			return this.attr('owner_uid', v, def);
		},

		repo : function(v, def) {
			return this.attr('repo_name', v, def);
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

		myNickname : function(def) {
			return this._inner.myNickname(null, def);
		},

		myUid : function(def) {
			return this._inner.myUid(null, def);
		},

		ownerUid : function(def) {
			return this._inner.ownerUid(null, def);
		},

		repositoryName : function(def) {
			return this._inner.repo(null, def);
		},

		signed : function(def) {
			return this._inner.mySigned(null, def);
		},

		toString : function() {
			return this._str;
		},

	};

	/***************************************************************************
	 * class WebPageController
	 */

	function WebPageController(context) {
		this.Attributes();
		this._context = context;
		this.inner_load_context_atts(context);
		context.i18n($(document));
	}

	mc.class(function(cc) {
		cc.type(WebPageController);
		cc.extends(Attributes);
	});

	WebPageController.prototype = {

		init : function() {
			throw new Exception('override this method in sub-class');
		},

		inner_load_context_atts : function(context) {

			var vpt = new Viewport();

			var ses_uid = vpt.myUid();
			var vpt_uid = vpt.ownerUid();
			var vpt_repo_id = vpt.repositoryName();

			context.attr('session-uid', ses_uid);
			context.attr('viewport-uid', vpt_uid);
			context.attr('viewport-repo-name', vpt_repo_id);

		},

	};

});

/*******************************************************************************
 * EOF
 */
