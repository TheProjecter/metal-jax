/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package modus.core.message;

import static modus.core.message.MessageMapperTest.MessageMapperTestMessageCode.*;
import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import modus.core.common.AnyException;
import modus.core.message.MessageMapper;
import modus.core.test.TestBase;

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
