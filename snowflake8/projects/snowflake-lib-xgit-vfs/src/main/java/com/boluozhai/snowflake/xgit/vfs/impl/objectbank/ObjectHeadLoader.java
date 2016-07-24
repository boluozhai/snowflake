package com.boluozhai.snowflake.xgit.vfs.impl.objectbank;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ObjectHeadLoader {

	public ObjectHead load(InputStream in) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream(32);

		for (;;) {
			final int b = in.read();
			if (b == 0) {
				// end of head
				break;
			} else if (b < 0) {
				throw new IOException("the file is end at bad pos.");
			} else {
				baos.write(b);
			}
			if (baos.size() > 512) {
				throw new IOException("the head is too large.");
			}
		}

		final String str = baos.toString("utf-8");
		final int i = str.lastIndexOf(' ');
		if (i < 0) {
			throw new IOException("bad format.");
		}
		final String s1 = str.substring(0, i);
		final String s2 = str.substring(i + 1);

		ObjectHead oh = new ObjectHead();
		oh.body_size = Long.parseLong(s2);
		oh.head_size = baos.size() + 1;
		oh.type = s1;
		return oh;
	}
}
