/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package modus.core.aop;

import javax.annotation.Resource;

import modus.core.message.Logger;
import modus.core.service.ServiceException;
import modus.core.service.TestService;
import modus.core.test.TestBase;

import org.junit.Test;

public class ExceptionAdviceTest extends TestBase {

	@Resource
	private TestService service;
	
	@Resource
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
