/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mop;

public class NameDeclaration extends Setting<String, Class<?>> {

	public NameDeclaration(String name, Class<?> type) {
		super(name, type);
	}

	public String getName() {
		return this.getKey();
	}
	
	public Class<?> getType() {
		return this.getValue();
	}
	
}
