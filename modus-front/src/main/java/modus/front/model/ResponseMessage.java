/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package modus.front.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import modus.core.mapper.ValueAdapter;

@XmlRootElement(name="response")
public class ResponseMessage {

	private Object result;
	private List<DisplayMessage> messages;

	public ResponseMessage() {}
	
	public ResponseMessage(Object result) {
		this.result = result;
	}
	
	public ResponseMessage(Object result, List<DisplayMessage> messages) {
		this.result = result;
		this.messages = messages;
	}
	
	@XmlJavaTypeAdapter(ValueAdapter.class)
	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	@XmlElementWrapper
	@XmlElement(name="message")
	public List<DisplayMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<DisplayMessage> messages) {
		this.messages = messages;
	}

}
