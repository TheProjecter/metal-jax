/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public abstract class BaseMapper implements Mapper {

	@Override
	public <T> T read(Class<T> type, String input) {
		return read(type, new ByteArrayInputStream(input.getBytes()));
	}

	@Override
	public String write(Object object) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		write(object, out);
		return new String(out.toByteArray());
	}

}
