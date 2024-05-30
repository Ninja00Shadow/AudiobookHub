package com.example.audiobookhub

import android.os.Bundle
import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.compose.rememberNavController
import com.example.audiobookhub.player.service.AudioService
import com.example.audiobookhub.ui.screens.playerScreen.AudioViewModel
import com.example.audiobookhub.ui.navigation.NavigationGraph
import com.example.audiobookhub.ui.navigation.bottomNavigation.BottomNavigationBar
import com.example.audiobookhub.ui.screens.addBook.AddBookScreenViewModel
import com.example.audiobookhub.ui.screens.bookshelf.BookListViewModel
import com.example.audiobookhub.ui.screens.details.AudiobookDetailsViewModel
import com.example.audiobookhub.ui.theme.AudiobookHubTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val playerViewModel: AudioViewModel by viewModels()
    private val bookListViewModel: BookListViewModel by viewModels()
    private val bookDetailsViewModel: AudiobookDetailsViewModel by viewModels()
    private val addBookViewModel: AddBookScreenViewModel by viewModels()
    private var isServiceRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AudiobookHubTheme {
                val navController = rememberNavController()

                GetPermission()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold (
                        bottomBar = {
                            BottomNavigationBar(
                                navController = navController
                            )
                        }
                    ) {
                        Surface (
                            modifier = Modifier
                                .padding(it)
                        ) {
                            NavigationGraph(
                                navController = navController,
                                playerViewModel = playerViewModel,
                                bookListViewModel = bookListViewModel,
                                bookDetailsViewModel = bookDetailsViewModel,
                                addBookViewModel = addBookViewModel,
                                startService = { startService() }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun startService() {
        if (!isServiceRunning) {
            val intent = Intent(this, AudioService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
            isServiceRunning = true
        }
    }

    private fun stopService() {
        if (isServiceRunning) {
            val intent = Intent(this, AudioService::class.java)
            stopService(intent)
            isServiceRunning = false
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    private fun GetPermission() {
        val lifecycleOwner = LocalLifecycleOwner.current
        val permissionState = rememberPermissionState(
            permission = Manifest.permission.READ_EXTERNAL_STORAGE
        )
        DisposableEffect(key1 = lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    permissionState.launchPermissionRequest()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    }
}