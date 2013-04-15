/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.front;

import javax.annotation.Resource;

import metal.core.mapper.ModelMapper;
import metal.core.mapper.TestHelper;
import metal.core.model.BaseObject;
import metal.core.test.TestBase;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class ResponseTest extends TestBase {

	@Resource(name = "test-jax-xmlMapper")
	private ModelMapper xmlMapper;

	@Test
	public void testResponse_write() {
		ResponseMessage response = null, response2 = null;
		String xml = null, xml2 = null;

		try {
			response = new ResponseMessage();
			response.setMessage("hello world");
			response.setMessageCode("hello");
			response.setResult(TestHelper.init(new BaseObject()));
			xml = xmlMapper.write(response);
		} catch (Exception e) {
			fail(e);
		}

		try {
			response2 = xmlMapper.read(ResponseMessage.class, xml);
			xml2 = xmlMapper.write(response2);
		} catch (Exception e) {
			fail(e);
		}

		assertEqualsIgnoreWhitespace(xml, xml2);
	}

	@Test
	public void testResponse_read() {
		ResponseMessage response;
		String xml = null, xml2 = null;

		try {
			xml = IOUtils.toString(source("response.xml"));
		} catch (Exception e) {
			fail(e);
		}

		try {
			response = xmlMapper.read(ResponseMessage.class, xml);
			xml2 = xmlMapper.write(response);
		} catch (Exception e) {
			fail(e);
		}

		assertEqualsIgnoreWhitespace(xml, xml2);
	}

}
