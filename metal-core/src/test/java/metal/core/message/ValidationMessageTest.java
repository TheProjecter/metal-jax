/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.message;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import metal.core.message.MessageMapper;
import metal.core.message.ValidationMessage;
import metal.core.model.TestObject;
import metal.core.test.TestBase;

import org.junit.Test;

public class ValidationMessageTest extends TestBase {

	@Resource
	private MessageMapper mapper;
	
	@Resource(name="metal-core-validator")
	private Validator validator;
	
	@Test
	public void testValidator() {
		TestObject object = new TestObject("value1", "value", null);
		Set<ConstraintViolation<TestObject>> errors = validator.validate(object);
		assertEquals(errors.size(), 2);
		
		Map<String,ConstraintViolation<TestObject>> map = new HashMap<String,ConstraintViolation<TestObject>>();
		for (ConstraintViolation<TestObject> error : errors) {
			map.put(error.getPropertyPath().toString(), error);
		}
		
		assertEquals("Validation failed: class: TestObject, property: name2, value: value, reason: size must be between 6 and 16", mapper.getMessage(new ValidationMessage(map.get("name2"))));
		assertEquals("Validation failed: class: TestObject, property: name3, value: null, reason: may not be null", mapper.getMessage(new ValidationMessage(map.get("name3"))));
	}

}
