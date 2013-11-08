/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mapper;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import metal.core.mop.Model;

public class ValueMapper {
	
	private Map<String, Reader> readers;
	private Map<String, Writer> writers;
	
	public void setReaders(Map<String, Reader> readers) {
		this.readers = readers;
	}
	
	public void setWriters(Map<String, Writer> writers) {
		this.writers = writers;
	}
	
	public <T extends Model> T read(T value, InputStream input, String contentType) {
		return readers.get(contentType.toLowerCase()).read(value, input);
	}
	
	public void write(Object value, OutputStream output, String contentType) {
		writers.get(contentType.toLowerCase()).write(value, output);
	}
	
}
