/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Message {

	private String value;
	
	public Message() {}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	};
	
}
