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

	};

	function FilePropertiesDialog_onload(dlg, file) {

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

	}

});

this.snowflake.FilePropertiesDialog = this.com.boluozhai.h2o.widget.file_properties.FilePropertiesDialog;

/*******************************************************************************
 * EOF
 */
