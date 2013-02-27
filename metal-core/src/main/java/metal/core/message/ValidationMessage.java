/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.message;

import javax.validation.ConstraintViolation;

public class ValidationMessage implements Message {

	private String[] codes;
	private Object[] arguments;
	
	public ValidationMessage(ConstraintViolation<?> violation) {
		this(ValidationMessage.class.getSimpleName(), violation);
	}
	
	public ValidationMessage(String code, ConstraintViolation<?> violation) {
		setCodes(code, violation);
		setArguments(violation);
	}
	
	private void setCodes(String code, ConstraintViolation<?> violation) {
		this.codes = new String[]{ code, violation.getMessageTemplate() };
	}
	
	private void setArguments(ConstraintViolation<?> violation) {
		this.arguments = new Object[]{
			violation.getRootBeanClass().getSimpleName(),
			violation.getPropertyPath().toString(),
			violation.getInvalidValue(),
			violation.getMessage()
		};
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

}
