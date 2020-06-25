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
import java.util.ArrayList;
import java.util.List;

import com.appslandia.common.utils.AssertUtils;
import com.appslandia.sweetsop.http.RequestBody;
import com.appslandia.sweetsop.utils.HttpUtils;

/**
 * 
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 */
public class MultipartBody implements RequestBody {

	final byte[] boundary;
	final List<Part<?>> parts = new ArrayList<>();

	public MultipartBody(String boundary) {
		this.boundary = HttpUtils.toHeaderBytes(boundary);
	}

	@Override
	public long getContentLength() {
		long total = 0;
		for (Part<?> part : this.getParts()) {
			part.boundary(this.boundary);
			long len = part.getPartLength();
			if (len < 0) {
				return -1;
			}
			total += len;
		}
		total += Part.BOUNDARY_EXTRA.length;
		total += boundary.length;
		total += Part.BOUNDARY_EXTRA.length;
		total += Part.CRLF.length;
		return total;
	}

	@Override
	public void writeTo(OutputStream os) throws IOException {
		for (Part<?> part : this.getParts()) {
			part.boundary(this.boundary);
			part.writePart(os);
		}
		os.write(Part.BOUNDARY_EXTRA);
		os.write(this.boundary);
		os.write(Part.BOUNDARY_EXTRA);
		os.write(Part.CRLF);
	}

	public MultipartBody part(Part<?> part) {
		this.parts.add(part);
		return this;
	}

	protected List<Part<?>> getParts() {
		AssertUtils.assertTrue(!this.parts.isEmpty(), "No parts provided.");
		return this.parts;
	}
}
