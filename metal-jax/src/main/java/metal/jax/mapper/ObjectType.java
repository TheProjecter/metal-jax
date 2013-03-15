/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

public enum ObjectType {
	INT("int", Integer.class),
	LONG("long", Long.class),
	DOUBLE("double", Double.class),
	BOOLEAN("boolean", Boolean.class),
	STRING("string", String.class),
	DATE("date", Date.class),
	LIST("list", List.class),
	MAP("map", Map.class),
	OBJECT("object", Object.class),
	NULL("null", Object.class);
	
	public final String name;
	public final Class<?> type;
	private ObjectType(String name, Class<?> type) { this.name = name; this.type = type; }
	private static final ObjectType[] values = ObjectType.values();
	
	public static ObjectType typeOf(String name) {
		for (ObjectType value : values) {
			if (value.name.equals(name)) return value;
		}
		return OBJECT;
	}
	
	public static ObjectType typeOf(Object object) {
		if (object == null) return NULL;
		Class<?> type = object.getClass();
		for (ObjectType value : values) {
			if (value.type == type || value.type.isAssignableFrom(type)) {
				return value;
			}
		}
		return OBJECT;
	}
	
}
