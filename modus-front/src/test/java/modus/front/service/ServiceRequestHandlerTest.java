/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package modus.front.service;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import modus.core.mapper.Mapper;
import modus.core.mop.MethodDeclaration;
import modus.core.mop.ServiceRegistry;
import modus.core.test.TestBase;
import modus.front.common.HttpContentType;
import modus.front.model.RequestMessage;
import modus.front.model.ResponseMessage;
import modus.front.service.ServiceRequestHandler;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class ServiceRequestHandlerTest extends TestBase {

	@Resource(name = "modus.core.mapper.XmlMapper")
	private Mapper xmlMapper;
	
	@Resource(name="modus.front.service.TestService")
	private Object service;
	
	@Resource
	private ServiceRequestHandler handler;
	
	@Resource
	private ServiceRegistry serviceRegistry;

	@Test
	public void testInvoke() throws Exception {
		MethodDeclaration method = serviceRegistry.getServiceMethodDeclaration("/modus/front/Test", "hello");
		RequestMessage message = new RequestMessage(method.getParamDeclarations());
		Object[] params = xmlMapper.read(message, source("serviceRequest.xml")).getValues();
		ResponseMessage response = handler.invoke("hello", service, params, message.getMemberTypes());
		assertNotNull(response);
		assertEquals("test: 12345678, from: whoever", response.getResult());
	}

	@Test
	public void testHandleRequestForm() throws Exception {
		MockHttpServletRequest httpRequest = new MockHttpServletRequest("POST", "/modus/service/modus/front/Test/hello.xml");
		httpRequest.setServletPath("/service");
		httpRequest.setPathInfo("/modus/front/Test/hello.xml");
		httpRequest.addParameter("message", "12345678");
		httpRequest.addParameter("from", "whoever");
		httpRequest.setContentType(HttpContentType.FORM.contentType);
		
		String response1 = IOUtils.toString(source("serviceResponse.xml"));
		MockHttpServletResponse httpResponse = new MockHttpServletResponse();
		handler.handleRequest(httpRequest, httpResponse);
		String response2 = httpResponse.getContentAsString();
		
		assertEqualsIgnoreWhitespace(response1, response2);
	}

	@Test
	public void testHandleRequestXml() throws Exception {
		MockHttpServletRequest httpRequest = new MockHttpServletRequest("POST", "/modus/service/modus/front/Test/hello.xml");
		httpRequest.setServletPath("/service");
		httpRequest.setPathInfo("/modus/front/Test/hello.xml");
		httpRequest.setContent(IOUtils.toByteArray(source("serviceRequest.xml")));
		httpRequest.setContentType(HttpContentType.XML.contentType);
		
		String response1 = IOUtils.toString(source("serviceResponse.xml"));
		MockHttpServletResponse httpResponse = new MockHttpServletResponse();
		handler.handleRequest(httpRequest, httpResponse);
		String response2 = httpResponse.getContentAsString();
		
		assertEqualsIgnoreWhitespace(response1, response2);
	}

}
