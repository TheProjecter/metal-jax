/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.model;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import metal.core.mapper.ObjectType;
import metal.jax.mapper.PropertyAdapter;

@XmlJavaTypeAdapter(PropertyAdapter.class)
public class Property {

	private Class<?> type;
	
	private Object value;

	public Property() {}
	
	public Property(Class<?> type, Object value) {
		this.type = type;
		this.value = value;
	}

	public Property(ObjectType type, Object value) {
		this.type = type != ObjectType.OBJECT ? type.type : value != null ? value.getClass() : null;
		this.value = value;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(String type) {
		this.type = null;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
