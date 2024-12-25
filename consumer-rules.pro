# Keep all public classes and methods in the feat_ble package
-keep class com.mwkg.ble.** { *; }

# Keep specific classes and their public members (if applicable)
-keep class com.mwkg.ble.model.HiBleResult { *; }
-keep class com.mwkg.ble.model.HiBleDevice { *; }
-keep class com.mwkg.ble.util.HiBleParser { *; }
-keep class com.mwkg.ble.util.HiBeaconParser { *; }
-keep class com.mwkg.ble.util.HiBleScanner { *; }
-keep class com.mwkg.ble.view.HiBleDeviceListActivity { *; }
-keep class com.mwkg.ble.viewmodel.HiBleDeviceListViewModel { *; }

# Keep all annotations in the library
-keepattributes *Annotation*

# Keep the method parameters and signatures
-keepattributes Signature, MethodParameters

# Preserve Serializable classes
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    private void readObjectNoData();
}