package com.appslandia.sweetsop.bodys;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;

public class JsonBodyTest {

	@Test
	public void test() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		User user = new User();
		user.id = 1;
		user.name = "user1";

		JsonBody body = new JsonBody(user, StandardCharsets.UTF_8);
		try {
			body.writeTo(os);
			String out = os.toString("UTF-8");

			Assert.assertTrue(out.contains("\"id\":1"));
			Assert.assertTrue(out.contains("\"name\":\"user1\""));

		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_null() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		JsonBody body = new JsonBody(null, StandardCharsets.UTF_8);

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
