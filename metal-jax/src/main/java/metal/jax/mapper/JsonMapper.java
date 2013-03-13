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
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

public class JsonMapper extends BaseMapper implements Mapper {
	
	private ObjectMapper mapper = new ObjectMapper();
	
	public JsonMapper() {
		JaxbAnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
//		mapper.getSerializationConfig().enable(Feature.WRAP_ROOT_VALUE);
//		mapper.getSerializationConfig().setAnnotationIntrospector(introspector);
//		mapper.getDeserializationConfig().setAnnotationIntrospector(introspector);
	}
	
	@Override
	public <T> T deserialize(Class<T> type, InputStream input) {
		try {
			return mapper.readValue(input, type);
		} catch (Exception ex) {
			throw new MapperException("UnexpectedException", ex);
		}
	}

	@Override
	public void serialize(Object object, OutputStream output) {
		try {
			mapper.writeValue(output, object);
		} catch (Exception ex) {
			throw new MapperException("UnexpectedException", ex);
		}
	}
	
}
