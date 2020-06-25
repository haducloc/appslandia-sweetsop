package com.appslandia.sweetsop.pinning;

import java.security.cert.CertificateEncodingException;

import org.junit.Assert;
import org.junit.Test;

import com.appslandia.common.base.BaseEncoder;
import com.appslandia.common.crypto.Digester;
import com.appslandia.common.crypto.DigesterImpl;
import com.appslandia.common.utils.MathUtils;

public class CertificateTest {

	final Digester digester = new DigesterImpl("SHA-256");

	@Test
	public void test() {

		byte[] encoded = MathUtils.toByteArray(1, 100);
		String base64Pin = BaseEncoder.BASE64.encode(digester.digest(encoded));

		MockX509Certificate cert = new MockX509Certificate(encoded);

		CertificatePin pin = new CertificatePin(base64Pin, digester);

		try {
			boolean result = pin.verify(cert);
			Assert.assertTrue(result);

		} catch (CertificateEncodingException ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
