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

public class JsonMapperTest extends TestBase {

	@Resource(name="test-jax.xmlMapper")
	private Mapper xmlMapper;
	
	@Resource(name="test-jax.jsonMapper")
	private Mapper jsonMapper;
	
	@Test
	public void testMapper0() {
		TestObject object = new TestObject();
		object.setStringValue("value1");
		object.setStringValue("value2");
		object.setStringValue("value3");
		String expected = "{" +
				"\"property1\":\"property1\"," +
				"\"property2\":\"property2\"," +
				"\"modelProperties\":[" +
					"4," +
					"\"ABC\"," +
					"{\"object\":{" +
						"\"name1\":\"value1\"," +
						"\"name2\":\"value2\"," +
						"\"name3\":\"value3\"" +
					"}}" +
				"]}";

		// test write
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			jsonMapper.serialize(new TestObject[]{object}, out);
			String actual = new String(out.toByteArray());
			assertEqualsIgnoreWhitespace(expected, actual);
		} catch (Exception e) {
			fail(e);
		}

	}

//	@Test
	public void testMapper1() {
		TestModel model = null;
		String expected = "{\"property1\":\"property1\",\"property2\":\"property2\",\"modelProperties\":[{\"int\":4},{\"string\":\"ABC\"},{\"object\":{\"name1\":\"value1\",\"name2\":\"value2\",\"name3\":\"value3\"}}]}";

		// test read/write
		try {
			model = jsonMapper.deserialize(TestModel.class, new ByteArrayInputStream(expected.getBytes()));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			jsonMapper.serialize(model, out);
			String actual = new String(out.toByteArray());
			assertEqualsIgnoreWhitespace(expected, actual);
		} catch (Exception e) {
			fail(e);
		}
	}

//	@Test
	public void testMapperInline() {
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
		String expected = "{\"property1\":\"property1\",\"property2\":\"property2\",\"modelProperties\":{\"int\":4,\"string\":\"ABC\",\"object\":{\"name1\":\"value1\",\"name2\":\"value2\",\"name3\":\"value3\"}}}";

		// test write
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			jsonMapper.serialize(model, out);
			String actual = new String(out.toByteArray());
			assertEqualsIgnoreWhitespace(expected, actual);
		} catch (Exception e) {
			fail(e);
		}

		// test read/write
		try {
			model = jsonMapper.deserialize(TestModel.class, new ByteArrayInputStream(expected.getBytes()));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			jsonMapper.serialize(model, out);
			String actual = new String(out.toByteArray());
			assertEqualsIgnoreWhitespace(expected, actual);
		} catch (Exception e) {
			fail(e);
		}
	}

//	@Test
	public void testMapper() {
		TestModel model = null;
		try {
			model = xmlMapper.deserialize(TestModel.class, source("testModel.xml"));
		} catch (Exception e) {
			fail(e);
		}
		
		assertModel(model);
		
		String json = null;
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			jsonMapper.serialize(model, out);
			json = new String(out.toByteArray());
		} catch (Exception e) {
			fail(e);
		}
		
		try {
			model = jsonMapper.deserialize(TestModel.class, new ByteArrayInputStream(json.getBytes()));
		} catch (Exception e) {
			fail(e);
		}
		
		assertModel(model);
		
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

	protected void assertModel(TestModel model) {
		assertNotNull(model);
		assertTrue("property1".equals(model.getProperty1()));
		assertTrue("property2".equals(model.getProperty2()));
		assertNotNull(model.getModelProperties());
		assertTrue(model.getModelProperties().length == 9);
		assertEquals(4, model.getModelProperties()[0].getValue());
		assertTrue(model.getModelProperties()[8].getValue() instanceof TestObject);
		assertEquals("value1", ((TestObject)model.getModelProperties()[8].getValue()).getStringValue());
	}
}
