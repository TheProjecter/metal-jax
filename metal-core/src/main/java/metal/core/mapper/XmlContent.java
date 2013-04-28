/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mapper;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class XmlContent {

	private Object value;
	
	@XmlJavaTypeAdapter(ValueAdapter.class)
	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
}
