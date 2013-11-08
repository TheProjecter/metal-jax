/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.service;

import metal.core.mop.annotation.Service;

@Service(path="/metal/core/Model")
public interface ModelService {
	
	Object load(String key);
	
	Object save(Object value);
	
}
