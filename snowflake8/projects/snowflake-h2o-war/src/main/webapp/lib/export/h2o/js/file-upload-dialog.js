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

	mc.package('com.boluozhai.h2o.widget.file_upload');

	var System = mc.import('js.lang.System');
	var Attributes = mc.import('js.lang.Attributes');
	var ResourceLoader = mc.import('com.boluozhai.h2o.widget.ResourceLoader');
	var ModalDialog = mc.use(snowflake.ModalDialog);
	var CurrentLocationEvent = mc.use(snowflake.CurrentLocationEvent);
	var Viewport = mc.use(snowflake.Viewport);

	/***************************************************************************
	 * class FileUploadDialog
	 */

	function FileUploadDialog(context) {

		this.ModalDialog(context);
		this.impl = new FileUploadDialogImpl(context, this);

		this.htmlURL('~/lib/export/h2o/html/FileUploadDialog.html');
		this.dialogSelector('#the-dialog');

	}

	mc.class(function(cc) {
		cc.type(FileUploadDialog);
		cc.extends(ModalDialog);
	});

	FileUploadDialog.prototype = {

		onCreate : function() {
			var q = this.dialogQuery();
			this.impl.setupForm(q);
		},

		setCurrentLocation : function(cl) {
			this._cur_location = cl;
			// cl.addEventHandler(this);
		},

	};

	/***************************************************************************
	 * class FileUploadDialogImpl
	 */

	function FileUploadDialogImpl(context, outer) {
		this._context = context;
		this._outer = outer;
	}

	FileUploadDialogImpl.prototype = {

		onload : function(dlg, file) {

			var q = dlg.dialogQuery();

			var name = file.getName();
			var size = file.length();
			var type = 'todo...'; // file.ty(); TODO
			var time = file.lastModified();
			var path = file.getPath();

			q.find('.file-name').text(name);
			q.find('.file-size').text(size);
			q.find('.file-type').text(type);
			q.find('.file-time').text(time);
			q.find('.file-path').text(path);

		},

		setupForm : function(q) {

			var self = this;
			var input_file = q.find('#input-file');
			var input_up = q.find('#input-upload');

			input_file.change(function() {
				self.onFileSelected(q);
			});

			input_up.click(function() {
				self.onClickUpload(q);
			});

		},

		onFileSelected : function(q) {

			var pre = q.find('.output');
			var input = q.find('#input-file')[0];
			var files = input.files;

			pre.empty();

			if (files) {
				var len = files.length;
				for (var i = 0; i < len; i++) {
					var file = files[i];
					var sb = '';
					sb += (' name:' + file.name);
					sb += (' size:' + file.size);
					sb += (' type:' + file.type);
					sb += (' time:' + file.lastModified);
					pre.append(sb + '\n');
				}
			}

		},

		get_current_path : function() {
			// TODO
			var cl = this._outer._cur_location;
			var file = cl.location();
			var path = file.getPath();
			return path;
		},

		onClickUpload : function(q) {

			// load param
			var vpt = new Viewport();
			var uid = vpt.ownerUid();
			var repo = vpt.repositoryName();
			var path = this.get_current_path();

			// make URL
			var context = this._context;
			var url = '';
			url += '~/file-upload.do';
			// url += '~/{user}/{repo}/repo-api/upload/file';
			url += '?service=plain-file-upload';
			url += '&uid={user}&repository={repo}&path={path}';
			url = url.replace('{user}', uid);
			url = url.replace('{repo}', repo);
			url = url.replace('{path}', path);
			url = context.normalizeURL(url);

			var xhr = new XMLHttpRequest();
			var form1 = q.find('#form1')[0];
			var fd = new FormData(form1);

			/* event listners */

			var uploadProgress = function(e) {
				var str = '';
				if (e.lengthComputable) {
					var cb = e.loaded;
					var total = e.total;
					str = cb + '/' + total;
				} else {
					str = 'err';
				}
				q.find('.progress').text(str);
			};
			var uploadComplete = function(e) {
				alert('completed');
			};
			var uploadFailed = function(e) {
				alert('failed');
			};
			var uploadCanceled = function(e) {
				alert('canceled');
			};

			xhr.upload.addEventListener("progress", uploadProgress, false);
			xhr.addEventListener("load", uploadComplete, false);
			xhr.addEventListener("error", uploadFailed, false);
			xhr.addEventListener("abort", uploadCanceled, false);
			/*
			 * Be sure to change the url below to the url of your upload server
			 * side script
			 */
			xhr.open("POST", url);
			xhr.send(fd);

		},

	};

});

this.snowflake.FileUploadDialog = this.com.boluozhai.h2o.widget.file_upload.FileUploadDialog;

/*******************************************************************************
 * EOF
 */
