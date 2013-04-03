/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mapper;

import static metal.core.mapper.Adapter.Kind.PROPERTYLIST;

public class PropertyListAdapter<V, B> extends BaseAdapter<V, B> {

	public PropertyListAdapter() {}
	
	public PropertyListAdapter(Adapter<V, B> adapter) {
		super(adapter);
	}
	
	@Override
	public V marshal(B object) throws Exception {
		return adapter.marshal(PROPERTYLIST, object);
	}

	@Override
	public B unmarshal(V value) throws Exception {
		return adapter.unmarshal(PROPERTYLIST, value);
	}
	
}
