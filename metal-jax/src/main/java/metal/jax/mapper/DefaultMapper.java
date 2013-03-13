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

public class DefaultMapper extends BaseMapper implements Mapper {
	
	private Serializer serializer;

	private Deserializer deserializer;

	public Serializer getSerializer() {
		return serializer;
	}

	public void setSerializer(Serializer serializer) {
		this.serializer = serializer;
	}

	public Deserializer getDeserializer() {
		return deserializer;
	}

	public void setDeserializer(Deserializer deserializer) {
		this.deserializer = deserializer;
	}

	@Override
	public <T> T deserialize(Class<T> type, InputStream input) {
		return getDeserializer().deserialize(type, input);
	}
	
	@Override
	public void serialize(Object object, OutputStream output) {
		getSerializer().serialize(object, output);
	}

}
