/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mop;

import java.util.HashMap;

public class Model extends HashMap<String, Object> {

	private NameDeclaration[] members;

	public Model() {
		this.members = new NameDeclaration[0];
	}
	
	public Model(NameDeclaration... members) {
		this.members = members;
	}

	public String[] getMemberNames() {
		String[] names = new String[members.length];
		int i = 0;
		for (NameDeclaration member : members) {
			names[i++] = member.getName();
		}
		return names;
	}
	
	public Class<?>[] getMemberTypes() {
		Class<?>[] types = new Class<?>[members.length];
		int i = 0;
		for (NameDeclaration member : members) {
			types[i++] = member.getType();
		}
		return types;
	}
	
	public Class<?> getMemberType(String name) {
		for (NameDeclaration member : members) {
			if (member.getName().equals(name)) {
				return member.getType();
			}
		}
		return null;
	}
	
	public Object[] getValues() {
		Object[] values = new Object[members.length];
		int i = 0;
		for (NameDeclaration member : members) {
			values[i++] = get(member.getName());
		}
		return values;
	}
	
}
