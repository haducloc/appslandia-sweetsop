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

package com.appslandia.sweetsop.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.zip.DeflaterInputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import com.appslandia.common.utils.IOUtils;
import com.appslandia.common.utils.ObjectUtils;
import com.appslandia.sweetsop.readers.TextReader;
import com.appslandia.sweetsop.utils.SSLUtils;

/**
 * 
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 */
public class HttpClient {

	protected final HttpURLConnection conn;
	protected final String originHost;
	protected RedirectVerifier redirectVerifier;

	protected RequestBody requestBody;
	protected ResponseReader resultReader, errorReader;
	protected Class<?> resultClass, errorClass;
	protected Object resultObject, errorObject;

	private HttpClient(String url) throws IOException {
		URL requestUrl = new URL(url);
		this.originHost = requestUrl.getHost();
		this.conn = (HttpURLConnection) requestUrl.openConnection();
	}

	public HttpURLConnection getConnection() {
		return this.conn;
	}

	public HttpClient setRedirectVerifier(RedirectVerifier redirectVerifier) {
		this.redirectVerifier = redirectVerifier;
		return this;
	}

	public HttpClient setRequestBody(RequestBody requestBody) {
		this.requestBody = requestBody;
		return this;
	}

	public HttpClient setResultReader(ResponseReader resultReader) {
		this.resultReader = resultReader;
		return this;
	}

	public HttpClient setResultClass(Class<?> resultClass) {
		this.resultClass = resultClass;
		return this;
	}

	public HttpClient setErrorReader(ResponseReader errorReader) {
		this.errorReader = errorReader;
		return this;
	}

	public HttpClient setErrorClass(Class<?> errorClass) {
		this.errorClass = errorClass;
		return this;
	}

	public <R> R getResultObject() {
		return ObjectUtils.cast(this.resultObject);
	}

	public <E> E getErrorObject() {
		return ObjectUtils.cast(this.errorObject);
	}

	public HttpClient setRequestProperty(String headerName, String value) {
		this.conn.setRequestProperty(headerName, value);
		return this;
	}

	public HttpClient addRequestProperty(String headerName, String value) {
		this.conn.addRequestProperty(headerName, value);
		return this;
	}

	public HttpClient setRequestMethod(String method) throws ProtocolException {
		this.conn.setRequestMethod(method);
		return this;
	}

	public HttpClient setUseCaches(boolean useCaches) {
		this.conn.setUseCaches(useCaches);
		return this;
	}

	public HttpClient setInstanceFollowRedirects(boolean followRedirects) {
		this.conn.setInstanceFollowRedirects(followRedirects);
		return this;
	}

	public HttpClient setIfModifiedSince(long ifModifiedSince) {
		this.conn.setIfModifiedSince(ifModifiedSince);
		return this;
	}

	public HttpClient setConnectTimeout(int connectTimeoutMs) {
		this.conn.setConnectTimeout(connectTimeoutMs);
		return this;
	}

	public HttpClient setReadTimeout(int readTimeoutMs) {
		this.conn.setReadTimeout(readTimeoutMs);
		return this;
	}

	public HttpClient setFixedLengthStreamingMode(int contentLength) {
		this.conn.setFixedLengthStreamingMode(contentLength);
		return this;
	}

	public HttpClient setChunkedStreamingMode(int chunkLength) {
		this.conn.setChunkedStreamingMode(chunkLength);
		return this;
	}

	public HttpClient setHostnameVerifier(HostnameVerifier hostnameVerifier) {
		if (this.conn instanceof HttpsURLConnection) {
			((HttpsURLConnection) this.conn).setHostnameVerifier(hostnameVerifier);
		}
		return this;
	}

