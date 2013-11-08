/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.validation;

import static org.junit.Assert.*;

import java.util.List;

import javax.annotation.Resource;

import metal.core.message.MessageMapper;
import metal.core.model.TestObject;
import metal.core.test.TestBase;

import org.junit.Test;

public class ValidationMessageTest extends TestBase {

	@Resource
	private MessageMapper mapper;
	
	@Resource
	private Validator validator;
	
	@Test
	public void testValidator() {
		TestObject value = new TestObject("value1", "value", null);
		List<ValidationMessage> errors = validator.validate(value);
		assertEquals(errors.size(), 2);
		
		assertEquals("Validation failed: class: TestObject, property: name2, value: value, reason: size must be between 6 and 16", mapper.getMessage(errors.get(0)));
		assertEquals("Validation failed: class: TestObject, property: name3, value: null, reason: may not be null", mapper.getMessage(errors.get(1)));
	}

}
