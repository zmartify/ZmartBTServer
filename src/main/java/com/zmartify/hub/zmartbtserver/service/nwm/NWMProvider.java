/**
 * 
 */
package com.zmartify.hub.zmartbtserver.service.nwm;

import static com.zmartify.hub.zmartbtserver.service.nwm.NWMConstants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.freedesktop.DBus;
import org.freedesktop.NetworkManager;
import org.freedesktop.ObjectManager;
import org.freedesktop.Properties;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusSigHandler;
import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.UInt32;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Peter Kristensen
 *
 */
public class NWMProvider implements INWMProvider {

	private static final Logger log = LoggerFactory.getLogger(NWMProvider.class);

	/**
	 * The DBus connection used to talk to the Bluez service.
	 */
	private DBusConnection dbusConnection = null;

	/**
	 * The DBus ObjectManager for the root Bluez object.
	 */
	private ObjectManager nwmObjectManager;

	private NetworkManager nwmNetworkManager;

	private Properties nwmProperties;

	private NetworkManager.Settings nwmSettings;

	/**
	 * The DBus signal handler for the ObjectManager's InterfacesAdded signal.
	 */
	private DBusSigHandler<ObjectManager.InterfacesAdded> interfacesAddedSignalHandler;

	/**
	 * The DBus signal handler for the ObjectManager's InterfacesRemoved signal.
	 */
	private DBusSigHandler<ObjectManager.InterfacesRemoved> interfacesRemovedSignalHandler;

	/**
	 * The unique name of the DBus bus for NetworkManager.
	 */
	private String nwmDbusBusName;

	public NWMProvider() {

	}

	@Override
	public void startup() throws Exception {

		dbusConnection = DBusConnection.getConnection(DBusConnection.SYSTEM);

		DBus dbus = dbusConnection.getRemoteObject("org.freedesktop.DBus", "/", DBus.class);

		nwmDbusBusName = dbus.GetNameOwner(DBUS_NETWORKMANAGER);

		nwmObjectManager = (ObjectManager) dbusConnection.getRemoteObject(DBUS_NETWORKMANAGER,
				"/org/freedesktop/NetworkManager", ObjectManager.class);
/*
		for (Entry<DBusInterface, Map<String, Map<String, Variant<?>>>> object : nwmObjectManager.GetManagedObjects()
				.entrySet()) {
			log.info("Object: {}", object.getKey().getObjectPath());
		}
*/
		nwmNetworkManager = dbusConnection.getRemoteObject(DBUS_NETWORKMANAGER, "/org/freedesktop/NetworkManager",
				NetworkManager.class);

		nwmProperties = (Properties) dbusConnection.getRemoteObject(DBUS_NETWORKMANAGER,
				"/org/freedesktop/NetworkManager", Properties.class);

		nwmSettings = (NetworkManager.Settings) dbusConnection.getRemoteObject(DBUS_NETWORKMANAGER,
				"/org/freedesktop/NetworkManager/Settings", NetworkManager.Settings.class);

		interfacesAddedSignalHandler = new DBusSigHandler<ObjectManager.InterfacesAdded>() {
			@Override
			public void handle(ObjectManager.InterfacesAdded signal) {
				log.info("Interfaces added {} :: {}", signal.getObjectPath(), signal.getSig());
			}
		};

		interfacesRemovedSignalHandler = new DBusSigHandler<ObjectManager.InterfacesRemoved>() {

			@Override
			public void handle(ObjectManager.InterfacesRemoved signal) {
				log.info("Interfaces removed {} :: {}", signal.getObjectPath(), signal.getSig());
			}
		};

		dbusConnection.addSigHandler(ObjectManager.InterfacesAdded.class, nwmDbusBusName, nwmObjectManager,
				interfacesAddedSignalHandler);

		dbusConnection.addSigHandler(ObjectManager.InterfacesRemoved.class, nwmDbusBusName, nwmObjectManager,
				interfacesRemovedSignalHandler);

	}

	@Override
	public void shutdown() throws Exception {
		dbusConnection.removeSigHandler(ObjectManager.InterfacesAdded.class, interfacesAddedSignalHandler);
		dbusConnection.removeSigHandler(ObjectManager.InterfacesRemoved.class, interfacesRemovedSignalHandler);

		dbusConnection.disconnect();
	}

	public List<DBusInterface> listAccessPoints() throws Exception {
		List<DBusInterface> accessPoints = new ArrayList<DBusInterface>();
		for (DBusInterface device : nwmNetworkManager.GetAllDevices()) {
			try {
				org.freedesktop.NetworkManager.Device.Wireless wifi = dbusConnection.getRemoteObject(
						DBUS_NETWORKMANAGER, device.getObjectPath(),
						org.freedesktop.NetworkManager.Device.Wireless.class);
				accessPoints.addAll(wifi.GetAllAccessPoints());
			} catch (org.freedesktop.dbus.exceptions.DBusExecutionException e) {
				// Unable to retrieve Wireless information from this device,
				// skip to the next one
			}
		}
		return accessPoints;
	}

