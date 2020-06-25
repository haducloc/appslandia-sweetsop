package com.appslandia.sweetsop.bodys;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;

public class StreamBodyTest {

	@Test
	public void test() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		InputStream content = new ByteArrayInputStream("data".getBytes(StandardCharsets.UTF_8));
		StreamBody body = new StreamBody(content);

		try {
			body.writeTo(os);
			String out = os.toString("UTF-8");

			Assert.assertEquals(out, "data");

		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_null() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		StreamBody body = new StreamBody(null);

		try {
			body.writeTo(os);
			byte[] out = os.toByteArray();

			Assert.assertEquals(body.getContentLength(), 0);
			Assert.assertEquals(out.length, 0);

		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
