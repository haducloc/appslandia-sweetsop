// The MIT License (MIT)
// Copyright © 2015 AppsLandia. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.appslandia.sweetsop.utils;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 */
public class SSLUtils {

	public static final String TLS_V1_1 = "TLSv1.1";
	public static final String TLS_V1_2 = "TLSv1.2";
	public static final String TLS_V1_3 = "TLSv1.3";

	public static SSLContext initSSLContext(String protocol, TrustManager trustManager) {
		try {
			SSLContext ctx = SSLContext.getInstance(protocol);
			ctx.init(null, new TrustManager[] { trustManager }, new SecureRandom());
			return ctx;
		} catch (GeneralSecurityException ex) {
			throw new IllegalArgumentException(ex);
		}
	}

	public static SSLContext initFakeSSLContext(String protocol) {
		return initSSLContext(protocol, FakeTrustManager.INSTANCE);
	}

	public static class FakeTrustManager implements X509TrustManager {
		public static final FakeTrustManager INSTANCE = new FakeTrustManager();

		final X509Certificate[] EMPTY_ACCEPTED_ISSUERS = {};

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return EMPTY_ACCEPTED_ISSUERS;
		}
	}

	public static class FakeHostnameVerifier implements HostnameVerifier {
		public static final FakeHostnameVerifier INSTANCE = new FakeHostnameVerifier();

		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}
}
