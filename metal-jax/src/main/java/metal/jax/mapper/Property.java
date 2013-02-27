/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.mapper;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(PropertyAdapter.class)
public class Property {

	private Class<?> type;
	
	private Object value;

	public Property() {}
	
	public Property(Class<?> type, Object value) {
		this.type = type;
		this.value = value;
	}

	public Property(PropertyType type, Object value) {
		this.type = type != PropertyType.OBJECT ? type.type : value != null ? value.getClass() : null;
		this.value = value;
	}

	public Class<?> getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

}
