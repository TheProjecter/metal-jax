/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.annotation.Resource;

import metal.core.model.AnnotatedObject;
import metal.core.model.BaseObject;
import metal.core.model.GenericObject;
import metal.core.test.TestBase;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class XmlMapperTest extends TestBase {

	@Resource(name="test-core-xmlMapper")
	private ModelMapper xmlMapper;
	
	@Test
	public void testMapper_write() {
		testMapper_write("ABC");
		testMapper_write(TestUtils.dateValue("20131231", "yyyyMMdd"));
		testMapper_write(TestUtils.init(new BaseObject()));
		testMapper_write(TestUtils.init(new AnnotatedObject()));
		testMapper_write(TestUtils.init(new GenericObject()));
		testMapper_write(TestUtils.init(new ArrayList<Object>()));
		testMapper_write(TestUtils.init(new LinkedHashMap<String,Object>()));
	}
	
	@Test
	public void testMapper_read() throws Exception {
		testMapper_read("ABC", "string.xml");
		testMapper_read(TestUtils.dateValue("20131231", "yyyyMMdd"), "date.xml");
		testMapper_read(TestUtils.init(new BaseObject()), "baseObject.xml");
		testMapper_read(TestUtils.init(new AnnotatedObject()), "annotatedObject.xml");
		testMapper_read(TestUtils.init(new GenericObject()), "genericObject.xml");
		testMapper_read(TestUtils.init(new ArrayList<Object>()), "list.xml");
		testMapper_read(TestUtils.init(new LinkedHashMap<String,Object>()), "map.xml");
	}
	
	void testMapper_write(Object object) {
		String xml = xmlMapper.write(object);
		Object object2 = xmlMapper.read(object.getClass(), xml);
		String xml2 = xmlMapper.write(object2);
		TestUtils.assertEquals(object, object2);
		assertEqualsIgnoreWhitespace(xml, xml2);
	}
	
	void testMapper_read(Object object, String file) throws Exception {
		String xml = IOUtils.toString(source(file));
		Object object2 = xmlMapper.read(object.getClass(), xml);
		String xml2 = xmlMapper.write(object2);
		TestUtils.assertEquals(object, object2);
		assertEqualsIgnoreWhitespace(xml, xml2);
	}
	
}
