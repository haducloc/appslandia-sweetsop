package com.appslandia.sweetsop.http;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.appslandia.common.utils.MimeTypes;
import com.appslandia.sweetsop.utils.HttpUtils;

public class HttpClientTest {

	@Test
	public void test_setRequestProperty() {
		try {
			HttpClient httpClient = HttpClient.create("http://server.com");
			httpClient.setRequestProperty("p1", "v1");
			Assert.assertEquals(httpClient.getConnection().getRequestProperty("p1"), "v1");
		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_addRequestProperty() {
		try {
			HttpClient httpClient = HttpClient.create("http://server.com");
			httpClient.addRequestProperty("p1", "v1");
			httpClient.addRequestProperty("p1", "v2");

			Assert.assertEquals(httpClient.getConnection().getRequestProperties().get("p1").size(), 2);
			Assert.assertTrue(httpClient.getConnection().getRequestProperties().get("p1").contains("v1"));
			Assert.assertTrue(httpClient.getConnection().getRequestProperties().get("p1").contains("v2"));

		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_addAccept() {
		try {
			HttpClient httpClient = HttpClient.create("http://server.com");
			httpClient.addAccept(MimeTypes.APP_JSON);

			Assert.assertEquals(httpClient.getConnection().getRequestProperty("Accept"), "application/json");
		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_addAcceptCharset() {
		try {
			HttpClient httpClient = HttpClient.create("http://server.com");
			httpClient.addAcceptCharset("UTF-8");

			Assert.assertEquals(httpClient.getConnection().getRequestProperty("Accept-Charset"), "UTF-8");
		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_addAcceptEncoding() {
		try {
			HttpClient httpClient = HttpClient.create("http://server.com");
			httpClient.addAcceptEncoding("gzip");

			Assert.assertEquals(httpClient.getConnection().getRequestProperty("Accept-Encoding"), "gzip");
		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_useMultipartForm() {
		try {
			HttpClient httpClient = HttpClient.post("http://server.com");
			httpClient.useMultipartForm(HttpUtils.generateBoundary());

			Assert.assertTrue(httpClient.getConnection().getRequestProperty("Content-Type").startsWith("multipart/form-data; boundary="));
		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_setContentType() {
		try {
			HttpClient httpClient = HttpClient.create("http://server.com");
			httpClient.setContentType(MimeTypes.APP_JSON);

			Assert.assertEquals(httpClient.getConnection().getRequestProperty("Content-Type"), "application/json");
		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_setContentEncoding() {
		try {
			HttpClient httpClient = HttpClient.create("http://server.com");
			httpClient.setContentEncoding("gzip");

			Assert.assertEquals(httpClient.getConnection().getRequestProperty("Content-Encoding"), "gzip");
		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_setRequestMethod() {
		try {
			HttpClient httpClient = HttpClient.create("http://server.com");

			httpClient.setRequestMethod("GET");
			Assert.assertEquals(httpClient.getConnection().getRequestMethod(), "GET");

			httpClient.setRequestMethod("POST");
			Assert.assertEquals(httpClient.getConnection().getRequestMethod(), "POST");

			httpClient.setRequestMethod("HEAD");
			Assert.assertEquals(httpClient.getConnection().getRequestMethod(), "HEAD");

			httpClient.setRequestMethod("PUT");
			Assert.assertEquals(httpClient.getConnection().getRequestMethod(), "PUT");

			httpClient.setRequestMethod("DELETE");
			Assert.assertEquals(httpClient.getConnection().getRequestMethod(), "DELETE");

			// httpClient.setRequestMethod("PATCH");
			// Assert.assertEquals(httpClient.getConnection().getRequestMethod(), "PATCH");

			httpClient.setRequestMethod("OPTIONS");
			Assert.assertEquals(httpClient.getConnection().getRequestMethod(), "OPTIONS");

			httpClient.setRequestMethod("TRACE");
			Assert.assertEquals(httpClient.getConnection().getRequestMethod(), "TRACE");
		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
