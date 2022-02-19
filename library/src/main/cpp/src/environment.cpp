#include <climits>
#include <cstdio>
#include <jni.h>
#include <string_view>
#include <xposed-detector.h>

static int xposed_status = NO_XPOSED;

extern "C"
JNIEXPORT jboolean JNICALL
Java_icu_nullptr_applistdetector_AbnormalEnvironment_detectXposed(JNIEnv* env, jobject thiz) {
    int res = get_xposed_status(env, android_get_device_api_level());
    if (res > xposed_status) xposed_status = res;
    return xposed_status != NO_XPOSED;
}


jint JNI_OnLoad(JavaVM* jvm, void*) {
    JNIEnv* env;
    if (jvm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }
    xposed_status = get_xposed_status(env, android_get_device_api_level());
    return JNI_VERSION_1_6;
}
