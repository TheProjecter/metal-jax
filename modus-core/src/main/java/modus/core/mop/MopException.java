/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package modus.core.mop;

import modus.core.common.AnyException;
import modus.core.message.MessageCode;

public class MopException extends AnyException {

	public MopException(MessageCode code) {
		super(code);
	}

	public MopException(MessageCode code, Throwable cause) {
		super(code, cause);
	}

	public MopException(MessageCode code, Object... args) {
		super(code, args);
	}

	public MopException(MessageCode code, Throwable cause, Object... args) {
		super(code, cause, args);
	}

}
