/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.service;

public class TestImpl implements TestService {

	@Override
	public String hello(String message) {
		return "test: " + message;
	}
	
}
