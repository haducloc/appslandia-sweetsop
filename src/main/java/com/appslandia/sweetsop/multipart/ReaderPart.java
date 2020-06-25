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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.appslandia.common.utils.IOUtils;

/**
 * 
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 */
public class ReaderPart extends FileNamePart<Reader> {

	public ReaderPart(String partName, String fileName, Reader content, String contentType) {
		super(partName, fileName, content, contentType, StandardCharsets.UTF_8);
	}

	public ReaderPart(String partName, String fileName, Reader content, String contentType, Charset charset) {
		super(partName, fileName, content, contentType, charset);
	}

	@Override
	public long getBodyLength() {
		if (this.content == null) {
			return 0;
		}
		return -1;
	}

	@Override
	protected void writeBody(OutputStream os) throws IOException {
		if (this.content == null) {
			return;
		}
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os, this.charset));
		IOUtils.copy(this.content, out);
		out.flush();
	}

	@Override
	public ReaderPart header(String name, String... values) {
		super.header(name, values);
		return this;
	}
}
