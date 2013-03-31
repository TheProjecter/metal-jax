/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mapper;

import static metal.core.mapper.MapperMessageCode.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

public class JsonMapper extends BaseMapper implements Adapter<Object, Object> {
	
	private ObjectMapper mapper;
	
	public JsonMapper() {
		final JsonMapper _this = this;
		JaxbAnnotationIntrospector introspector = new JaxbAnnotationIntrospector() {
			@Override
			protected XmlAdapter<Object, Object> findAdapter(Annotated am, boolean forSerialization) {
				XmlAdapter<Object,Object> adapter = super.findAdapter(am, forSerialization);
				if (adapter instanceof JavaTypeAdapter) {
					((JavaTypeAdapter)adapter).setAdapter(_this);
				}
				return adapter;
			}
		};
		mapper = new ObjectMapper();
		mapper.getSerializationConfig().setAnnotationIntrospector(introspector);
		mapper.getDeserializationConfig().setAnnotationIntrospector(introspector);
	}
	
	@Override
	public <T> T read(Class<T> type, InputStream input) {
		try {
			return mapper.readValue(input, type);
		} catch (Exception ex) {
			throw new MapperException(UnexpectedException, ex);
		}
	}

	@Override
	public void write(Object object, OutputStream output) {
		try {
			mapper.writeValue(output, object);
		} catch (Exception ex) {
			throw new MapperException(UnexpectedException, ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public Object marshal(Object object) {
		JavaType type = JavaType.typeOf(object);
		switch (type) {
		case LONG:
		case DATE:
		case OBJECT:
			return marshalSimple(object, type);
		case LIST:
			return marshalList((List<Object>) object);
		case MAP:
			return marshalMap((Map<String, Object>) object);
		default:
			return object;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Object unmarshal(Object source) {
		JavaType type = JavaType.typeOf(source);
		switch (type) {
		case LIST:
			return unmarshalList((List<Object>)source);
		case MAP:
			Object result = unmarshalSimple((Map<String, Object>)source);
			if (result != null) {
				return result;
			} else {
				return unmarshalMap((Map<String, Object>)source);
			}
		default:
			return source;
		}
	}
	
	protected Object marshalSimple(Object object, JavaType type) {
		Map<String,Object> result = new HashMap<String,Object>();
		switch (type) {
		case LONG:
			result.put(type.name, String.valueOf(object));
			break;
		case DATE:
			result.put(type.name, String.valueOf(((Date)object).getTime()));
			break;
		case OBJECT:
		default:
			String name = modelName(object.getClass());
			if (name == null) return object;
			result.put(name, object);
			break;
		}
		return result;
	}

	protected Object marshalList(List<Object> list) {
		List<Object> result = null;
		if (list != null) {
			result = new ArrayList<Object>();
			for (Object item : list) {
				result.add(marshal(item));
			}
		}
		return result;
	}
	
	protected Object marshalMap(Map<String, Object> map) {
		Map<String, Object> result = null;
		if (map != null) {
			result = new LinkedHashMap<String, Object>();
			for (Map.Entry<String, Object> item : map.entrySet()) {
				result.put(item.getKey(), marshal(item.getValue()));
			}
		}
		return result;
	}
	
	protected Object unmarshalSimple(Map<String, Object> map) {
		JavaType type;
		Object source;
		Class<?> modelClass = null;
		if (map.size() == 1) {
			Map.Entry<String, Object> entry = map.entrySet().iterator().next();
			source = entry.getValue();
			type = JavaType.typeOf(entry.getKey());
			if (type == JavaType.OBJECT) {
				modelClass = modelClass(entry.getKey());
				if (modelClass == null) return null;
			}
		} else {
			return null;
		}
		
		switch (type) {
		case LONG:
			return Long.valueOf(String.valueOf(source));
		case DATE:
			return new Date(Long.valueOf(String.valueOf(source)));
		case OBJECT:
		default:
			return mapper.convertValue(source, modelClass);
		}
	}
	
	protected Object unmarshalList(List<Object> list) {
		List<Object> result = null;
		if (list != null) {
			result = new ArrayList<Object>();
			for (Object item : list) {
				result.add(unmarshal(item));
			}
		}
		return result;
	}
	
	protected Object unmarshalMap(Map<String, Object> map) {
		Map<String, Object> result = null;
		if (map != null) {
			result = new LinkedHashMap<String, Object>();
			for (Map.Entry<String, Object> item : map.entrySet()) {
				result.put(item.getKey(), unmarshal(item.getValue()));
			}
		}
		return result;
	}
	
}
