/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.front.common;

public enum HttpContentType {
	XML("application/xml", "text/xml", "xml"),
	JSON("application/json", "text/json", "json"),
	JS("application/javascript", "text/javascript", "js"),
	FORM("application/x-www-form-urlencoded");
	
	private final String[] types;
	public final String contentType;
	private HttpContentType(String... types) { this.types = types; contentType = types[0]; }
	private static final HttpContentType[] values = HttpContentType.values();
	public static HttpContentType typeOf(String contentType, HttpContentType defaultType) {
		if (contentType != null) {
			for (HttpContentType value : values) {
				for (String type : value.types) {
					if (contentType.indexOf(type) >= 0) return value;
				}
			}
		}
		return defaultType;
	}
	
}
