/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zigbee.telegesis.internal.discovery;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.openhab.binding.zigbee.ZigBeeBindingConstants.CONFIGURATION_BAUD;
import static org.openhab.binding.zigbee.ZigBeeBindingConstants.CONFIGURATION_PORT;
import static org.openhab.binding.zigbee.telegesis.TelegesisBindingConstants.THING_TYPE_TELEGESIS;
import static org.openhab.binding.zigbee.telegesis.internal.discovery.ZigBeeTelegesisUsbSerialDiscoveryParticipant.QIVICON_DONGLE_V1_AND_TELEGESIS_USB_PRODUCT_ID;
import static org.openhab.binding.zigbee.telegesis.internal.discovery.ZigBeeTelegesisUsbSerialDiscoveryParticipant.QIVICON_DONGLE_V2_USB_PRODUCT_ID;
import static org.openhab.binding.zigbee.telegesis.internal.discovery.ZigBeeTelegesisUsbSerialDiscoveryParticipant.SILICON_LABS_USB_VENDOR_ID;

import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.usbserial.UsbSerialDeviceInformation;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.zigbee.telegesis.internal.discovery.ZigBeeTelegesisUsbSerialDiscoveryParticipant;

/**
 * Unit tests for the {@link ZigBeeTelegesisUsbSerialDiscoveryParticipant}.
 */
public class ZigBeeTelegesisUsbSerialDiscoveryParticipantTest {

    private ZigBeeTelegesisUsbSerialDiscoveryParticipant discoveryParticipant;
    
    @Before
    public void setup() {
        discoveryParticipant = new ZigBeeTelegesisUsbSerialDiscoveryParticipant();
    }
    
    /**
     * If only USB vendor ID or only USB product ID or none of them matches, then no device is discovered. 
     */
    @Test
    public void testNonTelegesisDongleNotDiscovered() {
        assertNull(discoveryParticipant.getThingUID(forUsbDongle(SILICON_LABS_USB_VENDOR_ID, 0x1234)));
        assertNull(discoveryParticipant.getThingUID(forUsbDongle(0xabcd, QIVICON_DONGLE_V1_AND_TELEGESIS_USB_PRODUCT_ID)));
        assertNull(discoveryParticipant.getThingUID(forUsbDongle(0xabcd, QIVICON_DONGLE_V2_USB_PRODUCT_ID)));
        assertNull(discoveryParticipant.getThingUID(forUsbDongle(0xabcd, 0x1234)));
        
        assertNull(discoveryParticipant.createResult(forUsbDongle(SILICON_LABS_USB_VENDOR_ID, 0x1234)));
        assertNull(discoveryParticipant.createResult(forUsbDongle(0xabcd, QIVICON_DONGLE_V1_AND_TELEGESIS_USB_PRODUCT_ID)));
        assertNull(discoveryParticipant.createResult(forUsbDongle(0xabcd, QIVICON_DONGLE_V2_USB_PRODUCT_ID)));
        assertNull(discoveryParticipant.createResult(forUsbDongle(0xabcd, 0x1234)));
    }
    
    /**
     * For matching USB vendor and product ID, a suitable thingUID is returned.
     */
    @Test
    public void testTelegesisDongleV1DiscoveredThingUID() {
        ThingUID thingUID = discoveryParticipant.getThingUID(
                forUsbDongle(SILICON_LABS_USB_VENDOR_ID, QIVICON_DONGLE_V1_AND_TELEGESIS_USB_PRODUCT_ID, "serial", "/dev/ttyUSB0"));
        
        assertNotNull(thingUID);
        assertEquals(thingUID, new ThingUID(THING_TYPE_TELEGESIS, "serial"));
    }
    
    /**
     * For matching USB vendor and product ID, a suitable discovery result is returned.
     */
    @Test
    public void testTelegesisDongleV1DiscoveredDiscoveryResult() {
        DiscoveryResult discoveryResult = discoveryParticipant.createResult(
                forUsbDongle(SILICON_LABS_USB_VENDOR_ID, QIVICON_DONGLE_V1_AND_TELEGESIS_USB_PRODUCT_ID, "serial", "/dev/ttyUSB0"));
        
        assertNotNull(discoveryResult);
        assertEquals(discoveryResult.getThingUID(), new ThingUID(THING_TYPE_TELEGESIS, "serial"));
        assertNotNull(discoveryResult.getLabel());
        assertEquals(discoveryResult.getRepresentationProperty(), CONFIGURATION_PORT);
        assertEquals(discoveryResult.getProperties().get(CONFIGURATION_PORT), "/dev/ttyUSB0");
        assertNotNull(discoveryResult.getProperties().get(CONFIGURATION_BAUD));
    }
    
    /**
     * For matching USB vendor and product ID, a suitable thingUID is returned.
     */
    @Test
    public void testTelegesisDongleV2DiscoveredThingUID() {
        ThingUID thingUID = discoveryParticipant.getThingUID(
                forUsbDongle(SILICON_LABS_USB_VENDOR_ID, QIVICON_DONGLE_V2_USB_PRODUCT_ID, "serial", "/dev/ttyUSB0"));
        
        assertNotNull(thingUID);
        assertEquals(thingUID, new ThingUID(THING_TYPE_TELEGESIS, "serial"));
    }
    
    /**
     * For matching USB vendor and product ID, a suitable discovery result is returned.
     */
    @Test
    public void testTelegesisDongleV2DiscoveredDiscoveryResult() {
        DiscoveryResult discoveryResult = discoveryParticipant.createResult(
                forUsbDongle(SILICON_LABS_USB_VENDOR_ID, QIVICON_DONGLE_V2_USB_PRODUCT_ID, "serial", "/dev/ttyUSB0"));
        
        assertNotNull(discoveryResult);
        assertEquals(discoveryResult.getThingUID(), new ThingUID(THING_TYPE_TELEGESIS, "serial"));
        assertNotNull(discoveryResult.getLabel());
        assertEquals(discoveryResult.getRepresentationProperty(), CONFIGURATION_PORT);
        assertEquals(discoveryResult.getProperties().get(CONFIGURATION_PORT), "/dev/ttyUSB0");
        assertNotNull(discoveryResult.getProperties().get(CONFIGURATION_BAUD));
    }
    
    private UsbSerialDeviceInformation forUsbDongle(int vendorId, int productId, String serial, String device) {
        return new UsbSerialDeviceInformation(vendorId, productId, serial, null, null, 0, null, device);
    }

    private UsbSerialDeviceInformation forUsbDongle(int vendorId, int productId) {
        return new UsbSerialDeviceInformation(vendorId, productId, null, null, null, 0, null, "/dev/ttyUSB0");
    }
    
}