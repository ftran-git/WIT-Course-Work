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
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FriendRequestsScreen(
    navController: NavController,
    coroutineScope: CoroutineScope,
    viewModel: FriendRequestsViewModel
) {
    // Refresh data when the composable is recomposed
    LaunchedEffect(Unit) {
        viewModel.checkFriendRequests()
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
            val incomingFriendRequests = viewModel.incomingFriendRequests.collectAsState()
            val outgoingFriendRequests = viewModel.outgoingFriendRequests.collectAsState()

            // Manage state of pull refresh feature
            val pullRefreshState = rememberPullRefreshState(
                refreshing = isLoading,
                onRefresh = { viewModel.checkFriendRequests() }
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
                            text = "Friend Requests",
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
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Incoming",
                                    style = Typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )

                                Icon(
                                    painter = painterResource(id = R.drawable.back_arrow),
                                    contentDescription = "Back"
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            if (incomingFriendRequests.value.isNotEmpty()) {
                                items(incomingFriendRequests.value) { friend ->
                                    FriendRequestItem(
                                        friend,
                                        navController,
                                        coroutineScope,
                                        FriendRequestsViewModel()
                                    )
                                }
                            } else {
                                item {
                                    FriendRequestItem(
                                        user = null,
                                        navController,
                                        coroutineScope = coroutineScope,
                                        viewModel = FriendRequestsViewModel()
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Outgoing",
                                    style = Typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )

                                Icon(
                                    painter = painterResource(id = R.drawable.back_arrow),
                                    contentDescription = "Back",
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .graphicsLayer {
                                            rotationZ = 180f
                                        }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1.75f)
                                .padding(bottom = 100.dp)
                        ) {
                            if (outgoingFriendRequests.value.isNotEmpty()) {
                                items(outgoingFriendRequests.value) { friend ->
                                    FriendRequestItem(
                                        friend,
                                        navController,
                                        coroutineScope,
                                        FriendRequestsViewModel()
                                    )
                                }
                            } else {
                                item {
                                    FriendRequestItem(
                                        null,
                                        navController,
                                        coroutineScope,
                                        FriendRequestsViewModel()
                                    )
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
fun PreviewFriendRequestsScreen() {
    IceBreakerTheme {
        val navController = rememberNavController()
        val coroutineScope = rememberCoroutineScope()
        val friendRequestsViewModel = remember { FriendRequestsViewModel() }
        FriendRequestsScreen(navController, coroutineScope, friendRequestsViewModel)
    }
}

@Composable
fun FriendRequestItem(
    user: User?,
    navController: NavController,
    coroutineScope: CoroutineScope,
    viewModel: FriendRequestsViewModel,
) {
    // Define to interact with Firebase Authentication
    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    // Handle visibility of popups
    var showAcceptOrDenyPopup by remember { mutableStateOf(false) }
    var showRemoveFriendPopup by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Define max characters for text
            if (user != null) {
                Text(
                    text = user.email,
                    style = Typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                )

                when (user.friendStatus) {
                    FriendStatus.FRIEND -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(onClick = { showRemoveFriendPopup = true }) {
                                Text(text = "Added", style = Typography.labelSmall)
                            }

                            RemoveFriendPopup(
                                showRemoveFriendPopup = showRemoveFriendPopup,
                                onConfirm = {
                                    coroutineScope.launch {
                                        // Current user's email
                                        val currentUserEmail = auth.currentUser?.email
                                        // Other user's email
                                        val otherUserEmail = user.email

                                        // Process friend request unadding
                                        if (currentUserEmail != null) {
                                            // Delete other user from current user's friends collection
                                            FirestoreQueries.deleteUserFromCollection(
                                                "friends",
                                                currentUserEmail,
                                                otherUserEmail
                                            )

                                            // Delete current user from other user's friends collection
                                            FirestoreQueries.deleteUserFromCollection(
                                                "friends",
                                                otherUserEmail,
                                                currentUserEmail
                                            )
                                        }

                                        // Dismiss popup after accepting
                                        showRemoveFriendPopup = false

                                        // Update view model
                                        viewModel.checkFriendRequests()

                                        // Reload screen
                                        navController.navigate("FriendRequestsScreen")
                                    }
                                },
                                onCancel = {
                                    coroutineScope.launch {
                                        // Dismiss popup
                                        showRemoveFriendPopup = false
                                    }
                                },
                                onDismiss = {
                                    // Dismiss popup
                                    showAcceptOrDenyPopup = false
                                }
                            )
                        }
                    }

                    FriendStatus.NOT_FRIEND -> {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    // Current user's email
                                    val currentUserEmail = auth.currentUser?.email
                                    // Other user's email
                                    val otherUserEmail = user.email

                                    // Process friend request initiation
                                    if (currentUserEmail != null) {
                                        // Define data for current user
                                        val currentUserData = hashMapOf(
                                            "email" to currentUserEmail,
                                        )
                                        // Define data for other user
                                        val otherUserData = hashMapOf(
                                            "email" to otherUserEmail,
                                        )

                                        // Add other user to current user's outgoingFriendRequests collection
                                        FirestoreQueries.addUserToCollection(
                                            "outgoingFriendRequests",
                                            currentUserEmail,
                                            otherUserData
                                        )

                                        // Add current user to other user's incomingFriendRequests collection
                                        FirestoreQueries.addUserToCollection(
                                            "incomingFriendRequests",
                                            otherUserEmail,
                                            currentUserData
                                        )
                                    }

                                    // Update view model
                                    viewModel.checkFriendRequests()

                                    // Reload screen
                                    navController.navigate("FriendRequestsScreen")
                                }

                            },
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Text(text = "Add", style = Typography.labelSmall)
                        }
                    }

                    FriendStatus.INCOMING_REQUEST -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp) // Adjust the spacing as needed
                        ) {
                            Button(onClick = { showAcceptOrDenyPopup = true }) {
                                Text(text = "Accept?", style = Typography.labelSmall)
                            }

                            AcceptOrDenyFriendRequestPopup(
                                showAcceptOrDenyPopup = showAcceptOrDenyPopup,
                                onAccept = {
                                    coroutineScope.launch {
                                        // Current user's email
                                        val currentUserEmail = auth.currentUser?.email
                                        // Other user's email
                                        val otherUserEmail = user.email

                                        // Process friend request acceptance
                                        if (currentUserEmail != null) {
                                            // Define data for current user
                                            val currentUserData = hashMapOf(
                                                "email" to currentUserEmail,
                                            )
                                            // Define data for other user
                                            val otherUserData = hashMapOf(
                                                "email" to otherUserEmail,
                                            )

                                            // Add other user to current user's friends collection
                                            FirestoreQueries.addUserToCollection(
                                                "friends",
                                                currentUserEmail,
                                                otherUserData
                                            )

                                            // Delete other user from current user's incomingFriendRequests collection
                                            FirestoreQueries.deleteUserFromCollection(
                                                "incomingFriendRequests",
                                                currentUserEmail,
                                                otherUserEmail
                                            )

                                            // Delete current user from other user's outgoingFriendRequests collection
                                            FirestoreQueries.deleteUserFromCollection(
                                                "outgoingFriendRequests",
                                                otherUserEmail,
                                                currentUserEmail
                                            )

                                            // Add current user to other user's friends collection
                                            FirestoreQueries.addUserToCollection(
                                                "friends",
                                                otherUserEmail,
                                                currentUserData
                                            )

                                            // Add notification document to friend's notification collection
                                            FirestoreQueries.addNotification(
                                                Notification(NotificationType.FRIEND_REQUEST, currentUserEmail + " has accepted your friend request!"),
                                                otherUserEmail
                                            )
                                        }

                                        // Dismiss popup after accepting
                                        showAcceptOrDenyPopup = false

                                        // Update view model
                                        viewModel.checkFriendRequests()

                                        // Reload screen
                                        navController.navigate("FriendRequestsScreen")
                                    }
                                },
                                onDeny = {
                                    coroutineScope.launch {
                                        // Current user's email
                                        val currentUserEmail = auth.currentUser?.email
                                        // Other user's email
                                        val otherUserEmail = user.email

                                        // Process friend request denial
                                        if (currentUserEmail != null) {
                                            // Delete other user from current user's incomingFriendRequests collection
                                            FirestoreQueries.deleteUserFromCollection(
                                                "incomingFriendRequests",
                                                currentUserEmail,
                                                otherUserEmail
                                            )

                                            // Delete current user from other user's outgoingFriendRequests collection
                                            FirestoreQueries.deleteUserFromCollection(
                                                "outgoingFriendRequests",
                                                otherUserEmail,
                                                currentUserEmail
                                            )

                                            // Add notification document to friend's notification collection
                                            FirestoreQueries.addNotification(
                                                Notification(NotificationType.FRIEND_REQUEST, currentUserEmail + " has denied your friend request!"),
                                                otherUserEmail
                                            )
                                        }

                                        // Dismiss popup after denying
                                        showAcceptOrDenyPopup = false

                                        // Update view model
                                        viewModel.checkFriendRequests()

                                        // Reload screen
                                        navController.navigate("FriendRequestsScreen")
                                    }
                                },
                                onDismiss = {
                                    // Dismiss popup
                                    showAcceptOrDenyPopup = false
                                }
                            )
                        }
                    }

                    FriendStatus.OUTGOING_REQUEST -> {
                        Button(
                            onClick = {
                                // Do nothing
                            },
                            enabled = false,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Text(text = "Pending", style = Typography.labelSmall)
                        }
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