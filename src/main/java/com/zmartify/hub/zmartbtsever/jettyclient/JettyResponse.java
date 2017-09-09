/**
 * 
 */
package com.zmartify.hub.zmartbtsever.jettyclient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Peter Kristensen
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JettyResponse extends AbstractJettyResponse {

	public JettyResponse(String reqId) {
		super();
		this.reqId = reqId;
	}

	public JettyResponse(JettyRequest jettyRequest) {
		super();
		this.reqId = jettyRequest.getReqId();
		this.method = jettyRequest.getMethod();
		// this.uri = jettyRequest.getUri();
	}
}