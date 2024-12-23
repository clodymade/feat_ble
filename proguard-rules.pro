# feat_base 의존성 모델 유지
-keep class com.mwkg.base.model.** { *; }
-keepnames class com.mwkg.base.model.** { *; }

# BLE 관련 모델 유지
-keep class com.mwkg.ble.model.** { *; }
-keepnames class com.mwkg.ble.model.** { *; }

# BLE 관련 유틸리티 유지
-keep class com.mwkg.ble.util.** { *; }

# 기타 난독화 제거 관련 설정
-dontwarn com.mwkg.ble.**
-dontwarn kotlin.**



# Compose 관련 경고 억제
-dontwarn androidx.compose.**
-dontwarn com.mwkg.ble.view.**

# ViewModel 클래스 유지 (사용 중인 경우)
-keep class com.mwkg.ble.viewmodel.** {
    <fields>;
    <methods>;
}

# Google ML Kit 바코드 라이브러리 경고 억제 (필요 시 추가)
#-dontwarn com.google.mlkit.**

# Android Camera 관련 클래스 유지 (CameraX 사용)
#-keep class androidx.camera.** {
#    <fields>;
#    <methods>;
#}

# 디버깅 목적으로 유지하려는 메서드나 클래스 정의
-keep public class com.mwkg.ble.** {
    public *;
}


