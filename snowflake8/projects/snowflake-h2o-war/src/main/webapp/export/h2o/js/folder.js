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

	};

	/***************************************************************************
	 * class PathBarEvent
	 */

	function PathBarEvent(context) {
		this._context = context;
	}

	mc.class(function(cc) {
		cc.type(PathBarEvent);
		cc.extends(Event);
	});

	PathBarEvent.prototype = {

		item : function(value) {
			return this.attr('item', value);
		},

	};

	/***************************************************************************
	 * class PathBarEventHandler
	 */

	function PathBarEventHandler() {
	}

	mc.class(function(cc) {
		cc.type(PathBarEventHandler);
	});

	PathBarEventHandler.prototype = {

		onEvent : function(event) {
			var item = event.item();
			this._ctrl = event.source();
			this.onClickItem(item);
		},

		onClickItem : function(item) {
			this.open(item);
		},

		open : function(item) {
			var name = item.data();
			var index = item.index();
			var ctrl = this._ctrl;
			ctrl.open(name, index);
		},

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
				var view = item.view();
				view.find('.btn').click(function() {
					self.fireOnClickRoot(item);
				});
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

		fireOnClickRoot : function(item) {
			var cl = this.currentLocation();
			var file = cl.location();
			var vfs = file.vfs();
			cl.location(vfs.root());
		},

	};

	/***************************************************************************
	 * class FileListModel
	 */

	function FileListModel(dir_data_model) {
		this._array = [];
		this._dir_data_model = dir_data_model;
		dir_data_model.addEventHandler(this);
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

		onEvent : function(event) {
			this.update();
		},

		update : function() {

			var data = this._dir_data_model;
			var array0 = data.items();

			var array = [];
			for ( var i in array0) {
				array.push(array0[i]);
			}

			var fn = this.sortFunction();
			this._array = array.sort(fn);

			this.fire();
		},

		fire : function() {
			var e = new Event();
			e.message('changed');
			this.dispatchEvent(e, this);
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
	 * class FileListEvent
	 */

	function FileListEvent(context) {
		this._context = context;
	}

	mc.class(function(cc) {
		cc.type(FileListEvent);
		cc.extends(Event);
	});

	FileListEvent.prototype = {

		item : function(value) {
			return this.attr('item', value);
		},

	};

	/***************************************************************************
	 * class FileListEventHandler
	 */

	function FileListEventHandler() {
	}

	mc.class(function(cc) {
		cc.type(FileListEventHandler);
	});

	FileListEventHandler.prototype = {

		onEvent : function(event) {
			var item = event.item();
			this._ctrl = event.source();
			this.onClickItem(item);
		},

		onClickItem : function(item) {
			this.open(item);
		},

		open : function(item) {
			var data = item.data();
			var ctrl = this._ctrl;
			var name = data.name();
			ctrl.open(name);
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
			model.addEventHandler(this);
			this.model(model);

			// init view
			var context = this._context;
			var loader = new ResourceLoader(context);
			var self = this;
			loader.loadHTML('~/export/h2o/html/FileList.html', function(query) {
				self.onHtmlReday(query.find('.filelist'));
			});

			// init event handler
			var h = this.eventHandler();
			if (h == null) {
				h = new FileListEventHandler();
				this.eventHandler(h);
			}

		},

		binder : function() {
			return this._binder;
		},

		currentLocation : function(value) {
			return this.attr('current_location', value);
		},

		onHtmlReday : function(query) {
			var parent = this.parent();
			var child = query;
			parent.append(child);
			this._jq_view = child;
			this.setupListCtrl();
		},

		open : function(name) {
			var ds = this.dataSource();
			var base = ds.currentPath();
			var offset = [ name ];
			ds.load(base, offset);
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
					type = '<DIR>';
				} else {
					type = '<FILE>';
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
			var src = this.dataSource();
			var data_model = src.model();
			return new FileListModel(data_model);
		},

		model : function(value) {
			return this.attr('model', value);
		},

		onEvent : function(event) {
			this.updateView();
		},

		updateView : function() {
			var model = this.model();
			var c2 = this._list_ctrl;
			c2.model(model);
			c2.update();
			c2.update(true);
		},

		eventHandler : function(h) {
			return this.attr('event_handler', h);
		},

		fireOnClickItem : function(item) {
			var event = new FileListEvent();
			event.source(this);
			event.item(item);
			var h = this.eventHandler();
			h.onEvent(event);
		},

	};

	/***************************************************************************
	 * class DirDataModel
	 */

	function DirDataModel(context) {
		this._context = context;
		this._atts = new Attributes();
	}

	mc.class(function(cc) {
		cc.type(DirDataModel);
		cc.extends(EventDispatcher);
	});

	DirDataModel.prototype = {

		items : function(array) {
			return this._atts.attr('items', array);
		},

		pathElements : function(array) {
			return this._atts.attr('path_elements', array);
		},

		baseURI : function(str) {
			return this._atts.attr('base_uri', str);
		},

		fileURI : function(str) {
			return this._atts.attr('file_uri', str);
		},

		fire : function() {
			var e = new Event();
			e.message('changed');
			this.dispatchEvent(e, this);
		},

	};

	/***************************************************************************
	 * class DirItem
	 */

	function DirItem() {
	}

	mc.class(function(cc) {
		cc.type(DirItem);
		cc.extends(Attributes);
	});

	DirItem.prototype = {

		name : function(value) {
			return this.attr('name', value);
		},

		uri : function(value) {
			return this.attr('uri', value);
		},

		size : function(value) {
			return this.attr('size', value);
		},

		time : function(value) {
			return this.attr('time', value);
		},

		type : function(value) {
			return this.attr('type', value);
		},

		isdir : function(value) {
			return this.attr('isdir', value);
		},

	};

	/***************************************************************************
	 * class AbstractDirDataLoader
	 */

	function AbstractDirDataLoader(context) {
		this._context = context;
	}

	mc.class(function(cc) {
		cc.type(AbstractDirDataLoader);
		cc.extends(Attributes);
	});

	AbstractDirDataLoader.prototype = {

		model : function(value) {
			return this.attr('model', value);
		},

		init : function() {
			// implements in subclass
		},

		load : function(path_elements) {
			// implements in subclass
		},

	};

	/***************************************************************************
	 * class DefaultDirDataLoader
	 */

	function DefaultDirDataLoader(context) {
		this._context = context;
	}

	mc.class(function(cc) {
		cc.type(DefaultDirDataLoader);
		cc.extends(AbstractDirDataLoader);
	});

	DefaultDirDataLoader.prototype = {

		load : function(path_elements) {

			var id = null;
			for (var i = 0; i < path_elements.length; i++) {
				var s = path_elements[i];
				if (id == null) {
					id = s;
				} else {
					id += ('/' + s);
				}
			}
			if (id == null) {
				id = '';
			}

			var client = RESTClient.getInstance(this._context);
			var app = client.getApplication();
			var api = app.getAPI('rest');
			var type = api.getType('file');
			var res = type.getResource(id);
			var request = res.get();

			var self = this;

			request.execute(function(response) {

				if (response.ok()) {
					var entity = response.entity();
					var js = entity.toJSON();
					self.onDataReady(js);
				} else {
					var msg = response.message();
					alert(msg);
				}

			});

		},

		onDataReady : function(js) {
			// var str = JSON.stringify(js);
			// alert(str);

			var model = this.model();

			var path_list = js.vfile.path;
			var file_list = js.vfile.list;
			var array = [];

			for ( var i in file_list) {
				var it1 = file_list[i];
				var it2 = new DirItem();
				it2.name(it1.name);
				it2.type(it1.type);
				it2.time(it1.lastModified);
				it2.size(it1.length);
				it2.isdir(it1.directory);
				array.push(it2);
			}

			model.items(array);
			model.pathElements(path_list);
			model.baseURI(js.vfile.baseURI);
			model.fileURI(js.vfile.fileURI);

			model.fire();

		},

	};

	/***************************************************************************
	 * class DirDataCtrl
	 */

	function DirDataCtrl(context) {
		this._context = context;
	}

	mc.class(function(cc) {
		cc.type(DirDataCtrl);
		cc.extends(Attributes);
	});

	DirDataCtrl.prototype = {

		model : function(value) {
			return this.attr('model', value);
		},

		loader : function(value) {
			return this.attr('loader', value);
		},

		init : function() {

			var context = this._context;
			var model = this.model();
			var loader = this.loader();

			if (model == null) {
				model = new DirDataModel();
				this.model(model);
			}

			if (loader == null) {
				loader = new DefaultDirDataLoader(context);
				this.loader(loader);
			}

			model.addEventHandler(this);

			loader.model(model);
			loader.init();

		},

		load : function(base_path /* String */, offset_elements /* Array */) {

			var array = [];
			array.addElement = function(s) {
				if (s == null) {
					return;
				} else if (s.length == 0) {
					return;
				} else if (s == '.') {
					return;
				} else if (s == '..') {
					this.pop();
				} else {
					this.push(s);
				}
			};
			if (base_path != null) {
				base_path = base_path.replace('\\', '/');
				var i = 0;
				for (;;) {
					var next = base_path.indexOf('/', i);
					if (next < 0) {
						var s = base_path.substring(i);
						array.addElement(s);
						break;
					} else {
						var s = base_path.substring(i, next);
						array.addElement(s);
						i = next + 1;
					}
				}
			}
			if (offset_elements != null) {
				for ( var i in offset_elements) {
					var s = offset_elements[i];
					array.addElement(s);
				}
			}
			var loader = this.loader();
			loader.load(array);
		},

		onEvent : function(event) {
		},

		currentPath : function() {
			var model = this.model();
			var array = model.pathElements();
			var sb = null;
			for ( var i in array) {
				var s = '/' + array[i];
				if (sb == null) {
					sb = s;
				} else {
					sb += s;
				}
			}
			return sb;
		},

	};

});

/*******************************************************************************
 * EOF
 */
