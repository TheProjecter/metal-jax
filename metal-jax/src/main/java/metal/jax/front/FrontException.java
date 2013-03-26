/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.front;

import metal.core.common.AnyException;
import metal.core.message.MessageCode;

@SuppressWarnings("serial")
public class FrontException extends AnyException {

	enum FrontMessageCode implements MessageCode {
		UnknownService,
		UnknownServicePath
	}
	
	public FrontException(MessageCode code) {
		super(code);
	}

	public FrontException(MessageCode code, Object... args) {
		super(code, args);
	}

	public FrontException(MessageCode code, Throwable cause) {
		super(code, cause);
	}

	public FrontException(MessageCode code, Throwable cause, Object... args) {
		super(code, cause, args);
	}

}
