/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package modus.front.common;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import modus.core.common.AnyException;
import modus.core.message.Message;
import modus.core.message.MessageMapper;
import modus.front.model.DisplayMessage;

public class DisplayMessageMapper {
	
	@Resource
	private MessageMapper messageMapper;
	
	public List<DisplayMessage> createMessages(String kind, List<? extends Message> messages) {
		List<DisplayMessage> displayMessages = null;
		for (Message message : messages) {
			if (displayMessages == null) displayMessages = new ArrayList<DisplayMessage>();
			addMessage(kind, message, displayMessages);
		}
		return displayMessages;
	}
	
	public List<DisplayMessage> createMessages(String kind, AnyException ex) {
		return addMessage(kind, ex, new ArrayList<DisplayMessage>());
	}
	
	public List<DisplayMessage> createMessages(String kind, Throwable ex) {
		List<DisplayMessage> messages = new ArrayList<DisplayMessage>();
		messages.add(new DisplayMessage(kind, ex.getClass().getName(), ex.toString()));
		return messages;
	}
	
	private List<DisplayMessage> addMessage(String kind, Message message, List<DisplayMessage> messages) {
		messages.add(new DisplayMessage(kind, message.getCode().name(), messageMapper.getMessage(message)));
		return messages;
	}
	
}
