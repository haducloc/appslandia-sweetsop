package com.appslandia.sweetsop.multipart;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class FieldPartTest {

	@Test
	public void test() {
		FieldPart part = new FieldPart("part", "data");
		Assert.assertEquals(part.getBodyLength(), 4);
	}

	@Test
	public void test_null() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		FieldPart part = new FieldPart("part", null);

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
