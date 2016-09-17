package com.boluozhai.snowflake.xgit.vfs;

import java.io.File;
import java.io.IOException;

import com.boluozhai.snowflake.test.TestBean;
import com.boluozhai.snowflake.test.TestContext;
import com.boluozhai.snowflake.vfs.VFS;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.vfs.impl.objectbank.pack.PackIndexReader;

public class PackIndexReaderTest {

	// @Test

	public void test() {
		TestBean tb = new TestBean();
		try {
			TestContext context = tb.open(this);
			File working = context.getWorkingPath();

			VFS vfs = VFS.Factory.getVFS(context);
			VFile file = vfs.newFile(working.toURI());
			file = file.child("pack-test.idx");

			PackIndexReader reader = new PackIndexReader(file);
			for (;;) {
				int res = reader.read();
				if (res < 0) {
					break;
				}
			}

		} catch (IOException e) {
			throw new RuntimeException(e);

		} finally {
			tb.close();
		}
	}

}
