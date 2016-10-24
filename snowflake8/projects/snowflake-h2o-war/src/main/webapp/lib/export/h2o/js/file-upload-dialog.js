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
			var btn_start = q.find('#btn-upload-start');
			var btn_cancel = q.find('#btn-upload-cancel');

			input_file.change(function() {
				self.onFileSelected(q);
			});

			btn_start.click(function() {
				self.onClickUpload(q);
			});

			btn_cancel.click(function() {
				self.onClickCancelUpload(q);
			});

		},

		onFileSelected : function(q) {

			var temp = q.find('.template');
			var inst = q.find('.instance');
			var input = q.find('#input-file')[0];
			var files = input.files;
			var total = 0;

			var table = temp.find('.list').clone();
			var head = table.find('.list-head');
			var item = table.find('.list-item');
			table.empty();
			table.append(head);
			inst.empty();
			inst.append(table);

			if (files) {
				var len = files.length;
				for (var i = 0; i < len; i++) {
					var file = files[i];
					var name = file.name;
					var size = file.size;
					var it = item.clone();
					it.find('.file-name').text(name);
					it.find('.file-size').text(size);
					table.append(it);
					total += size;
				}
			}
			q.find('.total-size').text(total);
		},

		get_current_location : function() {
			return this._outer._cur_location;
		},

		set_current_xhr : function(xhr) {
			var old = this._cur_xhr;
			this._cur_xhr = xhr;
			if (old != null) {
				old.abort();
			}
		},

		onClickCancelUpload : function(q) {
			this.set_current_xhr(null);
		},

		onClickUpload : function(q) {

			var self = this;

			// load param
			var cl = this.get_current_location();
			var file = cl.location();
			var desc = file.toDescriptor();

			// make URL
			var context = this._context;
			var url = '~/file-upload.do';
			var query = desc.createQuery();
			query.service = 'plain-file-upload';
			url = desc.createURL(url, query);
			url = context.normalizeURL(url);

			// make XHR
			var xhr = new XMLHttpRequest();
			var form1 = q.find('#form1')[0];
			var fd = new FormData(form1);

			/* event listeners */

			var uploadProgress = function(e) {
				var str = '';
				if (e.lengthComputable) {
					var cb = e.loaded;
					var total = e.total;
					self.onUploadProgress(cb, total, q);
				} else {
					self.onUploadProgress(-1, -1, q);
				}
			};
			var uploadComplete = function(e) {
				self.close('completed');
				self.onUploadCompleted();
			};
			var uploadFailed = function(e) {
				self.close('failed');
			};
			var uploadCanceled = function(e) {
				self.close('canceled');
			};

			xhr.upload.addEventListener("progress", uploadProgress, false);
			xhr.addEventListener("load", uploadComplete, false);
			xhr.addEventListener("error", uploadFailed, false);
			xhr.addEventListener("abort", uploadCanceled, false);
			/*
			 * Be sure to change the URL below to the URL of your upload server
			 * side script
			 */

			this.set_current_xhr(xhr);
			xhr.open("POST", url);
			xhr.send(fd);

		},

		onUploadCompleted : function() {
			var cl = this.get_current_location();
			var node = cl.location();
			cl.location(node);
		},

		onUploadProgress : function(done, total, query) {

			var value = Math.floor(done * 100.0 / total);
			var str = value + '';
			var i = str.indexOf('.');
			if (i < 0) {
				// NOP
			} else {
				str = str.substring(0, i);
			}
			var txt = query.find('#text-progress');
			var bar = query.find('.progress-bar-success');
			txt.text(str + '%');
			bar.attr('style', 'width:' + value + '%');
		},

		close : function(code) {

			var i18n = this._context.getBean('i18n');
			var txt = i18n.getString(code);
			var q = this._outer.dialogQuery();
			var bar = q.find('.progress-bar-success');

			q.modal('hide');
			bar.attr('style', 'width: 0%');

			alert(txt);

		},

	};

});

this.snowflake.FileUploadDialog = this.com.boluozhai.h2o.widget.file_upload.FileUploadDialog;

/*******************************************************************************
 * EOF
 */
