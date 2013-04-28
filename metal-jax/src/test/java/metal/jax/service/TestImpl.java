/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.service;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import metal.jax.model.Message;

@XmlRootElement(name="/test/jax/test")
public class TestImpl implements TestService {

	@XmlElement
	public String hello(Message message) {
		return "test: " + message.getValue();
	}
	
}