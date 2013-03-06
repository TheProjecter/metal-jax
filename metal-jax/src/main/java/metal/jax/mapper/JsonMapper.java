/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.mapper;

import java.io.InputStream;
import java.io.OutputStream;

import org.codehaus.jackson.map.ObjectMapper;

public class JsonMapper implements Mapper {
	
	private ObjectMapper mapper = new ObjectMapper();
	
	public JsonMapper() {
		mapper.getJsonFactory();
	}
	
	@Override
	public <T> T read(Class<T> type, InputStream input) {
		try {
			return mapper.readValue(input, type);
		} catch (Exception ex) {
			throw new MapperException("UnexpectedException", ex);
		}
	}

	@Override
	public void write(Object object, OutputStream output) {
		try {
			mapper.writeValue(output, object);
		} catch (Exception ex) {
			throw new MapperException("UnexpectedException", ex);
		}
	}
	
}
