#include <jni.h>
#include <sys/stat.h>
#include <android/log.h>
#include <linux_syscall_support.h>
#include <ctime>
#include <cstdio>
#include <algorithm>

#include "xposed-detector.h"

bool syscall_failed;

enum Result {
    NOT_FOUND, PERMISSION_DENIED, FOUND
}result;

static void signal_handler(int sig) {
    syscall_failed = true;
    __android_log_print(ANDROID_LOG_INFO, "[Detector]", "Syscall was denied");
}

static void update(Result out) {
    result = std::max(result, out);
}

static void syscall_detect(int call) {
    update(syscall_failed ? PERMISSION_DENIED : (call == 0 ? FOUND : NOT_FOUND));
    syscall_failed = false;
}

extern "C" JNIEXPORT jobject JNICALL
Java_com_tsng_applistdetector_detections_FileDetections_detect(JNIEnv *env, jobject, jstring path, jboolean use_syscall) {
    syscall_failed = false;
    result = NOT_FOUND;
    const char *cpath = env->GetStringUTFChars(path, nullptr);

    if (use_syscall) {
        signal(SIGSYS, signal_handler);
        struct kernel_stat buf{};
        syscall_detect(sys_stat(cpath, &buf));
        syscall_detect(sys_fstat(sys_open(cpath, O_PATH, 0), &buf));
    } else {
        struct stat buf{};
        update(access(cpath, F_OK) == 0 ? FOUND : NOT_FOUND);
        update(stat(cpath, &buf) == 0 ? FOUND : NOT_FOUND);
        update(fstat(open(cpath, O_PATH), &buf) == 0 ? FOUND : NOT_FOUND);
    }

    env->ReleaseStringUTFChars(path, cpath);
    jclass enum_class = env->FindClass("com/tsng/applistdetector/detections/IDetector$Results");
    jfieldID id;
    switch (result) {
        case NOT_FOUND:
            id = env->GetStaticFieldID(enum_class, "NOT_FOUND", "Lcom/tsng/applistdetector/detections/IDetector$Results;");
            break;
        case PERMISSION_DENIED:
            id = env->GetStaticFieldID(enum_class, "PERMISSION_DENIED", "Lcom/tsng/applistdetector/detections/IDetector$Results;");
            break;
        case FOUND:
            id = env->GetStaticFieldID(enum_class, "FOUND", "Lcom/tsng/applistdetector/detections/IDetector$Results;");
            break;
    }

    return env->GetStaticObjectField(enum_class, id);
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_tsng_applistdetector_detections_AbnormalEnvironment_xposedDetector(JNIEnv *env, jobject) {
    return get_xposed_status(env, android_get_device_api_level()) != NO_XPOSED;
}