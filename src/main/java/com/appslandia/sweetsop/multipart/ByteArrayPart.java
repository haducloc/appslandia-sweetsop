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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 */
public class ByteArrayPart extends FileNamePart<byte[]> {

	public ByteArrayPart(String partName, String fileName, byte[] content) {
		super(partName, fileName, content, null, null);
	}

	public ByteArrayPart(String partName, String fileName, byte[] content, String contentType) {
		super(partName, fileName, content, contentType, null);
	}

	public ByteArrayPart(String partName, String fileName, byte[] content, String contentType, Charset charset) {
		super(partName, fileName, content, contentType, charset);
	}

	@Override
	public long getBodyLength() {
		if (this.content == null) {
			return 0;
		}
		return this.content.length;
	}

	@Override
	protected void writeBody(OutputStream os) throws IOException {
		if (this.content == null) {
			return;
		}
		os.write(this.content, 0, this.content.length);
	}

	@Override
	public ByteArrayPart header(String name, String... values) {
		super.header(name, values);
		return this;
	}
}
