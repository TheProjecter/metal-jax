/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package modus.core.validation;

import static modus.core.validation.ValidationMessageCode.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

public class Validator {

	static class MessageComparator implements Comparator<ValidationMessage> {
		@Override
		public int compare(ValidationMessage m1, ValidationMessage m2) {
			return m1.getPropertyPath().compareTo(m2.getPropertyPath());
		}
	}
	
	private javax.validation.Validator validator;
	
	public void setValidator(javax.validation.Validator validator) {
		this.validator = validator;
	}
	
	public void assertValid(Object[] values) {
		List<ValidationMessage> messages = validate(values);
		if (messages != null) {
			throw new ValidationException(ValidationFailed, messages);
		}
	}
	
	public List<ValidationMessage> validate(Object[] values) {
		List<ValidationMessage> messages = null;
		if (values != null) {
			for (Object value : values) {
				messages = validate(value, messages);
			}
		}
		return sort(messages);
	}
	
	public List<ValidationMessage> validate(Object value) {
		return sort(validate(value, null));
	}
	
	private List<ValidationMessage> validate(Object value, List<ValidationMessage> messages) {
		if (validator != null) {
			Set<ConstraintViolation<Object>> errors = validator.validate(value);
			for (ConstraintViolation<Object> error : errors) {
				if (messages == null) messages = new ArrayList<ValidationMessage>();
				messages.add(new ValidationMessage(error));
			}
		}
		return messages;
	}

	private List<ValidationMessage> sort(List<ValidationMessage> messages) {
		if (messages != null && messages.size() > 1) {
			Collections.sort(messages, new MessageComparator());
		}
		return messages;
	}
	
}
