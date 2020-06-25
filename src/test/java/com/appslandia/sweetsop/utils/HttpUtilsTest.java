package com.appslandia.sweetsop.utils;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.appslandia.common.utils.CollectionUtils;

public class HttpUtilsTest {

	@Test
	public void test() {
		List<String> values = CollectionUtils.toList("val1");
		String headerValue = HttpUtils.toHeaderValues(values);
		Assert.assertEquals(headerValue, "val1");
	}

	@Test
	public void test_multiValues() {
		List<String> values = CollectionUtils.toList("val1", "val2");
		String headerValue = HttpUtils.toHeaderValues(values);
		Assert.assertEquals(headerValue, "val1, val2");
	}
}
