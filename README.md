# LocationApp

This project is a simple Android application built using **Jetpack Compose** that demonstrates how to request and manage location permissions. The app displays a button to request location access and provides feedback to the user based on their interaction with the permission prompts.

---

## Features

1. **Request Location Permissions**:
   - Handles both `ACCESS_FINE_LOCATION` and `ACCESS_COARSE_LOCATION`.
   - Displays rationale when permissions are denied.

2. **Compose UI**:
   - Uses Jetpack Compose for declarative UI design.

3. **Toast Feedback**:
   - Shows messages based on user interaction with permission dialogs.

---

## Prerequisites

- Android Studio (latest version).
- Minimum SDK: 21
- Target SDK: 31
- Kotlin 1.8 or later

---

## Code Overview

### MainActivity
```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LocationAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }
}
````
### Permissions Request Logic
````kotlin
val requestPermissionLauncher = rememberLauncherForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()
) { permissions ->
    if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
        permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
        Toast.makeText(context, "Permissions granted!", Toast.LENGTH_SHORT).show()
    } else {
        val rationaleNeeded = ActivityCompat.shouldShowRequestPermissionRationale(
            context as MainActivity, Manifest.permission.ACCESS_FINE_LOCATION
        ) || ActivityCompat.shouldShowRequestPermissionRationale(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (rationaleNeeded) {
            Toast.makeText(context, "Please allow location access.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permission denied.", Toast.LENGTH_SHORT).show()
        }
    }
}
````
### Manifest Configuration
```XML
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
````

### How It Works

1. **UI Flow**:
Displays a "Get Location" button.
When clicked, it checks if location permissions are already granted.

2. **Permissions Request**:
If permissions are not granted, requests ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION using ActivityResultContracts.RequestMultiplePermissions.

3. **Feedback**:
If permissions are denied, checks whether to show a rationale using shouldShowRequestPermissionRationale.
Displays appropriate messages using Toast.

4. **Permissions Check Utility**:
Verifies if the app has necessary permissions:

```Kotlin
fun hasLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
    ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}
````
### Installation
1. Clone Repository
````
git clone https://github.com/yourusername/location-app.git
````
2. Open the project in Android studio
3. sync gradlle files and Build the project
4. run the application on an emularor or physical device.
 ## Future Improvements
Implement functionality to fetch and display actual location data.
Provide better guidance for users who deny permissions.
Enhance UI with detailed status updates.
