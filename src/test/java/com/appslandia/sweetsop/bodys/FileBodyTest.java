package com.appslandia.sweetsop.bodys;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class FileBodyTest {

	@Test
	public void test_null() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		FileBody body = new FileBody(null);

		try {
			body.writeTo(os);
			byte[] out = os.toByteArray();

			Assert.assertEquals(body.getContentLength(), 0);
			Assert.assertEquals(out.length, 0);

		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	static class User {
		public int id;
		public String name;
	}
}
