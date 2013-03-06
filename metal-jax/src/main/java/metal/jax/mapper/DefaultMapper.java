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

public class DefaultMapper implements Mapper {
	
	private Reader reader;
	
	private Writer writer;

	public Reader getReader() {
		return reader;
	}

	public void setReader(Reader reader) {
		this.reader = reader;
	}

	public Writer getWriter() {
		return writer;
	}

	public void setWriter(Writer writer) {
		this.writer = writer;
	}

	@Override
	public <T> T read(Class<T> type, InputStream input) {
		return getReader().read(type, input);
	}
	
	@Override
	public void write(Object object, OutputStream output) {
		getWriter().write(object, output);
	}

}
