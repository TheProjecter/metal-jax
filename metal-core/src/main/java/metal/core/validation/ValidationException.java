/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.validation;

import java.util.List;

import metal.core.common.AnyException;

public class ValidationException extends AnyException {

	private List<ValidationMessage> messages;
	
	public ValidationException(ValidationMessageCode code, List<ValidationMessage> messages) {
		super(code);
		this.messages = messages;
	}

	public List<ValidationMessage> getMessages() {
		return this.messages;
	}
	
}
