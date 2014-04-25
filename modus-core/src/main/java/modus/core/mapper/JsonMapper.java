/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package modus.core.mapper;

import static modus.core.mapper.MapperMessageCode.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import modus.core.mapper.model.ValueWrapper;
import modus.core.mop.Model;
import modus.core.mop.ValueType;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
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
				if (adapter instanceof ValueAdapter) {
					((ValueAdapter<Object, Object>)adapter).setAdapter(_this);
				}
				return adapter;
			}
		};
		mapper = new ObjectMapper();
		mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
		mapper.getSerializationConfig().setAnnotationIntrospector(introspector);
		mapper.getDeserializationConfig().setAnnotationIntrospector(introspector);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T read(Class<T> type, InputStream input) {
		T value = null;
		try {
			ValueType valueType = ValueType.typeOf(type);
			switch (valueType) {
			case LIST:
			case MAP:
				StringBuilder valueWrapper = new StringBuilder("{\"value\":").append(IOUtils.toCharArray(input)).append("}");
				value = (T) mapper.readValue(valueWrapper.toString(), ValueWrapper.class).getValue();
				break;
			default:
				value = (T) mapper.readValue(input, type);
				break;
			}
		} catch (Exception ex) {
			throw new MapperException(UnexpectedException, ex);
		}
		if (type == null || value == null || type.isAssignableFrom(value.getClass()))
			return value;
		throw new MapperException(UnexpectedType, type.getName(), value.getClass().getName());
	}

	@Override
	public <T extends Model> T read(T value, InputStream input) {
		try {
			JsonNode root = mapper.readTree(input);
			Iterator<String> iterator = root.getFieldNames();
			while (iterator.hasNext()) {
				String name = iterator.next();
				value.put(name, mapper.readValue(root.get(name), value.getMemberType(name)));
			}
			return value;
		} catch (Exception ex) {
			throw new MapperException(UnexpectedException, ex);
		}
	}

	@Override
	public void write(Object value, OutputStream output) {
		try {
			ValueType valueType = ValueType.typeOf(value);
			switch (valueType) {
			case LIST:
			case MAP:
				JsonGenerator gen = mapper.getJsonFactory().createJsonGenerator(output, JsonEncoding.UTF8);
				JsonNode valueWrapper = mapper.valueToTree(new ValueWrapper(value));
				mapper.writeTree(gen, valueWrapper.get("value"));
				break;
			default:
				mapper.writeValue(output, value);
				break;
			}
		} catch (Exception ex) {
			throw new MapperException(UnexpectedException, ex);
		}
	}
	
	@Override
	public Object marshal(Object value) {
		return marshalValue(value);
	}
	
	@Override
	public Object unmarshal(Object value) {
		return unmarshalValue(value);
	}
	
	@SuppressWarnings("unchecked")
	protected Object marshalValue(Object value) {
		ValueType valueType = ValueType.typeOf(value);
		switch (valueType) {
		case LIST:
			return marshalList((List<Object>) value);
		case MAP:
			return marshalMap((Map<String, Object>) value);
		case LONG:
		case DATE:
		case OBJECT:
			return marshalSimple(value, valueType);
		case NULL:
		default:
			return value;
		}
	}
	
	@SuppressWarnings("unchecked")
	protected Object unmarshalValue(Object value) {
		ValueType valueType = ValueType.typeOf(value);
		switch (valueType) {
		case LIST:
			return unmarshalList((List<Object>)value);
		case MAP:
			Object result = unmarshalSimple((Map<String, Object>)value);
			if (result != null) {
				return result;
			} else {
				return unmarshalMap((Map<String, Object>)value);
			}
		case NULL:
		default:
			return value;
		}
	}
	
	protected Object marshalSimple(Object value, ValueType valueType) {
		Map<String,Object> map = new HashMap<String,Object>();
		switch (valueType) {
		case LONG:
			map.put(valueType.name, value);
			break;
		case DATE:
			map.put(valueType.name, ((Date)value).getTime());
			break;
		case OBJECT:
		default:
			String modelName = modelRegistry.getModelName(value.getClass());
			if (modelName != null) {
				map.put(modelName, value);
			}
			break;
		}
		return map;
	}

	protected Object marshalList(List<Object> value) {
		List<Object> list = new ArrayList<Object>();
		for (Object item : value) {
			list.add(marshalValue(item));
		}
		return list;
	}
	
	protected Object marshalMap(Map<String, Object> value) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		for (Map.Entry<String, Object> item : value.entrySet()) {
			map.put(item.getKey(), marshalValue(item.getValue()));
		}
		return map;
	}
	
	protected Object unmarshalSimple(Map<String, Object> map) {
		if (map.size() != 1) return null;
		Class<?> modelClass = null;
		Map.Entry<String, Object> entry = map.entrySet().iterator().next();
		Object value = entry.getValue();
		ValueType valueType = ValueType.typeOf(entry.getKey());
		if (valueType == ValueType.OBJECT) {
			modelClass = modelRegistry.getModelClass(entry.getKey());
			if (modelClass == null) return null;
		}
		switch (valueType) {
		case LONG:
			return Long.valueOf(String.valueOf(value));
		case DATE:
			return new Date(Long.valueOf(String.valueOf(value)));
		case OBJECT:
		default:
			return mapper.convertValue(value, modelClass);
		}
	}
	
	protected Object unmarshalList(List<Object> value) {
		List<Object> list = new ArrayList<Object>();
		for (Object item : value) {
			list.add(unmarshalValue(item));
		}
		return list;
	}
	
	protected Object unmarshalMap(Map<String, Object> value) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		for (Map.Entry<String, Object> item : value.entrySet()) {
			map.put(item.getKey(), unmarshalValue(item.getValue()));
		}
		return map;
	}
	
}
