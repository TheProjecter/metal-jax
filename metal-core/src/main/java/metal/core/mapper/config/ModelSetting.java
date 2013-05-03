/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mapper.config;

import metal.core.config.Setting;

public class ModelSetting extends Setting<String, Class<?>> {

	public ModelSetting(String name, Class<?> modelClass) {
		super(name, modelClass);
	}

	public String getName() {
		return this.getKey();
	}
	
	public Class<?> getModelClass() {
		return this.getValue();
	}
	
}
