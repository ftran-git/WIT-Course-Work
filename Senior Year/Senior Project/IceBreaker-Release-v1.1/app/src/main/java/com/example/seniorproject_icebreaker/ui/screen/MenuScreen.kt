package com.example.seniorproject_icebreaker.ui.screen

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.seniorproject_icebreaker.R
import com.example.seniorproject_icebreaker.ui.theme.AppPadding
import com.example.seniorproject_icebreaker.ui.theme.IceBreakerTheme
import com.example.seniorproject_icebreaker.ui.theme.Typography
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MenuScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            MenuScreenBottomNavBar(navController)
        }
    ) {
        // Surface to contain elements
        Surface {
            // Get the system's dark mode state
            val isDarkTheme = isSystemInDarkTheme()

            // Box to contain all elements
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(AppPadding.large)
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(AppPadding.large),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = if (isDarkTheme) R.drawable.app_banner_polar_blue_dark else R.drawable.app_banner_polar_blue_light),
                        contentDescription = "App Logo",
                        modifier = Modifier.size(150.dp)
                    )

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "Menu",
                            style = Typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Display menu items
                    MenuItem(
                        title = "Profile",
                        onClick = {
                            navController.navigate("ProfileScreen")
                        }
                    )
                    MenuItem(
                        title = "Friend Requests",
                        onClick = {
                            navController.navigate("FriendRequestsScreen")
                        }
                    )
                    MenuItem(
                        title = "Notifications",
                        onClick = {
                            navController.navigate("NotificationsScreen")
                        }
                    )
                }
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewMenuScreen() {
    IceBreakerTheme {
        val navController = rememberNavController()
        MenuScreen(navController)
    }
}

@Composable
fun MenuScreenBottomNavBar(navController: NavController) {
    var selectedTab by remember { mutableStateOf("Menu") }

    NavigationBar {
        NavigationBarItem(
            label = {
                Text(text = "Home", style = Typography.labelSmall)
            },
            icon = {
                Icon(Icons.Filled.Home, contentDescription = "Home")
            },
            onClick = {
                if (selectedTab != "Home") {
                    selectedTab = "Home"
                    navController.navigate("HomeScreen")
                }
            },
            selected = selectedTab == "Home",
        )
        NavigationBarItem(
            label = {
                Text(text = "Search", style = Typography.labelSmall)
            },
            icon = {
                Icon(Icons.Filled.Search, contentDescription = "Search")
            },
            onClick = {
                if (selectedTab != "Search") {
                    selectedTab = "Search"
                    navController.navigate("SearchUserScreen")
                }
            },
            selected = selectedTab == "Search",
        )
        NavigationBarItem(
            label = {
                Text(text = "Friends", style = Typography.labelSmall)
            },
            icon = {
                Icon(Icons.Filled.Person, contentDescription = "Friends")
            },
            onClick = {
                if (selectedTab != "Friends") {
                    selectedTab = "Friends"
                    navController.navigate("FriendsListScreen")
                }
            },
            selected = selectedTab == "Friends",
        )
        NavigationBarItem(
            label = {
                Text(text = "Menu", style = Typography.labelSmall)
            },
            icon = {
                Icon(Icons.Filled.Menu, contentDescription = "Menu")
            },
            onClick = {
                if (selectedTab != "Menu") {
                    selectedTab = "Menu"
                    navController.navigate("MenuScreen")
                }
            },
            selected = selectedTab == "Menu",
        )
    }
}

@Composable
fun MenuItem(title: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        tonalElevation = 4.dp,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onClick()
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = title, style = Typography.bodyLarge)
                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                        contentDescription = "Chat Bubble",
                    )
                }
            }
        }
    }
}
