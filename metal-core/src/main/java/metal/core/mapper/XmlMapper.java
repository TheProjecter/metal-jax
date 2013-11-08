/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mapper;

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


import metal.core.mapper.model.ValueWrapper;
import metal.core.mop.Model;
import metal.core.mop.ValueType;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class XmlMapper extends BaseMapper implements Adapter<Node, Object>, ApplicationListener<ContextRefreshedEvent> {

	private static class Jaxb2Mapper extends Jaxb2Marshaller {
		public void marshal(Object value, Node result) throws XmlMappingException {
			try {
				createMarshaller().marshal(value, result);
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
		mapper.setAdapters(new ValueAdapter[] {
			new ValueAdapter<Node, Object>(this)
		});
		mapper.setValidationEventHandler(new ValidationEventHandler() {
			@Override
			public boolean handleEvent(ValidationEvent event) { return false; }
		});
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent e) {
		mapper.setClassesToBeBound(modelRegistry.getModelClasses());
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
	public <T> T read(Class<T> type, InputStream input) {
		T value = null;
		try {
			Document doc = getDocumentBuilder().parse(input);
			ValueType valueType = ValueType.typeOf(doc.getDocumentElement().getNodeName());
			switch (valueType) {
			case OBJECT:
				value = (T) mapper.unmarshal(new DOMSource(doc.getDocumentElement()));
				break;
			default:
				Node valueWrapper = doc.createElement("valueWrapper");
				valueWrapper.appendChild(doc.createElement("value")).appendChild(doc.getDocumentElement());
				value = (T) mapper.unmarshal(new DOMSource(valueWrapper), ValueWrapper.class).getValue();
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
			Node root = getDocumentBuilder().parse(input).getDocumentElement();
			Node node = ensureElementOrNull(root.getFirstChild());
			while (node != null) {
				String name = node.getNodeName();
				value.put(name, mapper.unmarshal(new DOMSource(node), value.getMemberType(name)));
				node = ensureElementOrNull(node.getNextSibling());
			}
			return value;
		} catch (Exception ex) {
			throw new MapperException(UnexpectedException, ex);
		}
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void write(Object value, OutputStream output) {
		try {
			ValueType valueType = ValueType.typeOf(value);
			switch (valueType) {
			case OBJECT:
				mapper.marshal(value, new StreamResult(output));
				break;
			case LIST:
			case MAP:
				Node wrapper = getDocumentBuilder().newDocument();
				mapper.marshal(new ValueWrapper(value), wrapper);
				getDocumentTransformer().transform(new DOMSource(wrapper.getFirstChild().getFirstChild().getFirstChild()),
						new StreamResult(output));
				break;
			default:
				mapper.marshal(new JAXBElement(new QName(valueType.name), valueType.type, value),
						new StreamResult(output));
				break;
			}
		} catch (Exception ex) {
			throw new MapperException(UnexpectedException, ex);
		}
	}

	@Override
	public Node marshal(Object value) {
		Node result = getDocumentBuilder().newDocument().createElement("result");
		marshalValue(value, result);
		return result;
	}

	@Override
	public Object unmarshal(Node value) {
		return unmarshalValue(ensureElement(value.getFirstChild()));
	}

	@SuppressWarnings("unchecked")
	protected void marshalValue(Object value, Node result) {
		ValueType valueType = ValueType.typeOf(value);
		switch (valueType) {
		case LIST:
			marshalList((List<Object>) value, result);
			break;
		case MAP:
			marshalMap((Map<String, Object>) value, result);
			break;
		case OBJECT:
			mapper.marshal(value, new DOMResult(result));
			break;
		default:
			marshalSimple(value, valueType, result);
			break;
		}
	}

	protected Object unmarshalValue(Node value) {
		ValueType valueType = ValueType.typeOf(value.getNodeName());
		switch (valueType) {
		case LIST:
			return unmarshalList(value);
		case MAP:
			return unmarshalMap(value);
		case OBJECT:
			return mapper.unmarshal(new DOMSource(value));
		default:
			return unmarshalSimple(value, valueType);
		}
	}

	protected void marshalSimple(Object value, ValueType valueType, Node result) {
		Document doc = result.getOwnerDocument();
		Node node = result.appendChild(doc.createElement(valueType.name));
		switch (valueType) {
		case DATE:
			Calendar cal = Calendar.getInstance();
			cal.setTime((Date) value);
			node.appendChild(doc.createTextNode(DatatypeConverter.printDateTime(cal)));
			break;
		case NULL:
			break;
		default:
			node.appendChild(doc.createTextNode(String.valueOf(value)));
			break;
		}
	}

	protected void marshalList(List<Object> value, Node result) {
		Document doc = result.getOwnerDocument();
		Node node = result.appendChild(doc.createElement(ValueType.LIST.name));
		for (Object item : value) {
			marshalValue(item, node);
		}
	}

	protected void marshalMap(Map<String, Object> value, Node result) {
		Document doc = result.getOwnerDocument();
		Node node = result.appendChild(doc.createElement(ValueType.MAP.name));
		for (Map.Entry<String, Object> item : value.entrySet()) {
			marshalValue(item.getValue(), node.appendChild(doc.createElement(item.getKey())));
		}
	}

	protected Object unmarshalSimple(Node value, ValueType valueType) {
		Node node = value.getChildNodes().getLength() == 1 ? value.getFirstChild() : null;
		if (node != null && node.getNodeType() != Node.TEXT_NODE) {
			if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals(ValueType.NULL.name))
				return null;
			throw new MapperException(IncorrectContentModel, node.getNodeName());
		}
		String text = node != null ? node.getNodeValue() : null;
		if (text == null) return valueType.value;
		try {
			switch (valueType) {
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
			throw new MapperException(IncorrectContentModel, value.getNodeName());
		}
	}

	protected Object unmarshalList(Node value) {
		List<Object> list = new ArrayList<Object>();
		Node item = ensureElementOrNull(value.getFirstChild());
		while (item != null) {
			list.add(unmarshalValue(item));
			item = ensureElementOrNull(item.getNextSibling());
		}
		return list;
	}

	protected Object unmarshalMap(Node value) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Node item = ensureElementOrNull(value.getFirstChild());
		while (item != null) {
			map.put(item.getNodeName(), unmarshalValue(ensureElement(item.getFirstChild())));
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
		while (next != null && next.getNodeType() != Node.ELEMENT_NODE) {
			next = next.getNextSibling();
		}
		return next;
	}

}
