/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mop;

import java.util.List;

public class MethodDeclaration extends Setting<String, List<NameDeclaration>> {

	public MethodDeclaration(String name, List<NameDeclaration> paramDecls) {
		super(name, paramDecls);
	}
	
	public String getName() {
		return this.getKey();
	}
	
	public NameDeclaration[] getParamDeclarations() {
		return this.getValue().toArray(new NameDeclaration[this.getValue().size()]);
	}
	
	public Class<?>[] getParamTypes() {
		Class<?>[] types = new Class<?>[this.getValue().size()];
		int i = 0;
		for (NameDeclaration decl : this.getValue()) {
			types[i++] = decl.getType();
		}
		return types;
	}
	
}
