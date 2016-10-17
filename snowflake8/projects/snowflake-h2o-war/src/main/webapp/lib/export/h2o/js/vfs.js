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

	/***************************************************************************
	 * class VFS
	 */

	function VFS() {
	}

	mc.class(function(cc) {
		cc.type(VFS);
	});

	VFS.prototype = {

		newFile : function(uri) {
			throw new Exception('implements in sub-class');
		},

		context : function() {
			throw new Exception('implements in sub-class');
		},

		root : function() {
			throw new Exception('implements in sub-class');
		},

	};

	VFS.newInstance = function(context) {
		var builder = VFS.newBuilder(context);
		return builder.create(context);
	};

	VFS.newBuilder = function(context) {
		var key = VFSFactory.class.getName();
		var factory = context.getBean(key);
		return factory.newBuilder(context);
	};

	/***************************************************************************
	 * class VFSFactory
	 */

	function VFSFactory() {
	}

	mc.class(function(cc) {
		cc.type(VFSFactory);
		// cc.extends(Attributes);
	});

	VFSFactory.prototype = {

		newBuilder : function(context) {
			return new VFSBuilder(context);
		},

	};

	/***************************************************************************
	 * class VFSBuilder
	 */

	function VFSBuilder(context) {
		this.context(context);
	}

	mc.class(function(cc) {
		cc.type(VFSBuilder);
		cc.extends(Attributes);
	});

	VFSBuilder.prototype = {

		create : function() {
			throw new Exception('implements in sub-class');
		},

		context : function(context) {
			return this.attr('context', context);
		},

	};

	/***************************************************************************
	 * class VFile
	 */

	function VFile() {
	}

	mc.class(function(cc) {
		cc.type(VFile);
	});

	VFile.prototype = {

		load : function(fn) {
			throw new Exception('implements in sub-class');
		},

		vfs : function() {
			throw new Exception('implements in sub-class');
		},

		child : function(name) {
			throw new Exception('implements in sub-class');
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
			throw new Exception('implements in sub-class');
		},

		getParent : function() {
			throw new Exception('implements in sub-class');
		},

		getParentFile : function() {
			throw new Exception('implements in sub-class');
		},

		getPath : function() {
			throw new Exception('implements in sub-class');
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
	 * class CurrentLocation
	 */

	function CurrentLocation(context) {
		this._context = context;
	}

	mc.class(function(cc) {
		cc.type(CurrentLocation);
		cc.extends(EventDispatcher);
	});

	CurrentLocation.prototype = {

		location : function(file) {
			if (file == null) {
				file = this._location;
			} else {
				var self = this;
				this._location = file;
				file.load(function() {
					CurrentLocation_onload(self, file);
				});
			}
			return file;
		},

	};

	function CurrentLocation_onload(cl, file) {

		if (file.exists()) {
			var e = new Event();
			cl.dispatchEvent(e);
			return;
		}

		var p = file;
		for (; p != null; p = p.getParentFile()) {
			if (p.exists()) {
				break;
			} else {
				continue;
			}
		}

		if (p == null) {
			return;
		} else {
			cl.location(p);
		}

	}

	/***************************************************************************
	 * class VFSFacade
	 */

	function VFSFacade(inner) {
		this.inner = inner;
	}

	mc.class(function(cc) {
		cc.type(VFSFacade);
	});

	VFSFacade.prototype = {

		newFile : function(uri) {
			return this.inner.newFile(uri);
		},

		context : function() {
			return this.inner.context();
		},

		root : function() {
			return this.inner.root();
		},

	};

	/***************************************************************************
	 * class VFileFacade
	 */

	function VFileFacade(inner) {
		this.inner = inner;
	}

	mc.class(function(cc) {
		cc.type(VFileFacade);
	});

	VFileFacade.prototype = {

		load : function(fn) {
			return this.inner.load(fn);
		},

		vfs : function() {
			return this.inner.vfs();
		},

		child : function(name) {
			return this.inner.child(name);
		},

		// next: like java.io.File

		canExecute : function() {
			return this.inner.canExecute();
		},

		canRead : function() {
			return this.inner.canRead();
		},

		canWrite : function() {
			return this.inner.canWrite();
		},

		compareTo : function(pathname) {
			return this.inner.compareTo(pathname);
		},

		createNewFile : function() {
			return this.inner.createNewFile();
		},

		del : function() {
			return this.inner.del();
		},

		equals : function(obj) {
			return this.inner.equals(obj);
		},

		exists : function() {
			return this.inner.exists();
		},

		getAbsoluteFile : function() {
			return this.inner.getAbsoluteFile();
		},

		getAbsolutePath : function() {
			return this.inner.getAbsolutePath();
		},

		getCanonicalFile : function() {
			return this.inner.getCanonicalFile();
		},

		getCanonicalPath : function() {
			return this.inner.getCanonicalPath();
		},

		getFreeSpace : function() {
			return this.inner.getFreeSpace();
		},

		getName : function() {
			return this.inner.getName();
		},

		getParent : function() {
			return this.inner.getParent();
		},

		getParentFile : function() {
			return this.inner.getParentFile();
		},

		getPath : function() {
			return this.inner.getPath();
		},

		getTotalSpace : function() {
			return this.inner.getTotalSpace();
		},

		getUsableSpace : function() {
			return this.inner.getUsableSpace();
		},

		hashCode : function() {
			return this.inner.hashCode();
		},

		isAbsolute : function() {
			return this.inner.isAbsolute();
		},

		isDirectory : function() {
			return this.inner.isDirectory();
		},

		isFile : function() {
			return this.inner.isFile();
		},

		isHidden : function() {
			return this.inner.isHidden();
		},

		lastModified : function() {
			return this.inner.lastModified();
		},

		length : function() {
			return this.inner.length();
		},

		list : function() {
			return this.inner.list();
		},

		list : function(filter) {
			return this.inner.list(filter);
		},

		listFiles : function() {
			return this.inner.listFiles();
		},

		listFiles : function(filter) {
			return this.inner.listFiles(filter);
		},

		listFiles : function(filter) {
			return this.inner.listFiles(filter);
		},

		mkdir : function() {
			return this.inner.mkdir();
		},

		mkdirs : function() {
			return this.inner.mkdirs();
		},

		renameTo : function(dest) {
			return this.inner.renameTo(dest);
		},

		setExecutable : function(executable) {
			return this.inner.xxxxxxxxxx();
		},

		setExecutable : function(executable, ownerOnly) {
			return this.inner.xxxxxxxxxx();
		},

		setLastModified : function(time) {
			return this.inner.xxxxxxxxxx();
		},

		setReadable : function(readable) {
			return this.inner.xxxxxxxxxx();
		},

		setReadable : function(readable, ownerOnly) {
			return this.inner.xxxxxxxxxx();
		},

		setReadOnly : function() {
			return this.inner.xxxxxxxxxx();
		},

		setWritable : function(writable) {
			return this.inner.xxxxxxxxxx();
		},

		setWritable : function(writable, ownerOnly) {
			return this.inner.xxxxxxxxxx();
		},

		toString : function() {
			return this.inner.toString();
		},

		toURI : function() {
			return this.inner.toURI();
		},

		toHttpURI : function() {
			return this.inner.toHttpURI();
		},

		toFileURI : function() {
			return this.inner.toFileURI();
		},

	};

});

JS.module(function(mc) {

	mc.package('snowflake.vfs');

	function PackageInfo() {
	}

	mc.class(function(cc) {
		cc.type(PackageInfo);
	});

	PackageInfo.prototype = {};

});

// this.snowflake.VFSFactory = com.boluozhai.snowflake.vfs.VFSFactory;

this.snowflake.vfs.VFSFactory = com.boluozhai.snowflake.vfs.VFSFactory;
this.snowflake.vfs.VFSBuilder = com.boluozhai.snowflake.vfs.VFSBuilder;

this.snowflake.vfs.VFS = com.boluozhai.snowflake.vfs.VFS;
this.snowflake.vfs.VFSFacade = com.boluozhai.snowflake.vfs.VFSFacade;

this.snowflake.vfs.VFile = com.boluozhai.snowflake.vfs.VFile;
this.snowflake.vfs.VFileFacade = com.boluozhai.snowflake.vfs.VFileFacade;

/*******************************************************************************
 * EOF
 */
