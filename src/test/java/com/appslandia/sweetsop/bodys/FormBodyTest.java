package com.appslandia.sweetsop.bodys;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class FormBodyTest {

	@Test
	public void test() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		FormBody body = new FormBody();

		body.put("p1", "value1");
		body.put("p2", "value 2");
		body.put("p3", "v31", "v32");

		try {
			body.writeTo(os);
			String out = os.toString("UTF-8");

			Assert.assertTrue(out.contains("p1=value1"));
			Assert.assertTrue(out.contains("p2=value+2"));
			Assert.assertTrue(out.contains("p3=v31&p3=v32"));

		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_isEmpty() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		FormBody body = new FormBody();

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
