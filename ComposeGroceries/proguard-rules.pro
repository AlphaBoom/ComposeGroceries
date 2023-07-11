# proguard-rules.pro
-dontoptimize
-ignorewarnings
-keep class com.google.api.** {
    *;
}
-keep class io.grpc.** {
    *;
}
-keep class com.github.kwhat.jnativehook.** {
    *;
}