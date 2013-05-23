/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.service;

import org.springframework.stereotype.Service;

@Service("metal-jax-echoService")
public class EchoImpl implements EchoService {

	public String hello(Long message, String from) {
		return "echo: " + message + ", from: " + from;
	}
	
}
