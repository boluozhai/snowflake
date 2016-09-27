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
	}

	mc.class(function(cc) {
		cc.type(PathBarBinder);
		cc.extends(DocumentBinder);
	});

	PathBarBinder.prototype = {

		parent : function(value) {
			return this.bind('parent', value);
		},

		head : function(value) {
			return this.bind('head', value);
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
			loader.loadHTML('~/export/h2o/html/PathBar.html', function(query) {
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

			var head = this.binder().head();
			if (head != null) {
				var listhead = query.find('.list-head');
				listhead.empty();
				listhead.append(head);
			}

			var parent = this.binder().parent();
			var child = query;
			parent.append(child);
			this._jq_view = child;
			this.setupListCtrl();
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

				var view = item.view();
				var head = binder.head();

				if (head == null) {
					var btn = view.find('.btn-root-path');
					btn.click(function() {
						self.fireOnClickRoot();
					});
				} else {
					view.empty();
					view.append(head);
				}

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
			var t = this._type;
			if (t == null) {
				if (this.isdir()) {
					t = 'DIR';
				} else {
					var name = this.name();
					var i = name.lastIndexOf('.');
					if (i < 0) {
						t = 'FILE';
					} else {
						t = name.substring(i).toLowerCase();
					}
				}
				this._type = t;
			}
			return t;
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
		var current = isdir ? 'filelist-icon-dir' : 'filelist-icon-file';
		var array = [ 'filelist-icon-dir', 'filelist-icon-file' ];
		for ( var i in array) {
			var cn = array[i];
			if (cn == current) {
				query.addClass(cn);
			} else {
				query.removeClass(cn);
			}
		}
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
			loader.loadHTML('~/export/h2o/html/FileList.html', function(query) {
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
			var model = this.model();
			var view = this._jq_view.find('.list');
			var builder = new ListBuilder();

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
				view.find('button.btn').click(function() {
					self.fireOnClickItem(item);
				});

			}).onUpdate(function(item) {

				var view = item.view();
				var data = item.data();

				var name = data.name();
				var isdir = data.isdir();
				var type = data.type();
				var size = data.size();
				var time = data.time();

				if (isdir) {
					size = '-';
					type = 'DIR';
				} else {
					// type = '<FILE>';
				}

				var date = new Date();
				date.setTime(time);

				view.find('.f_name').text(name);
				view.find('.f_size').text(size);
				view.find('.f_type').text(type);
				view.find('.f_time').text(date.toLocaleString());

				var icon = view.find('.f_icon');
				select_icon_for_file(icon, isdir, type);

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
			cl.location(file);
		},

	};

});

/*******************************************************************************
 * EOF
 */
