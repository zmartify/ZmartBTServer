/**
 * 
 */
package com.zmartify.hub.zmartbtserver;

import java.util.List;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.zmartify.hub.zmartbtserver.service.bluetooth.IBluezBluetoothDevice;
import com.zmartify.hub.zmartbtserver.service.bluetooth.bluez5.BluezBluetoothProvider;
import com.zmartify.hub.zmartbtserver.service.nwm.NWMAccessPoint;
import com.zmartify.hub.zmartbtserver.service.nwm.NWMDevice;
import com.zmartify.hub.zmartbtserver.service.nwm.NWMProvider;
import com.zmartify.hub.zmartbtserver.utils.VariantSerializer;

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

		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addSerializer(new VariantSerializer(Variant.class));
		mapper.registerModule(module);

		BluezBluetoothProvider bluezProvider = new BluezBluetoothProvider();
		NWMProvider nwmProvider = new NWMProvider();

		boolean BLUETOOTH = false;
		boolean WIRELESS = true;

		try {
			if (BLUETOOTH) {
				bluezProvider.startup();

				log.info("Adapters: {}", bluezProvider.listAdapters());
				log.info("Devices:  {}", bluezProvider.listDevices());
				log.info("Agents:   {}", bluezProvider.listAgents());
			}

			if (WIRELESS) {
				nwmProvider.startup();

				/*
				 * List<String> devicesWireless = nwmProvider.listDevicesWireless();
				
				devicesWireless.forEach(deviceWireless -> log.info("\n\n****>Wireless Device: {}\n\n", deviceWireless));
*/
				List<DBusInterface> devices = nwmProvider.getAllDevices();
				devices.forEach(device -> {
					try {
						log.info("<DEVICE> {}", device);
						NWMDevice dev = new NWMDevice(nwmProvider,device.getObjectPath());
						dev.startup();
						log.info("Device: {} PhysicalPort: {}", dev, dev.getInterface());
						log.info("Device: {} \n{}\n",device,  mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dev.getAll()));
						dev.shutdown();
					} catch (JsonProcessingException e) {
						log.error("Error writing device properties {}", device);
						e.printStackTrace();
					} 
					catch (Exception e) {
						log.error("Error when starting device");
						e.printStackTrace();
					}
				});
				
				
				List<DBusInterface> connections = nwmProvider.listConnections();

				connections.forEach(connection -> {
					log.info("\n----->Settings: {}\n {}\n", connection.getObjectPath(), nwmProvider.getConnectionSettings(connection.getObjectPath()));
					try {
						log.info("\n----->Settings: {}\n {}\n", connection.getObjectPath(), mapper.writerWithDefaultPrettyPrinter().writeValueAsString(nwmProvider.getConnectionSettings(connection.getObjectPath())));
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});

				List<DBusInterface> accessPoints = nwmProvider.listAccessPoints();

				accessPoints.forEach(accessPoint -> {
					log.info("AccessPoint: *** {} ***", accessPoint);
					try {
						NWMAccessPoint ap = new NWMAccessPoint(nwmProvider, accessPoint.getObjectPath());
						ap.startup();
						log.info("Hardware Address: {} ({}) {}", accessPoint, ap.getSsidAsString(), ap.getHwAddress());
						log.info("GetAll          : {} \n{}\n", accessPoint, ap.getAll());
						log.info("GetAll          : {} \n{}\n", accessPoint, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ap.getAll()));
						ap.shutdown();
					} catch (JsonProcessingException e) {
						log.error("Error Jackson Serializing :: {} \n\n {} \n\n", e.getMessage());
						e.printStackTrace();
					} catch (Exception e) {
						log.error("Error starting AccessPoint");
						e.printStackTrace();
					}
				});

				log.info("Connections: {} ", nwmProvider.listConnections());
			}

			if (BLUETOOTH) {
				bluezProvider.getBluezAdapter().startScanning();

				log.info("Now scanning");

				String address = "B0:B4:48:BD:D0:83";
				if (args.length > 0) {
					address = args[0];
				}

				IBluezBluetoothDevice device = bluezProvider.getBluezAdapter().newDevice(address);

				device.startup();

				log.info("Adapters: {}", bluezProvider.listAdapters());
				log.info("Devices:  {}", bluezProvider.listDevices());
			}

			if (BLUETOOTH)
				bluezProvider.shutdown();

			if (WIRELESS)
				nwmProvider.shutdown();

		} catch (Exception e) {
			log.error("Something BAD happened ;-) :: {}", e.getMessage());
			e.printStackTrace();
		}

		// serverThread = new Thread(new BluetoothRfcommServer());
		// serverThread.start();
	}

}
