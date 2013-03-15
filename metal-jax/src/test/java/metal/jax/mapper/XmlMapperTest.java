/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.mapper;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.annotation.Resource;

import metal.core.test.TestBase;
import metal.jax.model.BaseObject;
import metal.jax.model.GenericObject;
import metal.jax.model.Property;
import metal.jax.model.TestModel;
import metal.jax.model.TestObject;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class XmlMapperTest extends TestBase {

	@Resource(name="test-jax.xmlMapper")
	private Mapper xmlMapper;
	
	@Test
	public void testBaseObject_write() {
		BaseObject object = null, object2 = null;
		String xml = null, xml2 = null;

		try {
			// create object, then serialize
			object = TestHelper.initBase(new BaseObject());
			xml = xmlMapper.serialize(object);
			
			// de-serialize object, then serialize
			object2 = xmlMapper.deserialize(BaseObject.class, xml);
			xml2 = xmlMapper.serialize(object2);
		} catch (Exception e) {
			fail(e);
		}
		
		assertTrue(TestHelper.baseEquals(object, object2));
		assertEqualsIgnoreWhitespace(xml, xml2);
	}
	
	@Test
	public void testBaseObject_read() {
		BaseObject object = null, object2 = null;
		String xml = null, xml2 = null;

		try {
			object = TestHelper.initBase(new BaseObject());
			xml = IOUtils.toString(source("baseObject.xml"));
			
			// de-serialize object, then serialize
			object2 = xmlMapper.deserialize(BaseObject.class, xml);
			xml2 = xmlMapper.serialize(object2);
		} catch (Exception e) {
			fail(e);
		}

		assertTrue(TestHelper.baseEquals(object, object2));
		assertEqualsIgnoreWhitespace(xml, xml2);
	}
	
	@Test
	public void testGenericObject_write() {
		GenericObject object = null, object2 = null;
		String xml = null, xml2 = null;

		try {
			// create object, then serialize
			object = TestHelper.initBase(new GenericObject());
			object.setObjectValue(TestHelper.initBase(new BaseObject()));
			xml = xmlMapper.serialize(object);
			
			// de-serialize object, then serialize
			object2 = xmlMapper.deserialize(GenericObject.class, xml);
			xml2 = xmlMapper.serialize(object2);
		} catch (Exception e) {
			fail(e);
		}
		
		assertTrue(TestHelper.baseEquals(object, object2));
		assertEqualsIgnoreWhitespace(xml, xml2);
	}
	
//	@Test
	public void testMapper_testObject1() {
		TestObject object = null;
		try {
			object = xmlMapper.deserialize(TestObject.class, source("testObject.xml"));
		} catch (Exception e) {
			fail(e);
		}
		
		assertNotNull(object);
//		assertTrue("property1".equals(model.getProperty1()));
//		assertTrue("property2".equals(model.getProperty2()));
//		assertNotNull(model.getModelProperties());
//		assertTrue(model.getModelProperties().length == 9);
//		assertEquals(4, model.getModelProperties()[0].getValue());
//		assertTrue(model.getModelProperties()[8].getValue() instanceof TestObject);
//		assertEquals("value1", ((TestObject)model.getModelProperties()[8].getValue()).getStringValue());
		
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			xmlMapper.serialize(object, out);
			String expected = IOUtils.toString(source("testObject.xml"));
			String actual = new String(out.toByteArray());
			assertEqualsIgnoreWhitespace(expected, actual);
		} catch (Exception e) {
			fail(e);
		}
		
	}
	
//	@Test
	public void testMapper_testObject2() {
		TestObject object = new TestObject();
		object.setStringValue("value1");
		object.setStringValue("value2");
		object.setStringValue("value3");
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
			"<object>" +
				"<name1>value1</name1>" +
				"<name2>value2</name2>" +
				"<name3>value3</name3>" +
			"</object>";

		// test write
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			xmlMapper.serialize(object, out);
			String actual = new String(out.toByteArray());
			assertEqualsIgnoreWhitespace(expected, actual);
		} catch (Exception e) {
			fail(e);
		}

		// test read/write
		try {
			object = xmlMapper.deserialize(TestObject.class, new ByteArrayInputStream(expected.getBytes()));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			xmlMapper.serialize(object, out);
			String actual = new String(out.toByteArray());
			assertEqualsIgnoreWhitespace(expected, actual);
		} catch (Exception e) {
			fail(e);
		}
	}

//	@Test
	public void testMapper_testModel() {
		TestModel model = new TestModel();
		TestObject object;
		model.setProperty1("property1");
		model.setProperty2("property2");
		model.setModelProperties(new Property[3]);
		model.getModelProperties()[0] = new Property(Integer.class, Integer.valueOf(4));
		model.getModelProperties()[1] = new Property(String.class, "ABC");
		model.getModelProperties()[2] = new Property(TestObject.class, object = new TestObject());
		object.setStringValue("value1");
		object.setStringValue("value2");
		object.setStringValue("value3");
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
				"<model>" +
					"<property1>property1</property1>" +
					"<property2>property2</property2>" +
					"<modelProperties>" +
						"<int>4</int>" +
						"<string>ABC</string>" +
						"<object>" +
							"<name1>value1</name1>" +
							"<name2>value2</name2>" +
							"<name3>value3</name3>" +
						"</object>" +
					"</modelProperties>" +
				"</model>";

		// test write
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			xmlMapper.serialize(model, out);
			String actual = new String(out.toByteArray());
			assertEqualsIgnoreWhitespace(expected, actual);
		} catch (Exception e) {
			fail(e);
		}

		// test read/write
		try {
			model = xmlMapper.deserialize(TestModel.class, new ByteArrayInputStream(expected.getBytes()));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			xmlMapper.serialize(model, out);
			String actual = new String(out.toByteArray());
			assertEqualsIgnoreWhitespace(expected, actual);
		} catch (Exception e) {
			fail(e);
		}
	}

//	@Test
	public void testMapperFromFile() {
		TestModel model = null;
		try {
			model = xmlMapper.deserialize(TestModel.class, source("testModel.xml"));
		} catch (Exception e) {
			fail(e);
		}
		
		assertNotNull(model);
		assertTrue("property1".equals(model.getProperty1()));
		assertTrue("property2".equals(model.getProperty2()));
		assertNotNull(model.getModelProperties());
		assertTrue(model.getModelProperties().length == 9);
		assertEquals(4, model.getModelProperties()[0].getValue());
		assertTrue(model.getModelProperties()[8].getValue() instanceof TestObject);
		assertEquals("value1", ((TestObject)model.getModelProperties()[8].getValue()).getStringValue());
		
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			xmlMapper.serialize(model, out);
			String expected = IOUtils.toString(source("testModel.xml"));
			String actual = new String(out.toByteArray());
			assertEqualsIgnoreWhitespace(expected, actual);
		} catch (Exception e) {
			fail(e);
		}
		
	}

}
