/*******************************************************************************
 * 
 * folder.js
 * 
 * @Copyright (c) 2016 boluozhai.com
 * @License MIT License
 * @Author xukun<xukun17@sina.com>
 * 
 */

JS.module(function(mc) {

	mc.package('com.boluozhai.h2o.widget.folder');

	var System = mc.import('js.lang.System');
	var ListBuilder = mc.import('snowflake.view.list.ListBuilder');
	var ListModel = mc.import('snowflake.view.list.ListModel');
	var Attributes = mc.import('js.lang.Attributes');
	var RESTClient = mc.import('snowflake.rest.RESTClient');
	var Event = mc.import('js.event.Event');
	var FunctionAdapter = mc.import('js.event.FunctionAdapter');
	var EventDispatcher = mc.import('js.event.EventDispatcher');
	var ResourceLoader = mc.import('com.boluozhai.h2o.widget.ResourceLoader');
	var DocumentBinder = mc.import('com.boluozhai.h2o.widget.DocumentBinder');

	/***************************************************************************
	 * class PathBarModel
	 */

	function PathBarModel(current_loc) {

		var array = [];
		var file = current_loc.location();
		var p = file;
		for (; p != null; p = p.getParentFile()) {
			array.push(p);
		}
		array.pop();
		array = array.reverse();

		this._array = array;
	}

	mc.class(function(cc) {
		cc.type(PathBarModel);
		// cc.extends( );
	});

	PathBarModel.prototype = {

		size : function() {
			return this._array.length;
		},

		get : function(index) {
			return this._array[index];
		},

	};

	/***************************************************************************
	 * class PathBarBinder
	 */

	function PathBarBinder() {

		var inner = $('<div></div>');
		var outer = $('<div></div>');
		outer.append(inner);
		this.headOuter(outer);
		this.headInner(inner);

	}

	mc.class(function(cc) {
		cc.type(PathBarBinder);
		cc.extends(DocumentBinder);
	});

	PathBarBinder.prototype = {

		parent : function(value) {
			return this.bind('parent', value);
		},

		headOuter : function(value) {
			return this.attr('headOuter', value);
		},

		headInner : function(value) {
			return this.attr('headInner', value);
		},

	// onCreateHead : function(fn /* (list_item) */) {
	// return this.attr('onCreateHead', fn);
	// },

	};

	/***************************************************************************
	 * class PathBarCtrl
	 */

	function PathBarCtrl(context) {
		this._context = context;
		this._binder = new PathBarBinder();
	}

	mc.class(function(cc) {
		cc.type(PathBarCtrl);
		cc.extends(Attributes);
	});

	PathBarCtrl.prototype = {

		init : function() {

			// init model
			var model = this.createListModel();
			this.model(model);

			// init view
			var context = this._context;
			var loader = new ResourceLoader(context);
			var self = this;
			var url = '~/lib/export/h2o/html/PathBar.html';
			loader.loadHTML(url, function(query) {
				self.onHtmlReday(query.find('.pathbar'));
			});

			// init event handler
			this.setupDataListener();

		},

		setupDataListener : function() {
			var self = this;
			var li = new FunctionAdapter(function() {
				self.updateView();
			});
			var cl = this.currentLocation();
			cl.addEventHandler(li);
		},

		binder : function() {
			return this._binder;
		},

		currentLocation : function(value) {
			return this.attr('current_location', value);
		},

		onHtmlReday : function(query) {
			this.setupMyselfToParent(query);
			this.setupRootButton(query);
			this.setupListCtrl();
		},

		setupMyselfToParent : function(query) {
			var parent = this.binder().parent();
			var child = query;
			parent.append(child);
			this._jq_view = child;
		},

		setupRootButton : function(query) {

			var parent = this.binder().headInner();
			var child = query.find('.list-head-inner');
			parent.empty();
			parent.append(child);

			var self = this;// TODO
			var btn = child.find('.btn');
			btn.click(function() {
				self.fireOnClickRoot();
			});

		},

		setupListCtrl : function() {

			var self = this;
			var model = this.model();
			var view = this._jq_view.find('.list');
			var builder = new ListBuilder();
			var binder = this.binder();

			builder.view(view);
			builder.model(model);
			builder.onSelectItem(function(model, index) {
				return '.list-item';
			});

			// items

			builder.addHead('.list-head').onCreate(function(item) {

				var parent = item.view().find('.list-head-outer');
				var child = binder.headOuter();
				parent.empty();
				parent.append(child);

			}).onUpdate(function(item) {
				// NOP
			});

			builder.addItem('.list-item').onCreate(function(item) {
				var view = item.view();
				view.find('button.btn').click(function() {
					self.fireOnClickItem(item);
				});
			}).onUpdate(function(item) {
				var view = item.view();
				var file = item.data();
				var data = file.getName();
				view.find('button.btn').text(data);
			});

			this._list_ctrl = builder.create();
		},

		createListModel : function() {
			var cl = this.currentLocation();
			return new PathBarModel(cl);
		},

		model : function(value) {
			return this.attr('model', value);
		},

		updateView : function() {

			var model = this.createListModel();
			this.model(model);

			var c2 = this._list_ctrl;
			c2.model(model);
			c2.update();
			c2.update(true);
		},

		eventHandler : function(h) {
			return this.attr('event_handler', h);
		},

		fireOnClickItem : function(item) {
			var file = item.data();
			var cl = this.currentLocation();
			cl.location(file);
		},

		fireOnClickRoot : function() {
			var cl = this.currentLocation();
			var file = cl.location();
			var vfs = file.vfs();
			cl.location(vfs.root());
		},

	};

	/***************************************************************************
	 * class FileListItem
	 */

	function FileListItem(file) {
		this._file = file;
	}

	mc.class(function(cc) {
		cc.type(FileListItem);
	});

	FileListItem.prototype = {

		file : function() {
			return this._file;
		},

		name : function() {
			return this._file.getName();
		},

		isdir : function() {
			return this._file.isDirectory();
		},

		type : function() {
			return this._file.type();
		},

		time : function() {
			return this._file.lastModified();
		},

		size : function() {
			return this._file.length();
		},

	};

	/***************************************************************************
	 * class FileListModel
	 */

	function FileListModel(current_location) {

		var array = [];
		var dir = current_location.location();
		if (dir != null) {
			var list = dir.list();
			for ( var i in list) {
				var name = list[i];
				var ch = dir.child(name);
				array.push(new FileListItem(ch));
			}
		}

		array.sort(this.sortFunction());

		this._array = array;

	}

	mc.class(function(cc) {
		cc.type(FileListModel);
		cc.extends(EventDispatcher);
	});

	FileListModel.prototype = {

		size : function() {
			return this._array.length;
		},

		get : function(index) {
			return this._array[index];
		},

		sortFunction : function() {
			var fn = function(i1, i2) {

				var d1 = i1.isdir();
				var d2 = i2.isdir();
				if (d1 != d2) {
					return (d1 ? -1 : 1);
				}

				var n1 = i1.name();
				var n2 = i2.name();
				return n1.localeCompare(n2);

			};
			return fn;
		},

	};

	/***************************************************************************
	 * class FileListBinder
	 */

	function FileListBinder() {
	}

	mc.class(function(cc) {
		cc.type(FileListBinder);
		cc.extends(DocumentBinder);
	});

	FileListBinder.prototype = {

		parent : function(value) {
			return this.bind('parent', value);
		},

	};

	/***************************************************************************
	 * class FileListCtrl
	 */

	function FileListCtrl(context) {
		this._context = context;
		this._binder = new FileListBinder();
	}

	mc.class(function(cc) {
		cc.type(FileListCtrl);
		cc.extends(Attributes);
	});

	function select_icon_for_file(query, isdir, type) {
		var key = null;
		if (isdir) {
			key = 'directory';
		} else {
			var i = type.indexOf('/');
			if (i < 0) {
				key = type;
			} else {
				key = type.substring(0, i);
			}
		}
		var table = {
			directory : 'filelist-icon-dir',
			file : 'filelist-icon-file',
			application : 'filelist-icon-application',
			text : 'filelist-icon-text',
			image : 'filelist-icon-image',
			audio : 'filelist-icon-audio',
			video : 'filelist-icon-video',
			arch : 'filelist-icon-arch'
		};
		for ( var k in table) {
			var cn = table[k];
			query.removeClass(cn);
		}
		var cn = table[key];
		if (cn == null) {
			cn = table.file;
		}
		query.addClass(cn);
	}

	FileListCtrl.prototype = {

		init : function() {

			// init model
			var model = this.createListModel();
			this.model(model);

			// init view
			var context = this._context;
			var loader = new ResourceLoader(context);
			var self = this;
			var url = '~/lib/export/h2o/html/FileList.html';
			loader.loadHTML(url, function(query) {
				self.onHtmlReday(query.find('.filelist'));
			});

			// init event handler
			this.setupDataListener();

		},

		setupDataListener : function() {
			var self = this;
			var li = new FunctionAdapter(function() {
				self.updateView();
			});
			var cl = this.currentLocation();
			cl.addEventHandler(li);
		},

		binder : function() {
			return this._binder;
		},

		currentLocation : function(value) {
			return this.attr('current_location', value);
		},

		onHtmlReday : function(query) {
			var parent = this.binder().parent();
			var child = query;
			parent.append(child);
			this._jq_view = child;
			this.setupListCtrl();
		},

		setupListCtrl : function() {

			var self = this;
			var context = this._context;
			var model = this.model();
			var view = this._jq_view.find('.list');
			var builder = new ListBuilder();

			var fmt = context.getBean('format');
			var fmt_time = fmt.from('time');
			var fmt_size = fmt.from('size');
			var fmt_type = fmt.from('type');

			var i18n = context.getBean('i18n');
			var txt_item = i18n.getString('items{count}');
			var txt_dir = i18n.getString('folder');

			builder.view(view);
			builder.model(model);
			builder.onSelectItem(function(model, index) {
				return '.list-item';
			});

			// items

			builder.addHead('.list-head').onCreate(function(item) {
				// NOP

			}).onUpdate(function(item) {
				// NOP
			});

			builder.addItem('.list-item').onCreate(function(item) {

				var view = item.view();
				view.find('.on-click').click(function() {
					self.fireOnClickItem(item);
				});
				view.find('.on-click-modify').click(function() {
					self.fireOnClickItemModify(item);
				});

			}).onUpdate(function(item) {

				var view = item.view();
				var data = item.data();

				var name0 = data.name();
				var isdir0 = data.isdir();
				var type0 = data.type();
				var size0 = data.size();
				var time0 = data.time();

				var name = name0;
				var time = fmt_time.toString(time0);
				var type = fmt_type.toString(type0);
				var size = null;
				if (isdir0) {
					size = txt_item.replace('{count}', size0 + '');
				} else {
					size = fmt_size.toString(size0);
				}

				view.find('.f_name').text(name);
				view.find('.f_size').text(size);
				view.find('.f_type').text(type);
				view.find('.f_time').text(time);

				var icon = view.find('.f_icon');
				select_icon_for_file(icon, isdir0, type0);

			});

			this._list_ctrl = builder.create();

		},

		createListModel : function() {
			var cl = this.currentLocation();
			return new FileListModel(cl);
		},

		model : function(value) {
			return this.attr('model', value);
		},

		updateView : function() {
			var model = this.createListModel();
			this.model(model);
			var c2 = this._list_ctrl;
			c2.model(model);
			c2.update();
			c2.update(true);

			var cl = this.currentLocation();
			var isdir = false;
			var path = cl.location();
			if (path != null) {
				isdir = path.isDirectory();
			}
			var view = this.binder().parent();
			if (isdir) {
				view.show();
			} else {
				view.hide();
			}

		},

		fireOnClickItem : function(item) {
			var data = item.data();
			var file = data.file();
			var cl = this.currentLocation();
			cl.open(file);
		},

		fireOnClickItemModify : function(item) {
			var data = item.data();
			var file = data.file();
			var cl = this.currentLocation();
			cl.selection(file);
		},

	};

});

/*******************************************************************************
 * EOF
 */
