package com.appslandia.sweetsop.multipart;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class JsonPartTest {

	@Test
	public void test() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		User user = new User();
		user.id = 1;
		user.name = "user1";

		JsonPart part = new JsonPart("part", user);
		try {
			part.writeBody(os);
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
		JsonPart part = new JsonPart("part", null);

		try {
			part.writeBody(os);
			byte[] out = os.toByteArray();

			Assert.assertEquals(part.getBodyLength(), 0);
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
