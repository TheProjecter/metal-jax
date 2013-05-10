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

	private NameDeclaration[] decls;

	public Model(NameDeclaration... decls) {
		this.decls = decls;
	}

	public String[] getDeclaredNames() {
		String[] names = new String[decls.length];
		int i = 0;
		for (NameDeclaration decl : decls) {
			names[i++] = decl.getName();
		}
		return names;
	}
	
	public Class<?>[] getDeclaredTypes() {
		Class<?>[] types = new Class<?>[decls.length];
		int i = 0;
		for (NameDeclaration decl : decls) {
			types[i++] = decl.getType();
		}
		return types;
	}
	
	public Class<?> getDeclaredType(String name) {
		for (NameDeclaration decl : decls) {
			if (decl.getName().equals(name)) {
				return decl.getType();
			}
		}
		return null;
	}
	
	public Object[] getValues() {
		Object[] values = new Object[decls.length];
		int i = 0;
		for (NameDeclaration decl : decls) {
			values[i++] = get(decl.getName());
		}
		return values;
	}
	
}
