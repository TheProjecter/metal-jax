/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.message;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import metal.core.common.AnyException;
import metal.core.message.MessageMapper;
import metal.core.test.TestBase;

import org.junit.Test;

public class MessageMapperTest extends TestBase {

	@Resource(name="metal-core-messageMapper")
	private MessageMapper mapper;
	
	@Test
	public void testErrorMessage() {
		assertEquals("GenericException: a, b and c", mapper.getMessage(new AnyException("GenericException", "a", "b", "c")));
		assertEquals("MoreSpecificException: a, b and c", mapper.getMessage(new AnyException("SpecificException", "a", "b", "c")));
	}

}
