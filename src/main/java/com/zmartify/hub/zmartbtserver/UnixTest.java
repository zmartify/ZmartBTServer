/**
 * 
 */
package com.zmartify.hub.zmartbtserver;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zmartify.hub.zmartbtserver.bluetooth.Message;
import com.zmartify.hub.zmartbtserver.unixclient.UnixClient;
import com.zmartify.hub.zmartbtsever.jettyclient.JettyRequest;

/**
 * @author peter
 *
 */
public class UnixTest {

	private static final Logger log = LoggerFactory.getLogger(UnixTest.class);

	private static ObjectMapper jsonMapper = new ObjectMapper();

	static Thread serverThread;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		UnixClient unixClient = new UnixClient();
		unixClient.startup();
		String test = "{ \"uri\": \"http://zmarthub/v2/system-info\", \"method\": \"GET\","
				+ " \"headers\": { \"CONTENT-TYPE\": \"application/json\" }, "
				+ " \"body\": { \"test\": [ \"ABC\", \"DEF\" ], \"AUTHORIZATION\": \"Basic test\" , \"CONTENT-TYPE\": \"application/json\" } }";

		Message message = new Message(test);

		JettyRequest request;
		try {
			request = jsonMapper.readValue(message.getPayload(), JettyRequest.class);
			unixClient.handleRequest(request);
		} catch (IOException e) {
			log.error("Error parsing message - skipped! :: {}", e.getMessage());
		}

		unixClient.shutdown();
	}

}
