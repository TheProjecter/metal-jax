/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.front;

public enum HttpContentType {
	XML("text/xml"),
	FORM("application/x-www-form-urlencoded");
	
	public final String type;
	private HttpContentType(String type) { this.type = type; }
	private static final HttpContentType[] values = HttpContentType.values();
	public static HttpContentType typeOf(String type) {
		for (HttpContentType value : values) {
			if (value.type.equals(type)) return value;
		}
		return FORM;
	}
	
}
