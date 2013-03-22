/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mapper;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

public class JsonMapper extends BaseMapper implements Mapper {
	
	private ObjectMapper mapper = new ObjectMapper();
	
	public JsonMapper() {
		JaxbAnnotationIntrospector introspector = new JaxbAnnotationIntrospector() {
			@Override
			protected XmlAdapter<Object, Object> findAdapter(Annotated am, boolean forSerialization) {
				XmlAdapter<Object,Object> adapter = super.findAdapter(am, forSerialization);
				return adapter;
			}
		};
//		mapper.getSerializationConfig().enable(Feature.WRAP_ROOT_VALUE);
		mapper.getSerializationConfig().setAnnotationIntrospector(introspector);
		mapper.getDeserializationConfig().setAnnotationIntrospector(introspector);
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
