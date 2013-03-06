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
	public void testMapper() {
		TestModel model = null;
		try {
			model = xmlMapper.read(TestModel.class, source("testModel.xml"));
		} catch (Exception e) {
			fail(e);
		}
		
		assertModel(model);
		
		String json = null;
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			jsonMapper.write(model, out);
			json = new String(out.toByteArray());
		} catch (Exception e) {
			fail(e);
		}
		
		try {
			model = jsonMapper.read(TestModel.class, new ByteArrayInputStream(json.getBytes()));
		} catch (Exception e) {
			fail(e);
		}
		
		assertModel(model);
		
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			xmlMapper.write(model, out);
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
		assertNotNull(model.getProperties());
		assertTrue(model.getProperties().length == 9);
		assertEquals(4, model.getProperties()[0].getValue());
		assertTrue(model.getProperties()[8].getValue() instanceof TestObject);
		assertEquals("value1", ((TestObject)model.getProperties()[8].getValue()).getName1());
	}
}
