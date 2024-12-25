/**
 * File: HiBleParser.kt
 * Description: Utility object for parsing advertisement data from BLE scan records, including
 *              device name, manufacturer-specific data, service UUIDs, and Tx Power levels.
 *
 * Author: netcanis
 * Created: 2024-11-19
 *
 * License: MIT
 *
 * References:
 * - Android BLE Advertisement Data: https://developer.android.com/guide/topics/connectivity/bluetooth/ble-advertising
 * - BLE Manufacturer Data Specification
 */

package com.mwkg.ble.util

import android.bluetooth.le.ScanRecord

/**
 * Utility object for parsing BLE (Bluetooth Low Energy) advertisement data.
 */
object HiBleParser {

    /**
     * Extracts advertisement data from a BLE scan record and returns it as a map.
     *
     * @param scanRecord The BLE scan record containing advertisement data.
     * @return A map of parsed advertisement data with keys such as "deviceName", "manufacturerData", "serviceUuids", "serviceData", etc.
     */
    fun getAdvertisementData(scanRecord: ScanRecord?): Map<String, Any> {
        if (scanRecord == null) return emptyMap()

        val advertisementData = mutableMapOf<String, Any>()

        // Device Name (Local Name)
        val deviceName = scanRecord.deviceName
        if (deviceName != null) {
            advertisementData["deviceName"] = deviceName
        }

        // Manufacturer Specific Data
        val manufacturerData = scanRecord.manufacturerSpecificData
        if (manufacturerData != null) {
            val manufacturerEntries = mutableMapOf<Int, ByteArray>()
            for (i in 0 until manufacturerData.size()) {
                val key = manufacturerData.keyAt(i)
                val value = manufacturerData.valueAt(i)
                manufacturerEntries[key] = value
            }
            advertisementData["manufacturerData"] = manufacturerEntries
        }

        // Service UUIDs
        val serviceUuids = scanRecord.serviceUuids
        if (!serviceUuids.isNullOrEmpty()) {
            advertisementData["serviceUuids"] = serviceUuids.map { it.uuid.toString() }
        }

        // Service Data
        val serviceData = scanRecord.serviceData
        if (serviceData != null) {
            val serviceDataEntries = mutableMapOf<String, ByteArray>()
            for (entry in serviceData.entries) {
                serviceDataEntries[entry.key.toString()] = entry.value
            }
            advertisementData["serviceData"] = serviceDataEntries
        }

        // Transmit Power Level (Tx Power Level)
        val txPowerLevel = scanRecord.txPowerLevel
        if (txPowerLevel != Int.MIN_VALUE) {
            advertisementData["txPowerLevel"] = txPowerLevel
        }

        // Is Connectable
        val isConnectable = isDeviceConnectable(scanRecord)
        advertisementData["isConnectable"] = isConnectable

        return advertisementData
    }

    /**
     * Determines whether the BLE device is connectable based on its scan record.
     *
     * @param scanRecord The BLE scan record containing advertisement flags.
     * @return `true` if the device is connectable; `false` otherwise.
     */
    fun isDeviceConnectable(scanRecord: ScanRecord?): Boolean {
        // Get the advertisement bytes from the ScanRecord
        val bytes = scanRecord?.bytes ?: return false

        // Advertising Flags are stored in the first byte
        val flags = bytes.getOrNull(0)?.toInt() ?: return false

        // Check if the LE General Discoverable Mode bit (0x02) is set
        return flags and 0x02 != 0
    }
}
