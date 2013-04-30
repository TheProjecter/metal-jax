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

public class JsonMapperTest extends TestBase {

	@Resource(name="test-core-jsonMapper")
	private ModelMapper jsonMapper;
	
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
		testMapper_read("ABC", "string.json");
		testMapper_read(TestUtils.dateValue("20131231", "yyyyMMdd"), "date.json");
		testMapper_read(TestUtils.init(new BaseObject()), "baseObject.json");
		testMapper_read(TestUtils.init(new AnnotatedObject()), "annotatedObject.json");
		testMapper_read(TestUtils.init(new GenericObject()), "genericObject.json");
		testMapper_read(TestUtils.init(new ArrayList<Object>()), "list.json");
		testMapper_read(TestUtils.init(new LinkedHashMap<String,Object>()), "map.json");
	}
	
	void testMapper_write(Object object) {
		String json = jsonMapper.write(object);
		Object object2 = jsonMapper.read(object.getClass(), json);
		String json2 = jsonMapper.write(object2);
		TestUtils.assertEquals(object, object2);
		assertEqualsIgnoreWhitespace(json, json2);
	}
	
	void testMapper_read(Object object, String file) throws Exception {
		String json = IOUtils.toString(source(file));
		Object object2 = jsonMapper.read(object.getClass(), json);
		String json2 = jsonMapper.write(object2);
		TestUtils.assertEquals(object, object2);
		assertEqualsIgnoreWhitespace(json, json2);
	}
	
}
