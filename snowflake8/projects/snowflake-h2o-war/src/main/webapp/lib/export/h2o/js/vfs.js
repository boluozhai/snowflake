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

	function VFS(inner) {
		this._inner = inner;
	}

	mc.class(function(cc) {
		cc.type(VFS);
		// cc.extends(Attributes);
	});

	VFS.prototype = {

		newFile : function(uri) {
			return this._inner.newFile(uri);
		},

		context : function() {
			return this._inner.context();
		},

		ready : function(fn /* () */) {
			var ready = this._inner.isReady();
			if (ready) {
				if (fn != null) {
					fn();
				}
			} else {
				this.root().load(fn);
			}
			return ready;
		},

		root : function() {
			return this._inner.root();
		},

	};

	VFS.getInstance = function(context) {
		var key = VFS.class.getName();
		return context.getBean(key);
	};

	/***************************************************************************
	 * class VFSCore
	 */

	function InnerVFS(context, builder) {
		this._context = context;
		this._http_uri = builder.httpURI();
		this._file_uri = builder.fileURI();
	}

	InnerVFS.prototype = {

		init : function() {

		},

		context : function() {
			return this._context;
		},

		isReady : function() {
			var root = this.root();
			var file_uri = root.toFileURI();
			return (file_uri != null);
		},

		root : function() {
			return this.inner_root().file();
		},

		inner_root : function() {
			var r = this._in_root;
			if (r == null) {
				r = new InnerFile(this, null, null);
				this._in_root = r;
			}
			return r;
		},

		vfs : function() {
			var vfs = this._vfs;
			if (vfs == null) {
				vfs = new VFS(this);
				this._vfs = vfs;
			}
			return vfs;
		},

		httpURI : function() {
			return this._http_uri;
		},

		fileURI : function() {
		},

		newFile : function(uri) {

			if (uri == null) {
				throw new RuntimeException('unsupported');
			} else if (uri.indexOf('file:') == 0) {

				var prefix = this.fileURI();
				if (uri.indexOf(prefix) == 0) {
					var base = this.root();
					var offset = uri.substring(base.length);
					return base.child(offset);
				} else {
					throw new RuntimeException('bad uri : ' + uri);
				}

			} else if (uri.indexOf('http:') == 0) {
				throw new RuntimeException('unsupported');
			} else if (uri.indexOf('https:') == 0) {
				throw new RuntimeException('unsupported');
			} else {
				throw new RuntimeException('unsupported');
			}

		},

	};

	/***************************************************************************
	 * class VFSFactory
	 */

	function VFSFactory() {
	}

	mc.class(function(cc) {
		cc.type(VFSFactory);
		cc.extends(Attributes);
	});

	VFSFactory.prototype = {

		create : function(context) {
			var inner_vfs = new InnerVFS(context, this);
			return inner_vfs.vfs();
		},

		httpURI : function(value) {
			return this.attr('http_uri', value);
		},

		fileURI : function(value) {
			return this.attr('file_uri', value);
		},

	};

	/***************************************************************************
	 * class VFile
	 */

	function VFile(inner) {
		this._inner = inner;
	}

	mc.class(function(cc) {
		cc.type(VFile);
	});

	VFile.prototype = {

		load : function(fn) {
			return this._inner.load(fn);
		},

		vfs : function() {
			return this._inner.vfs();
		},

		child : function(name) {
			return this._inner.child(name);
		},

		// next: like java.io.File

		canExecute : function() {
			throw new RuntimeException('no impl');
		},

		canRead : function() {
			throw new RuntimeException('no impl');
		},

		canWrite : function() {
			throw new RuntimeException('no impl');
		},

		compareTo : function(pathname) {
			throw new RuntimeException('no impl');
		},

		createNewFile : function() {
			throw new RuntimeException('no impl');
		},

		del : function() {
			throw new RuntimeException('no impl');
		},

		equals : function(obj) {
			throw new RuntimeException('no impl');
		},

		exists : function() {
			return this._inner.exists();
		},

		getAbsoluteFile : function() {
			throw new RuntimeException('no impl');
		},

		getAbsolutePath : function() {
			throw new RuntimeException('no impl');
		},

		getCanonicalFile : function() {
			throw new RuntimeException('no impl');
		},

		getCanonicalPath : function() {
			throw new RuntimeException('no impl');
		},

		getFreeSpace : function() {
			throw new RuntimeException('no impl');
		},

		getName : function() {
			return this._inner.name();
		},

		getParent : function() {
			return this._inner.parent_path();
		},

		getParentFile : function() {
			return this._inner.parent_file();
		},

		getPath : function() {
			throw new RuntimeException('no impl');
		},

		getTotalSpace : function() {
			throw new RuntimeException('no impl');
		},

		getUsableSpace : function() {
			throw new RuntimeException('no impl');
		},

		hashCode : function() {
			throw new RuntimeException('no impl');
		},

		isAbsolute : function() {
			return this._inner.is_absolute();
		},

		isDirectory : function() {
			return this._inner.is_directory();
		},

		isFile : function() {
			return this._inner.is_file();
		},

		isHidden : function() {
			return this._inner.is_hidden();
		},

		lastModified : function() {
			return this._inner.last_modified();
		},

		length : function() {
			return this._inner.length();
		},

		list : function() {
			return this._inner.list();
		},

		list : function(filter) {
			return this._inner.list(filter);
		},

		listFiles : function() {
			throw new RuntimeException('no impl');
		},

		listFiles : function(filter) {
			throw new RuntimeException('no impl');
		},

		listFiles : function(filter) {
			throw new RuntimeException('no impl');
		},

		mkdir : function() {
			throw new RuntimeException('no impl');
		},

		mkdirs : function() {
			throw new RuntimeException('no impl');
		},

		renameTo : function(dest) {
			throw new RuntimeException('no impl');
		},

		setExecutable : function(executable) {
			throw new RuntimeException('no impl');
		},

		setExecutable : function(executable, ownerOnly) {
			throw new RuntimeException('no impl');
		},

		setLastModified : function(time) {
			throw new RuntimeException('no impl');
		},

		setReadable : function(readable) {
			throw new RuntimeException('no impl');
		},

		setReadable : function(readable, ownerOnly) {
			throw new RuntimeException('no impl');
		},

		setReadOnly : function() {
			throw new RuntimeException('no impl');
		},

		setWritable : function(writable) {
			throw new RuntimeException('no impl');
		},

		setWritable : function(writable, ownerOnly) {
			throw new RuntimeException('no impl');
		},

		toString : function() {
			return this._inner.fileURI();
		},

		toURI : function() {
			return this._inner.toURI();
		},

		toHttpURI : function() {
			return this._inner.httpURI();
		},

		toFileURI : function() {
			return this._inner.fileURI();
		},

	};

	/***************************************************************************
	 * class InnerFile
	 */

	function InnerFile(in_vfs, in_parent, name, vfile) {

		if (vfile == null) {
			vfile = {};
		}

		this._in_vfs = in_vfs;
		this._in_parent = in_parent;
		this._vfs = in_vfs.vfs();
		this.vfile = vfile;

		if (in_parent == null) {
			// the root
			var base_http_uri = in_vfs.httpURI();
			this._http_uri = base_http_uri;
			this._name = '{root}';
		} else {
			// a child
			var base_http_uri = in_parent.httpURI();
			this._parent = in_parent.file();
			this._http_uri = InnerFile.mix_url(base_http_uri, name);
			this._name = name;
		}

	}

	InnerFile.mix_url = function(base, offset) {
		if (offset == null) {
			return base;
		}
		if (base == null) {
			return offset;
		}
		if (base.lastIndexOf('/') == base.length - 1) {
			return (base + offset);
		} else if (offset.indexOf('/') == 0) {
			return (base + offset);
		} else {
			return (base + '/' + offset);
		}
	};

	InnerFile.prototype = {

		child_simple : function(name) {
			return this.inner_child_simple(name).file();
		},

		inner_child_simple : function(name) {

			var init_vfile = null;
			var child = this.vfile.child;
			if (child != null) {
				init_vfile = child[name];
			}

			var in_vfs = this._in_vfs;
			var in_parent = this;
			return new InnerFile(in_vfs, in_parent, name, init_vfile);
		},

		child_array : function(array) {
			var p = this;
			for ( var index in array) {
				var name = array[index];
				p = p.inner_child_simple(name);
			}
			return p.file();
		},

		child : function(path) {

			var in_base = null;
			var array = null;

			if (path.indexOf('file:') == 0) {
				// to absolute
				var prefix = this._vfs.root().toFileURI();
				if (path.indexOf(prefix) == 0) {
					in_base = this._in_vfs.inner_root();
					path = path.substring(prefix.length);
				} else {
					throw new RuntimeException('bad location: ' + path);
				}

			} else if (path.indexOf('/') == 0) {
				// a absolute
				in_base = this._in_vfs.inner_root();

			} else {
				in_base = this;
			}

			array = [];
			var a2 = path.split('/');
			for ( var i in a2) {
				var name = a2[i];
				if (name == null) {
					continue;
				} else {
					name = name.trim();
				}
				if (name.length == 0) {
					continue;
				} else {
					array.push(name);
				}
			}

			return in_base.child_array(array);

		},

		exists : function() {
			return this.vfile.exists;
		},

		file : function() {
			var file = this._file;
			if (file == null) {
				file = new VFile(this);
				this._file = file;
			}
			return file;
		},

		is_directory : function() {
			return this.vfile.directory;
		},

		parent_path : function() {
			var parent = this._parent;
			if (parent == null) {
				return null;
			} else {
				return parent.getAbsolutePath();
			}
		},

		parent_file : function() {
			return this._parent;
		},

		last_modified : function() {
			return this.vfile.lastModified;
		},

		length : function() {
			return this.vfile.length;
		},

		list : function(filter) {
			if (filter == null) {
				filter = function() {
					return true;
				};
			}
			var list = [];
			var src = this.vfile.child;
			for ( var name in src) {
				if (filter(name)) {
					list.push(name);
				}
			}
			return list;
		},

		name : function() {
			return this._name;
		},

		fileURI : function() {
			return this._file_uri;
		},

		httpURI : function() {
			return this._http_uri;
		},

		load : function(fn) {

			if (fn == null) {
				fn = function() {
				};
			}

			var self = this;
			var url = this._http_uri;
			var context = this._vfs.context();
			url = context.normalizeURL(url);

			CurrentLocationLoader_load(url, function(js) {
				self.onload(js);
				fn();
			});

		},

		onload : function(js) {

			this.vfile = js.vfile;
			this._file_uri = js.vfile.fileURI;

			var list = js.vfile.list;
			var map = {};
			for ( var i in list) {
				var item = list[i];
				var key = item.name;
				map[key] = item;
			}
			js.vfile.child = map;

			var http_uri = this._http_uri;
			var ht1 = (http_uri.indexOf('http:') < 0);
			var ht2 = (http_uri.indexOf('https:') < 0);
			if (ht1 && ht2) {
				this._http_uri = js.vfile.httpURI;
			}

			// System.out.println('onload(vfs.js,560) : ' + this._file_uri);

		},

		vfs : function() {
			return this._vfs;
		},

		inner_vfs : function() {
			return this._in_vfs;
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
				this.load(file);
				this._location = file;
			}
			return file;
		},

		load : function(file) {

			var self = this;

			file.load(function() {
				// onload

				CurrentLocation_onload(self, file);

			});

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
	 * class CurrentLocationLoader
	 */

	function CurrentLocationLoader_load(url, fn /* (js) */) {

		var request = {};

		var xhr = new XMLHttpRequest();
		xhr.open('GET', url, true);
		xhr.onreadystatechange = function() {

			var state = xhr.readyState;
			if (state != 4) {
				return;
			}
			var code = xhr.status;
			if (code != 200) {
				return;
			}

			var response = JSON.parse(xhr.responseText);
			response.vfile.httpURI = xhr.responseURL;
			fn(response);

		};
		xhr.send(JSON.stringify(request));
	}

});

/*******************************************************************************
 * EOF
 */
