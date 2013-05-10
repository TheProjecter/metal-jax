/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.service;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="/metal/jax/echo")
public class EchoImpl implements EchoService {

	@XmlElement
	@XmlAttribute(name="message")
	public String hello(HashMap<String,Object> message) {
		return "echo: " + message.get("value");
	}
	
}
