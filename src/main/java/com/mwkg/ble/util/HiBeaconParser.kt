/**
 * File: HiBeaconParser.kt
 * Description: Utility object for parsing iBeacon-specific data from BLE scan records. Extracts
 *              UUID, major, and minor values from the BLE advertisement payload.
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
import java.util.UUID

/**
 * Utility object for parsing iBeacon data from BLE scan records.
 */
object HiBeaconParser {

    /**
     * Data class representing iBeacon-specific data, including UUID, major, and minor values.
     *
     * @property uuid The UUID of the iBeacon.
     * @property major The major identifier of the iBeacon. Defaults to -1 if unavailable.
     * @property minor The minor identifier of the iBeacon. Defaults to -1 if unavailable.
     */
    data class IBeaconData(
        val uuid: UUID? = null,
        val major: Int = -1,
        val minor: Int = -1
    )

    /**
     * Extracts iBeacon-specific data (UUID, Major, and Minor values) from a BLE scan record.
     *
     * This function decodes the BLE advertisement data following the iBeacon protocol format.
     * It extracts the UUID, Major, and Minor values from specific byte ranges in the advertisement payload.
     *
     * ### iBeacon Advertisement Data Structure:
     * ```
     * Byte Index    | Description
     * ---------------|-----------------------
     * 0-8            | Preamble and Flags
     * 9-24           | Beacon UUID (16 bytes)
     * 25-26          | Major (2 bytes)
     * 27-28          | Minor (2 bytes)
     * 29             | Tx Power (1 byte)
     * ```
     *
     * #### Note:
     * - The `scanRecord` must contain at least 30 bytes of data for successful parsing.
     * - If the `scanRecord` is null or lacks sufficient data, the function returns `null`.
     *
     * @param scanRecord The BLE scan record containing the advertisement data.
     * @return An [IBeaconData] object containing the parsed UUID, Major, and Minor values, or `null` if parsing fails.
     */
    fun extractIBeaconData(scanRecord: ScanRecord?): IBeaconData? {
        val bytes = scanRecord?.bytes ?: return null
        // iBeacon data requires at least 30 bytes.
        if (bytes.size < 30) return null

        // Extract UUID (bytes 9 to 24)
        val uuid = try {
            val uuidBytes = bytes.copyOfRange(9, 25)
            UUID(
                uuidBytes.copyOfRange(0, 8).toLong(),
                uuidBytes.copyOfRange(8, 16).toLong()
            )
        } catch (e: Exception) {
            null
        }

        // Extract Major and Minor
        val major = extractMajor(scanRecord)
        val minor = extractMinor(scanRecord)

        return IBeaconData(uuid, major, minor)
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

    /**
     * Converts a 8-byte ByteArray to a Long.
     *
     * @receiver ByteArray The 8-byte array to be converted.
     * @return The Long value represented by the ByteArray.
     */
    private fun ByteArray.toLong(): Long {
        require(this.size == 8) { "ByteArray size must be 8 to convert to Long" }
        return this.foldIndexed(0L) { index, acc, byte ->
            acc or ((byte.toLong() and 0xFF) shl (8 * (7 - index)))
        }
    }
}