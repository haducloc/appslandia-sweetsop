package com.appslandia.sweetsop.http;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.head;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

public class HttpHeadTest {

	@Rule
	public WireMockRule httpMock = new WireMockRule(9000);

	private static String baseUrl() {
		return "http://localhost:9000/action";
	}

	@Test
	public void test() {
		MappingBuilder mockReq = head(urlPathMatching("/action"));
		ResponseDefinitionBuilder mockResp = aResponse().withStatus(200).withHeader("header1", "value1");
		httpMock.stubFor(mockReq.willReturn(mockResp));

		HttpClient httpClient = null;
		try {
			String url = baseUrl();
			httpClient = HttpClient.head(url);
			httpClient.execute();

			Assert.assertEquals(httpClient.getResponseCode(), 200);
			Assert.assertEquals(httpClient.getResponseHeader("header1"), "value1");

			Assert.assertNull(httpClient.getResultObject());

		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
