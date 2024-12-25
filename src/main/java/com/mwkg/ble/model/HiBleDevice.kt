/**
 * File: HiBleDevice.kt
 * Description: Data class representing a Bluetooth Low Energy (BLE) device with relevant details,
 *              including device name, RSSI, UUID, advertisement data, and iBeacon-specific values.
 *
 * Author: netcanis
 * Created: 2024-11-19
 *
 * License: MIT
 *
 * References:
 * - Android BLE Overview: https://developer.android.com/guide/topics/connectivity/bluetooth/ble-overview
 */

package com.mwkg.ble.model

/**
 * Represents a BLE (Bluetooth Low Energy) device and its associated details.
 *
 * @property deviceName The name of the BLE device.
 * @property rssi The Received Signal Strength Indicator (RSSI) value, which indicates the signal strength.
 * @property uuid The Universally Unique Identifier (UUID) of the BLE device.
 * @property advertisementData A map containing advertisement data received from the BLE device.
 * @property major The major value in the BLE device's beacon data.
 * @property minor The minor value in the BLE device's beacon data.
 * @property beaconUUID The UUID of the Beacon (default is null if unavailable).
 */
data class HiBleDevice(
        val deviceName: String,
        val rssi: Int,
        val uuid: String,
        val advertisementData: Map<String, Any>,
        val major: Int,
        val minor: Int,
        val beaconUUID: String
)