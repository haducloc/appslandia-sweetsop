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

package com.appslandia.sweetsop.multipart;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.CollectionUtils;
import com.appslandia.sweetsop.utils.HttpUtils;

/**
 * 
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 */
public abstract class Part<T> {

	static final byte[] CRLF = HttpUtils.toHeaderBytes("\r\n");
	static final byte[] QUOTE = HttpUtils.toHeaderBytes("\"");

	static final byte[] BOUNDARY_EXTRA = HttpUtils.toHeaderBytes("--");
	static final byte[] CONTENT_DISPOSITION = HttpUtils.toHeaderBytes("Content-Disposition: form-data; name=");

	static final byte[] COLON = HttpUtils.toHeaderBytes(": ");
	static final byte[] CONTENT_TYPE = HttpUtils.toHeaderBytes("Content-Type");
	static final byte[] CHARSET = HttpUtils.toHeaderBytes("; charset=");

	protected final String partName;
	protected final T content;

	protected final String contentType;
	protected final Charset charset;

	protected byte[] boundary;
	protected Map<String, List<String>> headers;

	public Part(String partName, T content, String contentType, Charset charset) {
		this.partName = AssertUtils.assertNotNull(partName);
		this.content = content;
		this.contentType = contentType;
		this.charset = charset;
	}

	protected abstract long getBodyLength();

	protected abstract void writeBody(OutputStream os) throws IOException;

	public void writePart(OutputStream os) throws IOException {
		writeStart(os);
		writeDisposition(os);
		writeContentType(os);
		writeOtherHeaders(os);
		writeEndOfHeaders(os);
		writeBody(os);
		writeEnd(os);
	}

	protected void writeStart(OutputStream os) throws IOException {
		os.write(BOUNDARY_EXTRA);
		os.write(this.boundary);
		os.write(CRLF);
	}

	protected void writeDisposition(OutputStream os) throws IOException {
		os.write(CONTENT_DISPOSITION);
		os.write(QUOTE);
		os.write(HttpUtils.toHeaderBytes(this.partName));
		os.write(QUOTE);
	}

	protected void writeContentType(OutputStream os) throws IOException {
		if (this.contentType == null) {
			return;
		}
		os.write(CRLF);
		os.write(CONTENT_TYPE);
		os.write(COLON);
		os.write(HttpUtils.toHeaderBytes(this.contentType));

		if (this.charset != null) {
			os.write(CHARSET);
			os.write(HttpUtils.toHeaderBytes(this.charset.name()));
		}
	}

	protected void writeOtherHeaders(OutputStream os) throws IOException {
		if (this.headers == null) {
			return;
		}
		for (Entry<String, List<String>> header : this.headers.entrySet()) {
			os.write(CRLF);
			os.write(HttpUtils.toHeaderBytes(header.getKey()));
			os.write(COLON);
			os.write(HttpUtils.toHeaderBytes(HttpUtils.toHeaderValues(header.getValue())));
		}
	}

	protected void writeEndOfHeaders(OutputStream os) throws IOException {
		os.write(CRLF);
		os.write(CRLF);
	}

	protected void writeEnd(OutputStream os) throws IOException {
		os.write(CRLF);
	}

	public long getPartLength() {
		long bodyLen = getBodyLength();
		if (bodyLen < 0) {
			return -1;
		}
		ByteArrayOutputStream overhead = new ByteArrayOutputStream();
		try {
			writeStart(overhead);
			writeDisposition(overhead);
			writeContentType(overhead);
			writeOtherHeaders(overhead);
			writeEndOfHeaders(overhead);
			writeEnd(overhead);

			return overhead.size() + bodyLen;
		} catch (IOException ex) {
			return -1;
		}
	}

	public void boundary(byte[] boundary) {
		this.boundary = boundary;
	}

	protected Map<String, List<String>> getHeaders() {
		if (this.headers == null) {
			this.headers = new HashMap<>();
		}
		return this.headers;
	}

	public Part<T> header(String name, String... values) {
		getHeaders().put(name, CollectionUtils.toList(values));
		return this;
	}
}
