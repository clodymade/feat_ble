/**
 * File: HiBleDeviceListScreen.kt
 * Description: Composable function for displaying a scrollable list of BLE devices in a
 *              Jetpack Compose UI, with support for a top app bar and back navigation.
 *
 * Author: netcanis
 * Created: 2024-11-19
 *
 * License: MIT
 *
 * References:
 * - Jetpack Compose Scaffold: https://developer.android.com/reference/kotlin/androidx/compose/material3/Scaffold
 */

package com.mwkg.ble.view

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mwkg.ble.model.HiBleDevice
import kotlinx.coroutines.flow.StateFlow

/**
 * Composable function to display a list of BLE devices using Jetpack Compose.
 *
 * @param devices A [StateFlow] emitting the list of BLE devices to display.
 * @param onBackPressed A lambda function invoked when the back button is pressed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HiBleDeviceListScreen(
    devices: StateFlow<List<HiBleDevice>>,
    onBackPressed: () -> Unit
) {
    // Observing the StateFlow of device list as Compose state.
    val deviceList by devices.collectAsState()

    Scaffold(
        // Defines the top app bar with a title and a back button.
        topBar = {
            TopAppBar(
                title = { Text("BLE Device List") }, // Title of the app bar.
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Default.Close, contentDescription = "Back") // Back button icon.
                    }
                }
            )
        }
    ) { paddingValues ->
        // Displays a scrollable list of BLE devices.
        LazyColumn(
            modifier = Modifier.padding(paddingValues), // Applies the padding values from Scaffold.
            contentPadding = PaddingValues(16.dp) // Adds padding around the list content.
        ) {
            // Iterates over the list of devices and renders each one using HiBleDeviceItem.
            items(deviceList) { device ->
                HiBleDeviceItem(device)
            }
        }
    }
}
