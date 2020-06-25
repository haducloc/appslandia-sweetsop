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
import java.security.cert.X509Certificate;

import com.appslandia.common.base.BaseEncoder;
import com.appslandia.common.crypto.Digester;
import com.appslandia.common.utils.AssertUtils;

/**
 * 
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 */
public abstract class Pin {

	final String base64Pin;
	final Digester digester;

	public Pin(String base64Pin, Digester digester) {
		AssertUtils.assertNotNull(base64Pin);
		AssertUtils.assertNotNull(digester);

		this.base64Pin = base64Pin;
		this.digester = digester;
	}

	public boolean verify(X509Certificate cert) throws CertificateEncodingException {
		byte[] digested = this.digester.digest(getPinData(cert));
		return this.base64Pin.equals(BaseEncoder.BASE64.encode(digested));
	}

	protected abstract byte[] getPinData(X509Certificate cert) throws CertificateEncodingException;
}
