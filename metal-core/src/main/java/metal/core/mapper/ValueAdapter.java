/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mapper;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ValueAdapter<V, B> extends XmlAdapter<V, B> {

	private Adapter<V, B> adapter;
	
	public ValueAdapter() {}
	
	public ValueAdapter(Adapter<V, B> adapter) {
		this.adapter = adapter;
	}
	
	public void setAdapter(Adapter<V, B> adapter) {
		this.adapter = adapter;
	}
	
	@Override
	public V marshal(B value) throws Exception {
		return adapter.marshal(value);
	}

	@Override
	public B unmarshal(V value) throws Exception {
		return adapter.unmarshal(value);
	}
	
}
