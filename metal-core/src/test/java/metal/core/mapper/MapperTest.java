/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mapper;

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
import metal.core.model.TestModel;
import metal.core.test.TestBase;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class MapperTest extends TestBase {

	@Resource(name = "test-core-xmlMapper")
	private Mapper xmlMapper;

	@SuppressWarnings("unchecked")
	@Test
	public void testXmlMapper_write() {
		TestModel model = null, model2 = null;
		String xml = null, xml2 = null;

		try {
			model = new TestModel();
			model.setProperty1("property1");
			model.setProperty2("property2");
			model.setProperties(Arrays.asList(
					new Property<Class<?>, Object>(Integer.class, 4),
					new Property<Class<?>, Object>(Long.class, 5L),
					new Property<Class<?>, Object>(Double.class, 7.123),
					new Property<Class<?>, Object>(Boolean.class, true),
					new Property<Class<?>, Object>(String.class, "ABC"),
					new Property<Class<?>, Object>(Date.class, TestHelper
							.dateValue("20131231", "yyyyMMdd")),
					new Property<Class<?>, Object>(String.class, ""),
					new Property<Class<?>, Object>((Class<?>) null, null),
					new Property<Class<?>, Object>(BaseObject.class, TestHelper
							.init(new BaseObject())),
					new Property<Class<?>, Object>(List.class, TestHelper
							.init(new ArrayList<Object>())),
					new Property<Class<?>, Object>(Map.class, TestHelper
							.init(new LinkedHashMap<String, Object>()))));
			xml = xmlMapper.write(model);
		} catch (Exception e) {
			fail(e);
		}

		try {
			model2 = xmlMapper.read(TestModel.class, xml);
			xml2 = xmlMapper.write(model2);
		} catch (Exception e) {
			fail(e);
		}

		assertEqualsIgnoreWhitespace(xml, xml2);
	}

	@Test
	public void testXmlMapper_read() {
		TestModel model;
		String xml = null, xml2 = null;

		try {
			xml = IOUtils.toString(source("testModel.xml"));
		} catch (Exception e) {
			fail(e);
		}

		try {
			model = xmlMapper.read(TestModel.class, xml);
			xml2 = xmlMapper.write(model);
		} catch (Exception e) {
			fail(e);
		}

		assertEqualsIgnoreWhitespace(xml, xml2);
	}

}
