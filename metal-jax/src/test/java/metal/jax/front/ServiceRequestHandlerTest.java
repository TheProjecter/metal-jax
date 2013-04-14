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

import metal.core.mapper.Mapper;
import metal.core.test.TestBase;

import org.junit.Test;

public class ServiceRequestHandlerTest extends TestBase {

	@Resource(name = "test-jax-xmlMapper")
	private Mapper xmlMapper;
	
	@Resource(name="test-jax-testService")
	private Object service;
	
	@Resource(name="metal-jax-serviceRequestHandler")
	private ServiceRequestHandler handler;

	@Test
	public void testInvoke() {
		RequestMessage request = null;

		try {
			request = xmlMapper.read(RequestMessage.class, source("serviceRequest.xml"));
		} catch (Exception e) {
			fail(e);
		}
		
		ResponseMessage response = handler.invoke(null, service, request);
		assertNotNull(response);
	}

}
