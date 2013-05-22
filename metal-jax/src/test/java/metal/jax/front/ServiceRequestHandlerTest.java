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
import metal.core.mop.MethodDeclaration;
import metal.core.mop.ServiceRegistry;
import metal.core.test.TestBase;
import metal.jax.front.model.RequestMessage;
import metal.jax.front.model.ResponseMessage;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class ServiceRequestHandlerTest extends TestBase {

	@Resource(name = "metal-core-xmlMapper")
	private ModelMapper xmlMapper;
	
	@Resource(name="metal-jax-testService")
	private Object service;
	
	@Resource(name="metal-jax-serviceRequestHandler")
	private ServiceRequestHandler handler;
	
	@Resource(name="metal-core-serviceRegistry")
	private ServiceRegistry serviceRegistry;

	@Test
	public void testInvoke() throws Exception {
		MethodDeclaration method = serviceRegistry.getServiceMethodDeclaration("/metal/jax/test", "hello");
		RequestMessage message = new RequestMessage(method.getParamDeclarations());
		Object[] params = xmlMapper.read(message, source("serviceRequest.xml")).getValues();
		ResponseMessage response = handler.invoke("hello", service, params, message.getMemberTypes());
		assertNotNull(response);
		assertEquals("test: 12345678, from: whoever", response.getResult());
	}

	@Test
	public void testHandleRequestForm() throws Exception {
		MockHttpServletRequest httpRequest = new MockHttpServletRequest("POST", "/metal-jax/service/metal/jax/test/hello");
		httpRequest.setServletPath("/service");
		httpRequest.setPathInfo("/metal/jax/test/hello");
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
		MockHttpServletRequest httpRequest = new MockHttpServletRequest("POST", "/metal-jax/service/metal/jax/test/hello");
		httpRequest.setServletPath("/service");
		httpRequest.setPathInfo("/metal/jax/test/hello");
		httpRequest.setContent(IOUtils.toByteArray(source("serviceRequest.xml")));
		httpRequest.setContentType(HttpContentType.XML.type);
		
		String response1 = IOUtils.toString(source("serviceResponse.xml"));
		MockHttpServletResponse httpResponse = new MockHttpServletResponse();
		handler.handleRequest(httpRequest, httpResponse);
		String response2 = httpResponse.getContentAsString();
		
		assertEqualsIgnoreWhitespace(response1, response2);
	}

}
