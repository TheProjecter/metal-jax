/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mapper;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public abstract class BaseAdapter extends XmlAdapter<Object, Object> {

	protected Adapter<Object, Object> adapter;

	protected BaseAdapter() {}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected BaseAdapter(Adapter adapter) {
		this.adapter = adapter;
	}

	public void setAdapter(Adapter<Object, Object> adapter) {
		this.adapter = adapter;
	}

}
