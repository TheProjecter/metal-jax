/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import metal.core.mapper.JavaType;
import metal.core.mapper.XmlMapper;
import metal.jax.model.Property;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PropertyAdapter extends XmlAdapter<Object,Property> {

	public static final class EventHandler implements ValidationEventHandler {
		@Override
		public boolean handleEvent(ValidationEvent event) {
			return false;
		}
	}
	
	private XmlMapper marshaller;
	private DocumentBuilder documentBuilder;
	
	public PropertyAdapter() {}

	public void setMapper(XmlMapper marshaller) {
		this.marshaller = marshaller;
	}
	
	protected DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
		if (documentBuilder == null) {
			documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}
		return documentBuilder;
	}
	
	@Override
	public Object marshal(Property property) throws Exception {
		JavaType type = JavaType.typeOf(property.getType());
		if (marshaller != null) {
			return marshal(type, property.getValue(), getDocumentBuilder().newDocument());
		} else {
			return marshal(type, property.getValue());
		}
	}
	
	@SuppressWarnings("unchecked")
	protected Element marshal(JavaType type, Object value, Document doc) {
		Element element = null;
		switch (type) {
		case INT:
		case LONG:
		case DOUBLE:
		case BOOLEAN:
		case STRING:
			element = doc.createElement(type.name);
			element.appendChild(doc.createTextNode(String.valueOf(value)));
			return element;
		case DATE:
			element = doc.createElement(type.name);
			element.appendChild(doc.createTextNode(String.valueOf(((Date)value).getTime())));
			return element;
		case LIST:
			return marshalList((List<Object>)value, doc);
		case MAP:
			return marshalMap((Map<String,Object>)value, doc);
		default:
			return (Element)marshaller.marshal(value);
		}
	}

	protected String marshal(JavaType type, Object value) {
		StringBuilder buffer = new StringBuilder();
		switch (type) {
		case INT:
		case LONG:
		case DOUBLE:
		case BOOLEAN:
		case STRING:
			buffer.append(type.name).append(":").append(String.valueOf(value));
			return buffer.toString();
		case DATE:
			buffer.append(type.name).append(":").append(String.valueOf(((Date)value).getTime()));
			return buffer.toString();
		case LIST:
			return marshalList((List<Object>)value);
		case MAP:
			return marshalMap((Map<String,Object>)value);
		default:
			return null;
//			marshaller.marshal(value, new DOMResult(doc));
//			return doc.getDocumentElement();
		}
	}

	protected Element marshalList(List<Object> list, Document doc) {
		Element element = doc.createElement(JavaType.LIST.name);
		for (Object item : list) {
			JavaType type = JavaType.typeOf(item);
			element.appendChild((marshal(type, item, doc)));
		}
		return element;
	}
	
	protected String marshalList(List<Object> list) {
		return null;
//		Element element = doc.createElement(JavaType.LIST.name);
//		for (Object item : list) {
//			JavaType type = JavaType.typeOf(item.getClass());
//			element.appendChild((marshal(type, item, doc)));
//		}
//		return element;
	}
	
	protected Element marshalMap(Map<String,Object> map, Document doc) {
		Element element = doc.createElement(JavaType.MAP.name);
		Set<Entry<String,Object>> entries = map.entrySet();
		for (Entry<String,Object> entry : entries) {
			Element child = doc.createElement(entry.getKey());
			JavaType type = JavaType.typeOf(entry.getValue());
			child.appendChild(marshal(type, entry.getValue(), doc));
			element.appendChild(child);
		}
		return element;
	}
	
	protected String marshalMap(Map<String,Object> map) {
		return null;
//		Element element = doc.createElement(JavaType.MAP.name);
//		Set<Entry<String,Object>> entries = map.entrySet();
//		for (Entry<String,Object> entry : entries) {
//			Element child = doc.createElement(entry.getKey());
//			JavaType type = JavaType.typeOf(entry.getValue().getClass());
//			child.appendChild(marshal(type, entry.getValue(), doc));
//			element.appendChild(child);
//		}
//		return element;
	}
	
	@Override
	public Property unmarshal(Object element) throws Exception {
		JavaType type = null;
		Object value = null;
		if (marshaller != null) {
			type = JavaType.typeOf(((Element)element).getNodeName());
			value = unmarshal(type, (Element)element);
		} else {
			Map<String,Object> map = (Map) element;
			Map.Entry<String,Object> entry = map.entrySet().iterator().next();
			type = JavaType.typeOf(entry.getKey());
			value = unmarshal(type, entry.getValue());
		}
		return new Property(type , value);
	}
	
	protected Object unmarshal(JavaType type, Node element) throws Exception {
		String value = element.getChildNodes().getLength() == 1 ? element.getFirstChild().getNodeValue() : null;
		switch (type) {
		case INT:
			return Integer.valueOf(value);
		case LONG:
			return Long.valueOf(value);
		case DOUBLE:
			return Double.valueOf(value);
		case BOOLEAN:
			return Boolean.valueOf(value);
		case STRING:
			return String.valueOf(value);
		case DATE:
			return new Date(Long.valueOf(value));
		case LIST:
			return unmarshalList(element);
		case MAP:
			return unmarshalMap(element);
		default:
			return marshaller.unmarshal(element);
		}
	}
	
	protected Object unmarshal(JavaType type, Object object) throws Exception {
		String value = object.toString();
		switch (type) {
		case INT:
			return Integer.valueOf(value);
		case LONG:
			return Long.valueOf(value);
		case DOUBLE:
			return Double.valueOf(value);
		case BOOLEAN:
			return Boolean.valueOf(value);
		case STRING:
			return String.valueOf(value);
		case DATE:
			return new Date(Long.valueOf(value));
		case LIST:
			return null;
//			return unmarshalList(object);
		case MAP:
			return null;
//			return unmarshalMap(object);
		default:
			return null;
//			return marshaller.unmarshal(new DOMSource(element));
		}
	}
	
	protected List<Object> unmarshalList(Node element) throws Exception {
		List<Object> value = new ArrayList<Object>();
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node node = childNodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				JavaType type = JavaType.typeOf(node.getNodeName());
				value.add(unmarshal(type, node));
			}
		}
		return value;
	}
	
	protected Map<String,Object> unmarshalMap(Node element) throws Exception {
		Map<String,Object> value = new LinkedHashMap<String,Object>();
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node node = childNodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Node child = getFirstChild(node);
				JavaType type = JavaType.typeOf(child.getNodeName());
				value.put(node.getNodeName(), unmarshal(type, child));
			}
		}
		return value;
	}
	
	protected Node getFirstChild(Node element) {
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node node = childNodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				return node;
			}
		}
		return null;
	}
	
}
