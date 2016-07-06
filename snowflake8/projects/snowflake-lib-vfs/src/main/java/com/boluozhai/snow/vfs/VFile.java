package com.boluozhai.snow.vfs;

import java.io.IOException;
import java.net.URI;

public interface VFile {

	VPath toPath();

	VFS vfs();

	VFile child(String name);

	// next: like java.io.File

	boolean canExecute();

	boolean canRead();

	boolean canWrite();

	int compareTo(VFile pathname);

	boolean createNewFile() throws IOException;

	boolean delete();

	void deleteOnExit();

	boolean equals(Object obj);

	boolean exists();

	VFile getAbsoluteFile();

	String getAbsolutePath();

	VFile getCanonicalFile() throws IOException;

	String getCanonicalPath() throws IOException;

	long getFreeSpace();

	String getName();

	String getParent();

	VFile getParentFile();

	String getPath();

	long getTotalSpace();

	long getUsableSpace();

	int hashCode();

	boolean isAbsolute();

	boolean isDirectory();

	boolean isFile();

	boolean isHidden();

	long lastModified();

	long length();

	String[] list();

	String[] list(VFilenameFilter filter);

	VFile[] listFiles();

	VFile[] listFiles(VFileFilter filter);

	VFile[] listFiles(VFilenameFilter filter);

	boolean mkdir();

	boolean mkdirs();

	boolean renameTo(VFile dest);

	boolean setExecutable(boolean executable);

	boolean setExecutable(boolean executable, boolean ownerOnly);

	boolean setLastModified(long time);

	boolean setReadable(boolean readable);

	boolean setReadable(boolean readable, boolean ownerOnly);

	boolean setReadOnly();

	boolean setWritable(boolean writable);

	boolean setWritable(boolean writable, boolean ownerOnly);

	String toString();

	URI toURI();

}
