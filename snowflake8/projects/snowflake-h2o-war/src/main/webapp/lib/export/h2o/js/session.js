/*******************************************************************************
 * 
 * Session.js
 * 
 * @Copyright (c) 2016 boluozhai.com
 * @License MIT License
 * @Author xukun<xukun17@sina.com>
 * 
 */

JS.module(function(mc) {

	mc.package('com.boluozhai.h2o.web');

	var System = mc.import('js.lang.System');
	var Attributes = mc.import('js.lang.Attributes');
	var RESTClient = mc.import('snowflake.rest.RESTClient');
	// var SHA1 = mc.import('snowflake.security.sha1.SHA1');

	var widget_x = 'com.boluozhai.h2o.widget';

	// var FileListCtrl = mc.import(widget_x + '.folder.FileListCtrl');
	// var PathBarCtrl = mc.import(widget_x + '.folder.PathBarCtrl');
	// var DirDataCtrl = mc.import(widget_x + '.folder.DirDataCtrl');
	// var ConsoleCtrl = mc.import(widget_x + '.console.ConsoleCtrl');

	/***************************************************************************
	 * class SessionInfo
	 */

	function SessionInfo(context) {

		var ses = SessionInfo.data.session;
		if (ses == null) {
			ses = {};
		}

		this._context = context;
		this.session = ses;
	}

	mc.class(function(cc) {
		cc.type(SessionInfo);
		cc.extends(Attributes);
	});

	SessionInfo.prototype = {

		signed : function() {
			return this.session.login;
		},

		nickname : function() {
			return this.session.nickname;
		},

		uid : function() {
			return this.session.uid;
		},

		avatarURL : function() {
			return this.session.avatar;
		},

		startupTime : function() {
			return this.session.loginTimestamp;
		},

	};

});

/*******************************************************************************
 * EOF
 */
