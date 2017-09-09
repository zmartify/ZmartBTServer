/**
 * 
 */
package com.zmartify.hub.zmartbtserver.service.nwm;

import java.util.List;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.UInt32;

/**
 * @author Peter Kristensen
 *
 */
public interface INWMDeviceWireless {

	/**
	 * @throws Exception
	 */
	void startup() throws Exception;

	/**
	 * @throws Exception
	 */
	void shutdown() throws Exception;

	String getHwAddress();

	String getPermHwAddress();

	UInt32 getMode();

	UInt32 getBitrate();

	List<DBusInterface> getAccessPoints();

	DBusInterface getActiveAccessPoint();

	UInt32 getWirelessCapabilities();
}
