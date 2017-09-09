/**
 * 
 */
package com.zmartify.hub.zmartbtserver.bluetooth;

/**
 * @author Peter Kristensen
 *
 */
public interface ISendMessage {
	
	public void sendMessage(Message message);
	
	public void sendMessage(byte[] payload);

	public void sendMessage(String payloadStr);

}
