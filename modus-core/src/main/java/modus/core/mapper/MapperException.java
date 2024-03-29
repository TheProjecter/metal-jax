/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package modus.core.mapper;

import modus.core.common.AnyException;

public class MapperException extends AnyException {

	public MapperException(MapperMessageCode code) {
		super(code);
	}

	public MapperException(MapperMessageCode code, Throwable cause) {
		super(code, cause, cause.getClass().getName(), cause.getMessage());
	}

	public MapperException(MapperMessageCode code, Object... args) {
		super(code, args);
	}

	public MapperException(MapperMessageCode code, Throwable cause, Object... args) {
		super(code, cause, args);
	}

}
