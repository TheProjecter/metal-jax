/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package modus.core.service;

import static modus.core.service.TestImpl.TestServiceMessageCode.*;

import org.springframework.stereotype.Service;

@Service("modus.core.service.TestService")
public class TestImpl implements TestService {
	
	enum TestServiceMessageCode implements modus.core.message.MessageCode {
		test
	}
	
	public void throwsServiceException() {
		throw new ServiceException(test);
	}
	
	public void throwsRuntimeException() {
		throw new RuntimeException(test.name());
	}
	
}
