/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.front;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import metal.core.mapper.Mapper;
import metal.core.mapper.Property;
import metal.core.mapper.TestHelper;
import metal.core.model.BaseObject;
import metal.core.test.TestBase;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class ResponseTest extends TestBase {

	@Resource(name = "test-jax-xmlMapper")
	private Mapper xmlMapper;

	@Test
	@SuppressWarnings("unchecked")
	public void testResponse_write() {
		ResponseMessage response = null, response2 = null;
		String xml = null, xml2 = null;

		try {
			response = new ResponseMessage();
			response.setStatus("hello");
			response.setResults(Arrays.asList(
					new Property<Class<?>, Object>(Integer.class, 4),
					new Property<Class<?>, Object>(Long.class, 5L),
					new Property<Class<?>, Object>(Double.class, 7.123),
					new Property<Class<?>, Object>(Boolean.class, true),
					new Property<Class<?>, Object>(String.class, "ABC"),
					new Property<Class<?>, Object>(Date.class, TestHelper.dateValue("20131231", "yyyyMMdd")),
					new Property<Class<?>, Object>(String.class, ""),
					new Property<Class<?>, Object>((Class<?>) null, null),
					new Property<Class<?>, Object>(BaseObject.class, TestHelper.init(new BaseObject())),
					new Property<Class<?>, Object>(List.class, TestHelper.init(new ArrayList<Object>())),
					new Property<Class<?>, Object>(Map.class, TestHelper.init(new LinkedHashMap<String, Object>()))));
			xml = xmlMapper.write(response);
		} catch (Exception e) {
			fail(e);
		}

		try {
			response2 = xmlMapper.read(ResponseMessage.class, xml);
			xml2 = xmlMapper.write(response2);
		} catch (Exception e) {
			fail(e);
		}

		assertEqualsIgnoreWhitespace(xml, xml2);
	}

	@Test
	public void testResponse_read() {
		ResponseMessage response;
		String xml = null, xml2 = null;

		try {
			xml = IOUtils.toString(source("response.xml"));
		} catch (Exception e) {
			fail(e);
		}

		try {
			response = xmlMapper.read(ResponseMessage.class, xml);
			xml2 = xmlMapper.write(response);
		} catch (Exception e) {
			fail(e);
		}

		assertEqualsIgnoreWhitespace(xml, xml2);
	}

}
