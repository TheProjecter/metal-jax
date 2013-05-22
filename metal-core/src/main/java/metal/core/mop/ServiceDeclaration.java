/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mop;

import java.util.Map;

public class ServiceDeclaration extends Setting<String, Map<String, MethodDeclaration>> {

	public ServiceDeclaration(String name, Map<String, MethodDeclaration> methods) {
		super(name, methods);
	}
	
	public String getName() {
		return this.getKey();
	}
	
	public MethodDeclaration getMethodDeclaration(String name) {
		return this.getValue().get(name);
	}
	
}
