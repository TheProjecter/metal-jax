/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.config;

import metal.core.common.AnyException;

public class ConfigException extends AnyException {

	public ConfigException(ConfigMessageCode code) {
		super(code);
	}

	public ConfigException(ConfigMessageCode code, Throwable cause) {
		super(code, cause);
	}

	public ConfigException(ConfigMessageCode code, Object... args) {
		super(code, args);
	}

	public ConfigException(ConfigMessageCode code, Throwable cause, Object... args) {
		super(code, cause, args);
	}

}
