/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.service;

import static metal.core.service.TestImpl.TestServiceMessageCode.*;

import org.springframework.stereotype.Service;

@Service("metal.core.service.TestService")
public class TestImpl implements TestService {
	
	enum TestServiceMessageCode implements metal.core.message.MessageCode {
		test
	}
	
	public void throwsServiceException() {
		throw new ServiceException(test);
	}
	
	public void throwsRuntimeException() {
		throw new RuntimeException(test.name());
	}
	
}
