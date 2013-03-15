/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.mapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import metal.jax.model.Property;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ObjectAdapter extends XmlAdapter<Object,Object> {

	public static final class EventHandler implements ValidationEventHandler {
		@Override
		public boolean handleEvent(ValidationEvent event) {
			return false;
		}
	}
	
	private XmlMarshaller marshaller;
	private DocumentBuilder documentBuilder;
	
	public ObjectAdapter() {}
	
	public void setMarshaller(XmlMarshaller marshaller) {
		this.marshaller = marshaller;
	}
	
	protected DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
		if (documentBuilder == null) {
			documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}
		return documentBuilder;
	}
	
	@Override
	public Object marshal(Object object) throws Exception {
		ObjectType type = ObjectType.typeOf(object);
		return marshal(type, object, getDocumentBuilder().newDocument());
	}
	
	@SuppressWarnings("unchecked")
	protected Object marshal(ObjectType type, Object value, Document doc) {
		Element element, wrapper = doc.createElement("wrapper");
		switch (type) {
		case INT:
		case LONG:
		case DOUBLE:
		case BOOLEAN:
		case STRING:
			element = doc.createElement(type.name);
			element.appendChild(doc.createTextNode(String.valueOf(value)));
			wrapper.appendChild(element);
			return wrapper;
		case DATE:
			element = doc.createElement(type.name);
			Calendar cal = Calendar.getInstance();
			cal.setTime((Date)value);
			element.appendChild(doc.createTextNode(DatatypeConverter.printDateTime(cal)));
			wrapper.appendChild(element);
			return wrapper;
		case NULL:
			element = doc.createElement(type.name);
			wrapper.appendChild(element);
			return wrapper;
		case LIST:
			return marshalList((List<Object>)value, doc);
		case MAP:
			return marshalMap((Map<String,Object>)value, doc);
		default:
			marshaller.marshal(value, new DOMResult(wrapper));
			return wrapper;
		}
	}

	protected String marshal(ObjectType type, Object value) {
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
		Element element = doc.createElement(ObjectType.LIST.name);
//		for (Object item : list) {
//			ObjectType type = ObjectType.typeOf(item);
//			element.appendChild((marshal(type, item, doc)));
//		}
		return element;
	}
	
	protected String marshalList(List<Object> list) {
		return null;
//		Element element = doc.createElement(ObjectType.LIST.name);
//		for (Object item : list) {
//			ObjectType type = ObjectType.typeOf(item.getClass());
//			element.appendChild((marshal(type, item, doc)));
//		}
//		return element;
	}
	
	protected Element marshalMap(Map<String,Object> map, Document doc) {
		Element element = doc.createElement(ObjectType.MAP.name);
		Set<Entry<String,Object>> entries = map.entrySet();
//		for (Entry<String,Object> entry : entries) {
//			Element child = doc.createElement(entry.getKey());
//			ObjectType type = ObjectType.typeOf(entry.getValue());
//			child.appendChild(marshal(type, entry.getValue(), doc));
//			element.appendChild(child);
//		}
		return element;
	}
	
	protected String marshalMap(Map<String,Object> map) {
		return null;
//		Element element = doc.createElement(ObjectType.MAP.name);
//		Set<Entry<String,Object>> entries = map.entrySet();
//		for (Entry<String,Object> entry : entries) {
//			Element child = doc.createElement(entry.getKey());
//			ObjectType type = ObjectType.typeOf(entry.getValue().getClass());
//			child.appendChild(marshal(type, entry.getValue(), doc));
//			element.appendChild(child);
//		}
//		return element;
	}
	
	@Override
	public Object unmarshal(Object element) throws Exception {
		return element;
//		ObjectType type = null;
//		Object value = null;
//		if (marshaller != null) {
//			type = ObjectType.typeOf(((Element)element).getNodeName());
//			value = unmarshal(type, (Element)element);
//		} else {
//			Map<String,Object> map = (Map) element;
//			Map.Entry<String,Object> entry = map.entrySet().iterator().next();
//			type = ObjectType.typeOf(entry.getKey());
//			value = unmarshal(type, entry.getValue());
//		}
//		return new Property(type , value);
	}
	
	protected Object unmarshal(ObjectType type, Node element) throws Exception {
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
			return marshaller.unmarshal(new DOMSource(element));
		}
	}
	
	protected Object unmarshal(ObjectType type, Object object) throws Exception {
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
				ObjectType type = ObjectType.typeOf(node.getNodeName());
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
				ObjectType type = ObjectType.typeOf(child.getNodeName());
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
