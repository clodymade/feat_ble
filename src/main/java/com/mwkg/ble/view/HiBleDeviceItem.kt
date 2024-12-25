/**
 * File: HiBleDeviceItem.kt
 * Description: Composable function for rendering a card-based UI element displaying the details of
 *              a BLE device, including name, RSSI, major, minor, and advertisement data.
 *
 * Author: netcanis
 * Created: 2024-11-19
 *
 * License: MIT
 *
 * References:
 * - Jetpack Compose Documentation: https://developer.android.com/jetpack/compose
 */

package com.mwkg.ble.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mwkg.ble.model.HiBleDevice
import com.mwkg.util.hiToPrettyJsonString

/**
 * Composable function to display information about a BLE device.
 *
 * @param device The HiBleDevice object containing the BLE device's details.
 */
@Composable
fun HiBleDeviceItem(device: HiBleDevice) {
    // A Card to encapsulate BLE device details with a shadow effect.
    Card(
        modifier = Modifier
            .fillMaxWidth() // The card takes the full width of the parent.
            .padding(horizontal = 16.dp, vertical = 8.dp), // Adds padding around the card.
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Sets a slight elevation for visual depth.
    ) {
        // Arranges the BLE device details vertically inside the card.
        Column(modifier = Modifier.padding(16.dp)) {
            // Displays the device name.
            HiBleDeviceDetailText(label = "Device Name", value = device.deviceName)
            // Displays the RSSI (signal strength) value.
            HiBleDeviceDetailText(label = "RSSI", value = device.rssi.toString())
            // Displays the major value of the BLE device.
            HiBleDeviceDetailText(label = "Major", value = device.major.toString())
            // Displays the minor value of the BLE device.
            HiBleDeviceDetailText(label = "Minor", value = device.minor.toString())
            // Displays the advertisement data as a JSON string.
            HiBleDeviceDetailText(
                label = "Advertisement Data",
                value = device.advertisementData.hiToPrettyJsonString()
            )
            // Displays the beaconUUID.
            HiBleDeviceDetailText(
                label = "Beacon UUID",
                value = device.beaconUUID.toString()
            )
        }
    }
}

/**
 * A composable function to display a label and value pair as text.
 *
 * @param label The descriptive label for the value.
 * @param value The value to display next to the label.
 */
@Composable
fun HiBleDeviceDetailText(label: String, value: String) {
    // Displays the label and value in a single line of text.
    Text(
        text = "$label: $value",
        style = MaterialTheme.typography.bodyMedium // Applies the medium body text style from the theme.
    )
}