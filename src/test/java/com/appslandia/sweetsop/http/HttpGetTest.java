package com.appslandia.sweetsop.http;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.appslandia.common.base.Params;
import com.appslandia.common.utils.URLUtils;
import com.appslandia.sweetsop.readers.JsonReader;
import com.appslandia.sweetsop.readers.TextReader;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

public class HttpGetTest {

	@Rule
	public WireMockRule httpMock = new WireMockRule(9000);

	private static String baseUrl() {
		return "http://localhost:9000/action";
	}

	@Test
	public void test_resp() {
		MappingBuilder mockReq = get(urlPathMatching("/action"));
		ResponseDefinitionBuilder mockResp = aResponse().withStatus(200).withBody("data");
		httpMock.stubFor(mockReq.willReturn(mockResp));

		HttpClient httpClient = null;
		try {
			String url = baseUrl();
			httpClient = HttpClient.get(url);

			httpClient.setResultReader(TextReader.INSTANCE);
			httpClient.execute();

			Assert.assertEquals(httpClient.getResponseCode(), 200);
			Assert.assertEquals(httpClient.getResultObject(), "data");

			Assert.assertNull(httpClient.getErrorObject());
		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_jsonResp() {
		MappingBuilder mockReq = get(urlPathMatching("/action"));
		ResponseDefinitionBuilder mockResp = aResponse().withStatus(200).withBody("{\"prop1\": \"value1\"}");
		httpMock.stubFor(mockReq.willReturn(mockResp));

		HttpClient httpClient = null;
		try {
			String url = baseUrl();
			httpClient = HttpClient.get(url);

			httpClient.setResultReader(new JsonReader()).setResultClass(HashMap.class);
			httpClient.execute();

			Assert.assertEquals(httpClient.getResponseCode(), 200);
			Map<String, Object> result = httpClient.getResultObject();
			Assert.assertEquals(result.get("prop1"), "value1");

			Assert.assertNull(httpClient.getErrorObject());
		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_errorResp() {
		MappingBuilder mockReq = get(urlPathMatching("/action"));
		ResponseDefinitionBuilder mockResp = aResponse().withStatus(500).withBody("Error");
		httpMock.stubFor(mockReq.willReturn(mockResp));

		HttpClient httpClient = null;
		try {
			String url = baseUrl();
			httpClient = HttpClient.get(url);
			httpClient.setResultReader(TextReader.INSTANCE);
			httpClient.execute();

			Assert.assertEquals(httpClient.getResponseCode(), 500);
			Assert.assertEquals(httpClient.getErrorObject(), "Error");
		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_queryParams() {
		MappingBuilder mockReq = get(urlPathMatching("/action"));

		mockReq.withQueryParam("p1", equalTo("v1"));
		mockReq.withQueryParam("p2", equalTo("v 2"));

		ResponseDefinitionBuilder mockResp = aResponse().withStatus(200);
		httpMock.stubFor(mockReq.willReturn(mockResp));

		HttpClient httpClient = null;
		try {
			String url = baseUrl() + "?" + URLUtils.toQueryParams(new Params().set("p1", "v1").set("p2", "v 2"));

			httpClient = HttpClient.get(url);
			httpClient.execute();

			Assert.assertEquals(httpClient.getResponseCode(), 200);
		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
