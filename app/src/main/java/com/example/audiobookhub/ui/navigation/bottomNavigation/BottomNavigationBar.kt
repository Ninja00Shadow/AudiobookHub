package com.example.audiobookhub.ui.navigation.bottomNavigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.*


@Composable
fun BottomNavigationBar(
    navController: NavHostController,
) {
    NavigationBar {
        val currentRoute = currentRoute(navController)
        BottomNavigationItem.bottomNavigationItems().forEach { item ->

            NavigationBarItem(
                selected = currentRoute == item.route,
                label = {
                    Text(item.label)
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route)
                    }
                },

                )
        }
    }
}

@Composable
private fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}