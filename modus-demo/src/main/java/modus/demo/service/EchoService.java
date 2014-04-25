/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package modus.demo.service;

import modus.core.mop.annotation.Method;
import modus.core.mop.annotation.Service;
import modus.demo.model.Hello;

@Service(path="/modus/demo/Echo")
public interface EchoService {
	
	@Method(params={"model"})
	Hello hello(Hello model);
	
}
