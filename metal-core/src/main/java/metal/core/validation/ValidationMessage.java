/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.validation;

import static metal.core.validation.ValidationMessageCode.*;

import javax.validation.ConstraintViolation;

import metal.core.message.Message;
import metal.core.message.MessageCode;

public class ValidationMessage implements Message {

	private MessageCode code;
	private String[] codes;
	private Object[] arguments;
	
	public ValidationMessage(ConstraintViolation<?> violation) {
		this(ValidationFailed, violation);
	}
	
	public ValidationMessage(MessageCode code, ConstraintViolation<?> violation) {
		setCode(code, violation);
		setArguments(violation);
	}
	
	private void setCode(MessageCode code, ConstraintViolation<?> violation) {
		this.code = code;
		this.codes = new String[]{ MessageCode.Format.format(code), code.name(), violation.getMessageTemplate() };
	}
	
	private void setArguments(ConstraintViolation<?> violation) {
		this.arguments = new Object[]{
			violation.getRootBeanClass().getSimpleName(),
			violation.getPropertyPath().toString(),
			violation.getInvalidValue(),
			violation.getMessage()
		};
	}
	
	public MessageCode getCode() {
		return code;
	}

	@Override
	public String[] getCodes() {
		return codes;
	}

	@Override
	public Object[] getArguments() {
		return arguments;
	}

	@Override
	public String getDefaultMessage() {
		return (String)arguments[3];
	}

	public String getPropertyPath() {
		return (String)arguments[1];
	}
	
}
