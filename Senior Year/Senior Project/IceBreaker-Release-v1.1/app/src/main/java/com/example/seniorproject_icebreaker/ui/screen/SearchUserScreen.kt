package com.example.seniorproject_icebreaker.ui.screen

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
import com.example.seniorproject_icebreaker.ui.viewmodel.SearchUserViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchUserScreen(
    navController: NavController,
    viewModel: SearchUserViewModel,
    coroutineScope: CoroutineScope
) {
    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetSearchViewModel()
        }
    }

    Scaffold(
        bottomBar = {
            SearchUserScreenBottomNavBar(navController)
        }
    ) {
        // Surface to contain elements
        Surface {
            // Get the system's dark mode state
            val isDarkTheme = isSystemInDarkTheme()

            // Grab data from associated view model
            val isLoading by viewModel.isLoading.collectAsState()
            val searchText by viewModel.searchText.collectAsState()
            val usersAsState = viewModel.users.collectAsState()

            // Object to manage focus within composable elements
            val focusManager = LocalFocusManager.current

            // Manage state of pull refresh feature
            val pullRefreshState = rememberPullRefreshState(
                refreshing = isLoading,
                onRefresh = { viewModel.searchUsers(searchText) }
            )

            // Column to arrange elements vertically
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(AppPadding.large)
                    .pullRefresh(pullRefreshState)
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        viewModel.setSearchText(it)
                        viewModel.searchUsers(it)
                    },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.Gray
                        )
                    },
                    label = { Text("Search by email") },
                    modifier = Modifier
                        .background(Color.Transparent, RoundedCornerShape(16.dp))
                        .border(BorderStroke(width = 1.dp, color = Color.Transparent))
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    )
                )

                val users = usersAsState.value
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
                } else if (users.isNotEmpty()) {
                    PullRefreshIndicator(
                        refreshing = false,
                        state = pullRefreshState,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .scale(0.75f),
                        backgroundColor = if (isDarkTheme) Colors.DarkSchemePolarBlue else Colors.LightSchemePolarBlue
                    )

                    LazyColumn(
                        modifier = Modifier
                            .padding(vertical = 15.dp)
                    ) {
                        items(users) { user ->
                            UserItem(user, coroutineScope, viewModel, searchText)
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (searchText.isNotEmpty()) {
                            Text(
                                text = "No results found",
                                modifier = Modifier.padding(1.dp),
                                style = Typography.bodyLarge
                            )
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
fun PreviewSearchUserScreen() {
    IceBreakerTheme {
        val navController = rememberNavController()
        val searchUserViewModel = remember { SearchUserViewModel() }
        val coroutineScope = rememberCoroutineScope()
        SearchUserScreen(navController, searchUserViewModel, coroutineScope)
    }
}

@Composable
fun UserItem(
    user: User,
    coroutineScope: CoroutineScope,
    viewModel: SearchUserViewModel,
    searchText: String
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
            val maxChar = 25
            Text(
                text = if (user.email.length > maxChar) {
                    "${user.email.take(maxChar)}..."
                } else {
                    user.email
                },
                style = Typography.bodyLarge,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )

            when (user.friendStatus) {
                FriendStatus.FRIEND -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp) // Adjust the spacing as needed
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
                                    viewModel.searchUsers(searchText)
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
                                showRemoveFriendPopup = false
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
                                viewModel.searchUsers(searchText)
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
                                    viewModel.searchUsers(searchText)
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
                                    viewModel.searchUsers(searchText)
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
        }
    }
}

@Composable
fun AcceptOrDenyFriendRequestPopup(
    showAcceptOrDenyPopup: Boolean,
    onAccept: () -> Unit,
    onDeny: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showAcceptOrDenyPopup) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the popup when clicked outside
                onDismiss()
            },
            title = {
                Text(text = "Respond to Friend Request", style = Typography.titleLarge)
            },
            text = {
                Text(
                    text = "Would you like to accept or deny this friend request?",
                    style = Typography.bodyLarge
                )
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Button(onClick = {
                        // Accept the friend request
                        onAccept()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.symbol_green_check),
                            contentDescription = "Accept",
                            tint = Color.Unspecified
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(onClick = {
                        // Deny the friend request
                        onDeny()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.symbol_red_close),
                            contentDescription = "Deny",
                            tint = Color.Unspecified
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAcceptOrDenyFriendRequestPopup() {
    IceBreakerTheme {
        val showAcceptOrDenyPopup = true
        val onAccept: () -> Unit = {}
        val onDeny: () -> Unit = {}
        val onDismiss: () -> Unit = {}
        AcceptOrDenyFriendRequestPopup(showAcceptOrDenyPopup, onAccept, onDeny, onDismiss)
    }
}

@Composable
fun RemoveFriendPopup(
    showRemoveFriendPopup: Boolean,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showRemoveFriendPopup) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the popup when clicked outside
                onDismiss()
            },
            title = {
                Text(text = "Remove Friend", style = Typography.titleLarge)
            },
            text = {
                Text(
                    text = "Would you like to remove this user as a friend?",
                    style = Typography.bodyLarge
                )
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Button(onClick = {
                        // Remove the friend
                        onConfirm()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.symbol_green_check),
                            contentDescription = "Confirm",
                            tint = Color.Unspecified
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(onClick = {
                        // Cancel the friend removal
                        onCancel()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.symbol_red_close),
                            contentDescription = "Cancel",
                            tint = Color.Unspecified
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewRemoveFriendPopup() {
    IceBreakerTheme {
        val showRemoveFriendPopup = true
        val onAccept: () -> Unit = {}
        val onDeny: () -> Unit = {}
        val onDismiss: () -> Unit = {}
        RemoveFriendPopup(showRemoveFriendPopup, onAccept, onDeny, onDismiss)
    }
}

@Composable
fun SearchUserScreenBottomNavBar(navController: NavController) {
    var selectedTab by remember { mutableStateOf("Search") }

    NavigationBar {
        NavigationBarItem(
            label = {
                Text(text = "Home", style = Typography.bodyLarge)
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
