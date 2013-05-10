/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mop;

public class Setting<K, V> {

	private K key;
	private V value;

	protected Setting(K key, V value) {
		this.key = key;
		this.value = value;
	}

	protected K getKey() {
		return key;
	}

	protected V getValue() {
		return value;
	}

}
