/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.service;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="/metal/core/model")
public class ModelImpl implements ModelService {
	
	public Object load(String key) {
		return new Object();
	}
	
	public Object save(Object value) {
		return value;
	}
	
}
