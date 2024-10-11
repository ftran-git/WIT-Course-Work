package com.example.seniorproject_icebreaker.ui.screen

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.seniorproject_icebreaker.R
import com.example.seniorproject_icebreaker.ui.theme.AppPadding
import com.example.seniorproject_icebreaker.ui.theme.Colors
import com.example.seniorproject_icebreaker.ui.theme.IceBreakerTheme
import com.example.seniorproject_icebreaker.ui.theme.Typography

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            HomeScreenBottomNavBar(navController)
        }
    ) {
        // Surface to contain elements
        Surface {
            // Get the system's dark mode state
            val isDarkTheme = isSystemInDarkTheme()

            // Column to arrange elements vertically
            Column(
                // Fill parent space and assign padding
                modifier = Modifier
                    .fillMaxSize()
                    .padding(AppPadding.large),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = if (isDarkTheme) R.drawable.app_banner_polar_blue_dark else R.drawable.app_banner_polar_blue_light),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(300.dp)
                )

                // Display tutorial slideshow
                Slideshow()
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewHomeScreen() {
    IceBreakerTheme {
        val navController = rememberNavController()
        HomeScreen(navController)
    }
}

@Composable
fun HomeScreenBottomNavBar(navController: NavController) {
    var selectedTab by remember { mutableStateOf("Home") }

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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Slideshow() {
    // Number of tutorial pages
    val pagerState = rememberPagerState(pageCount = { 3 })

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // HorizontalPager composable to display pages
        HorizontalPager(
            state = pagerState,
        ) { page ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Determine which page to display based on the index
                when (page) {
                    0 -> TutorialPageOne()
                    1 -> TutorialPageTwo()
                    2 -> TutorialPageThree()
                    else -> throw IndexOutOfBoundsException("Page index out of bounds")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row {
            repeat(pagerState.pageCount) { iteration ->
                val color =
                    if (isSystemInDarkTheme()) {
                        if (pagerState.currentPage == iteration) Color.LightGray else Color.DarkGray
                    } else {
                        if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                    }
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                )
            }
        }
    }
}

@Composable
fun TutorialPageOne() {
    Text(
        text = "Find and Add Friends!",
        style = Typography.titleLarge,
    )

    Image(
        painter = painterResource(id = R.drawable.tutorial_group_icon),
        colorFilter = ColorFilter.tint(if (isSystemInDarkTheme()) Colors.DarkSchemePolarBlue else Colors.LightSchemePolarBlue),
        contentDescription = "App Logo",
        modifier = Modifier.size(200.dp)
    )
}

@Composable
fun TutorialPageTwo() {
    Text(
        text = "Engage in Daily Activities!",
        style = Typography.titleLarge,
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.tutorial_game_icon),
            colorFilter = ColorFilter.tint(if (isSystemInDarkTheme()) Colors.DarkSchemePolarBlue else Colors.LightSchemePolarBlue),
            contentDescription = "App Logo",
            modifier = Modifier.size(200.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.tutorial_question_icon),
            colorFilter = ColorFilter.tint(if (isSystemInDarkTheme()) Colors.DarkSchemePolarBlue else Colors.LightSchemePolarBlue),
            contentDescription = "App Logo",
            modifier = Modifier.size(200.dp)
        )
    }
}

@Composable
fun TutorialPageThree() {
    Text(
        text = "Chat Along the Way!",
        style = Typography.titleLarge,
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.tutorial_person_icon),
            colorFilter = ColorFilter.tint(if (isSystemInDarkTheme()) Colors.DarkSchemePolarBlue else Colors.LightSchemePolarBlue),
            contentDescription = "App Logo",
            modifier = Modifier.size(200.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.tutorial_chat_icon),
            colorFilter = ColorFilter.tint(if (isSystemInDarkTheme()) Colors.DarkSchemePolarBlue else Colors.LightSchemePolarBlue),
            contentDescription = "App Logo",
            modifier = Modifier.size(200.dp)
        )
    }
}
