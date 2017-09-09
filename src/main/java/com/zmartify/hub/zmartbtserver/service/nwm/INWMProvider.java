/**
 * 
 */
package com.zmartify.hub.zmartbtserver.service.nwm;

import java.util.List;
import java.util.Map;

import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.UInt32;
import org.freedesktop.dbus.Variant;

/**
 * @author Peter Kristensen
 *
 */
public interface INWMProvider {

	void startup() throws Exception;

	void shutdown() throws Exception;

	List<DBusInterface> getDevices();
	
	List<DBusInterface> getAllDevices();
	
	boolean getNetworkingEnabled();
	
	boolean getWirelessEnabled();
	void setWirelessEnabled(boolean enabled);
	
	boolean getWirelessHardwareEnabled();
	
	boolean getWwanEnabled();
	void setWwanEnabled(boolean enabled);
	
	boolean getWimaxEnabled();
	void setWimaxEnabled(boolean enabled);
	
	List<Path> getActiveConnections();
	
	Path getPrimaryConnection();
	
	String getPrimaryConnectionType();
	
	UInt32 getMetered();
	
	Path getActivatingConnection();
	
	boolean getStartup();
	
	String getVerion();
	
	UInt32 getState();
	
	UInt32 getConnectivity();
	
	Map<String,Variant<?>> getGlobalDnsConfiguration();
	void setGlobalDnsConfiguration(Map<String,Variant<?>> configuration);

	/**
	 * @return
	 */
	DBusConnection getDbusConnection();

	/**
	 * @return
	 */
	String getNWMDbusBusName();

	/**
	 * @return
	 * @throws Exception
	 */
	List<String> listDevicesWireless() throws Exception;

}
