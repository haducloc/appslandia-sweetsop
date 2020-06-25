package com.appslandia.sweetsop.multipart;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;

public class StreamPartTest {

	@Test
	public void test() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		InputStream content = new ByteArrayInputStream("data".getBytes(StandardCharsets.UTF_8));
		StreamPart part = new StreamPart("part", "part.dat", content, "image/png");

		try {
			part.writeBody(os);
			String out = os.toString("UTF-8");

			Assert.assertEquals(out, "data");

		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_null() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		StreamPart part = new StreamPart("part", "part.dat", null, "image/png");

		try {
			part.writeBody(os);
			byte[] out = os.toByteArray();

			Assert.assertEquals(part.getBodyLength(), 0);
			Assert.assertEquals(out.length, 0);

		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
