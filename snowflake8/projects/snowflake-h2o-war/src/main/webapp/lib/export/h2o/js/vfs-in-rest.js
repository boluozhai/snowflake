/*******************************************************************************
 * 
 * vfs.js
 * 
 * @Copyright (c) 2016 boluozhai.com
 * @License MIT License
 * @Author xukun<xukun17@sina.com>
 * 
 */

JS.module(function(mc) {

	mc.package('com.boluozhai.snowflake.vfs');

	var System = mc.import('js.lang.System');
	var Attributes = mc.import('js.lang.Attributes');
	var Event = mc.import('js.event.Event');
	var EventDispatcher = mc.import('js.event.EventDispatcher');

	var Viewport = mc.use(snowflake.Viewport);
	var JSONRestRequest = mc.use(snowflake.JSONRestRequest);

	var VFSFactory = mc.use(snowflake.vfs.VFSFactory);
	var VFSBuilder = mc.use(snowflake.vfs.VFSBuilder);
	var VFS = mc.use(snowflake.vfs.VFS);
	var VFSFacade = mc.use(snowflake.vfs.VFSFacade);
	var VFile = mc.use(snowflake.vfs.VFile);
	var VFileFacade = mc.use(snowflake.vfs.VFileFacade);
	var VFileDescriptor = mc.use(snowflake.vfs.VFileDescriptor);

	/***************************************************************************
	 * class VFSFactory
	 */

	function RestVFSFactory() {
	}

	mc.class(function(cc) {
		cc.type(RestVFSFactory);
		cc.extends(VFSFactory);
	});

	RestVFSFactory.prototype = {

		newBuilder : function(context) {
			return new RestVFSBuilder(context);
		},

	};

	/***************************************************************************
	 * class VFSBuilder
	 */

	function RestVFSBuilder(context) {
		this.VFSBuilder(context);
	}

	mc.class(function(cc) {
		cc.type(RestVFSBuilder);
		cc.extends(VFSBuilder);
	});

	RestVFSBuilder.prototype = {

		create : function() {
			var context = this.context();
			var impl = new VFSImpl(context);
			return impl.facade();
		},

	};

	/***************************************************************************
	 * class MyDataLoader
	 */

	function MyDataLoader(context) {
		this._context = context;
	}

	MyDataLoader.prototype = {

		toOffsetPath : function(file) {
			var array = [];
			var p = file;
			for (; p != null; p = p.getParentFile()) {
				var name = p.getName();
				if (name == null) {
					continue;
				} else {
					array.push(name);
				}
			}
			array = array.reverse();
			var sb = '.';
			for ( var i in array) {
				var name = array[i];
				sb = sb + '/' + name;
			}
			return sb;
		},

		load : function(file, fn) {

			var self = this;
			var context = this._context;
			var jrr = new JSONRestRequest(context);
			var descriptor = file.toDescriptor();
			var query = descriptor.createQuery();

			jrr.open('GET', {
				uid : query.owner,
				repo : query.repository,
				api : 'repo-api',
				type : 'file',
				id : query.id,
			});
			jrr.setParameters(query);
			jrr.onResult(function() {
				if (jrr.ok()) {
					self._model = jrr.responseEntity();
				} else {
					self._model = null;
				}
				fn();
			});
			jrr.send();
		},

		model : function() {
			return this._model;
		},

	};

	/***************************************************************************
	 * class VFileImpl
	 */

	function VFileImpl(vfs, parent, name) {

		var path = null;

		this._inner_vfs = vfs;
		this._inner_parent = parent;

		if (parent == null) {
			// the root
			name = null;
			path = '~';
			this._root_descriptor = this.make_root_descriptor();
		} else {
			// a child
			parent = parent.facade();
			path = parent.getPath() + '/' + name;
		}
		vfs = vfs.facade();

		this._vfs = vfs;
		this._parent = parent;
		this._facade = new VFileFacade(this);
		this._name = name;
		this._path = path;

		this._node = null;
		this._node_chs = null; // the children
		this._node_nil = null;

	}

	mc.class(function(cc) {
		cc.type(VFileImpl);
		cc.extends(VFile);
	});

	VFileImpl.prototype = {

		facade : function() {
			return this._facade;
		},

		load : function(fn) {
			if (fn == null) {
				fn = function() {
				};
			}
			var self = this;
			var context = this._vfs.context();
			var loader = new MyDataLoader(context);
			loader.load(this.facade(), function() {
				self.inner_onload(loader);
				fn();
			});
		},

		make_root_descriptor : function() {
			var vpt = new Viewport();
			var desc = new VFileDescriptor();
			desc.owner(vpt.ownerUid());
			desc.repository(vpt.repositoryName());
			desc.type('file');
			desc.id('working');
			desc.base('/');
			desc.offset('~');
			return desc;
		},

		inner_onload : function(loader) {
			var model = loader.model();
			var dir = model.vfile;
			var list = dir.list;
			var table = {};
			if (list != null) {
				for ( var i in list) {
					var ch = list[i];
					var name = ch.f_name();
					table[name] = ch;
				}
			}
			this._node = dir;
			this._node_chs = table;

			// this.xxx();
		},

		node_for_child : function(name) {
			var table = this._node_chs;
			if (table == null) {
				return null;
			} else {
				return table[name];
			}
		},

		node : function() {

			// get from self
			var node = this._node;
			if (node != null) {
				return node;
			}

			// get from parent
			var parent = this._inner_parent;
			if (parent != null) {
				var name = this._name;
				node = parent.node_for_child(name);
				if (node != null) {
					return node;
				}
			}

			// make a empty node
			node = this._node_nil;
			if (node == null) {
				node = new Object();
				this._node_nil = node;
			}

			return node;
		},

		inner_vfs : function() {
			return this._inner_vfs;
		},

		vfs : function() {
			return this._vfs;
		},

		child : function(name) {
			var vfs = this._inner_vfs;
			var parent = this;
			var ch = new VFileImpl(vfs, parent, name);
			return ch.facade();
		},

		// next: like java.io.File

		canExecute : function() {
			throw new Exception('implements in sub-class');
		},

		canRead : function() {
			throw new Exception('implements in sub-class');
		},

		canWrite : function() {
			throw new Exception('implements in sub-class');
		},

		compareTo : function(pathname) {
			throw new Exception('implements in sub-class');
		},

		createNewFile : function() {
			throw new Exception('implements in sub-class');
		},

		del : function() {
			throw new Exception('implements in sub-class');
		},

		equals : function(obj) {
			throw new Exception('implements in sub-class');
		},

		exists : function() {
			return this.node().f_exists();
		},

		getAbsoluteFile : function() {
			throw new Exception('implements in sub-class');
		},

		getAbsolutePath : function() {
			throw new Exception('implements in sub-class');
		},

		getCanonicalFile : function() {
			throw new Exception('implements in sub-class');
		},

		getCanonicalPath : function() {
			throw new Exception('implements in sub-class');
		},

		getFreeSpace : function() {
			throw new Exception('implements in sub-class');
		},

		getName : function() {
			return this._name;
		},

		getParent : function() {
			var file = this.getParentFile();
			if (file == null) {
				return null;
			} else {
				return file.getPath();
			}
		},

		getParentFile : function() {
			return this._parent;
		},

		getPath : function() {
			return this._path;
		},

		getTotalSpace : function() {
			throw new Exception('implements in sub-class');
		},

		getUsableSpace : function() {
			throw new Exception('implements in sub-class');
		},

		hashCode : function() {
			throw new Exception('implements in sub-class');
		},

		isAbsolute : function() {
			throw new Exception('implements in sub-class');
		},

		isDirectory : function() {
			return this.node().f_directory();
		},

		isFile : function() {
			throw new Exception('implements in sub-class');
		},

		isHidden : function() {
			throw new Exception('implements in sub-class');
		},

		lastModified : function() {
			return this.node().f_lastModified();
		},

		length : function() {
			return this.node().f_length();
		},

		list : function() {
			// To List<String>
			var node = this.node();
			var list = node.list;
			if (list == null) {
				list = [];
			}
			var list2 = [];
			for ( var i in list) {
				var name = list[i].f_name();
				list2.push(name);
			}
			return list2;
		},

		// list : function(filter) {
		// throw new Exception('implements in sub-class');
		// },

		listFiles : function() {

			// To List<VFile>
			var node = this.node();
			var list = node.list;
			if (list == null) {
				list = [];
			}
			var list2 = [];
			for ( var i in list) {
				var name = list[i].f_name();
				var ch = this.child(name);
				list2.push(ch);
			}
			return list2;
		},

		// listFiles : function(filter) {
		// throw new Exception('implements in sub-class');
		// },

		// listFiles : function(filter) {
		// throw new Exception('implements in sub-class');
		// },

		mkdir : function() {
			throw new Exception('implements in sub-class');
		},

		mkdirs : function() {
			throw new Exception('implements in sub-class');
		},

		renameTo : function(dest) {
			throw new Exception('implements in sub-class');
		},

		setExecutable : function(executable) {
			throw new Exception('implements in sub-class');
		},

		setExecutable : function(executable, ownerOnly) {
			throw new Exception('implements in sub-class');
		},

		setLastModified : function(time) {
			throw new Exception('implements in sub-class');
		},

		setReadable : function(readable) {
			throw new Exception('implements in sub-class');
		},

		setReadable : function(readable, ownerOnly) {
			throw new Exception('implements in sub-class');
		},

		setReadOnly : function() {
			throw new Exception('implements in sub-class');
		},

		setWritable : function(writable) {
			throw new Exception('implements in sub-class');
		},

		setWritable : function(writable, ownerOnly) {
			throw new Exception('implements in sub-class');
		},

		toString : function() {
			return '[file ' + this.getPath() + ']';
		},

		toURI : function() {
			throw new Exception('implements in sub-class');
		},

		toHttpURI : function() {
			throw new Exception('implements in sub-class');
		},

		toFileURI : function() {
			throw new Exception('implements in sub-class');
		},

		toDescriptor : function() {
			var parent = this.getParentFile();
			if (parent == null) {
				// this is the root
				// make copy for root descriptor
				var root_desc = this._root_descriptor;
				return new VFileDescriptor(root_desc);
			} else {
				// this is a child
				var vfs = this.vfs();
				var root = vfs.root();
				var desc = root.toDescriptor();
				var path = this.getPath();
				desc.offset(path);
				return desc;
			}
		},

	};

	/***************************************************************************
	 * class VFSImpl
	 */

	function VFSImpl(context) {

		this._context = context;
		this._facade = new VFSFacade(this);

		this._root = VFSImpl_make_root(this);

	}

	mc.class(function(cc) {
		cc.type(VFSImpl);
		cc.extends(VFS);
	});

	function VFSImpl_make_root(vfs_impl) {
		var file = new VFileImpl(vfs_impl);
		return file.facade();
	}

	VFSImpl.prototype = {

		facade : function() {
			return this._facade;
		},

		newFile : function(path) {
			// return this.inner.newFile(uri);
			throw new Exception('no impl');
		},

		context : function() {
			return this._context;
		},

		root : function() {
			return this._root;
		},

	};

});

this.snowflake.VFSFactory = com.boluozhai.snowflake.vfs.VFSFactory;

/*******************************************************************************
 * EOF
 */
