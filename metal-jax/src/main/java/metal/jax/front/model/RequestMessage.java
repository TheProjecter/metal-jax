/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.front.model;

import static metal.jax.front.FrontMessageCode.*;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.beanutils.BeanUtilsBean;

import metal.core.mop.Model;
import metal.core.mop.NameDeclaration;
import metal.jax.front.FrontException;

@XmlRootElement(name="request")
public class RequestMessage extends Model {

	public RequestMessage() {}
	
	public RequestMessage(NameDeclaration... params) {
		super(params);
	}

	@Override
	public Object get(Object key) {
		Object value;
		if (containsKey(key)) {
			value = super.get(key);
		} else {
			try {
				value = getMemberType((String)key).newInstance();
				super.put((String)key, value);
			} catch (Exception e) {
				throw new FrontException(FailedParamDefaultConstruction, e);
			}
		}
		return value;
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
