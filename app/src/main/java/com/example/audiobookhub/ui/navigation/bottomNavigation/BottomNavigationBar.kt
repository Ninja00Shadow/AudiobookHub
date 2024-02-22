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
import androidx.navigation.NavHostController

@Composable
fun BottomNavigationBar(
    navController: NavHostController,

    ) {
    var selectedItem by remember { mutableIntStateOf(1) }

    NavigationBar {
        BottomNavigationItem.bottomNavigationItems().forEachIndexed { index, item ->

            NavigationBarItem(
                selected = index == selectedItem,
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
                          selectedItem = index
                    navController.navigate(item.route)
                          },

                )
        }
    }
}