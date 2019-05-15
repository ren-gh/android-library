#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_r_library_demo_jni_JNIUtils_getUid(JNIEnv *env, jclass type, jobject context) {
//    jclass contextClass = (jclass) env->NewGlobalRef(env->FindClass("android/content/Context"));
//    jmethodID getApplicationInfoId = env->GetMethodID(contextClass, "getApplicationInfo",
//                                                      "()Landroid/content/pm/ApplicationInfo");
//    jobject applicationInfo = (jobject) env->CallObjectMethod(context, getApplicationInfoId);
//
//    jclass applicationInfoClass = (jclass) env->NewGlobalRef(
//            env->FindClass("android/content/pm/ApplicationInfo"));
//    jfieldID uidId = (jfieldID) env->GetFieldID(applicationInfoClass, "uid", "Ljava/lang/String");
//    jstring uid = (jstring) env->GetObjectField(applicationInfo, uidId);

//    return uid;
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_r_library_demo_jni_JNIUtils_stringFromJNI(JNIEnv *env, jclass type) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}