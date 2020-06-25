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

package com.appslandia.sweetsop.pinning;

import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;

import javax.net.ssl.X509TrustManager;

/**
 * 
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 */
public class PinningTrustManager implements X509TrustManager {

	final Collection<Pin> pins;
	final boolean anyMatched;

	public PinningTrustManager(Collection<Pin> pins) {
		this(pins, true);
	}

	public PinningTrustManager(Collection<Pin> pins, boolean anyMatched) {
		this.pins = pins;
		this.anyMatched = anyMatched;
	}

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		if (this.anyMatched) {
			for (X509Certificate cert : chain) {
				if (isPinned(cert)) {
					return;
				}
			}
			throw new CertificateException("checkServerTrusted (chain)");
		} else {
			X509Certificate leafCert = chain[0];
			if (isPinned(leafCert)) {
				return;
			}
			throw new CertificateException("checkServerTrusted (leaf)");
		}
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[0];
	}

	private boolean isPinned(X509Certificate cert) throws CertificateEncodingException {
		for (Pin pin : this.pins) {
			if (pin.verify(cert)) {
				return true;
			}
		}
		return false;
	}
}
