/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mapper;

import javax.annotation.Resource;

import metal.core.model.BaseObject;
import metal.core.model.GenericObject;
import metal.core.test.TestBase;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class JsonMapperTest extends TestBase {

	@Resource(name="test-core.xmlMapper")
	private Mapper xmlMapper;
	
	@Resource(name="test-core.jsonMapper")
	private Mapper jsonMapper;
	
	@Test
	public void testBaseObject_write() {
		BaseObject object = null, object2 = null;
		String json = null, json2 = null;

		try {
			object = TestHelper.init(new BaseObject());
			json = jsonMapper.write(object);
		} catch (Exception e) {
			fail(e);
		}
		
		try {
			object2 = jsonMapper.read(BaseObject.class, json);
			json2 = jsonMapper.write(object2);
		} catch (Exception e) {
			fail(e);
		}
		
		TestHelper.assertEquals(object, object2);
		assertEqualsIgnoreWhitespace(json, json2);
	}
	
	@Test
	public void testBaseObject_read() {
		BaseObject object = null, object2 = null;
		String json = null, json2 = null;

		try {
			object = TestHelper.init(new BaseObject());
			json = IOUtils.toString(source("baseObject.json"));
		} catch (Exception e) {
			fail(e);
		}

		try {
			object2 = jsonMapper.read(BaseObject.class, json);
			json2 = jsonMapper.write(object2);
		} catch (Exception e) {
			fail(e);
		}

		TestHelper.assertEquals(object, object2);
		assertEqualsIgnoreWhitespace(json, json2);
	}
	
	@Test
	public void testGenericObject_write() {
		GenericObject object = null, object2 = null;
		String json = null, json2 = null;

		try {
			object = TestHelper.init(new GenericObject());
			json = jsonMapper.write(object);
		} catch (Exception e) {
			fail(e);
		}
		
		try {
			object2 = jsonMapper.read(GenericObject.class, json);
			json2 = jsonMapper.write(object2);
		} catch (Exception e) {
			fail(e);
		}
		
		TestHelper.assertEquals(object, object2);
		assertEqualsIgnoreWhitespace(json, json2);
	}
	
//	@Test
	public void testGenericObject_read() {
		GenericObject object = null, object2 = null;
		String xml = null, xml2 = null;

		try {
			object = TestHelper.init(new GenericObject());
			xml = IOUtils.toString(source("genericObject.xml"));
		} catch (Exception e) {
			fail(e);
		}

		try {
			object2 = xmlMapper.read(GenericObject.class, xml);
			xml2 = xmlMapper.write(object2);
		} catch (Exception e) {
			fail(e);
		}

		TestHelper.assertEquals(object, object2);
		assertEqualsIgnoreWhitespace(xml, xml2);
	}

}