	@Override
	public List<String> listDevicesWireless() throws Exception {

		return getObjectsByInterface(NWM_DEVICEWIRELESS_INTERFACE);
	}

	/**
	 * Get a list of all NetworkManager objects that implement a given DBus
	 * interface.
	 * 
	 * @param objectInterface
	 *            the name of the interface to scan for
	 * 
	 * @return a list of all objects implementing the specified interface
	 */
	public List<String> getObjectsByInterface(String objectInterface) {
		List<String> results = new ArrayList<>();

		for (Entry<DBusInterface, Map<String, Map<String, Variant<?>>>> objectByPath : nwmObjectManager
				.GetManagedObjects().entrySet()) {
			if (objectByPath.getValue().containsKey(objectInterface)) {
				results.add(objectByPath.getKey().toString());
			}
		}
		return results;
	}

	/**
	 * Get the dbus connection.
	 * 
	 * @return the dbus connection
	 */
	@Override
	public DBusConnection getDbusConnection() {
		return dbusConnection;
	}

	/**
	 * Get the unique DBus bus name for the NetworkManager service.
	 * 
	 * @return the NetworkManager DBus bus name
	 */
	@Override
	public String getNWMDbusBusName() {
		return nwmDbusBusName;
	}

	public void printManagedObjects() {
		log.info(nwmObjectManager.GetManagedObjects().toString());
	}
	

	public List<DBusInterface> listConnections() {
		return (List<DBusInterface>) nwmSettings.ListConnections();
	}

	public boolean reloadConnections() {
		return (boolean) nwmSettings.ReloadConnections();
	}

	public DBusInterface addConnection(Map<String, Map<String, Variant<?>>> connection) {
		return (DBusInterface) nwmSettings.AddConnection(connection);
	}

	public void saveHostname(String hostname) {
		nwmSettings.SaveHostname(hostname);
	}

	public Map<String, Map<String, Variant<?>>> getConnectionSettings(String objectPath) {
		NetworkManager.Settings.Connection nwmSettingsConnection;
		try {
			nwmSettingsConnection = dbusConnection.getRemoteObject(DBUS_NETWORKMANAGER, objectPath,
					NetworkManager.Settings.Connection.class);
		} catch (DBusException e) {
			log.error("Unable to retrieve settings for connection {}", objectPath);
			return null;
		}
		return (Map<String, Map<String, Variant<?>>>) nwmSettingsConnection.GetSettings();
	}

	public DBusInterface getConnectionByUUID(String uuid) {
		return (DBusInterface) nwmSettings.GetConnectionByUuid(uuid);
	}

