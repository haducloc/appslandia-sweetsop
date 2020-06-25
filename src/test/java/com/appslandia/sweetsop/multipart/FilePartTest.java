package com.appslandia.sweetsop.multipart;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class FilePartTest {

	@Test
	public void test_null() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		FilePart part = new FilePart("part", "part.dat", null);

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
