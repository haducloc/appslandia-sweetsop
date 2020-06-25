package com.appslandia.sweetsop.bodys;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.appslandia.common.utils.MathUtils;

public class ByteArrayBodyTest {

	@Test
	public void test() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		byte[] content = MathUtils.toByteArray(1, 100);
		ByteArrayBody body = new ByteArrayBody(content);

		try {
			body.writeTo(os);
			byte[] out = os.toByteArray();

			Assert.assertEquals(body.getContentLength(), out.length);
			Assert.assertArrayEquals(out, MathUtils.toByteArray(1, 100));

		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_null() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ByteArrayBody body = new ByteArrayBody(null);

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
