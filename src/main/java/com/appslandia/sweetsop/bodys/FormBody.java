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

package com.appslandia.sweetsop.bodys;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import com.appslandia.common.utils.URLUtils;
import com.appslandia.sweetsop.http.RequestBody;

/**
 * 
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 */
public class FormBody implements RequestBody {

	final Charset charset;
	final Map<String, Object> params = new LinkedHashMap<>();

	public FormBody() {
		this(StandardCharsets.UTF_8);
	}

	public FormBody(Charset charset) {
		this.charset = charset;
	}

	@Override
	public long getContentLength() {
		if (this.params.isEmpty()) {
			return 0;
		}
		return -1;
	}

	@Override
	public void writeTo(OutputStream os) throws IOException {
		if (this.params.isEmpty()) {
			return;
		}
		String queryParams = URLUtils.toQueryParams(this.params);
		os.write(queryParams.toString().getBytes(this.charset));
	}

	public FormBody put(String name, Object value) {
		this.params.put(name, value);
		return this;
	}

	public FormBody put(String name, Object... values) {
		this.params.put(name, values);
		return this;
	}
}
