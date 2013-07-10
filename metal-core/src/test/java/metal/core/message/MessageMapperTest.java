/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.message;

import static metal.core.message.MessageMapperTest.MessageMapperTestMessageCode.*;
import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import metal.core.common.AnyException;
import metal.core.message.MessageMapper;
import metal.core.test.TestBase;

import org.junit.Test;

public class MessageMapperTest extends TestBase {

	enum MessageMapperTestMessageCode implements MessageCode {
		GenericException,
		SpecificException
	}
	
	static class TestException extends AnyException {
		protected TestException(MessageCode code, Object... args) { super(code, args); }
	}
	
	@Resource
	private MessageMapper mapper;
	
	@Test
	public void testErrorMessage() {
		assertEquals("GenericException: a, b and c", mapper.getMessage(new TestException(GenericException, "a", "b", "c")));
		assertEquals("MoreSpecificException: a, b and c", mapper.getMessage(new TestException(SpecificException, "a", "b", "c")));
	}

}
