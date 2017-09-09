/**
 * 
 */
package com.zmartify.hub.zmartbtserver.service.nwm;

/**
 * @author Peter Kristensen
 *
 */
public class NWMConstants {
	
	final static String DBUS_NETWORKMANAGER = "org.freedesktop.NetworkManager";

	/**
	 * The NetworkManager DBus interface for AccessPoint.
	 */
	static final String NWM_INTERFACE = "org.freedesktop.NetworkManager";
	static final String NWM_PATH = "/org/freedesktop/NetworkManager";
	
	
	/**
	 * The NetworkManager DBus interface for AccessPoints.
	 */
	public static final String NWM_ACCESSPOINT_INTERFACE = "org.freedesktop.NetworkManager.AccessPoint";

	/**
	 * The NetworkManager DBus interface for Device.
	 */
	public static final String NWM_DEVICE_INTERFACE = "org.freedesktop.NetworkManager.Device";

	/**
	 * The NetworkManager DBus interface for Device.Wireless.
	 */
	public static final String NWM_DEVICEWIRELESS_INTERFACE = "org.freedesktop.NetworkManager.Device.Wireless";
	
	static final String NWM_PROPERTY_DEVICES = "Devices";
	static final String NWM_PROPERTY_ALLDEVICES = "AllDevices";
	static final String NWM_PROPERTY_NETWORKINGENABLED = "NetworkingEnabled";
	static final String NWM_PROPERTY_WIRELESSENABLED = "WirelessEnabled";
	static final String NWM_PROPERTY_WIRELESSHARDWAREENABLED = "WirelessHardwareEnabled";
	static final String NWM_PROPERTY_WWANENABLED = "WwanEnabled";
	static final String NWM_PROPERTY_WWANHARDWAREENABLED = "WwanHardwareEnabled";
	static final String NWM_PROPERTY_WIMAXENABLED = "WimaxEnabled";
	static final String NWM_PROPERTY_WIMAXHARDWAREENABLED = "WimaxHardwareEnabled";
	static final String NWM_PROPERTY_ACTIVECONNECTIONS = "ActiveConnections";
	static final String NWM_PROPERTY_PRIMARYCONNECTION = "PrimaryConnection";
	static final String NWM_PROPERTY_PRIMARYCONNECTIONTYPE = "PrimayConnectionType";
	static final String NWM_PROPERTY_METERED = "Metered";
	static final String NWM_PROPERTY_ACTIVATINGCONNECTION = "ActivatingConnection";
	static final String NWM_PROPERTY_STARTUP = "Startup";
	static final String NWM_PROPERTY_VERSION = "Metered";
	static final String NWM_PROPERTY_STATE = "State";
	static final String NWM_PROPERTY_CONNECTIVITY = "Connectivity";
	static final String NWM_PROPERTY_GLOBALDNSCONFIGURATION = "GlobalDnsConfiguration";

}
