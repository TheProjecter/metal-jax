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
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class XmlMapper extends BaseMapper implements Mapper, Adapter<Node, Object> {

	private DocumentBuilder documentBuilder;
	private Jaxb2Marshaller mapper;

	public XmlMapper() {
		mapper = new Jaxb2Marshaller();
		mapper.setValidationEventHandler(new DefaultAdapter.EventHandler());
		mapper.setAdapters(new DefaultAdapter[] { new DefaultAdapter(this) });
	}

	public void setModelClasses(List<Class<?>> modelClasses) {
		super.setModelClasses(modelClasses);
		mapper.setClassesToBeBound(modelClasses.toArray(new Class<?>[0]));
	}

	public void setDocumentBuilder(DocumentBuilder documentBuilder) {
		this.documentBuilder = documentBuilder;
	}

	protected DocumentBuilder getDocumentBuilder() {
		try {
			if (documentBuilder == null) {
				documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			}
		} catch (Exception ex) {
			throw new MapperException(UnexpectedException, ex);
		}
		return documentBuilder;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T read(Class<T> type, InputStream input) {
		Object object = null;
		try {
			object = mapper.unmarshal(new StreamSource(input));
		} catch (Exception ex) {
			throw new MapperException(UnexpectedException, ex);
		}
		if (type == null || object == null || type.isInstance(object))
			return (T) object;
		throw new MapperException(UnexpectedType, type.getName(), object.getClass().getName());
	}

	@Override
	public void write(Object object, OutputStream output) {
		try {
			mapper.marshal(object, new StreamResult(output));
		} catch (Exception ex) {
			throw new MapperException(UnexpectedException, ex);
		}
	}

	public Node marshal(Object object) {
		Node result = getDocumentBuilder().newDocument().createElement("result");
		marshalInternal(object, result);
		return result;
	}

	public Object unmarshal(Node source) {
		Node node = ensureElement(source.getFirstChild());
		return unmarshalInternal(node);
	}

	@SuppressWarnings("unchecked")
	protected void marshalInternal(Object object, Node result) {
		JavaType type = JavaType.typeOf(object);
		switch (type) {
		case LIST:
			marshalList((List<Object>) object, result);
			break;
		case MAP:
			marshalMap((Map<String, Object>) object, result);
			break;
		case OBJECT:
			mapper.marshal(object, new DOMResult(result));
			break;
		default:
			marshalSimple(object, type, result);
			break;
		}
	}

	protected Object unmarshalInternal(Node source) {
		JavaType type = JavaType.typeOf(source.getNodeName());
		switch (type) {
		case LIST:
			return unmarshalList(source);
		case MAP:
			return unmarshalMap(source);
		case OBJECT:
			return mapper.unmarshal(new DOMSource(source));
		default:
			return unmarshalSimple(source, type);
		}
	}

	protected void marshalSimple(Object object, JavaType type, Node result) {
		Document doc = result.getOwnerDocument();
		Node node = result.appendChild(doc.createElement(type.name));
		switch (type) {
		case DATE:
			Calendar cal = Calendar.getInstance();
			cal.setTime((Date) object);
			node.appendChild(doc.createTextNode(DatatypeConverter.printDateTime(cal)));
			break;
		case NULL:
			break;
		default:
			node.appendChild(doc.createTextNode(String.valueOf(object)));
			break;
		}
	}

	protected void marshalList(List<Object> list, Node result) {
		Document doc = result.getOwnerDocument();
		Node node = result.appendChild(doc.createElement(JavaType.LIST.name));
		if (list != null) {
			for (Object item : list) {
				marshalInternal(item, node);
			}
		}
	}

	protected void marshalMap(Map<String, Object> map, Node result) {
		Document doc = result.getOwnerDocument();
		Node node = result.appendChild(doc.createElement(JavaType.MAP.name));
		if (map != null) {
			for (Map.Entry<String, Object> item : map.entrySet()) {
				marshalInternal(item.getValue(), node.appendChild(doc.createElement(item.getKey())));
			}
		}
	}

	protected Object unmarshalSimple(Node source, JavaType type) {
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
		}
	}

	protected Object unmarshalList(Node source) {
		List<Object> list = new ArrayList<Object>();
		Node item = ensureElementOrNull(source.getFirstChild());
		while (item != null) {
			list.add(unmarshalInternal(item));
			item = ensureElementOrNull(item.getNextSibling());
		}
		return list;
	}

	protected Object unmarshalMap(Node source) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Node item = ensureElementOrNull(source.getFirstChild());
		while (item != null) {
			map.put(item.getNodeName(),
					unmarshalInternal(ensureElement(item.getFirstChild())));
			item = ensureElementOrNull(item.getNextSibling());
		}
		return map;
	}

	protected Node ensureElement(Node node) {
		Node next = ensureElementOrNull(node);
		if (next == null) {
			throw new MapperException(ContentModel, node.getParentNode().getNodeName());
		}
		return next;
	}

	protected Node ensureElementOrNull(Node node) {
		Node next = node;
		while (next != null && next.getNodeType() != Node.ELEMENT_NODE)
			next = next.getNextSibling();
		return next;
	}

}
