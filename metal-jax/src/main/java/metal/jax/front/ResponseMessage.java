/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.front;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import metal.core.mapper.ValueAdapter;

@XmlRootElement(name="response")
public class ResponseMessage {

	private Object result;
	private String message;
	private String messageCode;

	public ResponseMessage() {}
	
	public ResponseMessage(Object result, String message, String messageCode) {
		this.result = result;
		this.message = message;
		this.messageCode = messageCode;
	}
	
	@XmlJavaTypeAdapter(ValueAdapter.class)
	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

}
