package com.readyChatAI.readyChatMessages

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.media.audiofx.BassBoost
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale
import com.readyChatAI.readyChatMessages.navigation.NavigationGraph
import com.readyChatAI.readyChatMessages.navigation.Screen
import com.readyChatAI.readyChatMessages.presentation.components.menu.BottomMenu
import com.readyChatAI.readyChatMessages.presentation.screens.settings.SettingsViewModel
import com.readyChatAI.readyChatMessages.ui.theme.ReadyChatMessagesTheme
import com.readyChatAI.readyChatMessages.util.Util
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        settingsViewModel.initializePreferences()

        setContent {
            val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()
            val selectedLanguage by settingsViewModel.selectedLanguage.collectAsState()
            val requiresActivityRecreate by settingsViewModel.requiresActivityRecreate.collectAsState()

            LaunchedEffect(selectedLanguage) {
                Util.setLanguage(this@MainActivity, selectedLanguage)
            }

            LaunchedEffect(requiresActivityRecreate) {
                if (requiresActivityRecreate) {
                    recreate()
                    settingsViewModel.onRecreateDone()
                }
            }

            ReadyChatMessagesTheme(darkTheme = isDarkTheme) {
                Content(settingsViewModel)
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Content(settingsViewModel: SettingsViewModel) {
    val navHostController = rememberNavController()

    val context = LocalContext.current

    val currentBackStackEntry: NavBackStackEntry? by navHostController.currentBackStackEntryAsState()
    val currentScreen = currentBackStackEntry?.destination?.route?.split(".")?.last()

    val smsPermissions = listOf(
        Manifest.permission.SEND_SMS,
        Manifest.permission.READ_SMS,
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.READ_CONTACTS
    )
    val permissionState = rememberMultiplePermissionsState(permissions = smsPermissions)
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (!permissionState.allPermissionsGranted) {
                    permissionState.launchMultiplePermissionRequest()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val hasPermanentDenial = permissionState.permissions.any { perm ->
        !perm.status.isGranted && !perm.status.shouldShowRationale
    }

    if (permissionState.allPermissionsGranted) {
        Scaffold(
            bottomBar = {
                if (
                    currentScreen == Screen.ChatsScreen.name ||
                    currentScreen == Screen.CategoriesScreen.name ||
                    currentScreen == Screen.SettingsScreen.name
                ) {
                    BottomMenu(
                        navHostController = navHostController,
                        currentScreen = currentScreen
                    )
                }
            }
        ) { innerPadding ->
            NavigationGraph(
                navHostController,
                modifier = Modifier.padding(innerPadding),
                settingsViewModel = settingsViewModel
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = context.getString(R.string.sms_mandatory_permission_message),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (!hasPermanentDenial) {
                Button(onClick = { permissionState.launchMultiplePermissionRequest() }) {
                    Text(context.getString(R.string.accept))
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ReadyChatMessagesTheme {
        Greeting("Android")
    }
}