/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mop;

import java.util.List;

public class MethodDeclaration extends Setting<String, List<NameDeclaration>> {

	public MethodDeclaration(String name, List<NameDeclaration> params) {
		super(name, params);
	}
	
	public String getName() {
		return this.getKey();
	}
	
	public NameDeclaration[] getParamDeclarations() {
		return this.getValue().toArray(new NameDeclaration[this.getValue().size()]);
	}
	
	public String[] getParamNames() {
		String[] names = new String[this.getValue().size()];
		int i = 0;
		for (NameDeclaration param : this.getValue()) {
			names[i++] = param.getName();
		}
		return names;
	}
	
	public Class<?>[] getParamTypes() {
		Class<?>[] types = new Class<?>[this.getValue().size()];
		int i = 0;
		for (NameDeclaration param : this.getValue()) {
			types[i++] = param.getType();
		}
		return types;
	}
	
}
