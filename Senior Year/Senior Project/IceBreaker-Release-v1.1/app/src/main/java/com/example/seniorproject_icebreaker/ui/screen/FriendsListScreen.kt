package com.example.seniorproject_icebreaker.ui.screen

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.seniorproject_icebreaker.R
import com.example.seniorproject_icebreaker.data.model.FriendStatus
import com.example.seniorproject_icebreaker.data.model.User
import com.example.seniorproject_icebreaker.ui.theme.AppPadding
import com.example.seniorproject_icebreaker.ui.theme.Colors
import com.example.seniorproject_icebreaker.ui.theme.IceBreakerTheme
import com.example.seniorproject_icebreaker.ui.theme.Typography
import com.example.seniorproject_icebreaker.ui.viewmodel.FriendsListViewModel

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FriendsListScreen(navController: NavController, viewModel: FriendsListViewModel) {
    // Refresh data when the composable is recomposed
    LaunchedEffect(Unit) {
        viewModel.grabFriends()
    }

    Scaffold(
        bottomBar = {
            FriendsListScreenBottomNavBar(navController)
        }
    ) {
        // Surface to contain elements
        Surface {
            // Get the system's dark mode state
            val isDarkTheme = isSystemInDarkTheme()

            // Grab data from associated view model
            val friends = viewModel.friends.collectAsState()
            // Grab loading status from view model
            val isLoading by viewModel.isLoading.collectAsState()

            // Manage state of pull refresh feature
            val pullRefreshState = rememberPullRefreshState(
                refreshing = isLoading,
                onRefresh = { viewModel.grabFriends() }
            )

            // Box to contain all elements
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(AppPadding.large)
                    .pullRefresh(pullRefreshState)
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
                            text = "Friends",
                            style = Typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    //Spacer(modifier = Modifier.height(20.dp))

                    PullRefreshIndicator(
                        refreshing = false,
                        state = pullRefreshState,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .scale(0.75f),
                        backgroundColor = if (isDarkTheme) Colors.DarkSchemePolarBlue else Colors.LightSchemePolarBlue
                    )

                    if (isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Loading...",
                                style = Typography.bodyLarge
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 100.dp)
                        ) {
                            if (friends.value.isNotEmpty()) {
                                items(friends.value) { friend ->
                                    FriendItem(friend, navController)
                                }
                            } else {
                                item {
                                    FriendItem(null, navController)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewFriendsListScreen() {
    IceBreakerTheme {
        val navController = rememberNavController()
        val friendsListViewModel = remember { FriendsListViewModel() }
        FriendsListScreen(navController, friendsListViewModel)
    }
}

@Composable
fun FriendItem(friend: User?, navController: NavController) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        tonalElevation = 4.dp,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (friend != null) {
                    // Define to pass value when navigating from FriendListScreen to ChatScreen
                    val friendEmail = friend.email

                    // Define max characters for text
                    val maxChar = 15
                    Text(
                        text = if (friendEmail.length > maxChar) {
                            "${friendEmail.take(maxChar)}..."
                        } else {
                            friendEmail
                        },
                        style = Typography.bodyLarge
                    )

                    Row {
                        IconButton(
                            onClick = {
                                navController.navigate("ChatScreen/$friendEmail")
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.chat_bubble),
                                contentDescription = "Chat Bubble",
                            )
                        }
                        IconButton(
                            onClick = {
                                navController.navigate("ActivityScreen/$friendEmail")
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.play_button),
                                contentDescription = "Play Button",
                            )
                        }
                    }
                } else {
                    Text(
                        text = "none",
                        style = Typography.bodyLarge,
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewFriendItem() {
    IceBreakerTheme {
        val testUser = User("example@email.com", FriendStatus.FRIEND)
        val navController = rememberNavController()
        FriendItem(testUser, navController)
    }
}

@Composable
fun FriendsListScreenBottomNavBar(navController: NavController) {
    var selectedTab by remember { mutableStateOf("Friends") }

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
