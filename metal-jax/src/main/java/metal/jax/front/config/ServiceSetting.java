/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.front.config;

import java.util.Map;

import metal.core.config.Setting;;

public class ServiceSetting extends Setting<String, Map<String, MethodSetting>> {

	public ServiceSetting(String name, Map<String, MethodSetting> methodMap) {
		super(name, methodMap);
	}
	
	public String getName() {
		return this.getKey();
	}
	
	public MethodSetting getMethodSetting(String name) {
		return this.getValue().get(name);
	}
	
}
