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
	 * 
	 * @insert : post | mkdir(fn)
	 * @delete : delete| delete(fn)
	 * @update : put | renameTo(dst,fn)
	 * @get : get | load(fn)
	 * 
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

		type : function() {
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

		del : function(fn) {
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

		mkdir : function(fn) {
			throw new Exception('implements in sub-class');
		},

		mkdirs : function() {
			throw new Exception('implements in sub-class');
		},

		renameTo : function(dest, fn) {
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

		toDescriptor : function() {
			throw new Exception('implements in sub-class');
		},

	};

	/***************************************************************************
	 * class CurrentLocationEvent
	 */

	function CurrentLocationEvent() {
	}

	mc.class(function(cc) {
		cc.type(CurrentLocationEvent);
		cc.extends(Event);
	});

	CurrentLocationEvent.prototype = {

		location : function(file) {
			return this.attr('location', file);
		},

	};

	CurrentLocationEvent.ON_LOAD = "ON_LOAD";
	CurrentLocationEvent.ON_OPEN = "ON_OPEN";
	CurrentLocationEvent.ON_LOCATE = "ON_LOCATE";
	CurrentLocationEvent.ON_SELECT = "ON_SELECT";

	/***************************************************************************
	 * class CurrentLocationEventHandler
	 */

	function CurrentLocationEventHandler(cl) {
		this.inner = new Event();
		cl.addEventHandler(this);
	}

	CurrentLocationEventHandler.prototype = {

		onEvent : function(e) {

			var cl = e.source();
			var code = e.code();
			var node = e.location();

			if (code == CurrentLocationEvent.ON_OPEN) {
				if (node.isDirectory()) {
					cl.location(node);
				} else {
					// alert('open ' + node);
				}
			}

		},

		hashCode : function() {
			return this.inner.hashCode();
		},

	};

	/***************************************************************************
	 * class CurrentLocation
	 */

	function CurrentLocation(context) {
		this.inner = new CurrentLocationImpl(context, this);
		this._default_event_handler = new CurrentLocationEventHandler(this);
	}

	mc.class(function(cc) {
		cc.type(CurrentLocation);
		cc.extends(EventDispatcher);
	});

	CurrentLocation.prototype = {

		open : function(file) {
			if (file == null) {
				// NOP
			} else {
				return this.inner.open(file);
			}
		},

		location : function(file) {
			if (file == null) {
				return this.inner.getLocation();
			} else {
				return this.inner.setLocation(file);
			}
		},

		selection : function(file) {
			if (file == null) {
				return this.inner.getSelection();
			} else {
				return this.inner.setSelection(file);
			}
		},

		selections : function(file_list) {
			if (file == null) {
				return this.inner.getSelections();
			} else {
				return this.inner.setSelections(file_list);
			}
		},

	};

	/***************************************************************************
	 * class CurrentLocationImpl
	 */

	function CurrentLocationImpl(context, facade) {
		this._facade = facade;
		this._context = context;
	}

	CurrentLocationImpl.prototype = {

		getLocation : function() {
			return this._location;
		},

		getSelection : function() {
			return this._selection;
		},

		getSelections : function() {
			var rlt = this._selections;
			if (rlt == null) {
				rlt = {};
				this._selections = rlt;
			}
			return rlt;
		},

		setSelection : function(sel) {

			if (this.is_forbidden(sel)) {
				return this.alert_forbidden(sel);
			}

			this._selection = sel;
			this._selections = null;
			this.fireOnSelect(sel);
		},

		setSelections : function(sel) {
			this._selection = null;
			this._selections = sel;
			this.fireOnSelect();
		},

		open : function(file) {

			if (this.is_forbidden(file)) {
				return this.alert_forbidden(file);
			}

			this.fireOnOpen(file);
		},

		setLocation : function(location) {

			if (this.is_forbidden(location)) {
				return this.alert_forbidden(location);
			}

			if (location.isDirectory()) {
				this._location = location;
				this._selection = null;
				this._selections = null;
				this.load(location);
				this.fireOnLocate(location);
			}

		},

		alert_forbidden : function(file) {
			var context = this._context;
			var i18n = context.getBean('i18n');
			alert(i18n.getString('forbidden'));
			return 0;
		},

		is_forbidden : function(file) {
			for (var p = file; p != null; p = p.getParentFile()) {
				var name = p.getName();
				if (name == null) {
					continue;
				} else if (name.indexOf('.') == 0) {
					return true;
				} else {
					continue;
				}
			}
			return false;
		},

		load : function(file) {
			var self = this;
			file.load(function() {
				self.onload(file);
			});
		},

		onload : function(file) {
			if (file.exists()) {
				this.fireOnLoad(file);
				return;
			}
			// search for directory exists
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
				this.setLocation(p);
			}
		},

		fireOnEvent : function(file, type) {

			// TODO

			var facade = this._facade;
			var event = new CurrentLocationEvent();

			event.code(type);
			event.message(type);
			event.source(facade);
			event.location(file);

			facade.dispatchEvent(event);

		},

		fireOnLoad : function(file) {
			this.fireOnEvent(file, 'ON_LOAD');
		},

		fireOnOpen : function(file) {
			this.fireOnEvent(file, 'ON_OPEN');
		},

		fireOnLocate : function(file) {
			this.fireOnEvent(file, 'ON_LOCATE');
		},

		fireOnSelect : function(file) {
			this.fireOnEvent(file, 'ON_SELECT');
		},

	};

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
	 * class VFileDescriptor
	 */

	function VFileDescriptor(init) {

		if (init != null) {
			this.owner(init.owner());
			this.repository(init.repository());
			this.type(init.type());
			this.id(init.id());
			this.base(init.base());
			this.offset(init.offset());
		}

	}

	mc.class(function(cc) {
		cc.type(VFileDescriptor);
		cc.extends(Attributes);
	});

	VFileDescriptor.prototype = {

		owner : function(v) {
			return this.attr('owner', v);
		},

		repository : function(v) {
			return this.attr('repository', v);
		},

		type : function(v) {
			return this.attr('type', v);
		},

		id : function(v) {
			return this.attr('id', v);
		},

		base : function(v) {
			return this.attr('base', v);
		},

		offset : function(v) {
			return this.attr('offset', v);
		},

		createURL : function(base_url, query) {
			query = this.createQuery(query);
			var iq = base_url.indexOf('?');
			if (iq < 0) {
				// NOP
			} else {
				base_url = base_url.substring(0, iq);
			}
			var qstr = null;
			for ( var key in query) {
				var value = query[key];
				value = encodeURIComponent(value);
				if (qstr == null) {
					qstr = '?';
				} else {
					qstr += '&';
				}
				qstr += key;
				qstr += '=';
				qstr += value;
			}
			if (qstr == null) {
				return base_url;
			} else {
				return base_url + qstr;
			}
		},

		createQuery : function(query) {
			if (query == null) {
				query = {};
			}
			query.owner = this.owner();
			query.repository = this.repository();
			query.type = this.type();
			query.id = this.id();
			query.base = this.base();
			query.offset = this.offset();
			return query;
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

		type : function() {
			return this.inner.type();
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

		del : function(fn) {
			return this.inner.del(fn);
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

		mkdir : function(fn) {
			return this.inner.mkdir(fn);
		},

		mkdirs : function() {
			return this.inner.mkdirs();
		},

		renameTo : function(dest, fn) {
			return this.inner.renameTo(dest, fn);
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

		toDescriptor : function() {
			return this.inner.toDescriptor();
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

this.snowflake.CurrentLocationEvent = com.boluozhai.snowflake.vfs.CurrentLocationEvent;

this.snowflake.vfs.VFSFactory = com.boluozhai.snowflake.vfs.VFSFactory;
this.snowflake.vfs.VFSBuilder = com.boluozhai.snowflake.vfs.VFSBuilder;
this.snowflake.vfs.VFileDescriptor = com.boluozhai.snowflake.vfs.VFileDescriptor;

this.snowflake.vfs.VFS = com.boluozhai.snowflake.vfs.VFS;
this.snowflake.vfs.VFSFacade = com.boluozhai.snowflake.vfs.VFSFacade;

this.snowflake.vfs.VFile = com.boluozhai.snowflake.vfs.VFile;
this.snowflake.vfs.VFileFacade = com.boluozhai.snowflake.vfs.VFileFacade;

/*******************************************************************************
 * EOF
 */
