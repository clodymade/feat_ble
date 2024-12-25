# --- General ProGuard rules for feat_ble module ---

# Keep all public classes and methods in the feat_ble package
-keep public class com.mwkg.ble.** { *; }

# Preserve annotations
-keepattributes *Annotation*

# Preserve method signatures for reflection
-keepattributes Signature, EnclosingMethod, InnerClasses

# Keep all enums and their values
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Preserve Serializable classes
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    private void readObjectNoData();
}

# --- Rules for java.lang.invoke package and StringConcatFactory ---

# Keep all classes from java.lang.invoke package
-keep class java.lang.invoke.** { *; }

# Keep StringConcatFactory explicitly
-keep class java.lang.invoke.StringConcatFactory { *; }

# --- Specific ProGuard rules for feat_ble classes ---

# Keep HiBleResult and its public members
-keep class com.mwkg.ble.model.HiBleResult { *; }

# Keep HiBleDevice and its public members
-keep class com.mwkg.ble.model.HiBleDevice { *; }

# Keep HiBleParser utility class and its public methods
-keep class com.mwkg.ble.util.HiBleParser { *; }

# Keep HiBeaconParser utility class and its public methods
-keep class com.mwkg.ble.util.HiBeaconParser { *; }

# Keep HiBleScanner class and its public methods
-keep class com.mwkg.ble.util.HiBleScanner { *; }

# --- Additional generic rules for safety ---

# Keep Parcelable implementations
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator CREATOR;
}

# Keep data binding generated classes
-keep class androidx.databinding.** { *; }
-keepclassmembers class androidx.databinding.** { *; }

# Keep Jetpack Compose-related classes
-keep class androidx.compose.** { *; }
-keep class kotlin.Unit { *; }

# Keep coroutines-related classes
-keep class kotlinx.coroutines.** { *; }

# Keep Retrofit models (if applicable)
-keep class retrofit2.** { *; }

# Keep Room Database and DAOs (if applicable)
-keep class androidx.room.** { *; }
-keepclassmembers class androidx.room.** { *; }

# Keep Gson models (if applicable)
-keep class com.google.gson.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}