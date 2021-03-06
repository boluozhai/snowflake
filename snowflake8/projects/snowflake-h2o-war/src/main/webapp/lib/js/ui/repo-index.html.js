/*******************************************************************************
 * 
 * user-home.html.js
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

	var Viewport = mc.use(snowflake.Viewport);
	var HtmlCtrl = mc.use(snowflake.html.HtmlCtrl);

	var CurrentLocation = mc
			.import('com.boluozhai.snowflake.vfs.CurrentLocation');

	var PathBarCtrl = mc.import('com.boluozhai.h2o.widget.folder.PathBarCtrl');

	var FileListCtrl = mc
			.import('com.boluozhai.h2o.widget.folder.FileListCtrl');

	var VFS = mc.use(snowflake.vfs.VFS);

	var FilePropertiesDialog = mc.use(snowflake.FilePropertiesDialog);
	var FileUploadDialog = mc.use(snowflake.FileUploadDialog);
	var AlertDialog = mc.use(snowflake.AlertDialog);

	/***************************************************************************
	 * class RepoIndexHtml
	 */

	function RepoIndexHtml(context) {
		this.HtmlCtrl(context);
	}

	mc.class(function(cc) {
		cc.type(RepoIndexHtml);
		cc.extends(HtmlCtrl);
	});

	RepoIndexHtml.prototype = {

		onCreate : function() {

			var self = this;
			var context = this._context;
			var cl = new CurrentLocation(context);
			this._cur_location = cl;

			this.setupHeadCtrl(cl);
			this.setupFootCtrl(cl);
			this.setupPathBar(cl);
			this.setupFileList(cl);

			this.setupCurrentLocationListener(cl);

			this.setupDirectoryFunctionPanel(cl);
			this.setupFilePropertiesDialog(cl);
			this.setupFileUploadDialog(cl);
			this.setupDirectoryDeleteButton(cl);
			this.setupDirectoryCreateButton(cl);
			this.setupWormholeButton(cl);
			this.setupVFS(cl);

		},

		setupDirectoryFunctionPanel : function(cl) {
			// TODO
		},

		setupWormholeButton : function(cl) {
			var self = this;
			$('.btn-wormhole').click(function() {
				self.onClickWormhole();
			});
		},

		setupDirectoryCreateButton : function(cl) {
			var self = this;
			$('.btn-dir-insert').click(function() {
				self.onClickMakeDir();
			});
		},

		setupDirectoryDeleteButton : function(cl) {
			var self = this;
			$('.btn-dir-delete').click(function() {
				self.doDeleteDir();
			});
		},

		setupFilePropertiesDialog : function(cl) {
			var dlg = new FilePropertiesDialog(this._context);
			dlg.setCurrentLocation(cl);
			dlg.init();
		},

		setupFileUploadDialog : function(cl) {
			var dlg = new FileUploadDialog(this._context);
			dlg.setCurrentLocation(cl);
			dlg.init();

			$('.btn-file-upload').click(function() {
				dlg.show();
			});

		},

		setupHeadCtrl : function(cl) {
			var context = this._context;
			var head_ctrl = new HeadCtrl(context);
			this._head_ctrl = head_ctrl;
			head_ctrl.binder().parent('#page-head');
			head_ctrl.init();
		},

		setupFootCtrl : function(cl) {
			var context = this._context;
			var foot_ctrl = new FootCtrl(context);
			this._foot_ctrl = foot_ctrl;
			foot_ctrl.binder().parent('#page-foot');
			foot_ctrl.init();
		},

		setupPathBar : function(location) {
			var context = this._context;
			var path_bar_ctrl = new PathBarCtrl(context);
			this._path_bar_ctrl = path_bar_ctrl;
			path_bar_ctrl.currentLocation(location);

			var path_bar_head = $('.path-bar-head');
			this.setupPathBarHead(path_bar_head);

			path_bar_ctrl.binder().parent('#path-bar');
			path_bar_ctrl.binder().headOuter(path_bar_head);
			// path_bar_ctrl.binder().onCreateHead(function(item) {
			// self.setupMagicButton(item);
			// });

			path_bar_ctrl.init();

		},

		setupFileList : function(location) {
			var context = this._context;
			var filelist_ctrl = new FileListCtrl(context);
			this._filelist_ctrl = filelist_ctrl;
			filelist_ctrl.currentLocation(location);
			filelist_ctrl.binder().parent('#file-list');

			filelist_ctrl.init();

		},

		setupVFS : function() {

			// vfs

			var self = this;
			var context = this._context;
			var vfs = VFS.newInstance(context);

			// vfs_factory.httpURI(this.genWorkingBaseHttpURI());

			this._vfs = vfs;
			vfs.root().load(function() {
				self.fireOnClickRootBtn();
			});

		},

		genWorkingBaseHttpURI : function() {

			var temp = '~/{user}/{repo}/rest/working/';
			var info = new Viewport();
			var user = info.ownerUid();
			var repo = info.repositoryName();

			temp = temp.replace('{user}', user);
			temp = temp.replace('{repo}', repo);

			return temp;
		},

		setupPathBarHead : function(query) {
			this.setupRootButton(query);
			this.setupMagicButton(query);
		},

		setupRootButton : function(q) {

			var context = this._context;

			var info = new Viewport();
			var user = info.ownerUid();
			var repo = info.repositoryName();

			var self = this;
			var btn = q.find('.h2o-root-button');
			// btn.text(user + '^' + repo);
			btn.click(function() {
				self.fireOnClickRootBtn();
			});

		},

		setupMagicButton : function(q) {

			var speed = 200;
			var btn = q.find('.btn-h2o-magic');
			var mark = q.find('.mark-h2o-plus');

			btn.click(function() {
				var magic = $('.magic');
				if (!is_head_visible) {
					mark.text('-');
					magic.show(speed);
					is_head_visible = true;
				} else {
					mark.text('+');
					magic.hide(speed);
					is_head_visible = false;
				}
			});

			$('.magic').hide();
			is_head_visible = false;

		},

		fireOnClickRootBtn : function() {

			var vfs = this._vfs;
			var cl = this._cur_location;
			var root = vfs.root();
			cl.location(root);

		},

		doDeleteDir : function() {

			// var i18n = this._context.getBean('i18n');
			// alert(i18n.getString('work-in-progress'));

			var cl = this._cur_location;
			var file = cl.location();
			file.del(function() {
				alert('delete ' + file);
			});

		},

		onClickWormhole : function() {

			var i18n = this._context.getBean('i18n');
			var txt = i18n.getString('work-in-progress');
			alert(txt);

		},

		onClickMakeDir : function() {

			var self = this;
			var i18n = this._context.getBean('i18n');

			var dlg = new AlertDialog();
			dlg.title(i18n.getString('make_dir'));
			dlg.message(i18n.getString('input_dir_name'));
			dlg.value('');
			dlg.buttons({
				'ok' : 'btn-primary',
				'cancel' : 'btn-default',
			});
			dlg.show(function() {
				var value = dlg.value();
				var result = dlg.result();
				if (result == 'ok') {
					self.doMakeDir(value);
				}
			});

		},

		doMakeDir : function(name) {

			var self = this;
			var name_ok = false;
			if (name != null) {
				name = name.trim();
			}
			if (name.length == 0) {
			} else if (name.indexOf('/') >= 0) {
			} else if (name.indexOf('\\') >= 0) {
			} else {
				name_ok = true;
			}

			var i18n = this._context.getBean('i18n');

			if (!name_ok) {
				var msg = i18n.getString('bad_dir_name');
				alert(msg + ':' + name);
				return;
			}

			var cl = this._cur_location;
			var file = cl.location();
			var dir = file.child(name);
			dir.mkdir(function() {
				self.refresh();
			});

		},

		refresh : function() {
			var cl = this._cur_location;
			var file = cl.location();
			cl.location(file);
		},

		setupCurrentLocationListener : function(cl) {
			var self = this;
			var handler = new js.lang.Object();
			handler.onEvent = function(event) {
				var code = event.code();
				if (code == 'ON_SELECT') {
					self.onClickModifyItem();
				}
			};
			cl.addEventHandler(handler);
		},

		onClickModifyItem : function() {

			var self = this;
			var context = this._context;
			var i18n = context.getBean('i18n');
			var cl = this._cur_location;
			var file = cl.selection();

			// alert('select ' + sel);
			// alert

			var dlg = new AlertDialog();
			dlg.title(i18n.getString('modify'));
			dlg.message(file.getName());
			dlg.buttons({
				'delete' : 'btn-danger  float-left ',
				'rename' : 'btn-primary',
				'cancel' : 'btn-default',
			});

			dlg.show(function() {

				var result = dlg.result();
				if (result == null) {
				} else if (result == 'delete') {
					self.doDelete(file);
				} else if (result == 'rename') {
					self.showRenameDialog(file);
				}

			});

		},

		doDelete : function(file) {
			var self = this;
			file.del(function() {
				// alert('delete ok.');
				self.refresh();
			});
		},

		doRename : function(file, new_name) {
			var self = this;
			var parent = file.getParentFile();
			var ch = parent.child(new_name);
			file.renameTo(ch, function() {
				// var str = file + ' >>> ' + ch;
				// alert(str);
				self.refresh();
			});
		},

		showRenameDialog : function(file) {

			var self = this;
			var context = this._context;
			var i18n = context.getBean('i18n');

			var old = file.getName();
			var nl = '\n';
			var msg = i18n.getString('rename');
			msg += (' ' + old + '' + nl);
			// msg += i18n.getString('input_new_name');

			var dlg = new AlertDialog();
			dlg.title(i18n.getString('rename'));
			dlg.message(msg);
			dlg.value(old);
			dlg.buttons({
				'ok' : 'btn-primary',
				'cancel' : 'btn-default',
			});
			dlg.show(function() {
				var result = dlg.result();
				var new_name = dlg.value();
				if (result == 'ok') {
					self.doRename(file, new_name);
				}
			});

		},

	};

	/***************************************************************************
	 * main
	 */

	$(document).ready(function() {

		var context = Snowflake.getContext();
		var ctrl = new RepoIndexHtml(context);
		ctrl.init();

	});

});

/*******************************************************************************
 * EOF
 */
