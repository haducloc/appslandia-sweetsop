package com.appslandia.sweetsop.multipart;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.appslandia.common.utils.MathUtils;

public class ByteArrayPartTest {

	@Test
	public void test() {
		ByteArrayPart part = new ByteArrayPart("part", "part.dat", MathUtils.toByteArray(1, 100));
		Assert.assertTrue(part.getBodyLength() == 100);
	}

	@Test
	public void test_null() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ByteArrayPart part = new ByteArrayPart("part", "part.dat", null);

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
