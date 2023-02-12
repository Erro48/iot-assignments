# Room Mobile App

## Installation
### Android device configuration
Connect the android device to the USB of the pc.
Select "No data transfer" from the "Use USB for" menu.
In `Settings > Additional settings > Developer options` enable `USB debugging` and `Install via USB`.

### SDK configuration
Install the SDK.
Set the environment variable `ANDROID_SDK_ROOT=<sdk_path>`.

### Build and run
Inside `RoomMobileApp` folder build the project with gradle command `./gradlew build`.
In the "Run and Debug" section of vscode click the green arrow to run the project.