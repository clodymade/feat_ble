/**
 * File: HiBleResult.kt
 *
 * Description: This data class represents the result of a BLE scan, including details about Beacons,
 *              advertisement data, and scanning errors. It supports multiple BLE use cases like Beacon scanning.
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

import android.bluetooth.le.ScanRecord

/**
 * Represents the result of a BLE (Bluetooth Low Energy) scan.
 *
 * @property scanRecord The BLE device scan record containing raw advertising data.
 * @property advertisementData A parsed map of advertisement data extracted from the scan record.
 * @property rssi The signal strength (RSSI) of the scanned device.
 * @property major The Major value of the Beacon (default is -1 if unavailable).
 * @property minor The Minor value of the Beacon (default is -1 if unavailable).
 * @property beaconUUID The UUID of the Beacon (default is null if unavailable).
 * @property error The error message that occurred during scanning, if any (default is an empty string).
 */
data class HiBleResult(
    val scanRecord: ScanRecord?,
    val advertisementData: Map<String, Any>,
    val rssi: Int,
    val major: Int = -1,
    val minor: Int = -1,
    val beaconUUID: String? = null,
    val error: String = ""
)