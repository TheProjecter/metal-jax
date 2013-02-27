/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.mapper;

import java.util.Map;

import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

public class JsonMarshaller extends MappingJacksonHttpMessageConverter {
	
	public void setModelMap(Map<String,Class<?>> modelMap) {
//		super.setClassesToBeBound(modelMap.values().toArray(new Class<?>[0]));
	}
	
}
