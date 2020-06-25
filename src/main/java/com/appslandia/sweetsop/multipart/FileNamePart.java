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

import com.appslandia.common.utils.AssertUtils;
import com.appslandia.sweetsop.utils.HttpUtils;

/**
 * 
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 */
public abstract class FileNamePart<T> extends Part<T> {

	static final byte[] FILE_NAME = HttpUtils.toHeaderBytes("; filename=");

	protected String fileName;

	public FileNamePart(String partName, String fileName, T content, String contentType, Charset charset) {
		super(partName, content, contentType, charset);
		this.fileName = AssertUtils.assertNotNull(fileName);
	}

	@Override
	protected void writeDisposition(OutputStream os) throws IOException {
		super.writeDisposition(os);

		os.write(FILE_NAME);
		os.write(QUOTE);
		os.write(HttpUtils.toHeaderBytes(this.fileName));
		os.write(QUOTE);
	}
}
