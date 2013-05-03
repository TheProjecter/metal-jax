/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.front.config;

import metal.core.config.Setting;;

public class MethodSetting extends Setting<String, ParamSetting> {

	public MethodSetting(String name, ParamSetting paramSetting) {
		super(name, paramSetting);
	}
	
	public String getName() {
		return this.getKey();
	}
	
	public ParamSetting getParamSetting() {
		return this.getValue();
	}
	
}
