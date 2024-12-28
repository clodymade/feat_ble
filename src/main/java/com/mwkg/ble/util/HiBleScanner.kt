/**
 * File: HiBleScanner.kt
 * Description: Manages Bluetooth Low Energy (BLE) scanning operations, including starting, stopping,
 *              and handling scan results. Supports runtime permission handling for Android 12 and above.
 *
 * Author: netcanis
 * Created: 2024-11-19
 *
 * License: MIT
 *
 * References:
 * - Android BLE Scanning: https://developer.android.com/guide/topics/connectivity/bluetooth/ble-overview
 * - Android Runtime Permissions: https://developer.android.com/training/permissions/requesting
 */

package com.mwkg.ble.util

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.util.Log
import com.mwkg.ble.model.HiBleResult
import com.mwkg.ble.util.HiBleToolkit.hasPermissions

/**
 * A utility object for managing Bluetooth Low Energy (BLE) scanning operations.
 */
@SuppressLint("StaticFieldLeak") // Suppresses warnings about potential memory leaks.
object HiBleScanner {
    private var callback: ((HiBleResult) -> Unit)? = null
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothLeScanner: BluetoothLeScanner? = null
    private var isScanning = false
    private var scanCallback: ScanCallback? = null
    private var activity: Activity? = null

    init {
        Log.d("ModularX::HiBleScanner", "BLE Scanner initialized")
    }

    /**
     * Initializes the BLE scanner by obtaining the [BluetoothAdapter] and [BluetoothLeScanner].
     */
    private fun initialize() {
        val bluetoothManager = activity?.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
        bluetoothAdapter = bluetoothManager?.adapter
        bluetoothLeScanner = bluetoothAdapter?.bluetoothLeScanner
        Log.d("ModularX::HiBleScanner", "BLE Scanner initialized successfully")
    }

    /**
     * Starts BLE scanning with the required permissions.
     *
     * @param activity The current [Activity], used for requesting permissions and accessing system services.
     * @param callback A lambda function to handle scan results, provided as [HiResult].
     */
    @SuppressLint("MissingPermission")
    fun start(
        activity: Activity,
        callback: (HiBleResult) -> Unit
    ) {
        this.activity = activity
        this.callback = callback

        initialize()

        // Check and request necessary permissions.
        val permissions = HiBlePermission.getMergedPermissions(HiBlePermissionType.BLE, HiBlePermissionType.BEACON)
        if (!activity.hasPermissions(permissions)) {
            activity.requestPermissions(permissions, HiBlePermissionReqCodes.BLE_BEACON)
            return
        }

        // Prevent multiple scan operations.
        if (isScanning) {
            Log.d("ModularX::HiBleScanner", "Scanning is already in progress.")
            return
        }

        isScanning = true
        scanCallback = createScanCallback()

        bluetoothLeScanner?.let {
            val scanSettings = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build()
            it.startScan(null, scanSettings, scanCallback)
            Log.d("ModularX::HiBleScanner", "BLE scan started")
        } ?: run {
            Log.e("ModularX::HiBleScanner", "BluetoothLeScanner is null.")
            isScanning = false
        }
    }

    /**
     * Stops BLE scanning if currently active.
     */
    @SuppressLint("MissingPermission")
    fun stop() {
        if (isScanning && scanCallback != null) {
            bluetoothLeScanner?.stopScan(scanCallback)
            Log.d("ModularX::HiBleScanner", "BLE scan stopped")
            isScanning = false
            scanCallback = null
            callback = null
        }
    }

    /**
     * Checks whether the required permissions for BLE scanning are granted.
     *
     * @return `true` if all required permissions are granted, `false` otherwise.
     */
    fun hasRequiredPermissions(): Boolean {
        return activity?.let {
            val permissions = HiBlePermission.getMergedPermissions(HiBlePermissionType.BLE, HiBlePermissionType.BEACON)
            it.hasPermissions(permissions)
        } ?: false
    }

    /**
     * Creates a [ScanCallback] to handle BLE scan results and errors.
     *
     * @return The created [ScanCallback] instance.
     */
    private fun createScanCallback(): ScanCallback {
        return object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult?) {
                result?.let {
                    handleScanResult(it)
                }
            }

            override fun onScanFailed(errorCode: Int) {
                Log.e("ModularX::HiBleScanner", "BLE scan failed: $errorCode")
                callback?.let {
                    it(
                        HiBleResult(
                            scanRecord = null,
                            advertisementData = mapOf(),
                            rssi = 0,
                            error = "BLE scan failed: $errorCode"
                        )
                    )
                }
                stop()
            }
        }
    }

    /**
     * Processes a single scan result and triggers the callback with parsed data.
     *
     * @param result The [ScanResult] received from BLE scanning.
     */
    private fun handleScanResult(result: ScanResult) {
        val scanRecord = result.scanRecord
        val rssi = result.rssi
        val advertisementData = HiBleParser.getAdvertisementData(scanRecord)
        val ibeaconData = HiBeaconParser.extractIBeaconData(scanRecord)

        // Extract Major and Minor
        val major = ibeaconData?.major ?: -1
        val minor = ibeaconData?.minor ?: -1

        // Extract beacon UUID
        val beaconUUID = ibeaconData?.uuid?.toString() ?: ""


        callback?.let {
            it(
                HiBleResult(
                    scanRecord = scanRecord,
                    rssi = rssi,
                    advertisementData = advertisementData,
                    major = major,
                    minor = minor,
                    beaconUUID = beaconUUID,
                    error = ""
                )
            )
        }
    }
}