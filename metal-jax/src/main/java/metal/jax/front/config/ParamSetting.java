/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.front.config;

import metal.core.config.Setting;

public class ParamSetting extends Setting<String, Class<?>> {

	public ParamSetting(String name, Class<?> paramType) {
		super(name, paramType);
	}
	
	public String getName() {
		return this.getKey();
	}
	
	public Class<?> getParamType() {
		return this.getValue();
	}
	
}
