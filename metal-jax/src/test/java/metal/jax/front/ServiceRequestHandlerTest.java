/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.front;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import metal.core.mapper.ModelMapper;
import metal.core.mop.NameDeclaration;
import metal.core.test.TestBase;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class ServiceRequestHandlerTest extends TestBase {

	@Resource(name = "test-jax-xmlMapper")
	private ModelMapper xmlMapper;
	
	@Resource(name="test-jax-testService")
	private Object service;
	
	@Resource(name="metal-jax-serviceRequestHandler")
	private ServiceRequestHandler handler;

	@Test
	public void testInvoke() throws Exception {
		RequestMessage message = new RequestMessage(new NameDeclaration("message", Long.class), new NameDeclaration("from", String.class));
		Object[] params = xmlMapper.read(message, source("serviceRequest.xml")).getValues();
		ResponseMessage response = handler.invoke("hello", service, params, message.getDeclaredTypes());
		assertNotNull(response);
		assertEquals("test: 12345678, from: whoever", response.getResult());
	}

	@Test
	public void testHandleRequestForm() throws Exception {
		MockHttpServletRequest httpRequest = new MockHttpServletRequest("POST", "/metal-jax/service/test/jax/test/hello");
		httpRequest.setServletPath("/service");
		httpRequest.setPathInfo("/test/jax/test/hello");
		httpRequest.addParameter("message", "12345678");
		httpRequest.addParameter("from", "whoever");
		httpRequest.setContentType(HttpContentType.FORM.type);
		
		String response1 = IOUtils.toString(source("serviceResponse.xml"));
		MockHttpServletResponse httpResponse = new MockHttpServletResponse();
		handler.handleRequest(httpRequest, httpResponse);
		String response2 = httpResponse.getContentAsString();
		
		assertEqualsIgnoreWhitespace(response1, response2);
	}

	@Test
	public void testHandleRequestXml() throws Exception {
		MockHttpServletRequest httpRequest = new MockHttpServletRequest("POST", "/metal-jax/service/test/jax/test/hello");
		httpRequest.setServletPath("/service");
		httpRequest.setPathInfo("/test/jax/test/hello");
		httpRequest.setContent(IOUtils.toByteArray(source("serviceRequest.xml")));
		httpRequest.setContentType(HttpContentType.XML.type);
		
		String response1 = IOUtils.toString(source("serviceResponse.xml"));
		MockHttpServletResponse httpResponse = new MockHttpServletResponse();
		handler.handleRequest(httpRequest, httpResponse);
		String response2 = httpResponse.getContentAsString();
		
		assertEqualsIgnoreWhitespace(response1, response2);
	}

}
