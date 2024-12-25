# **feat_ble**

A **feature module** for Bluetooth Low Energy (BLE) scanning and iBeacon parsing on Android.

---

## **Overview**

`feat_ble` is an Android module designed to:
- Scan and manage Bluetooth Low Energy (BLE) devices.
- Extract iBeacon details like **Major**, **Minor**, and **UUID**.
- Display BLE devices in a modern UI using **Jetpack Compose**.

This module is compatible with **Android 11 (API 30)** and above, with support for **Android Jetpack libraries** and **Kotlin Coroutines**.

---

## **Features**

- ✅ **BLE Scanning**: Detect BLE devices and retrieve information such as device name, RSSI, and advertisement data.
- ✅ **iBeacon Parsing**: Extract iBeacon-specific data, including Major, Minor, and UUID values.
- ✅ **Reactive UI**: Display scanned devices in a modern, responsive UI built with Jetpack Compose.
- ✅ **Modular Architecture**: Lightweight and easy to integrate into existing Android projects.
- ✅ **Runtime Permissionse**: Automatically manage required Bluetooth and location permissions for Android 12 (API 31) and above.

---

## **Requirements**

| Requirement        | Minimum Version         |
|--------------------|-------------------------|
| **Android OS**     | 11 (API 30)             |
| **Kotlin**         | 1.9.22                  |
| **Android Studio** | Giraffe (2022.3.1)      |
| **Gradle**         | 8.0                     |

---

## **Setup**

### **1. Add feat_ble to Your Project**

Include `feat_ble` as a module in your project. Add the following to your `settings.gradle` file:

```gradle
include ':feat_ble'
```

Then, add it as a dependency in your app module’s build.gradle file:
```gradle
implementation project(":feat_ble")
```

### **2. Permissions**

Add the required permissions to your AndroidManifest.xml:

```xml
<!-- Bluetooth Permissions -->
<uses-permission android:name="android.permission.BLUETOOTH_SCAN" android:required="false" />
<uses-permission android:name="android.permission.BLUETOOTH_CONNECT" android:required="false" />
<uses-permission android:name="android.permission.BLUETOOTH_ADVERTISE" />

<!-- Location Permissions -->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

Runtime Permissions:

Starting with Android 12 (API 31), runtime permissions are required for Bluetooth and location access. Make sure to request them at runtime:

```kotlin
val permissions = arrayOf(
    android.Manifest.permission.BLUETOOTH_SCAN,
    android.Manifest.permission.BLUETOOTH_CONNECT,
    android.Manifest.permission.ACCESS_FINE_LOCATION
)

ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE)
```

---

## **Usage**

### **1. Start BLE Scanning**

To start scanning for BLE devices:

import com.mwkg.ble.util.HiBleScanner

```kotlin
HiBleScanner.start(this) { result ->
    when (result) {
        is HiResult.HiBleResult -> {
            println("Device Found:")
            println("Name: ${result.scanRecord?.deviceName ?: "Unknown"}")
            println("RSSI: ${result.rssi}")
            println("UUID: ${result.scanRecord?.serviceUuids?.firstOrNull()?.toString() ?: ""}")
            println("Major: ${result.major}, Minor: ${result.minor}")
            println("Beacon UUID: ${result.beaconUUID}")")
        }
        else -> {
            println("Error: ${result.error}")
        }
    }
}
```

### **2. Stop BLE Scanning**

Stop scanning when it’s no longer needed:

```kotlin
HiBleScanner.stop()
```

### **3. iBeacon Parsing**

Parse iBeacon-specific data during a BLE scan:

```kotlin
HiBeaconParser.extractIBeaconData(scanRecord)?.let { iBeaconData ->
    println("Beacon Major: ${iBeaconData.major}")
    println("Beacon Minor: ${iBeaconData.minor}")
    println("Beacon UUID: ${iBeaconData.uuid}")
}
```

---

## **HiBleResult**

The BLE scan results are encapsulated in the HiBleResult class. Key properties include:

| Property          | Type             | Description                         |
|-------------------|------------------|-------------------------------------|
| rssi              | Int              | Signal strength (RSSI).             |
| scanRecord        | ScanRecord?      | Raw scan record data.               |
| advertisementData | Map<String, Any> | Advertisement data from the device. |
| major             | Int              | iBeacon's major value.              |
| minor             | Int              | iBeacon's minor value.              |
| beaconUUID        | String?          | iBeacon’s UUID, if available.       |
| error             | String           | Error message, if any.              |

---

## **Example UI**

To display BLE devices in a Jetpack Compose UI:

```kotlin
@Composable
fun HiBleDeviceListScreen(
    devices: StateFlow<List<HiBleDevice>>,
    onBackPressed: () -> Unit
) {
    val deviceList by devices.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BLE Device List") },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Default.Close, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(deviceList) { device ->
                HiBleDeviceItem(device)
            }
        }
    }
}
```

---

## **License**

feat_ble is available under the MIT License. See the LICENSE file for details.

---

## **Contributing**

Contributions are welcome! To contribute:

1. Fork this repository.
2. Create a feature branch:
```
git checkout -b feature/your-feature
```
3. Commit your changes:
```
git commit -m "Add feature: description"
```
4. Push to the branch:
```
git push origin feature/your-feature
```
5. Submit a Pull Request.

---

## **Author**

### **netcanis**
iOS GitHub: https://github.com/netcanis
Android GitHub: https://github.com/clodymade

---

