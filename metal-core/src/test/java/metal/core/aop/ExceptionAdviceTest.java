/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.aop;

import javax.annotation.Resource;

import metal.core.message.Logger;
import metal.core.service.ServiceException;
import metal.core.service.TestService;
import metal.core.test.TestBase;

import org.junit.Test;

public class ExceptionAdviceTest extends TestBase {

	@Resource(name="test-core.testService")
	private TestService service;
	
	@Resource(name="metal-core.logger")
	private Logger logger;
	
	@Test
	public void testServiceException() {
		try {
			logger.logInfo(this, "testServiceException", "test ERROR with service exception");
			service.throwsServiceException();
		} catch (ServiceException e) {
			// success
		} catch (Exception e) {
			fail(e);
		}
	}

	@Test
	public void testUnhandledException() {
		try {
			logger.logInfo(this, "testUnhandledException", "test ERROR with runtime exception");
			service.throwsRuntimeException();
		} catch (AopException e) {
			// success
		} catch (Exception e) {
			fail(e);
		}
	}

}
