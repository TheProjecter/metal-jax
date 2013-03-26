/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.common;

import metal.core.message.Message;
import metal.core.message.MessageCode;

@SuppressWarnings("serial")
public class AnyException extends RuntimeException implements Message {

	private String[] codes;
	private Object[] arguments;

	public AnyException(MessageCode code) {
		super(code.name());
		setCodes(code);
	}

	public AnyException(MessageCode code, Object... args) {
		this(code);
		this.arguments = args;
	}

	public AnyException(MessageCode code, Throwable cause) {
		super(code.name(), cause);
		setCodes(code);
	}

	public AnyException(MessageCode code, Throwable cause, Object... args) {
		this(code, cause);
		this.arguments = args;
	}

	private void setCodes(MessageCode code) {
		String qualifiedCode = new StringBuilder(getClass().getPackage().getName()).append(".").append(code).toString();
		this.codes = new String[]{qualifiedCode, code.name()};
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
		return getMessage();
	}
	
}
