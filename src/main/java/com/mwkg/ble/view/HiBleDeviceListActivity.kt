/**
 * File: HiBleDeviceListActivity.kt
 * Description: Activity for managing and displaying a list of BLE devices using a Jetpack Compose UI.
 *              Integrates BLE scanning via HiBleScanner and state management via ViewModel.
 *
 * Author: netcanis
 * Created: 2024-11-19
 *
 * License: MIT
 *
 * References:
 * - Jetpack Compose Activity Integration: https://developer.android.com/jetpack/compose/navigation
 * - Android ViewModel: https://developer.android.com/topic/libraries/architecture/viewmodel
 */

package com.mwkg.ble.view

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.mwkg.ble.model.HiBleDevice
import com.mwkg.ble.util.HiBlePermission
import com.mwkg.ble.util.HiBlePermissionType
import com.mwkg.ble.util.HiBlePermissionType.BEACON
import com.mwkg.ble.util.HiBlePermissionType.BLE
import com.mwkg.ble.util.HiBleScanner
import com.mwkg.ble.util.HiBleToolkit.hasPermissions
import com.mwkg.ble.viewmodel.HiBleDeviceListViewModel

/**
 * Activity to display a list of BLE devices and handle BLE scanning and data parsing.
 */
class HiBleDeviceListActivity : ComponentActivity() {
    // ViewModel to manage the BLE device list state
    private val viewModel: HiBleDeviceListViewModel by viewModels()

    /**
     * Called when the activity is starting. Sets up the content and initializes BLE scanning.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Sets the Composable content for the activity
        setContent {
            HiBleDeviceListScreen(
                devices = viewModel.devices, // Pass the list of devices from ViewModel
                onBackPressed = { finish() } // Define the action for the back button
            )
        }

        startBleScanner()
    }

    private fun startBleScanner() {
        // Start BLE scanning
        HiBleScanner.start(this) {
            // Create a HiBleDevice object from the scan result
            val device = HiBleDevice(
                deviceName = it.scanRecord?.deviceName ?: "Unknown",
                rssi = it.rssi,
                uuid = it.scanRecord?.serviceUuids?.firstOrNull()?.toString() ?: "",
                advertisementData = it.advertisementData,
                major = it.major,
                minor = it.minor,
                beaconUUID = it.beaconUUID ?: ""
            )
            // Update the device list in the ViewModel
            viewModel.update(device)

            // Stop the scanner after successful parsing
            //HiBleScanner.stop()
        }
    }

    /**
     * Called when the activity is being destroyed. Stops BLE scanning to release resources.
     */
    override fun onDestroy() {
        super.onDestroy()
        HiBleScanner.stop()
    }
}