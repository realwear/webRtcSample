package com.realwear.webrtcexample

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.webkit.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewClientCompat
import com.realwear.webrtcexample.ui.theme.SampleWebAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            SampleWebAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    // NOTE: This is equivalent to
                    // override fun onCreate() {
                    //   val webView = getElementById(r.id.webview)
                    //   ...
                    // }
                    // for xml-based layouts.
                    val context = LocalContext.current

                    var granted by remember { mutableStateOf<Boolean>(false) }

                    val permission = arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    val launcher = rememberLauncherForActivityResult(
                        ActivityResultContracts.RequestMultiplePermissions()
                    ) { permissionsMap ->
                        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
                        if (areGranted) {
                            granted = true
                        } else {
                            granted = false
                        }
                    }
                    SideEffect {
                        granted = checkAndRequestPermissions(context, permission)
                        if (!granted) {
                            launcher.launch(permission)
                        }
                    }

                    if (granted) {
                        AndroidView(
                            factory = {
                                WebView(it).apply {
                                    // Configure
                                    webChromeClient = LocalContentWebViewClient()
                                    webViewClient = WebViewClient()
                                    settings.javaScriptEnabled = true
                                    settings.mediaPlaybackRequiresUserGesture = false
                                    settings.builtInZoomControls = true
                                    settings.domStorageEnabled = true
                                    settings.allowFileAccess = true
                                    settings.allowContentAccess = true
                                    settings.setSupportZoom(false)

                                    // Load your website
                                    // NOTE: this line does not need to be changed! Your compiled website's
                                    // index.html should be in src/main/assets.
//                                    loadUrl("https://appassets.androidplatform.net/assets/index.html")
                                    loadUrl("file:///android_asset/index.html")

                                }
                            }
                        )
                    }
                }
            }
        }
    }

    fun checkAndRequestPermissions(
        context: Context,
        permissions: Array<String>
    ): Boolean {
        if (
            permissions.all {
                ContextCompat.checkSelfPermission(
                    context,
                    it
                ) == PackageManager.PERMISSION_GRANTED
            }
        ) {
            // Use location because permissions are already granted
            return true
        } else {
            return false
        }
    }
}

private class LocalContentWebViewClient() :
    WebChromeClient() {

    override fun onPermissionRequest(request: PermissionRequest?) {
        val requestedResources = request?.resources.orEmpty()
        if(requestedResources.size >0)
            request?.grant(requestedResources)
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SampleWebAppTheme {
        Greeting("Android")
    }
}