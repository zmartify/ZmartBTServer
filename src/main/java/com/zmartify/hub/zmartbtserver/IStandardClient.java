package com.zmartify.hub.zmartbtserver;

import com.zmartify.hub.zmartbtserver.bluetooth.MessageListener;
import com.zmartify.hub.zmartbtsever.jettyclient.JettyRequest;

/**
 * @author Peter Kristensen
 *
 */
public interface IStandardClient {

	public void startup();

	public void shutdown();

	public void handleRequest(JettyRequest jettyRequest);

	public void register(MessageListener listener);

}
