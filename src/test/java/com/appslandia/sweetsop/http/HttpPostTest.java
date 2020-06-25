package com.appslandia.sweetsop.http;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.appslandia.sweetsop.bodys.FormBody;
import com.appslandia.sweetsop.bodys.JsonBody;
import com.appslandia.sweetsop.bodys.TextBody;
import com.appslandia.sweetsop.readers.JsonReader;
import com.appslandia.sweetsop.readers.TextReader;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

public class HttpPostTest {

	@Rule
	public WireMockRule httpMock = new WireMockRule(9000);

	private static String baseUrl() {
		return "http://localhost:9000/action";
	}

	@Test
	public void test_resp() {
		MappingBuilder mockReq = post(urlPathMatching("/action"));
		ResponseDefinitionBuilder mockResp = aResponse().withStatus(200).withBody("data");
		httpMock.stubFor(mockReq.willReturn(mockResp));

		HttpClient httpClient = null;
		try {
			String url = baseUrl();
			httpClient = HttpClient.post(url);

			httpClient.setResultReader(TextReader.INSTANCE);
			httpClient.execute();

			Assert.assertEquals(httpClient.getResponseCode(), 200);

			String result = httpClient.getResultObject();
			Assert.assertEquals(result, "data");

		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_jsonResp() {
		MappingBuilder mockReq = post(urlPathMatching("/action"));
		ResponseDefinitionBuilder mockResp = aResponse().withStatus(200).withBody("{\"prop1\": \"value1\"}");

		httpMock.stubFor(mockReq.willReturn(mockResp));

		HttpClient httpClient = null;
		try {
			String url = baseUrl();
			httpClient = HttpClient.post(url);

			httpClient.setResultReader(new JsonReader()).setResultClass(HashMap.class);
			httpClient.execute();

			Assert.assertEquals(httpClient.getResponseCode(), 200);

			Map<String, Object> result = httpClient.getResultObject();
			Assert.assertEquals(result.get("prop1"), "value1");

		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_jsonErrorResp() {
		MappingBuilder mockReq = post(urlPathMatching("/action"));
		ResponseDefinitionBuilder mockResp = aResponse().withStatus(500).withBody("{\"success\": false}");

		httpMock.stubFor(mockReq.willReturn(mockResp));

		HttpClient httpClient = null;
		try {
			String url = baseUrl();
			httpClient = HttpClient.post(url);

			httpClient.setErrorReader(new JsonReader()).setErrorClass(HashMap.class);
			httpClient.execute();

			Assert.assertEquals(httpClient.getResponseCode(), 500);
			Assert.assertNull(httpClient.getResultObject());

			Map<String, Object> result = httpClient.getErrorObject();
			Assert.assertEquals(result.get("success"), Boolean.FALSE);

		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_errorResp() {
		MappingBuilder mockReq = post(urlPathMatching("/action"));
		ResponseDefinitionBuilder mockResp = aResponse().withStatus(500).withBody("Error");
		httpMock.stubFor(mockReq.willReturn(mockResp));

		HttpClient httpClient = null;
		try {
			String url = baseUrl();
			httpClient = HttpClient.post(url);
			httpClient.execute();

			Assert.assertEquals(httpClient.getResponseCode(), 500);
			Assert.assertEquals(httpClient.getErrorObject(), "Error");

		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_formBody() {
		MappingBuilder mockReq = post(urlPathMatching("/action"));
		ResponseDefinitionBuilder mockResp = aResponse();

		mockReq.withRequestBody(containing("p1=v1"));
		mockReq.withRequestBody(containing("p2=v+2"));

		httpMock.stubFor(mockReq.willReturn(mockResp));

		HttpClient httpClient = null;
		try {
			String url = baseUrl();
			httpClient = HttpClient.post(url);

			httpClient.setRequestBody(new FormBody().put("p1", "v1").put("p2", "v 2"));
			httpClient.execute();

			Assert.assertEquals(httpClient.getResponseCode(), 200);

		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_jsonBody() {
		MappingBuilder mockReq = post(urlPathMatching("/action"));
		ResponseDefinitionBuilder mockResp = aResponse();
		mockReq.withRequestBody(containing("\"id\":1"));
		mockReq.withRequestBody(containing("\"name\":\"user1\""));

		httpMock.stubFor(mockReq.willReturn(mockResp));

		HttpClient httpClient = null;
		try {
			String url = baseUrl();
			httpClient = HttpClient.post(url);

			httpClient.setRequestBody(new JsonBody(new User(1, "user1"), StandardCharsets.UTF_8));
			httpClient.execute();

			Assert.assertEquals(httpClient.getResponseCode(), 200);

		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_textBody() {
		MappingBuilder mockReq = post(urlPathMatching("/action"));
		ResponseDefinitionBuilder mockResp = aResponse();
		mockReq.withRequestBody(equalTo("textBody"));

		httpMock.stubFor(mockReq.willReturn(mockResp));

		HttpClient httpClient = null;
		try {
			String url = baseUrl();
			httpClient = HttpClient.post(url);

			httpClient.setRequestBody(new TextBody("textBody"));
			httpClient.execute();

			Assert.assertEquals(httpClient.getResponseCode(), 200);

		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	static class User {

		public int id;
		public String name;

		public User(int id, String name) {
			this.id = id;
			this.name = name;
		}
	}
}
