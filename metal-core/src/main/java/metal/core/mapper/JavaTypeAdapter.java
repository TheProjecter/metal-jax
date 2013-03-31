/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mapper;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class JavaTypeAdapter extends XmlAdapter<Object,Object> implements Adapter<Object,Object> {

	public static final class EventHandler implements ValidationEventHandler {
		public boolean handleEvent(ValidationEvent event) {
			return false;
		}
	}
	
	private Adapter<Object,Object> adapter;
	
	public JavaTypeAdapter() {}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JavaTypeAdapter(Adapter adapter) {
		this.adapter = adapter;
	}
	
	public void setAdapter(Adapter<Object,Object> adapter) {
		this.adapter = adapter;
	}
	
	@Override
	public Object marshal(Object object) throws Exception {
		return adapter.marshal(object);
	}

	@Override
	public Object unmarshal(Object value) throws Exception {
		return adapter.unmarshal(value);
	}
	
}
