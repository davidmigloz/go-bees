###############################################################################
#                     Launch Android emulator in Travis                       #
###############################################################################
# In travis.yml must be:                                                      #
# language: android                                                           #
# jdk: oraclejdk8                                                             #
# env:                                                                        #
#   global:                                                                   #
#     - ANDROID_TARGET=android-22                                             #
#     - ANDROID_ABI=armeabi-v7a                                               #
# android:                                                                    #
#   components:                                                               #
#     - ${ANDROID_TARGET}                                                     #
#     - sys-img-${ANDROID_ABI}-${ANDROID_TARGET}                              #
###############################################################################

chmod +x gradlew
./gradlew build jacocoTestReport assembleAndroidTest
echo no | android create avd --force -n test -t $ANDROID_TARGET --abi $ANDROID_ABI
emulator -avd test -no-audio -no-window &
android-wait-for-emulator
adb shell setprop dalvik.vm.dexopt-flags v=n,o=v
./gradlew connectedCheck