/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mop;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public enum ValueType {
	INT("int", Integer.class, null),
	LONG("long", Long.class, null),
	DOUBLE("double", Double.class, null),
	BOOLEAN("boolean", Boolean.class, null),
	STRING("string", String.class, ""),
	DATE("date", Date.class, null),
	LIST("list", List.class, Collections.emptyList()),
	MAP("map", Map.class, Collections.emptyMap()),
	OBJECT("object", Object.class, null),
	NULL("null", null, null);
	
	public final String name;
	public final Class<?> type;
	public final Object value;
	private ValueType(String name, Class<?> type, Object value) {
		this.name = name; this.type = type; this.value = value;
	}
	private static final ValueType[] values = ValueType.values();
	
	public static ValueType typeOf(String name) {
		for (ValueType value : values) {
			if (value.name.equals(name)) return value;
		}
		return OBJECT;
	}
	
	public static ValueType typeOf(Class<?> type) {
		if (type == null) return NULL;
		for (ValueType value : values) {
			if (value.type == type || value.type.isAssignableFrom(type)) {
				return value;
			}
		}
		return OBJECT;
	}
	
	public static ValueType typeOf(Object object, Class<?> clazz) {
		clazz = object == null ? null : clazz != null ? clazz : object.getClass();
		return typeOf(clazz);
	}
	
}
