/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.mapper;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;

public class XmlMapper implements Mapper {
	
	private XmlMapper parent;
	
	private Marshaller marshaller;
	
	private Unmarshaller unmarshaller;
	
	public void setParent(XmlMapper parent) {
		this.parent = parent;
	}
	
	public void setMarshaller(Marshaller marshaller) {
		this.marshaller = marshaller;
	}

	public void setUnmarshaller(Unmarshaller unmarshaller) {
		this.unmarshaller = unmarshaller;
	}

	public void write(Object object, OutputStream output) {
		if (marshaller.supports(object.getClass()) || parent == null) {
			try {
				marshaller.marshal(object, new StreamResult(output));
			} catch (Exception ex) {
				throw new MapperException("UnexpectedException", ex);
			}
		} else {
			parent.write(object, output);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T read(Class<T> type, InputStream input) {
		Object object = null;
		try {
			object = unmarshaller.unmarshal(new StreamSource(input));
		} catch (Exception ex) {
			throw new MapperException("UnexpectedException", ex);
		}
		if (type == null || object == null || type.isInstance(object)) return (T)object;
		throw new MapperException("UnexpectedType", object.getClass(), type);
	}
	
}
