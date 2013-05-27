/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.front;

public enum HttpContentType {
	XML("application/xml", "text/xml"),
	JSON("application/json", "text/json"),
	FORM("application/x-www-form-urlencoded");
	
	public final String[] types;
	private HttpContentType(String... types) { this.types = types; }
	private static final HttpContentType[] values = HttpContentType.values();
	public static HttpContentType typeOf(String contentType, HttpContentType defaultType) {
		for (HttpContentType value : values) {
			for (String type : value.types) {
				if (type.equals(contentType)) return value;
			}
		}
		return defaultType;
	}
	
}
