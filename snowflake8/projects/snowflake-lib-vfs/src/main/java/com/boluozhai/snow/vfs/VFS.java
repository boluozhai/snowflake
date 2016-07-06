package com.boluozhai.snow.vfs;

import java.net.URI;

import com.boluozhai.snow.context.SnowContext;
import com.boluozhai.snow.mvc.ModelContext;

public interface VFS {

	public static class Factory {

		public static VFS getVFS(SnowContext context) {
			String name = VFSFactory.class.getName();
			VFSFactory factory = context.getBean(name, VFSFactory.class);
			return factory.getVFS(context);
		}

	}

	ModelContext context();

	// factory

	VFile newFile(String path);

	VFile newFile(String dir, String name);

	VFile newFile(VFile dir, String name);

	VFile newFile(URI uri);

	// static

	VFile[] listRoots();

}
