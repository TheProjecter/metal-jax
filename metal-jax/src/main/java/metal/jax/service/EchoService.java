/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.service;

import metal.core.mop.annotation.Method;
import metal.core.mop.annotation.Service;

@Service(path="/metal/jax/echo")
public interface EchoService {

	@Method(params={"message","from"})
	String hello(Long message, String from);
	
}
