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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.transform.dom.DOMResult;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

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
	
	public Object marshal(Object object) {
		Map result = new HashMap();
		marshalInternal(object, result);
		return result.get("result");
	}
	
	public Object unmarshal(Object source) {
		return source;
	}
	
	protected void marshalInternal(Object object, Map result) {
		JavaType type = JavaType.typeOf(object);
		switch (type) {
		case INT:
		case LONG:
		case DOUBLE:
		case BOOLEAN:
		case STRING:
		case DATE:
		case NULL:
			marshalSimple(object, type, result);
			break;
		case LIST:
			//marshalList((List<Object>) object, type, result);
			break;
		case MAP:
			//marshalMap((Map<String, Object>) object, type, result);
			break;
		default:
			//marshaller.marshal(object, new DOMResult(result));
			break;
		}
	}
	
	protected void marshalSimple(Object object, JavaType type, Map result) {
//		Document doc = result.getOwnerDocument();
//		Node node = result.appendChild(doc.createElement(type.name));
		if (object != null) {
			String value;
			switch (type) {
			case DATE:
				value = String.valueOf(((Date)object).getTime());
				break;
			default:
				value = String.valueOf(object);
				break;
			}
			//node.appendChild(doc.createTextNode(value));
			result.put("result", value);
		}
	}

}
