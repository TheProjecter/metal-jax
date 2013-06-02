/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import metal.core.model.AnnotatedObject;
import metal.core.model.BaseObject;
import metal.core.model.GenericObject;
import metal.core.test.TestBase;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class XmlMapperTest extends TestBase {

	@Resource(name="metal-core-xmlMapper")
	private Mapper xmlMapper;
	
	@Test
	public void testMapper_write() throws Exception {
		testMapper_write("ABC", String.class);
		testMapper_write(TestUtils.dateValue("20131231", "yyyyMMdd"), Date.class);
		testMapper_write(TestUtils.init(new BaseObject()), BaseObject.class);
		testMapper_write(TestUtils.init(new AnnotatedObject()), AnnotatedObject.class);
		testMapper_write(TestUtils.init(new GenericObject()), GenericObject.class);
		testMapper_write(TestUtils.init(new ArrayList<Object>()), List.class);
		testMapper_write(TestUtils.init(new LinkedHashMap<String,Object>()), Map.class);
	}
	
	@Test
	public void testMapper_read() throws Exception {
		testMapper_read("ABC", String.class, "string.xml");
		testMapper_read(TestUtils.dateValue("20131231", "yyyyMMdd"), Date.class, "date.xml");
		testMapper_read(TestUtils.init(new BaseObject()), BaseObject.class, "baseObject.xml");
		testMapper_read(TestUtils.init(new AnnotatedObject()), AnnotatedObject.class, "annotatedObject.xml");
		testMapper_read(TestUtils.init(new GenericObject()), GenericObject.class, "genericObject.xml");
		testMapper_read(TestUtils.init(new ArrayList<Object>()), List.class, "list.xml");
		testMapper_read(TestUtils.init(new LinkedHashMap<String,Object>()), Map.class, "map.xml");
	}
	
	<T> void testMapper_write(T object, Class<T> clazz) throws Exception {
		String xml = xmlMapper.write(object);
		testMapper(object, clazz, xml);
	}
	
	<T> void testMapper_read(T object, Class<T> clazz, String file) throws Exception {
		String xml = IOUtils.toString(source(file));
		testMapper(object, clazz, xml);
	}
	
	<T> void testMapper(T object, Class<T> clazz, String xml) throws Exception {
		T object2 = xmlMapper.read(clazz, xml);
		String xml2 = xmlMapper.write(object2);
		TestUtils.assertEquals(object, object2);
		assertEqualsIgnoreWhitespace(xml, xml2);
	}
	
}
