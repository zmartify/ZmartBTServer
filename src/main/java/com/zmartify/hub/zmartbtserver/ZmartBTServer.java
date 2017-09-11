/**
 * 
 */
package com.zmartify.hub.zmartbtserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zmartify.hub.zmartbtserver.bluetooth.ZmartBTRfcommServer;

/**
 * @author peter
 *
 */
public class ZmartBTServer {

	private static final Logger log = LoggerFactory.getLogger(ZmartBTServer.class);

	static Thread serverThread;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		serverThread = new Thread(new ZmartBTRfcommServer());
		serverThread.start();
	}

}
