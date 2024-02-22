package com.example.audiobookhub.ui.navigation.bottomNavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.audiobookhub.ui.navigation.Routes

data class BottomNavigationItem(
    val label: String,
    val icon: ImageVector,
    val route: String
) {
    companion object {
        fun bottomNavigationItems(): List<BottomNavigationItem> {
            return listOf(
                BottomNavigationItem("Settings", Icons.Default.Settings, Routes.SETTINGS_SCREEN),
                BottomNavigationItem("Now", Icons.Default.Headphones, Routes.AUDIO_PLAYER_SCREEN),
                BottomNavigationItem("Shelf", Icons.Default.LibraryBooks, Routes.BOOK_LIST_SCREEN),
            )
        }
    }
}