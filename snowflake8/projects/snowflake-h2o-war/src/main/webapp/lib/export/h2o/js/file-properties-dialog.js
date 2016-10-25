/*******************************************************************************
 * 
 * file-properties-dialog.js
 * 
 * @Copyright (c) 2016 boluozhai.com
 * @License MIT License
 * @Author xukun<xukun17@sina.com>
 * 
 */

JS.module(function(mc) {

	mc.package('com.boluozhai.h2o.widget.file_properties');

	var System = mc.import('js.lang.System');
	var Attributes = mc.import('js.lang.Attributes');
	var ResourceLoader = mc.import('com.boluozhai.h2o.widget.ResourceLoader');
	var ModalDialog = mc.use(snowflake.ModalDialog);
	var CurrentLocationEvent = mc.use(snowflake.CurrentLocationEvent);

	/***************************************************************************
	 * class FilePropertiesDialog
	 */

	function FilePropertiesDialog(context) {

		this._context = context;

		this.ModalDialog(context);

		this.htmlURL('~/lib/export/h2o/html/FilePropertiesDialog.html');
		this.dialogSelector('#the-dialog');

	}

	mc.class(function(cc) {
		cc.type(FilePropertiesDialog);
		cc.extends(ModalDialog);
	});

	FilePropertiesDialog.prototype = {

		onCreate : function() {

		},

		setCurrentLocation : function(cl) {
			this._cur_location = cl;
			cl.addEventHandler(this);
		},

		onEvent : function(e) {

			var code = e.code();
			if (code == CurrentLocationEvent.ON_OPEN) {
				var file = e.location();
				if (file.isDirectory()) {
					// NOP
				} else {
					var self = this;
					file.load(function() {
						FilePropertiesDialog_onload(self, file);
					});
					this.show();
				}
			}

		},

		setupDeleteButton : function(file) {

			var context = this._context;
			var query = this.dialogQuery();
			var btn = query.find('#btn-delete');

			var msg = 'work-in-progress';
			var i18n = context.getBean('i18n');
			msg = i18n.getString(msg);

			btn.click(function() {
				alert(msg);
			});
		},

		setupDownloadButton : function(file) {

			var context = this._context;
			var query = this.dialogQuery();
			var btn = query.find('#btn-download');
			var txt = query.find('.text-url');
			var ico = query.find('.file-properties-icon');

			// prepare a new icon image element
			var ico_inst = ico.find('.instance');
			ico_inst.empty();
			var ico_image = ico.find('.image').clone();
			ico_inst.append(ico_image);

			// make URLs
			var type = file.type();
			var downloadURL = this.get_download_url(context, file);
			var iconURL = null;
			if (type.indexOf('image/') == 0) {
				iconURL = this.get_thumb_url(context, file);
			} else {
				iconURL = this.get_icon_url(context, file);
			}

			// set tags
			btn.attr('href', downloadURL);
			txt.text(downloadURL);
			ico_image.attr('src', iconURL);
		},

		get_download_url : function(context, file) {
			var url = '~/u/r/api/t/' + file.getName();
			var fd = file.toDescriptor();
			var query = {
				service : 'plain-file-download',
			};
			url = context.normalizeURL(url);
			return fd.createURL(url, query);
		},

		get_thumb_url : function(context, file) {
			var url = '~/u/r/api/t/' + file.getName();
			var fd = file.toDescriptor();
			var query = {
				service : 'plain-file-thumb',
				width : 256,
				height : 256,
			};
			url = context.normalizeURL(url);
			return fd.createURL(url, query);
		},

		get_icon_url : function(context, file) {
			var type = file.type();
			var index = type.indexOf('/');
			if (index < 0) {
				// NOP
			} else {
				type = type.substring(0, index);
			}
			var url = '~/lib/export/h2o/image/{type}.png';
			url = url.replace('{type}', type);
			return context.normalizeURL(url);
		},

	};

	function FilePropertiesDialog_onload(dlg, file) {

		var context = dlg._context;
		var q = dlg.dialogQuery();

		var fmt = context.getBean('format');
		var fmt_time = fmt.from('time');
		var fmt_size = fmt.from('size');
		var fmt_type = fmt.from('type');

		var name = file.getName();
		var size = file.length();
		var type = file.type();
		var time = file.lastModified();
		var path = file.getPath();

		var size2 = '  (' + size + ')';
		var type2 = '      (' + type + ')';

		size = fmt_size.toString(size);
		time = fmt_time.toString(time);
		type = fmt_type.toString(type);

		type = (type + type2);
		size = (size + size2);

		q.find('.file-name').text(name);
		q.find('.file-size').text(size);
		q.find('.file-type').text(type);
		q.find('.file-time').text(time);
		q.find('.file-path').text(path);

		dlg.setupDeleteButton(file);
		dlg.setupDownloadButton(file);

	}

});

this.snowflake.FilePropertiesDialog = this.com.boluozhai.h2o.widget.file_properties.FilePropertiesDialog;

/*******************************************************************************
 * EOF
 */
