/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mapper;

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
	public void testBaseObject_write() {
		BaseObject object = null, object2 = null;
		String xml = null, xml2 = null;

		try {
			object = TestHelper.init(new BaseObject());
			xml = xmlMapper.write(object);
		} catch (Exception e) {
			fail(e);
		}
		
		try {
			object2 = xmlMapper.read(BaseObject.class, xml);
			xml2 = xmlMapper.write(object2);
		} catch (Exception e) {
			fail(e);
		}
		
		TestHelper.assertEquals(object, object2);
		assertEqualsIgnoreWhitespace(xml, xml2);
	}
	
	@Test
	public void testBaseObject_read() {
		BaseObject object = null, object2 = null;
		String xml = null, xml2 = null;

		try {
			object = TestHelper.init(new BaseObject());
			xml = IOUtils.toString(source("baseObject.xml"));
		} catch (Exception e) {
			fail(e);
		}

		try {
			object2 = xmlMapper.read(BaseObject.class, xml);
			xml2 = xmlMapper.write(object2);
		} catch (Exception e) {
			fail(e);
		}

		TestHelper.assertEquals(object, object2);
		assertEqualsIgnoreWhitespace(xml, xml2);
	}
	
	@Test
	public void testAnnotatedObject_write() {
		AnnotatedObject object = null, object2 = null;
		String xml = null, xml2 = null;

		try {
			object = TestHelper.init(new AnnotatedObject());
			xml = xmlMapper.write(object);
		} catch (Exception e) {
			fail(e);
		}
		
		try {
			object2 = xmlMapper.read(AnnotatedObject.class, xml);
			xml2 = xmlMapper.write(object2);
		} catch (Exception e) {
			fail(e);
		}
		
		TestHelper.assertEquals(object, object2);
		assertEqualsIgnoreWhitespace(xml, xml2);
	}
	
	@Test
	public void testAnnotatedObject_read() {
		AnnotatedObject object = null, object2 = null;
		String xml = null, xml2 = null;

		try {
			object = TestHelper.init(new AnnotatedObject());
			xml = IOUtils.toString(source("annotatedObject.xml"));
		} catch (Exception e) {
			fail(e);
		}

		try {
			object2 = xmlMapper.read(AnnotatedObject.class, xml);
			xml2 = xmlMapper.write(object2);
		} catch (Exception e) {
			fail(e);
		}

		TestHelper.assertEquals(object, object2);
		assertEqualsIgnoreWhitespace(xml, xml2);
	}
	
	@Test
	public void testGenericObject_write() {
		GenericObject object = null, object2 = null;
		String xml = null, xml2 = null;

		try {
			object = TestHelper.init(new GenericObject());
			xml = xmlMapper.write(object);
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
	
	@Test
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
