package com.readyChatAI.readyChatMessages

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.readyChatAI.readyChatMessages.navigation.NavigationGraph
import com.readyChatAI.readyChatMessages.navigation.Screen
import com.readyChatAI.readyChatMessages.presentation.components.menu.BottomMenu
import com.readyChatAI.readyChatMessages.ui.theme.ReadyChatMessagesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReadyChatMessagesTheme {
                Content()
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Content() {
    val navHostController = rememberNavController()

    val context = LocalContext.current

    val currentBackStackEntry: NavBackStackEntry? by navHostController.currentBackStackEntryAsState()
    val currentScreen = currentBackStackEntry?.destination?.route?.split(".")?.last()

    val smsPermissions = listOf(
        Manifest.permission.SEND_SMS,
        Manifest.permission.READ_SMS,
        Manifest.permission.RECEIVE_SMS
    )

    val permissionState = rememberMultiplePermissionsState(
        permissions = smsPermissions
    )

    LaunchedEffect(Unit) {
        if (!permissionState.allPermissionsGranted) {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    if (permissionState.allPermissionsGranted) {
        Scaffold(
            bottomBar = {
                if (
                    currentScreen == Screen.MessagesScreen.name ||
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
            NavigationGraph(navHostController, modifier = Modifier.padding(innerPadding))
        }
    } else {
        Text(context.getString(R.string.sms_mandatory_permission_message))
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