package com.example.seniorproject_icebreaker.ui.screen

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.seniorproject_icebreaker.R
import com.example.seniorproject_icebreaker.data.firestore.FirestoreQueries
import com.example.seniorproject_icebreaker.data.model.FriendStatus
import com.example.seniorproject_icebreaker.data.model.Notification
import com.example.seniorproject_icebreaker.data.model.NotificationType
import com.example.seniorproject_icebreaker.data.model.User
import com.example.seniorproject_icebreaker.ui.theme.AppPadding
import com.example.seniorproject_icebreaker.ui.theme.Colors
import com.example.seniorproject_icebreaker.ui.theme.IceBreakerTheme
import com.example.seniorproject_icebreaker.ui.theme.Typography
import com.example.seniorproject_icebreaker.ui.viewmodel.FriendRequestsViewModel
import com.example.seniorproject_icebreaker.ui.viewmodel.NotificationsViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NotificationsScreen(
    navController: NavController,
    viewModel: NotificationsViewModel
) {
    // Refresh data when the composable is recomposed
    LaunchedEffect(Unit) {
        viewModel.checkNotifications()
    }

    Scaffold(
        bottomBar = {
            InnerMenuScreenBottomNavBar(navController)
        }
    ) {
        // Surface to contain elements
        Surface {
            // Get the system's dark mode state
            val isDarkTheme = isSystemInDarkTheme()

            // Grab data from associated view model
            val isLoading by viewModel.isLoading.collectAsState()
            val notifications = viewModel.notifications.collectAsState()

            // Manage state of pull refresh feature
            val pullRefreshState = rememberPullRefreshState(
                refreshing = isLoading,
                onRefresh = { viewModel.checkNotifications() }
            )

            // Box to contain all elements
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(AppPadding.large)
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(AppPadding.large)
                        .pullRefresh(pullRefreshState),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(
                            onClick = {
                                navController.navigate("MenuScreen")
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.back_arrow),
                                contentDescription = "Back"
                            )
                        }
                    }

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
                            text = "Notifications",
                            style = Typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

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
                            modifier = Modifier.fillMaxWidth()
                                .padding(bottom = 100.dp)
                        ) {
                            if (notifications.value.isNotEmpty()) {
                                items(notifications.value) { notification ->
                                    NotificationItem(notification)
                                }
                            } else {
                                item {
                                    NotificationItem(null)
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
fun PreviewNotificationsScreen() {
    IceBreakerTheme {
        val navController = rememberNavController()
        val viewModel = remember { NotificationsViewModel() }
        NotificationsScreen(navController, viewModel)
    }
}

@Composable
fun NotificationItem(
    notification: Notification?,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
        ,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (notification != null) {
                val header = if(notification.type == NotificationType.ACTIVITY) "Activity:\n" else "Friend Request:\n"
                Text(
                    text = header + notification.message,
                    style = Typography.bodyLarge,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                )
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

