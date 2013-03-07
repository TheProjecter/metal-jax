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
import metal.jax.model.Property;
import metal.jax.model.TestModel;
import metal.jax.model.TestObject;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class XmlMapperTest extends TestBase {

	@Resource(name="test-jax.xmlMapper")
	private Mapper mapper;
	
	@Test
	public void testMapperInline() {
		TestModel model = new TestModel();
		TestObject object;
		model.setProperty1("property1");
		model.setProperty2("property2");
		model.setModelProperties(new Property[3]);
		model.getModelProperties()[0] = new Property(Integer.class, Integer.valueOf(4));
		model.getModelProperties()[1] = new Property(String.class, "ABC");
		model.getModelProperties()[2] = new Property(TestObject.class, object = new TestObject());
		object.setName1("value1");
		object.setName2("value2");
		object.setName3("value3");
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><model><property1>property1</property1><property2>property2</property2><modelProperties><int>4</int><string>ABC</string><object><name1>value1</name1><name2>value2</name2><name3>value3</name3></object></modelProperties></model>";

		// test write
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			mapper.write(model, out);
			String actual = new String(out.toByteArray());
			assertEqualsIgnoreWhitespace(expected, actual);
		} catch (Exception e) {
			fail(e);
		}

		// test read/write
		try {
			model = mapper.read(TestModel.class, new ByteArrayInputStream(expected.getBytes()));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			mapper.write(model, out);
			String actual = new String(out.toByteArray());
			assertEqualsIgnoreWhitespace(expected, actual);
		} catch (Exception e) {
			fail(e);
		}
	}

	@Test
	public void testMapperFromFile() {
		TestModel model = null;
		try {
			model = mapper.read(TestModel.class, source("testModel.xml"));
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
		assertEquals("value1", ((TestObject)model.getModelProperties()[8].getValue()).getName1());
		
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			mapper.write(model, out);
			String expected = IOUtils.toString(source("testModel.xml"));
			String actual = new String(out.toByteArray());
			assertEqualsIgnoreWhitespace(expected, actual);
		} catch (Exception e) {
			fail(e);
		}
		
	}

}
