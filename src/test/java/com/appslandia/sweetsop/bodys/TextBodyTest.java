package com.appslandia.sweetsop.bodys;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class TextBodyTest {

	@Test
	public void test() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		String content = "data";
		TextBody body = new TextBody(content);

		try {
			body.writeTo(os);
			String out = os.toString("UTF-8");

			Assert.assertEquals(body.getContentLength(), out.getBytes("UTF-8").length);
			Assert.assertEquals(out, "data");

		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_null() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		TextBody body = new TextBody(null);

		try {
			body.writeTo(os);
			byte[] out = os.toByteArray();

			Assert.assertEquals(body.getContentLength(), out.length);
			Assert.assertEquals(out.length, 0);

		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
