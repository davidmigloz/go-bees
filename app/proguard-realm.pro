#----------------------------------------------------------------------------------------------
# Realm | https://stackoverflow.com/questions/40211916/proguard-with-parceler-and-realm
#----------------------------------------------------------------------------------------------

-keepnames public class * extends io.realm.RealmObject
-keep @io.realm.annotations.RealmModule class *
-keep class io.realm.** { *; }
-dontwarn javax.**
-dontwarn io.realm.**
