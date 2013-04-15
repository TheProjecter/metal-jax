/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mapper;

import static metal.core.mapper.Adapter.Kind.*;
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

public class JsonMapper extends BaseModelMapper implements Adapter<Object, Object> {
	
	private ObjectMapper mapper;
	
	public JsonMapper() {
		final JsonMapper _this = this;
		JaxbAnnotationIntrospector introspector = new JaxbAnnotationIntrospector() {
			@Override
			protected XmlAdapter<Object, Object> findAdapter(Annotated am, boolean forSerialization) {
				XmlAdapter<Object,Object> adapter = super.findAdapter(am, forSerialization);
				if (adapter instanceof BaseAdapter) {
					((BaseAdapter<Object, Object>)adapter).setAdapter(_this);
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
	
	public Object marshal(Kind kind, Object object) {
		return marshalValue(kind, object, null);
	}
	
	public Object unmarshal(Kind kind, Object source) {
		return unmarshalValue(kind, source);
	}
	
	@SuppressWarnings("unchecked")
	public Object marshalValue(Kind kind, Object object, Class<?> clazz) {
		JavaType type = JavaType.typeOf(object, clazz);
		switch (type) {
		case LIST:
			return marshalList(kind, (List<Object>) object);
		case MAP:
			return marshalMap((Map<String, Object>) object);
		case LONG:
		case DATE:
		case OBJECT:
			return marshalSimple(object, type, clazz);
		default:
			return object;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Object unmarshalValue(Kind kind, Object source) {
		JavaType type = JavaType.typeOf(source, null);
		switch (type) {
		case LIST:
			return unmarshalList(kind, (List<Object>)source);
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
	
	protected Object marshalSimple(Object object, JavaType type, Class<?> clazz) {
		Map<String,Object> result = new HashMap<String,Object>();
		switch (type) {
		case LONG:
			result.put(type.name, object);
			break;
		case DATE:
			result.put(type.name, ((Date)object).getTime());
			break;
		case OBJECT:
		default:
			String typeName = modelName(object.getClass());
			if (typeName != null) {
				result.put(typeName, object);
			}
			break;
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	protected Object marshalList(Kind kind, List<Object> list) {
		List<Object> result = null;
		if (list != null) {
			result = new ArrayList<Object>();
			switch (kind) {
			case PROPERTYLIST:
				for (Object item : list) {
					Property<Class<?>, Object> property = (Property<Class<?>,Object>)item;
					result.add(marshalValue(VALUE, property.getValue(), property.getKey()));
				}
				break;
			case VALUE:
				for (Object item : list) {
					result.add(marshalValue(VALUE, item, null));
				}
				break;
			}
		}
		return result;
	}
	
	protected Object marshalMap(Map<String, Object> map) {
		Map<String, Object> result = null;
		if (map != null) {
			result = new LinkedHashMap<String, Object>();
			for (Map.Entry<String, Object> item : map.entrySet()) {
				result.put(item.getKey(), marshalValue(VALUE, item.getValue(), null));
			}
		}
		return result;
	}
	
	protected Object unmarshalSimple(Map<String, Object> map) {
		if (map.size() != 1) return null;
		Class<?> clazz = null;
		Map.Entry<String, Object> entry = map.entrySet().iterator().next();
		Object source = entry.getValue();
		JavaType type = JavaType.typeOf(entry.getKey());
		if (type == JavaType.OBJECT) {
			clazz = modelClass(entry.getKey());
			if (clazz == null) return null;
		}
		
		switch (type) {
		case LONG:
			return Long.valueOf(String.valueOf(source));
		case DATE:
			return new Date(Long.valueOf(String.valueOf(source)));
		case OBJECT:
		default:
			return mapper.convertValue(source, clazz);
		}
	}
	
	protected Object unmarshalList(Kind kind, List<Object> list) {
		List<Object> result = null;
		if (list != null) {
			result = new ArrayList<Object>();
			for (Object item : list) {
				switch (kind) {
				case PROPERTYLIST:
					result.add(new Property<Class<?>,Object>(modelClass(item), unmarshalValue(VALUE, item)));
					break;
				case VALUE:
					result.add(unmarshalValue(VALUE, item));
					break;
				}
			}
		}
		return result;
	}
	
	protected Object unmarshalMap(Map<String, Object> map) {
		Map<String, Object> result = null;
		if (map != null) {
			result = new LinkedHashMap<String, Object>();
			for (Map.Entry<String, Object> item : map.entrySet()) {
				result.put(item.getKey(), unmarshalValue(VALUE, item.getValue()));
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	protected Class<?> modelClass(Object source) {
		JavaType type = JavaType.typeOf(source, null);
		switch (type) {
		case MAP:
			Map<String, Object> map = (Map<String,Object>) source;
			if (map.size() == 1) {
				Map.Entry<String, Object> entry = map.entrySet().iterator().next();
				JavaType valueType = JavaType.typeOf(entry.getKey());
				if (valueType != JavaType.OBJECT) return valueType.type;
				Class<?> clazz = super.modelClass(entry.getKey());
				if (clazz != null) return clazz;
			}
			// no break
		default:
			return type.type;
		}
	}
	
}
