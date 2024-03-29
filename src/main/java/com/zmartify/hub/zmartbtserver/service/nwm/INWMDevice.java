/**
 * 
 */
package com.zmartify.hub.zmartbtserver.service.nwm;

import java.util.List;
import java.util.Map;

import org.freedesktop.Pair;
import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.UInt32;
import org.freedesktop.dbus.Variant;

/**
 * @author Peter Kristensen
 *
 */
public interface INWMDevice {

	Map<String, Variant<?>> getLldpNeighbors();

	List<DBusInterface> getAvailableConnections();

	boolean getAutoconnect();

	boolean getFirmwareMissing();

	boolean getManaged();

	boolean getNmPluginMissing();

	boolean Real();

	DBusInterface getActiveConnection();

	DBusInterface getDhcp4Config();

	DBusInterface getDhcp6Config();

	DBusInterface getIp4Config();

	DBusInterface getIp6Config();

	String getDriver();

	String getDriverVersion();

	String getFirmwareVersion();

	String getInterface();

	String getPhysicalPort();

	String getUdi();

	Pair<UInt32, UInt32> getStateReason();

	UInt32 getCapabilities();

	UInt32 getDeviceType();

	UInt32 getIp4Address();

	UInt32 getMetered();

	UInt32 getMtu();

	UInt32 getState();

	Map<String, Variant<?>> getAll();

	/**
	 * @throws Exception
	 */
	void startup() throws Exception;

	/**
	 * @throws Exception
	 */
	void shutdown() throws Exception;


}
