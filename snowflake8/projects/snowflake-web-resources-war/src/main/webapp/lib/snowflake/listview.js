/*******************************************************************************
 * listview.js
 */

JS.module(function(mc) {

	mc.package('snowflake.view.list');

	// var Object = mc.import('js.lang.Object');
	// var Class = mc.import('js.lang.Class');
	var Attributes = mc.import('js.lang.Attributes');

	/***************************************************************************
	 * class ListController
	 */

	function ListController(inner) {
		this._inner = inner;
	}

	mc.class(function(cc) {
		cc.type(ListController);
		cc.extends(Attributes);
	});

	ListController.prototype = {

		model : function(model) {
			return this._inner.model(model);
		},

		update : function(fire/* : boolean */) {
			this._inner.update(fire);
		},

	};

	/***************************************************************************
	 * class ListItem
	 */

	function ListItem(inner) {
		this._inner = inner;
	}

	mc.class(function(cc) {
		cc.type(ListItem);
	});

	ListItem.prototype = {

		data : function() {
			return this._inner.data();
		},

		model : function() {
			return this._inner.model();
		},

		index : function() {
			return this._inner.index();
		},

		view : function() {
			return this._inner.view();
		},

		update : function(fire) {
			this._inner.update(fire);
		},

	};

	/***************************************************************************
	 * class ListItemBuilder
	 */

	function ListItemBuilder(inner) {
		this._inner = inner;
	}

	mc.class(function(cc) {
		cc.type(ListItemBuilder);
	});

	ListItemBuilder.prototype = {

		onCreate : function(fn/* (item) */) {
			this._inner.onCreate(fn);
			return this;
		},

		onUpdate : function(fn/* (item) */) {
			this._inner.onUpdate(fn);
			return this;
		},

		create : function() {
			return this._inner.create();
		},

	};

	/***************************************************************************
	 * class ListModel
	 */

	function ListModel() {
		this._list = [ 'a', 'b', 'c', 'd' ];
	}

	mc.class(function(cc) {
		cc.type(ListModel);
	});

	ListModel.prototype = {

		size : function() {
			return this._list.length;
		},

		insert : function(index, obj) {
			this._list;
		},

		remove : function(index, obj) {
			this._list;
		},

		replace : function(index, obj) {
			this._list[index] = obj;
		},

		get : function(index) {
			return this._list[index];
		},

	};

	/***************************************************************************
	 * class ListBuilder
	 */

	function ListBuilder() {
		this._inner = null;
		this.reset();
	}

	mc.class(function(cc) {
		cc.type(ListBuilder);
		cc.extends(Attributes);
	});

	ListBuilder.prototype = {

		reset : function() {
			this._inner = new InnerListBuilder(this);
		},

		create : function() {
			var facade = this._inner.create();
			this._inner = null;
			return facade;
		},

		view : function(query) {
			return this._inner.view(query);
		},

		model : function(model) {
			return this._inner.model(model);
		},

		onSelectItem : function(fn /* (model,i) */) {
			return this._inner.onSelectItem(fn);
		},

		addHead : function(sel) {
			return this._inner.addHead(sel);
		},

		addItem : function(sel) {
			return this._inner.addItem(sel);
		},

		addFoot : function(sel) {
			return this._inner.addFoot(sel);
		},

	};

	// /////////////////////////////////////////////////////////////////
	// Inner Implements ////////////////////////////////////////////////

	/***************************************************************************
	 * class InnerListBuilder
	 */

	function InnerListBuilder(facade) {

		this._facade = facade;

		this._model = null;
		this._jq_view = null;
		this._fn_sel_item = null;

		// next, the item is a : InnerListItemBuilder(lib)

		this._lib_head_list = [];
		this._lib_foot_list = [];
		this._lib_item_table = {};

	}

	InnerListBuilder.prototype = {

		facade : function() {
			return this._facade;
		},

		view : function(query) {
			if (query == null) {
				query = this._jq_view;
			} else {
				this._jq_view = query;
			}
			return query;
		},

		model : function(model) {
			if (model == null) {
				model = this._model;
			} else {
				this._model = model;
			}
			return model;
		},

		onSelectItem : function(fn) {
			if (fn == null) {
				fn = this._fn_sel_item;
			} else {
				this._fn_sel_item = fn;
			}
			return fn;
		},

		addHead : function(sel) {
			var view = this._jq_view;
			var item_view = view.find(sel);
			var lib = new InnerListItemBuilder(item_view);
			this._lib_head_list.push(lib);
			return lib.facade();
		},

		addFoot : function(sel) {
			var view = this._jq_view;
			var item_view = view.find(sel);
			var lib = new InnerListItemBuilder(item_view);
			this._lib_foot_list.push(lib);
			return lib.facade();
		},

		addItem : function(sel) {
			var view = this._jq_view;
			var item_view = view.find(sel);
			var lib = new InnerListItemBuilder(item_view);
			this._lib_item_table[sel] = lib;
			return lib.facade();
		},

		create : function() {

			var inner_ctrl = new InnerListCtrl(this);
			inner_ctrl.init();
			return inner_ctrl.facade();

		},

	};

	/***************************************************************************
	 * class InnerListCtrl
	 */

	function InnerListCtrl(builder) {

		this._facade = new ListController(this);
		this._version = 100;
		this._model = builder.model();

		this._head_tmp = builder._lib_head_list;
		this._item_tmp = builder._lib_item_table;
		this._foot_tmp = builder._lib_foot_list;

		this._parent_view = null; // a query
		this._item_list = []; // include heads & foots
		this._fn_on_select_item = builder.onSelectItem();

	}

	InnerListCtrl.prototype = {

		facade : function() {
			return this._facade;
		},

		model : function(model) {
			if (model == null) {
				model = this._model;
			} else {
				this._model = model;
				this.rebuild_item_list();
				this.rebuild_view_list();
			}
			return model;
		},

		update : function(fire) {
			if (fire) {
				this.sync();
			} else {
				this._version++;
			}
		},

		sync : function() {
			var list = this._item_list;
			for ( var i in list) {
				var item = list[i];
				item.sync();
			}
		},

		version : function() {
			return this._version;
		},

		init : function() {

			this.__init_item_builder_list(this._head_tmp);
			this.__init_item_builder_list(this._item_tmp);
			this.__init_item_builder_list(this._foot_tmp);

			this.rebuild_item_list();
			this.rebuild_view_list();

			this.update();
			this.sync();

		},

		__init_item_builder_list : function(list) {
			for ( var key in list) {
				var lib = list[key];
				lib.init();
				this._parent_view = lib.queryContainerView();
			}
		},

		rebuild_view_list : function() {

			var parent = this._parent_view;
			var list = this._item_list;

			parent.empty();

			for ( var i in list) {
				var item = list[i];
				var ch = item.view();
				parent.append(ch);
			}

		},

		rebuild_item_list : function() {

			var list = [];

			// head
			var index = 0;
			var heads = this._head_tmp;
			for ( var key in heads) {
				var lib = heads[key];
				var item = lib.create(this, index++);
				list.push(item);
			}

			// item
			var model = this._model;
			var size = model.size();
			for (var i = 0; i < size; i++) {
				var lib = this.get_list_item_builder(model, i);
				var item = lib.create(this, i);
				list.push(item);
			}

			// foot
			index = 0;
			var foots = this._foot_tmp;
			for ( var key in foots) {
				var lib = foots[key];
				var item = lib.create(this, index++);
				list.push(item);
			}

			this._item_list = list;
		},

		get_list_item_builder : function(model, i) {
			var fn = this._fn_on_select_item;
			var key = fn(model, i);
			return this._item_tmp[key];
		},

	};

	/***************************************************************************
	 * class InnerListItem
	 */

	function InnerListItem(model, view, list_ctrl, index, fn_on_update) {
		this._list_ctrl = list_ctrl;
		this._version = 0;
		this._facade = new ListItem(this);
		this._query_item_view = view;
		this._fn_on_update = fn_on_update;
		this._model = model;
		this._index = index;
	}

	InnerListItem.prototype = {

		init : function() {
			var fn = this._fn_on_create;
			var item = this._facade;
			fn(item);
		},

		facade : function() {
			return this._facade;
		},

		view : function() {
			return this._query_item_view;
		},

		model : function() {
			return this._model;
		},

		index : function() {
			return this._index;
		},

		data : function() {
			var index = this._index;
			var model = this._model;
			return model.get(index);
		},

		update : function(fire) {
			this._version = -1;
			if (fire) {
				this.sync();
			}
		},

		sync : function() {
			var ver1 = this._version;
			var ver2 = this._list_ctrl.version();
			if (ver1 != ver2) {
				this.do_sync();
				this._version = ver2;
			}
		},

		do_sync : function() {
			var fn = this._fn_on_update;
			var item = this._facade;
			fn(item);
		},

	};

	/***************************************************************************
	 * class InnerListItemBuilder
	 */

	function InnerListItemBuilder(query_item_view) {

		this._facade = new ListItemBuilder(this);

		this._query_item_view = query_item_view;
		this._query_container_view = null;
		this._fn_on_create = null;
		this._fn_on_update = null;

	}

	InnerListItemBuilder.prototype = {

		facade : function() {
			return this._facade;
		},

		onCreate : function(fn) {
			this._fn_on_create = fn;
		},

		onUpdate : function(fn) {
			this._fn_on_update = fn;
		},

		init : function() {
			var item = this._query_item_view;
			var parent = item.parent();
			this._query_container_view = parent;
		},

		create : function(list_ctrl, index) {

			var model = list_ctrl.model();
			var view = this._query_item_view.clone();

			var fn_on_create = this._fn_on_create;
			var fn_on_update = this._fn_on_update;

			var item = new InnerListItem(model, view, list_ctrl, index,
					fn_on_update);

			fn_on_create(item.facade());

			return item;

		},

		queryItemView : function() {
			return this._query_item_view;
		},

		queryContainerView : function() {
			return this._query_container_view;
		},

	};

});

/*******************************************************************************
 * EOF
 */
