/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.front.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="message")
public class DisplayMessage {

	private String kind;
	private String code;
	private String detail;

	public DisplayMessage() {}
	
	public DisplayMessage(String kind, String code, String detail) {
		this.kind = kind;
		this.code = code;
		this.detail = detail;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	
}
