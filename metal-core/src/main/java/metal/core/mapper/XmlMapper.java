/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mapper;

import static metal.core.common.XmlAnnotationUtils.*;
import static metal.core.mapper.Adapter.Kind.*;
import static metal.core.mapper.MapperMessageCode.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class XmlMapper extends BaseModelMapper implements Adapter<Node, Object> {

	private static class Jaxb2Mapper extends Jaxb2Marshaller {
		public void marshal(Object source, Node result) throws XmlMappingException {
			try {
				createMarshaller().marshal(source, result);
			} catch (JAXBException ex) {
				throw convertJaxbException(ex);
			}
		}
		public <T> T unmarshal(Source source, Class<T> type) throws XmlMappingException {
			try {
				return createUnmarshaller().unmarshal(source, type).getValue();
			} catch (JAXBException ex) {
				throw convertJaxbException(ex);
			}
		}
	}

	private DocumentBuilder documentBuilder;
	private Transformer documentTransformer;
	private Jaxb2Mapper mapper;

	public XmlMapper() {
		mapper = new Jaxb2Mapper();
		mapper.setAdapters(new BaseAdapter[] {
			new ValueAdapter<Node, Object>(this),
			new PropertyListAdapter<Node, Object>(this)
		});
		mapper.setValidationEventHandler(new ValidationEventHandler() {
			@Override
			public boolean handleEvent(ValidationEvent event) { return false; }
		});
	}

	public void setModelClasses(List<Class<?>> modelClasses) {
		super.setModelClasses(modelClasses);
		mapper.setClassesToBeBound(modelClasses.toArray(new Class<?>[0]));
	}

	public void setDocumentBuilder(DocumentBuilder documentBuilder) {
		this.documentBuilder = documentBuilder;
	}

	public void setDocumentTransformer(Transformer documentTransformer) {
		this.documentTransformer = documentTransformer;
	}

	protected DocumentBuilder getDocumentBuilder() {
		if (documentBuilder == null) {
			try {
				documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			} catch (Exception ex) {
				throw new MapperException(UnexpectedException, ex);
			}
		}
		return documentBuilder;
	}

	protected Transformer getDocumentTransformer() {
		if (documentTransformer == null) {
			try {
				documentTransformer = TransformerFactory.newInstance().newTransformer();
			} catch (Exception ex) {
				throw new MapperException(UnexpectedException, ex);
			}
		}
		return documentTransformer;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T read(Class<T> valueType, InputStream input) {
		T value = null;
		try {
			Document doc = getDocumentBuilder().parse(input);
			Node valueWrapper = doc.createElement("valueWrapper");
			valueWrapper.appendChild(doc.createElement("value")).appendChild(doc.getDocumentElement());
			value = (T) mapper.unmarshal(new DOMSource(valueWrapper), ValueWrapper.class).getValue();
		} catch (Exception ex) {
			throw new MapperException(UnexpectedException, ex);
		}
		if (valueType == null || value == null || valueType.isAssignableFrom(value.getClass()))
			return value;
		throw new MapperException(UnexpectedType, valueType.getName(), value.getClass().getName());
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void write(Object object, OutputStream output) {
		JavaType type = JavaType.typeOf(object, null);
		try {
			switch (type) {
			case OBJECT:
				mapper.marshal(object, new StreamResult(output));
				break;
			case LIST:
			case MAP:
				Node wrapper = getDocumentBuilder().newDocument();
				mapper.marshal(new ValueWrapper(object), wrapper);
				getDocumentTransformer().transform(new DOMSource(wrapper.getFirstChild().getFirstChild().getFirstChild()),
						new StreamResult(output));
				break;
			default:
				mapper.marshal(new JAXBElement(new QName(type.name), type.type, object),
						new StreamResult(output));
				break;
			}
		} catch (Exception ex) {
			throw new MapperException(UnexpectedException, ex);
		}
	}

	public Node marshal(Kind kind, Object object) {
		Node result = getDocumentBuilder().newDocument().createElement("result");
		marshalValue(kind, object, null, result);
		return result;
	}

	public Object unmarshal(Kind kind, Node source) {
		switch (kind) {
		case PROPERTYLIST:
			return unmarshalList(kind, source);
		case VALUE:
		default:
			return unmarshalValue(kind, ensureElement(source.getFirstChild()));
		}
	}

	@SuppressWarnings("unchecked")
	protected void marshalValue(Kind kind, Object object, Class<?> clazz, Node result) {
		JavaType type = JavaType.typeOf(object, clazz);
		switch (type) {
		case LIST:
			marshalList(kind, (List<Object>) object, result);
			break;
		case MAP:
			marshalMap((Map<String, Object>) object, result);
			break;
		case OBJECT:
			mapper.marshal(object, new DOMResult(result));
			break;
		default:
			marshalSimple(object, type, clazz, result);
			break;
		}
	}

	protected Object unmarshalValue(Kind kind, Node source) {
		JavaType type = JavaType.typeOf(source.getNodeName());
		switch (type) {
		case LIST:
			return unmarshalList(kind, source);
		case MAP:
			return unmarshalMap(source);
		case OBJECT:
			return mapper.unmarshal(new DOMSource(source));
		default:
			return unmarshalSimple(source, type);
		}
	}

	protected void marshalSimple(Object object, JavaType type, Class<?> clazz, Node result) {
		Document doc = result.getOwnerDocument();
		String typeName = ensureName(type != JavaType.NULL ? type.name : modelName(clazz),
				clazz != null ? clazz.getSimpleName() : JavaType.NULL.name);
		Node node = result.appendChild(doc.createElement(typeName));
		switch (type) {
		case DATE:
			Calendar cal = Calendar.getInstance();
			cal.setTime((Date) object);
			node.appendChild(doc.createTextNode(DatatypeConverter.printDateTime(cal)));
			break;
		case NULL:
			if (!JavaType.NULL.name.equals(typeName)) {
				node.appendChild(doc.createElement(JavaType.NULL.name));
			}
			break;
		default:
			node.appendChild(doc.createTextNode(String.valueOf(object)));
			break;
		}
	}

	@SuppressWarnings("unchecked")
	protected void marshalList(Kind kind, List<Object> list, Node result) {
		Document doc = result.getOwnerDocument();
		switch (kind) {
		case PROPERTYLIST:
			if (list != null) {
				for (Object item : list) {
					Property<Class<?>, Object> property = (Property<Class<?>,Object>)item;
					marshalValue(VALUE, property.getValue(), property.getKey(), result);
				}
			}
			break;
		case VALUE:
			Node node = result.appendChild(doc.createElement(JavaType.LIST.name));
			if (list != null) {
				for (Object item : list) {
					marshalValue(VALUE, item, null, node);
				}
			}
			break;
		}
	}

	protected void marshalMap(Map<String, Object> map, Node result) {
		Document doc = result.getOwnerDocument();
		Node node = result.appendChild(doc.createElement(JavaType.MAP.name));
		if (map != null) {
			for (Map.Entry<String, Object> item : map.entrySet()) {
				marshalValue(VALUE, item.getValue(), null, node.appendChild(doc.createElement(item.getKey())));
			}
		}
	}

	protected Object unmarshalSimple(Node source, JavaType type) {
		Node node = source.getChildNodes().getLength() == 1 ? source.getFirstChild() : null;
		if (node != null && node.getNodeType() != Node.TEXT_NODE) {
			if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals(JavaType.NULL.name))
				return null;
			throw new MapperException(IncorrectContentModel, node.getNodeName());
		}
		String text = node != null ? node.getNodeValue() : null;
		if (text == null) return type.value;
		try {
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
			}
		} catch (Exception ex) {
			throw new MapperException(IncorrectContentModel, source.getNodeName());
		}
	}

	protected Object unmarshalList(Kind kind, Node source) {
		List<Object> list = new ArrayList<Object>();
		Node item = ensureElementOrNull(source.getFirstChild());
		while (item != null) {
			switch (kind) {
			case PROPERTYLIST:
				list.add(new Property<Class<?>,Object>(modelClass(item), unmarshalValue(VALUE, item)));
				break;
			case VALUE:
				list.add(unmarshalValue(VALUE, item));
				break;
			}
			item = ensureElementOrNull(item.getNextSibling());
		}
		return list;
	}

	protected Object unmarshalMap(Node source) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Node item = ensureElementOrNull(source.getFirstChild());
		while (item != null) {
			map.put(item.getNodeName(),
					unmarshalValue(VALUE, ensureElement(item.getFirstChild())));
			item = ensureElementOrNull(item.getNextSibling());
		}
		return map;
	}

	protected Node ensureElement(Node node) {
		Node next = ensureElementOrNull(node);
		if (next == null) {
			throw new MapperException(IncorrectContentModel, node.getParentNode().getNodeName());
		}
		return next;
	}

	protected Node ensureElementOrNull(Node node) {
		Node next = node;
		while (next != null && next.getNodeType() != Node.ELEMENT_NODE)
			next = next.getNextSibling();
		return next;
	}

	protected Class<?> modelClass(Node source) {
		JavaType type = JavaType.typeOf(source.getNodeName());
		return type != JavaType.OBJECT ? type.type : modelClass(source.getNodeName()); 
	}
	
}