	public DBusInterface addConnectionUnSaved(Map<String, Map<String, Variant<?>>> connection) {
		return (DBusInterface) nwmSettings.AddConnectionUnsaved(connection);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zmartify.hub.zmartbtserver.service.nwm.INWMProvider#getDevices()
	 */
	@Override
	public List<DBusInterface> getDevices() {
		Variant<List<DBusInterface>> value = nwmProperties.Get(NWM_INTERFACE, NWM_PROPERTY_DEVICES);
		return value.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zmartify.hub.zmartbtserver.service.nwm.INWMProvider#getAllDevices()
	 */
	@Override
	public List<DBusInterface> getAllDevices() {
		return nwmNetworkManager.GetAllDevices();
//		Variant<List<DBusInterface>> value = nwmProperties.Get(NWM_INTERFACE, NWM_PROPERTY_ALLDEVICES);
	}
	
	public Map<String,Variant<?>> getDeviceProperties(Path devicePath) {
		return nwmProperties.GetAll(devicePath.getPath());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zmartify.hub.zmartbtserver.service.nwm.INWMProvider#getNetworkingEnabled(
	 * )
	 */
	@Override
	public boolean getNetworkingEnabled() {
		Variant<Boolean> value = nwmProperties.Get(NWM_INTERFACE, NWM_PROPERTY_NETWORKINGENABLED);
		return value.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zmartify.hub.zmartbtserver.service.nwm.INWMProvider#getWirelessEnabled()
	 */
	@Override
	public boolean getWirelessEnabled() {
		Variant<Boolean> value = nwmProperties.Get(NWM_INTERFACE, NWM_PROPERTY_WIRELESSENABLED);
		return value.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zmartify.hub.zmartbtserver.service.nwm.INWMProvider#setWirelessEnabled(
	 * boolean)
	 */
	@Override
	public void setWirelessEnabled(boolean enabled) {
		nwmProperties.Set(NWM_INTERFACE, NWM_PROPERTY_WIRELESSENABLED, new Variant<Boolean>(enabled));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zmartify.hub.zmartbtserver.service.nwm.INWMProvider#
	 * getWirelessHardwareEnabled()
	 */
	@Override
	public boolean getWirelessHardwareEnabled() {
		Variant<Boolean> value = nwmProperties.Get(NWM_INTERFACE, NWM_PROPERTY_WIRELESSHARDWAREENABLED);
		return value.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zmartify.hub.zmartbtserver.service.nwm.INWMProvider#getWwanEnabled()
	 */
	@Override
	public boolean getWwanEnabled() {
		Variant<Boolean> value = nwmProperties.Get(NWM_INTERFACE, NWM_PROPERTY_WWANENABLED);
		return value.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zmartify.hub.zmartbtserver.service.nwm.INWMProvider#setWwanEnabled(
	 * boolean)
	 */
	@Override
	public void setWwanEnabled(boolean enabled) {
		nwmProperties.Set(NWM_INTERFACE, NWM_PROPERTY_WWANENABLED, new Variant<Boolean>(enabled));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zmartify.hub.zmartbtserver.service.nwm.INWMProvider#getWimaxEnabled()
	 */
	@Override
	public boolean getWimaxEnabled() {
		Variant<Boolean> value = nwmProperties.Get(NWM_INTERFACE, NWM_PROPERTY_WIMAXENABLED);
		return value.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zmartify.hub.zmartbtserver.service.nwm.INWMProvider#setWimaxEnabled(
	 * boolean)
	 */
	@Override
	public void setWimaxEnabled(boolean enabled) {
		nwmProperties.Set(NWM_INTERFACE, NWM_PROPERTY_WIMAXENABLED, new Variant<Boolean>(enabled));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zmartify.hub.zmartbtserver.service.nwm.INWMProvider#getActiveConnections(
	 * )
	 */
	@Override
	public List<Path> getActiveConnections() {
		Variant<List<Path>> value = nwmProperties.Get(NWM_INTERFACE, NWM_PROPERTY_ACTIVECONNECTIONS);
		return value.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zmartify.hub.zmartbtserver.service.nwm.INWMProvider#getPrimaryConnection(
	 * )
	 */
	@Override
	public Path getPrimaryConnection() {
		Variant<Path> value = nwmProperties.Get(NWM_INTERFACE, NWM_PROPERTY_PRIMARYCONNECTION);
		return value.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zmartify.hub.zmartbtserver.service.nwm.INWMProvider#
	 * getPrimaryConnectionType()
	 */
	@Override
	public String getPrimaryConnectionType() {
		Variant<String> value = nwmProperties.Get(NWM_INTERFACE, NWM_PROPERTY_PRIMARYCONNECTIONTYPE);
		return value.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zmartify.hub.zmartbtserver.service.nwm.INWMProvider#getMetered()
	 */
	@Override
	public UInt32 getMetered() {
		Variant<UInt32> value = nwmProperties.Get(NWM_INTERFACE, NWM_PROPERTY_METERED);
		return value.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zmartify.hub.zmartbtserver.service.nwm.INWMProvider#ActivatingConnection(
	 * )
	 */
	@Override
	public Path getActivatingConnection() {
		Variant<Path> value = nwmProperties.Get(NWM_INTERFACE, NWM_PROPERTY_ACTIVATINGCONNECTION);
		return value.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zmartify.hub.zmartbtserver.service.nwm.INWMProvider#getStartup()
	 */
	@Override
	public boolean getStartup() {
		Variant<Boolean> value = nwmProperties.Get(NWM_INTERFACE, NWM_PROPERTY_STARTUP);
		return value.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zmartify.hub.zmartbtserver.service.nwm.INWMProvider#getVerion()
	 */
	@Override
	public String getVerion() {
		Variant<String> value = nwmProperties.Get(NWM_INTERFACE, NWM_PROPERTY_VERSION);
		return value.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zmartify.hub.zmartbtserver.service.nwm.INWMProvider#getState()
	 */
	@Override
	public UInt32 getState() {
		Variant<UInt32> value = nwmProperties.Get(NWM_INTERFACE, NWM_PROPERTY_STATE);
		return value.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zmartify.hub.zmartbtserver.service.nwm.INWMProvider#getConnectivity()
	 */
	@Override
	public UInt32 getConnectivity() {
		Variant<UInt32> value = nwmProperties.Get(NWM_INTERFACE, NWM_PROPERTY_CONNECTIVITY);
		return value.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zmartify.hub.zmartbtserver.service.nwm.INWMProvider#
	 * getGlobalDnsConfiguration()
	 */
	@Override
	public Map<String, Variant<?>> getGlobalDnsConfiguration() {
		Variant<Map<String, Variant<?>>> value = nwmProperties.Get(NWM_INTERFACE, NWM_PROPERTY_GLOBALDNSCONFIGURATION);
		return value.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zmartify.hub.zmartbtserver.service.nwm.INWMProvider#
	 * setGlobalDnsConfiguration(java.util.Map)
	 */
	@Override
	public void setGlobalDnsConfiguration(Map<String, Variant<?>> configuration) {
		nwmProperties.Set(NWM_INTERFACE, NWM_PROPERTY_WIMAXENABLED,
				new Variant<Map<String, Variant<?>>>(configuration));
	}

}
