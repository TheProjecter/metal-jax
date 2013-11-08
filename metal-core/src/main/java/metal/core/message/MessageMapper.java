/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.Resource;

public class MessageMapper {

	private ResourceBundleMessageSource messageSource;
	
	private Map<String,Resource[]> resourceMap;
	
	private MessageMapper parent;
	
	public void setMessageSource(ResourceBundleMessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	public void setResourceMap(Map<String,Resource[]> resourceMap) {
		this.resourceMap = resourceMap;
	}
	
	public void setParent(MessageMapper parent) {
		this.parent = parent;
	}
	
	@PostConstruct
	public void init() {
		List<String> names = new ArrayList<String>();
		for (Entry<String,Resource[]> entry : resourceMap.entrySet()) {
			String base = entry.getKey();
			Resource[] resources = entry.getValue();
			for (int i = 0; i < resources.length; i++) {
				String name = new StringBuilder(base).append("/").append(resources[i].getFilename().split("[.]")[0]).toString();
				names.add(name);
			}
		}
		messageSource.setBasenames(names.toArray(new String[0]));
		if (parent != null) {
			messageSource.setParentMessageSource(parent.messageSource);
		}
	}
	
	public String getMessage(Message message) {
		return messageSource.getMessage(message, null);
	}

}
