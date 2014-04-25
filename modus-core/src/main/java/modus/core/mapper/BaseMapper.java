/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package modus.core.mapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import modus.core.mop.Model;
import modus.core.mop.ModelRegistry;

public abstract class BaseMapper implements Mapper {

	protected ModelRegistry modelRegistry;
	
	public void setModelRegistry(ModelRegistry modelRegistry) {
		this.modelRegistry = modelRegistry;
	}
	
	@Override
	public <T> T read(Class<T> type, String input) {
		return read(type, new ByteArrayInputStream(input.getBytes()));
	}

	@Override
	public <T extends Model> T read(T value, String input) {
		return read(value, new ByteArrayInputStream(input.getBytes()));
	}

	@Override
	public String write(Object value) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		write(value, out);
		return new String(out.toByteArray());
	}

}