	public HttpClient setSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
		if (this.conn instanceof HttpsURLConnection) {
			((HttpsURLConnection) this.conn).setSSLSocketFactory(sslSocketFactory);
		}
		return this;
	}

	public HttpClient useFakeHostnameVerifier() {
		if (this.conn instanceof HttpsURLConnection) {
			((HttpsURLConnection) this.conn).setHostnameVerifier(SSLUtils.FakeHostnameVerifier.INSTANCE);
		}
		return this;
	}

	public HttpClient useFakeSSLSocketFactory(String protocol) {
		if (this.conn instanceof HttpsURLConnection) {
			((HttpsURLConnection) this.conn).setSSLSocketFactory(SSLUtils.initFakeSSLContext(protocol).getSocketFactory());
		}
		return this;
	}

	public HttpClient addAccept(String accept) {
		this.conn.addRequestProperty("Accept", accept);
		return this;
	}

	public HttpClient addAcceptCharset(String acceptCharset) {
		this.conn.addRequestProperty("Accept-Charset", acceptCharset);
		return this;
	}

	public HttpClient addAcceptEncoding(String acceptEncoding) {
		this.conn.addRequestProperty("Accept-Encoding", acceptEncoding);
		return this;
	}

	public HttpClient useMultipartForm(String boundary) {
		return setContentType("multipart/form-data; boundary=" + boundary);
	}

	public HttpClient setContentType(String contentType) {
		this.conn.setRequestProperty("Content-Type", contentType);
		return this;
	}

	public HttpClient setContentEncoding(String contentEncoding) {
		this.conn.setRequestProperty("Content-Encoding", contentEncoding);
		return this;
	}

	public HttpClient useChromeAgent() {
		return setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36");
	}

	public HttpClient setUserAgent(String userAgent) {
		this.conn.setRequestProperty("User-Agent", userAgent);
		return this;
	}

	public HttpClient setAuthorization(String type, String credentials) {
		this.conn.setRequestProperty("Authorization", type + " " + credentials);
		return this;
	}

	public HttpClient setBearerAuthorization(String credentials) {
		return setAuthorization("Bearer", credentials);
	}

	public HttpClient setBasicAuthorization(String credentials) {
		return setAuthorization("Basic", credentials);
	}

	public HttpClient setProxyAuthorization(String type, String credentials) {
		this.conn.setRequestProperty("Proxy-Authorization", type + " " + credentials);
		return this;
	}

	public HttpClient setBasicProxyAuthorization(String credentials) {
		return setProxyAuthorization("Basic", credentials);
	}

	protected void verifyRedirect() throws RedirectException {
		RedirectVerifier verifier = (this.redirectVerifier != null) ? this.redirectVerifier : defaultRedirectVerifier;
		if (verifier == null) {
			return;
		}
		if (!this.conn.getURL().getHost().equals(this.originHost)) {
			verifier.verify(this.conn.getURL().getHost());
		}
	}

	protected boolean writeBody() throws IOException {
		if (this.requestBody == null) {
			return false;
		}
		this.conn.setDoOutput(true);
		OutputStream os = null;
		try {
			// Compress?
			String ce = this.conn.getRequestProperty("Content-Encoding");
			boolean gzip = (ce != null) && ce.equalsIgnoreCase("gzip");
			boolean deflate = (ce != null) && ce.equalsIgnoreCase("deflate");

			// Content-Length?
			if (!gzip && !deflate) {
				int contentLength = (int) this.requestBody.getContentLength();
				if (contentLength >= 0) {
					this.conn.setFixedLengthStreamingMode(contentLength);
				}
			}

			os = this.conn.getOutputStream();
			verifyRedirect();

			// Compress Stream?
			if (gzip) {
				os = new GZIPOutputStream(os);
			} else if (deflate) {
				os = new DeflaterOutputStream(os);
			}

			this.requestBody.writeTo(os);
		} finally {
			if (os != null) {
				os.close();
			}
		}
		return true;
	}

	public int execute() throws IOException {
		InputStream is = null;
		try {
			boolean connected = this.writeBody();
			// HEAD
			if ("HEAD".equals(this.conn.getRequestMethod())) {
				if (!connected) {
					this.conn.connect();
					verifyRedirect();
				}
				return this.conn.getResponseCode();
			}

			// 2xx
			boolean status2xx = (this.conn.getResponseCode() / 100) == 2;
			if (!connected) {
				verifyRedirect();
			}

			// Wrap IS
			is = status2xx ? this.conn.getInputStream() : this.conn.getErrorStream();
			if (is != null) {
				if ("gzip".equalsIgnoreCase(this.conn.getContentEncoding())) {
					is = new GZIPInputStream(is);
				} else if ("deflate".equalsIgnoreCase(this.conn.getContentEncoding())) {
					is = new DeflaterInputStream(is);
				}
			}

			if (status2xx) {
				// Not 204 & 205
				if ((this.conn.getResponseCode() != 204) && (this.conn.getResponseCode() != 205)) {
					if (this.resultReader != null) {
						this.resultObject = this.resultReader.read(is, this.resultClass, this.conn);
					} else {
						this.resultObject = IOUtils.toByteArray(is);
					}
				}
			} else {
				// 4xx | 5xx
				if (is != null) {
					if (this.errorReader != null) {
						this.errorObject = this.errorReader.read(is, this.errorClass, this.conn);
					} else {
						this.errorObject = TextReader.INSTANCE.read(is, String.class, this.conn);
					}
				}
			}
			return this.conn.getResponseCode();
		} finally {
			if (is != null) {
				is.close();
			}
			this.conn.disconnect();
		}
	}

	public String getContentType() {
		return this.conn.getContentType();
	}

	public String getContentEncoding() {
		return this.conn.getContentEncoding();
	}

	public int getContentLength() {
		return this.conn.getContentLength();
	}

	public int getResponseCode() throws IOException {
		return this.conn.getResponseCode();
	}

	public String getResponseMessage() throws IOException {
		return this.conn.getResponseMessage();
	}

	public Map<String, List<String>> getResponseHeaders() {
		return this.conn.getHeaderFields();
	}

	public String getResponseHeader(String key) {
		return this.conn.getHeaderField(key);
	}

	public long getResponseDateHeader(String key, long defaultValue) {
		return this.conn.getHeaderFieldDate(key, defaultValue);
	}

	public long getResponseIntHeader(String key, int defaultValue) {
		return this.conn.getHeaderFieldInt(key, defaultValue);
	}

	public static HttpClient create(String url) throws IOException {
		return new HttpClient(url);
	}

	public static HttpClient get(String url) throws IOException {
		return new HttpClient(url).setRequestMethod("GET");
	}

	public static HttpClient post(String url) throws IOException {
		return new HttpClient(url).setRequestMethod("POST");
	}

	public static HttpClient head(String url) throws IOException {
		return new HttpClient(url).setRequestMethod("HEAD");
	}

	public static HttpClient put(String url) throws IOException {
		return new HttpClient(url).setRequestMethod("PUT");
	}

	public static HttpClient delete(String url) throws IOException {
		return new HttpClient(url).setRequestMethod("DELETE");
	}

	public static HttpClient patch(String url) throws IOException {
		return new HttpClient(url).setRequestMethod("PATCH");
	}

	public static HttpClient options(String url) throws IOException {
		return new HttpClient(url).setRequestMethod("OPTIONS");
	}

	public static HttpClient trace(String url) throws IOException {
		return new HttpClient(url).setRequestMethod("TRACE");
	}

	public static boolean getFollowRedirects() {
		return HttpURLConnection.getFollowRedirects();
	}

	public static void setFollowRedirects(boolean followRedirect) {
		HttpURLConnection.setFollowRedirects(followRedirect);
	}

	public static boolean getDefaultAllowUserInteraction() {
		return HttpURLConnection.getDefaultAllowUserInteraction();
	}

	public static void setDefaultAllowUserInteraction(boolean defaultallowuserinteraction) {
		HttpURLConnection.setDefaultAllowUserInteraction(defaultallowuserinteraction);
	}

	public static FileNameMap getFileNameMap() {
		return HttpURLConnection.getFileNameMap();
	}

	public static void setFileNameMap(FileNameMap map) {
		HttpURLConnection.setFileNameMap(map);
	}

	public static HostnameVerifier getDefaultHostnameVerifier() {
		return HttpsURLConnection.getDefaultHostnameVerifier();
	}

	public static void setDefaultHostnameVerifier(HostnameVerifier hostnameVerifier) {
		HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
	}

	public static SSLSocketFactory getDefaultSSLSocketFactory() {
		return HttpsURLConnection.getDefaultSSLSocketFactory();
	}

	public static void setDefaultSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
		HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
	}

	private static RedirectVerifier defaultRedirectVerifier;

	public static void setDefaultRedirectVerifier(RedirectVerifier redirectVerifier) throws SecurityException {
		SecurityManager sec = System.getSecurityManager();
		if (sec != null)
			sec.checkSetFactory();
		defaultRedirectVerifier = redirectVerifier;
	}

	public static RedirectVerifier getDefaultRedirectVerifier() {
		return defaultRedirectVerifier;
	}
}
