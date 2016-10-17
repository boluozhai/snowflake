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
	 * class RestVfsDataLoader
	 */

	function RestVfsDataLoader(context) {
		this._context = context;
	}

	RestVfsDataLoader.prototype = {

		toOffsetPath : function(file) {
			var array = [];
			var p = file;
			for (; p != null; p = p.getParentFile()) {
				var name = p.getName();
				if (name == null) {
					continue;
				} else {
					array.push[name];
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

			var context = this._context;
			var vpt = new Viewport();
			var jrr = new JSONRestRequest(context);

			var uid = vpt.ownerUid();
			var repo = vpt.repositoryName();
			var offset_path = this.toOffsetPath(file);

			jrr.open('GET', {
				uid : uid,
				repo : repo,
				api : 'repo-api',
				type : 'file',
				id : 'a-vfile-id',
			});

			jrr.setParameters({
				type : 'repository',
				id : 'working',
				base : '/',
				offset : offset_path,
			});

			jrr.onResult(function() {
				if (jrr.ok()) {
					fn();
				} else {
				}
			});

			jrr.send();

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
			var loader = this._inner_vfs.dataLoader();
			loader.load(this, function() {
				fn();
			});
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
			throw new Exception('implements in sub-class');
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
			throw new Exception('implements in sub-class');
		},

		isFile : function() {
			throw new Exception('implements in sub-class');
		},

		isHidden : function() {
			throw new Exception('implements in sub-class');
		},

		lastModified : function() {
			throw new Exception('implements in sub-class');
		},

		length : function() {
			throw new Exception('implements in sub-class');
		},

		list : function() {
			throw new Exception('implements in sub-class');
		},

		list : function(filter) {
			throw new Exception('implements in sub-class');
		},

		listFiles : function() {
			throw new Exception('implements in sub-class');
		},

		listFiles : function(filter) {
			throw new Exception('implements in sub-class');
		},

		listFiles : function(filter) {
			throw new Exception('implements in sub-class');
		},

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
			throw new Exception('implements in sub-class');
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

	};

	/***************************************************************************
	 * class VFSImpl
	 */

	function VFSImpl(context) {

		this._context = context;
		this._facade = new VFSFacade(this);

		this._root = VFSImpl_make_root(this);
		this._data_loader = new RestVfsDataLoader(context);

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

		dataLoader : function() {
			return this._data_loader;
		},

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
