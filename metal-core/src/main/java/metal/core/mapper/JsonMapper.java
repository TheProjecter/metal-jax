/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mapper;

import static metal.core.mapper.MapperException.MapperMessageCode.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

public class JsonMapper extends BaseMapper implements Mapper, Adapter<Object, Object> {
	
	private ObjectMapper mapper = new ObjectMapper();
	
	public JsonMapper() {
		final JsonMapper _this = this;
		JaxbAnnotationIntrospector introspector = new JaxbAnnotationIntrospector() {
			@Override
			protected XmlAdapter<Object, Object> findAdapter(Annotated am, boolean forSerialization) {
				XmlAdapter<Object,Object> adapter = super.findAdapter(am, forSerialization);
				if (adapter instanceof DefaultAdapter) {
					((DefaultAdapter)adapter).setAdapter(_this);
				}
				return adapter;
			}

			/*@Override
			public String findRootName(AnnotatedClass ac) {
				String name = super.findRootName(ac);
				if (StringUtils.isEmpty(name)) {
					name = ac.getAnnotated().getSimpleName();
					if (Character.isUpperCase(name.charAt(0))) {
						char[] chars = name.toCharArray();
						chars[0] = Character.toLowerCase(chars[0]);
						name = new String(chars);
					}
				}
				return name;
			}*/
		};
		//mapper.getSerializationConfig().enable(Feature.WRAP_ROOT_VALUE);
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
	
	public Object marshal(Object object) {
		return marshalInternal(object);
	}
	
	public Object unmarshal(Object source) {
		return unmarshalInternal(source);
	}
	
	protected Object marshalInternal(Object object) {
		JavaType type = JavaType.typeOf(object);
		switch (type) {
		case INT:
		case LONG:
		case DOUBLE:
		case BOOLEAN:
		case STRING:
		case DATE:
		case NULL:
			return marshalSimple(object, type);
		case LIST:
			return marshalList((List<Object>) object, type);
		case MAP:
			return marshalMap((Map<String, Object>) object, type);
		default:
			return object;
		}
	}
	
	protected Object unmarshalInternal(Object source) {
		JavaType type = JavaType.typeOf(source);
		switch (type) {
		case INT:
		case LONG:
		case DOUBLE:
		case BOOLEAN:
		case STRING:
		case DATE:
		case NULL:
			return unmarshalSimple(source, type);
		case LIST:
			return unmarshalList((List)source);
		case MAP:
			return unmarshalMap((Map)source);
		default:
			return source;
		}
	}
	
	protected Object marshalSimple(Object object, JavaType type) {
		Map<String,Object> map;
		switch (type) {
		case LONG:
			map = new HashMap<String,Object>();
			map.put(type.name, object);
			return map;
		case DATE:
			map = new HashMap<String,Object>();
			map.put(type.name, ((Date)object).getTime());
			return map;
		default:
			return object;
		}
	}

	protected Object marshalList(List<Object> list, JavaType type) {
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				list.set(i, marshalInternal(list.get(i)));
			}
		}
		return list;
	}
	
	protected Object marshalMap(Map<String, Object> map, JavaType type) {
		if (map != null) {
			for (Map.Entry<String, Object> item : map.entrySet()) {
				item.setValue(marshalInternal(item.getValue()));
			}
		}
		return map;
	}
	
	protected Object unmarshalSimple(Object source, JavaType type) {
		return source;
		/*
		String text = source.getChildNodes().getLength() == 1 ? source.getFirstChild().getNodeValue() : null;
		if (text == null)
			return (type == JavaType.STRING) ? "" : null;
		switch (type) {
		case INT:
			return Integer.valueOf(text);
		case LONG:
			return Long.valueOf(text);
		case DOUBLE:
			return Double.valueOf(text);
		case BOOLEAN:
			return Boolean.valueOf(text);
		case STRING:
			return String.valueOf(text);
		case DATE:
			return DatatypeConverter.parseDateTime(text).getTime();
		case NULL:
		default:
			return null;
		}*/
	}
	
	protected Object unmarshalList(List list) {
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				list.set(i, unmarshalInternal(list.get(i)));
			}
		}
		return list;
	}
	
	protected Object unmarshalMap(Map map) {
		Map.Entry entry = (Map.Entry)map.entrySet().iterator().next();
		JavaType type = JavaType.typeOf(entry.getKey().toString());
		Object value = entry.getValue();
		switch (type) {
		case INT:
			return Integer.valueOf(String.valueOf(value));
		case LONG:
			return Long.valueOf(String.valueOf(value));
		case DOUBLE:
			return Double.valueOf(String.valueOf(value));
		case BOOLEAN:
			return Boolean.valueOf(String.valueOf(value));
		case STRING:
			return String.valueOf(value);
		case DATE:
			return new Date(Long.valueOf(String.valueOf(value)));
		case NULL:
		default:
			return null;
		}
	}
	
}
