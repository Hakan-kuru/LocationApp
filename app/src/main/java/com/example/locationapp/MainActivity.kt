package com.example.locationapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import android.Manifest
import androidx.lifecycle.viewmodel.compose.viewModel
import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.example.locationapp.ui.theme.LocationAppTheme
import com.example.locationapp.ui.theme.LocationViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: LocationViewModel = viewModel()
            LocationAppTheme {
               Surface(
                   modifier = Modifier.fillMaxSize(),
                   color = androidx.compose.ui.graphics.Color.White
               ) {
                   MyApp(viewModel)
               }
            }
        }
    }
}
@Composable
fun MyApp(viewModel: LocationViewModel = viewModel()){
    val context= LocalContext.current
    val locationsUtils= LocationsUtils(context)
    LocationDisplay(locationsUtils, context, viewModel)
}

@Composable
fun LocationDisplay(
    locationsUtils: LocationsUtils,
    context: Context,
    viewModel: LocationViewModel
){
    val location = viewModel.location.value

    val requestPermissionLauncher =rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {permissions ->
            if(permissions[Manifest.permission.ACCESS_FINE_LOCATION]== true &&
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION]== true
                ){
            // I have access to location
                locationsUtils.requestLocationUpdates(viewModel = viewModel)
            }else{
            // or not

                // kullanıcıdan bu veriyi neden istediğimizi söylemek
                // reddederse true değer döndürür
                val rationalRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                    //sadece MainActivity'de çalışıyor
                    context as MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    context ,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                if(rationalRequired){
                    //ufak bir alt metin
                    Toast.makeText(context,
                        //ne yazacak
                        "devam etmek için konum bilgilerinize izin vermeniz gerekiyor",
                        //ne uzublukta yazacak
                        Toast.LENGTH_SHORT
                    ).show()
                }else{
                    Toast.makeText(context,
                        //ne yazacak
                        "Elon Musk!! Bunu da dinle Evini Soyduğum",
                        //ne uzublukta yazacak
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    )
    Column(
        modifier =Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        if(location != null){
            Text(text = "Latitude: ${location.latitude}")
            Text(text = "Longitude: ${location.longitude}")
        }else{
            Text(text = "Location not available")
        }
        Text(text = "Location App")

        Button(onClick = {
                if (locationsUtils.hasLocationPermission(context)) {
                //
                    locationsUtils.requestLocationUpdates(viewModel)
                } else {
                    requestPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            }

        ) {
            Text(text = "Get Location")
        }
    }
}

