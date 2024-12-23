/**
 * File: HiBeaconParser.kt
 * Description: Utility object for parsing iBeacon-specific data from BLE scan records. Extracts
 *              major and minor values from the BLE advertisement payload.
 *
 * Author: netcanis
 * Created: 2024-11-19
 *
 * License: MIT
 *
 * References:
 * - iBeacon Protocol Specification: https://developer.apple.com/ibeacon/
 * - BLE ScanRecord: https://developer.android.com/reference/android/bluetooth/le/ScanRecord
 */

package com.mwkg.ble.util

import android.bluetooth.le.ScanRecord

/**
 * Utility object for parsing iBeacon data from BLE scan records.
 */
object HiBeaconParser {

    /**
     * Data class representing iBeacon-specific data, including major and minor values.
     *
     * @property major The major identifier of the iBeacon. Defaults to -1 if unavailable.
     * @property minor The minor identifier of the iBeacon. Defaults to -1 if unavailable.
     */
    data class IBeaconData(
        val major: Int = -1,
        val minor: Int = -1
    )

    /**
     * Extracts iBeacon-specific data (major and minor values) from a BLE scan record.
     *
     * @param scanRecord The BLE scan record containing iBeacon data.
     * @return An [IBeaconData] object if extraction is successful, or `null` if the scan record is invalid or lacks sufficient data.
     */
    fun extractIBeaconData(scanRecord: ScanRecord?): IBeaconData? {
        val bytes = scanRecord?.bytes ?: return null
        // iBeacon data requires at least 30 bytes.
        if (bytes.size < 30) return null
        val major = extractMajor(scanRecord)
        val minor = extractMinor(scanRecord)
        return IBeaconData(major, minor)
    }

    /**
     * Extracts the major value from a BLE scan record.
     *
     * @param scanRecord The BLE scan record containing iBeacon data.
     * @return The major value, or -1 if the data is unavailable or insufficient.
     */
    fun extractMajor(scanRecord: ScanRecord?): Int {
        val bytes = scanRecord?.bytes ?: return -1
        // The major value is stored in bytes 25 and 26.
        if (bytes.size < 26) return -1
        return ((bytes[25].toInt() and 0xff) shl 8) or (bytes[26].toInt() and 0xff)
    }

    /**
     * Extracts the minor value from a BLE scan record.
     *
     * @param scanRecord The BLE scan record containing iBeacon data.
     * @return The minor value, or -1 if the data is unavailable or insufficient.
     */
    fun extractMinor(scanRecord: ScanRecord?): Int {
        val bytes = scanRecord?.bytes ?: return -1
        // The minor value is stored in bytes 27 and 28.
        if (bytes.size < 28) return -1
        return ((bytes[27].toInt() and 0xff) shl 8) or (bytes[28].toInt() and 0xff)
    }
}