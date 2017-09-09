/**
 * 
 */
package com.zmartify.hub.zmartbtserver.service.nwm;

/**
 * @author Peter Kristensen
 *
 */
public interface INWMConnection {

	/**
	 * @throws Exception
	 */
	void startup() throws Exception;

	/**
	 * @throws Exception
	 */
	void shutdown() throws Exception;

}
