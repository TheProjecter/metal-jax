/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.common;

import metal.core.message.Message;

@SuppressWarnings("serial")
public class AnyException extends RuntimeException implements Message {

	private String[] codes;
	private Object[] arguments;

	public AnyException(String code) {
		super(code);
		setCodes(code);
	}

	public AnyException(String code, Object... args) {
		super(code);
		setCodes(code);
		this.arguments = args;
	}

	public AnyException(String code, Throwable cause) {
		super(code, cause);
		setCodes(code);
	}

	public AnyException(String code, Throwable cause, Object... args) {
		super(code, cause);
		setCodes(code);
		this.arguments = args;
	}

	private void setCodes(String code) {
		String qualifiedCode = new StringBuilder(getClass().getPackage().getName()).append(".").append(code).toString();
		this.codes = new String[]{qualifiedCode, code};
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
