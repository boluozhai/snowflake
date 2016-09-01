package com.boluozhai.snowflake.vfs.impl;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import com.boluozhai.snowflake.vfs.VFS;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.VFileFilter;
import com.boluozhai.snowflake.vfs.VFilenameFilter;
import com.boluozhai.snowflake.vfs.VPath;

public class FileWrapper implements VFile {

	private final File file;
	private final VFS vfs;

	private FileWrapper(VFS the_vfs, File the_file) {
		this.vfs = the_vfs;
		this.file = the_file;
		if (file == null) {
			file.hashCode();
		}
	}

	public static VFile wrap(VFS vfs, File file) {

		if (file == null) {
			return null;
		}

		return new FileWrapper(vfs, file);
	}

	private static File unwrap(VFile dest) {
		FileWrapper d2 = (FileWrapper) dest;
		return d2.file;
	}

	public static VFile[] wrap(VFS vfs, File[] list) {
		VFile[] out = new VFile[list.length];
		for (int i = list.length - 1; i >= 0; i--) {
			File item = list[i];
			out[i] = wrap(vfs, item);
		}
		return out;
	}

	public File getFile() {
		return file;
	}

	private VFile wrap2(File f) {
		return wrap(vfs, f);
	}

	@Override
	public VPath toPath() {
		VPathBuilder builder = new VPathBuilder(this);
		return builder.create();
	}

	@Override
	public VFS vfs() {
		return vfs;
	}

	@Override
	public VFile child(String name) {
		File f2 = new File(file, name);
		return wrap(vfs, f2);
	}

	@Override
	public boolean canExecute() {
		return file.canExecute();
	}

	@Override
	public boolean canRead() {
		return file.canRead();
	}

	@Override
	public boolean canWrite() {
		return file.canWrite();
	}

	@Override
	public int compareTo(VFile pathname) {
		FileWrapper w2 = (FileWrapper) pathname;
		File f2 = w2.file;
		return file.compareTo(f2);
	}

	@Override
	public boolean createNewFile() throws IOException {
		return file.createNewFile();
	}

	@Override
	public boolean delete() {
		return file.delete();
	}

	@Override
	public void deleteOnExit() {
		file.deleteOnExit();
	}

	@Override
	public boolean exists() {
		return file.exists();
	}

	@Override
	public VFile getAbsoluteFile() {
		File f2 = file.getAbsoluteFile();
		return wrap(vfs, f2);
	}

	@Override
	public String getAbsolutePath() {
		return file.getAbsolutePath();
	}

	@Override
	public VFile getCanonicalFile() throws IOException {
		return this.wrap2(file.getCanonicalFile());
	}

	@Override
	public String getCanonicalPath() throws IOException {
		return file.getCanonicalPath();
	}

	@Override
	public long getFreeSpace() {
		return file.getFreeSpace();
	}

	@Override
	public String getName() {
		return file.getName();
	}

	@Override
	public String getParent() {
		return file.getParent();
	}

	@Override
	public VFile getParentFile() {
		return this.wrap2(file.getParentFile());
	}

	@Override
	public String getPath() {
		return file.getPath();
	}

	@Override
	public long getTotalSpace() {
		return file.getTotalSpace();
	}

	@Override
	public long getUsableSpace() {
		return file.getUsableSpace();
	}

	@Override
	public boolean isAbsolute() {
		return file.isAbsolute();
	}

	@Override
	public boolean isDirectory() {
		return file.isDirectory();
	}

	@Override
	public boolean isFile() {
		return file.isFile();
	}

	@Override
	public boolean isHidden() {
		return file.isHidden();
	}

	@Override
	public long lastModified() {
		return file.lastModified();
	}

	@Override
	public long length() {
		return file.length();
	}

	@Override
	public String[] list() {
		return file.list();
	}

	@Override
	public String[] list(VFilenameFilter filter) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not impl");
	}

	@Override
	public VFile[] listFiles() {
		File[] list = file.listFiles();
		return wrap(vfs, list);
	}

	@Override
	public VFile[] listFiles(VFileFilter filter) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not impl");
	}

	@Override
	public VFile[] listFiles(VFilenameFilter filter) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not impl");
	}

	@Override
	public boolean mkdir() {
		return file.mkdir();
	}

	@Override
	public boolean mkdirs() {
		return file.mkdirs();
	}

	@Override
	public boolean renameTo(VFile dest) {
		File d2 = unwrap(dest);
		return file.renameTo(d2);
	}

	@Override
	public boolean setExecutable(boolean executable) {
		return file.setExecutable(executable);
	}

	@Override
	public boolean setExecutable(boolean executable, boolean ownerOnly) {
		return file.setExecutable(executable, ownerOnly);
	}

	@Override
	public boolean setLastModified(long time) {
		return file.setLastModified(time);
	}

	@Override
	public boolean setReadable(boolean readable) {
		return file.setReadable(readable);
	}

	@Override
	public boolean setReadable(boolean readable, boolean ownerOnly) {
		return file.setReadable(readable, ownerOnly);
	}

	@Override
	public boolean setReadOnly() {
		return file.setReadOnly();
	}

	@Override
	public boolean setWritable(boolean writable) {
		return file.setWritable(writable);
	}

	@Override
	public boolean setWritable(boolean writable, boolean ownerOnly) {
		return file.setWritable(writable, ownerOnly);
	}

	@Override
	public URI toURI() {
		return file.toURI();
	}

	@Override
	public String toString() {
		return file.toString();
	}

}
