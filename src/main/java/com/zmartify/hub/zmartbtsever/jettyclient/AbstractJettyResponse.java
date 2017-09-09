/**
 * 
 */
package com.zmartify.hub.zmartbtsever.jettyclient;

import org.eclipse.jetty.http.HttpFields;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Peter Kristensen
 *
 */

abstract public class AbstractJettyResponse extends AbstractJettyRequest implements IJettyResponse {

	@JsonProperty("status-code")
	protected int statusCode;

	@JsonProperty("status")
	protected String statusText;

	public AbstractJettyResponse() {
		super();
	}

	public void setHeaders(HttpFields headers) {
		this.headers = headers;
	}
	
	public void setBody(JsonNode body) {
		this.body = body;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
}
