/**
 * File: HiBleDeviceListViewModel.kt
 * Description: ViewModel for managing a list of BLE devices. Provides reactive state management
 *              using StateFlow and supports updating device details dynamically.
 *
 * Author: netcanis
 * Created: 2024-11-19
 *
 * License: MIT
 *
 * References:
 * - Jetpack ViewModel: https://developer.android.com/topic/libraries/architecture/viewmodel
 * - Kotlin StateFlow: https://kotlinlang.org/docs/flow.html#stateflow
 */

package com.mwkg.ble.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.mwkg.ble.model.HiBleDevice

/**
 * ViewModel for managing a list of BLE devices.
 * Provides functionality to update the device list reactively.
 */
class HiBleDeviceListViewModel : ViewModel() {

    // Private mutable state for the list of devices
    private val _devices = MutableStateFlow<List<HiBleDevice>>(emptyList())

    // Public immutable state exposed to observers
    val devices: StateFlow<List<HiBleDevice>> get() = _devices

    /**
     * Updates the list of BLE devices.
     *
     * If the device already exists (based on UUID or a combination of device name and RSSI),
     * it updates the existing entry. Otherwise, it adds the new device to the list.
     *
     * @param device The BLE device to update or add.
     */
    fun update(device: HiBleDevice) {
        val currentDevices = _devices.value.toMutableList() // Create a mutable copy of the current device list.

        // Find the index of an existing device with the same UUID or matching device name and RSSI.
        val existingIndex = currentDevices.indexOfFirst {
            (it.uuid == device.uuid && it.uuid.isNotEmpty()) || // Match based on UUID (if not empty).
                    (it.deviceName == device.deviceName && it.rssi == device.rssi) // Match based on device name and RSSI.
        }

        if (existingIndex >= 0) {
            // Update the existing device.
            currentDevices[existingIndex] = device
        } else {
            // Add the new device to the list.
            currentDevices.add(device)
        }

        // Update the state with the new device list as an immutable copy.
        _devices.value = currentDevices.toList()
    }
}
