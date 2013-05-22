/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.front.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.beanutils.BeanUtilsBean;

import metal.core.mop.Model;
import metal.core.mop.NameDeclaration;

@XmlRootElement(name="request")
public class RequestMessage extends Model {

	public RequestMessage() {}
	
	public RequestMessage(NameDeclaration... params) {
		super(params);
	}

	@Override
	public Object put(String key, Object value) {
		Class<?> type = this.getMemberType(key);
		if (value != null && !type.isAssignableFrom(value.getClass())) {
			value = BeanUtilsBean.getInstance().getConvertUtils().convert(value, type);
		}
		return super.put(key, value);
	}
	
}
