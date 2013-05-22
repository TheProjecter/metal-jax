/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mapper;

public class ValueAdapter<V, B> extends BaseAdapter<V, B> {

	public ValueAdapter() {}
	
	public ValueAdapter(Adapter<V, B> adapter) {
		super(adapter);
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
