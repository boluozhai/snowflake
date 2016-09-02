JS.module(function(mc) {

	mc.package('com.boluozhai.h2o.folder');

	var System = mc.import('js.lang.System');
	var ListBuilder = mc.import('snowflake.view.list.ListBuilder');
	var ListModel = mc.import('snowflake.view.list.ListModel');
	var Attributes = mc.import('js.lang.Attributes');
	var RESTClient = mc.import('snowflake.rest.RESTClient');

	/***************************************************************************
	 * class FolderItem
	 */

	function FolderItem() {
	}

	mc.class(function(cc) {
		cc.type(FolderItem);
		cc.extends(Attributes);
	});

	FolderItem.prototype = {

		name : function(value) {
			return this.attr('name', value);
		},

		size : function(value) {
			return this.attr('size', value);
		},

		type : function(value) {
			return this.attr('type', value);
		},

		time : function(value) {
			return this.attr('time', value);
		},

	};

	FolderItem.create = function(name, size, type, time) {
		var item = new FolderItem();
		item.name(name);
		item.size(size);
		item.type(type);
		item.time(time);
		return item;
	};

	/***************************************************************************
	 * class FolderModel
	 */

	function FolderModel() {

		this.init();

	}

	mc.class(function(cc) {
		cc.type(FolderModel);
		cc.extends(Attributes);
	});

	FolderModel.prototype = {

		init : function() {

			var now = System.currentTimeMillis();
			var path = [ 'c:', 'aaa', 'bbb', 'ccc', 'ddd', 'eee', 'fff' ];
			var list = [];

			list.push(FolderItem.create('Hello.txt', 1005, 'text/plain', now));
			list.push(FolderItem.create('World.png', 9876, 'image/png', now));

			this.pathElements(path);
			this.items(list);

		},

		pathElements : function(value) {
			return this.attr('pathElements', value);
		},

		items : function(value) {
			return this.attr('items', value);
		},

		itemModel : function() {
			var list = this.items();
			return new ItemModel(list);
		},

		pathModel : function() {
			var list = this.pathElements();
			return new PathModel(list);
		},

	};

	/***************************************************************************
	 * class ItemModel
	 */

	function ItemModel(list) {
		this._list = list;
	}

	ItemModel.prototype = {

		size : function() {
			return this._list.length;
		},

		get : function(index) {
			return this._list[index];
		},

	};

	/***************************************************************************
	 * class PathModel
	 */

	function PathModel(list) {
		this._list = list;
		this._current_depth = 3;
	}

	PathModel.prototype = {

		size : function() {
			return this._list.length;
		},

		get : function(index) {
			return this._list[index];
		},

		currentDepth : function() {
			return this._current_depth;
		},

		toArray : function() {
			var list = this._list;
			var array = [];
			for (var i = list.length - 1; i >= 0; i--) {
				array[i] = list[i];
			}
			return array;
		},

	};

	/***************************************************************************
	 * class FolderLoader
	 */

	function FolderLoader(context, model) {
		this._context = context;
		this._model = model;
	}

	mc.class(function(cc) {
		cc.type(FolderLoader);
	});

	FolderLoader.prototype = {

		load : function(path, fn /* done (pojo) */) {

			var self = this;

			var client = RESTClient.getInstance(this._context);
			var app = client.getApplication();
			var api = app.getAPI('rest');
			var type = api.getType('file');
			var res = type.getResource(path);
			var request = res.get();
			request.execute(function(resp) {
				var ent = resp.entity();
				var js = ent.toJSON();
				self.update(js);
				fn(js);
			});

		},

		update : function(pojo) {

			{
				// TODO debug code
				var str = JSON.stringify(pojo, null, 4);
				$('.output').text(str);
			}

			var path = pojo.vfile.path;
			var list = pojo.vfile.list;
			var items = [];

			for ( var index in list) {
				var it = list[index];
				var name = it.name;
				var type = it.directory ? '<DIR>' : '-';
				var size = it.length;
				var time = it.lastModified;
				items.push(FolderItem.create(name, size, type, time));
			}

			var model = this._model;
			model.pathElements(path);
			model.items(items);

		},

	};

	/***************************************************************************
	 * class FolderController
	 */

	function FolderController(context) {

		this._context = context;
		this._model = new FolderModel();

	}

	mc.class(function(cc) {
		cc.type(FolderController);
	});

	FolderController.prototype = {

		load : function(path) {

			var self = this;
			var loader = new FolderLoader(this._context, this._model);
			loader.load(path, function(pojo) {
				// done
				self.update();
			});

		},

		update : function() {

			var item = this._item_ctrl;
			var path = this._path_ctrl;
			var model = this._model;

			path.model(model.pathModel());
			path.update(true);

			item.model(model.itemModel());
			item.update(true);

		},

		init : function() {

			this.inner_init_path_list();
			this.inner_init_item_list();

		},

		getCurrentPath : function() {
			var model = this._model;
			var path = model.pathElements();
			var sb = '';
			for ( var i in path) {
				sb += ('/' + path[i]);
			}
			return sb;
		},

		inner_init_item_list : function() {

			// Item List

			var self = this;
			var model = this._model.itemModel();
			var view = this._item_view;

			// builder
			var builder = new ListBuilder();
			builder.view(view);
			builder.model(model);
			builder.onSelectItem(function(model, index) {
				return '.item';
			});

			// items

			builder.addHead('.head').onCreate(function(item) {
				// NOP

			}).onUpdate(function(item) {
				// NOP
			});

			var onCreate = function(item) {

				var view = item.view();
				var data = item.data();
				var name = data.name();
				var url = self.getCurrentPath() + '/' + name;

				view.find('a').click(function() {
					self.load(url);
				});

			};
			var onUpdate = function(item) {

				var data = item.data();
				var view = item.view();

				var name = data.name();
				var time = data.time();
				var type = data.type();
				var size = data.size();

				view.find('.f_name').text(name);
				view.find('.f_time').text(time);
				view.find('.f_type').text(type);
				view.find('.f_size').text(size);

			};
			builder.addItem('.item').onCreate(onCreate).onUpdate(onUpdate);

			// create

			var ctrl = builder.create();
			ctrl.update();

			this._item_ctrl = ctrl;

		},

		inner_init_path_list : function() {

			// Path List

			var self = this;
			var model = this._model.pathModel();
			var view = this._path_view;

			var builder = new ListBuilder();
			builder.view(view);
			builder.model(model);
			builder.onSelectItem(function(model, index) {
				if (index < model.currentDepth()) {
					return '.item-l';
				} else {
					return '.item-s';
				}
			});

			// items

			builder.addHead('.head').onCreate(function(item) {

				var view = item.view();
				view.find('a').click(function() {
					self.load('/');
				});

			}).onUpdate(function(item) {
				// NOP
			});

			var onCreate = function(item) {

				var index = item.index();
				var view = item.view();
				var model = item.model();
				var array = model.toArray();

				var url = '';
				for (var i = 0; i <= index; i++) {
					var name = array[i];
					url += ('/' + name);
				}

				view.find('a').click(function() {
					self.load(url);
				});

			};
			var onUpdate = function(item) {

				var data = item.data();
				var view = item.view();

				var name = data;

				view.find('a').text(name);

			};
			builder.addItem('.item-l').onCreate(onCreate).onUpdate(onUpdate);
			builder.addItem('.item-s').onCreate(onCreate).onUpdate(onUpdate);

			// create

			var ctrl = builder.create();
			ctrl.update();

			this._path_ctrl = ctrl;

		},

		selectRootView : function(sel) {
			this._root_view = $(sel);
		},

		selectPathView : function(sel) {
			this._path_view = this._root_view.find(sel);
		},

		selectItemView : function(sel) {
			this._item_view = this._root_view.find(sel);
		},

	};

});