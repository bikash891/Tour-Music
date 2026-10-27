# RideBeats Android starter project

This is a source project, not a prebuilt APK. It provides a Compose interface, local tour-code demo, and local authorized HTTPS audio playback.

## Build an APK
1. Install Android Studio (current stable) and Android SDK Platform 35.
2. Open this folder in Android Studio.
3. Let Gradle sync and download dependencies.
4. Select Build > Build Bundle(s) / APK(s) > Build APK(s).
5. The debug APK is normally at `app/build/outputs/apk/debug/app-debug.apk`.

## Important limitations
- Tour codes are generated locally and are not shared between devices.
- Audio plays on the current device only; there is no Firebase room synchronization yet.
- Only use audio you own or are licensed to stream.
- This starter project has not been compiled or device-tested in this environment.

## Next production steps
Configure Firebase Authentication and Realtime Database, implement membership/security rules and shared playback state, then add a MediaSessionService for background playback.

## Build in the cloud (no Android Studio on your computer)
This project includes `.github/workflows/build-apk.yml` for GitHub Actions.
1. Sign in or create an account at https://github.com.
2. Create a new repository and upload the extracted project files (including the hidden `.github` folder). If uploading via the website, ensure `.github/workflows/build-apk.yml` is included.
3. Open the repository's **Actions** tab, choose **Build RideBeats APK**, and click **Run workflow**.
4. When the run succeeds, open the completed run and download the `RideBeats-debug-apk` artifact. Unzip it to get `app-debug.apk`.
5. Transfer the APK to your Android phone and install it. Android may ask you to allow installation from that source.

The cloud build workflow compiles the current starter prototype. It does not add real-time multi-phone synchronization; that still needs backend implementation and Firebase configuration.
