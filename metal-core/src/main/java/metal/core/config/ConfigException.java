/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.config;

import metal.core.common.AnyException;

@SuppressWarnings("serial")
public class ConfigException extends AnyException {

	public ConfigException(String code) {
		super(code);
	}

	public ConfigException(String code, Object... args) {
		super(code, args);
	}

	public ConfigException(String code, Throwable cause) {
		super(code, cause);
	}

	public ConfigException(String code, Throwable cause, Object... args) {
		super(code, cause, args);
	}

}
