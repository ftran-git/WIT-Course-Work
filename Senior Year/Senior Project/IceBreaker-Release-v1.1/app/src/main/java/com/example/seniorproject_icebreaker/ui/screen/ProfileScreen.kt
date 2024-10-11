package com.example.seniorproject_icebreaker.ui.screen

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.seniorproject_icebreaker.R
import com.example.seniorproject_icebreaker.data.firestore.FirestoreQueries
import com.example.seniorproject_icebreaker.data.model.User
import com.example.seniorproject_icebreaker.ui.theme.AppPadding
import com.example.seniorproject_icebreaker.ui.theme.IceBreakerTheme
import com.example.seniorproject_icebreaker.ui.theme.Typography
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.UUID

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(navController: NavController, coroutineScope: CoroutineScope) {
    // Define to interact with Firebase Authentication
    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    // To hold states and values
    var currentUserEmail by remember { mutableStateOf<String?>(null) }
    var profilePictureURL by remember { mutableStateOf<String?>(null) }

    // LaunchedEffect to perform asynchronous operation
    LaunchedEffect(Unit) {
        // Grab current user's email
        currentUserEmail = auth.currentUser?.email

        // Grab user data
        val querySnapshot = currentUserEmail?.let { FirestoreQueries.searchUsersCollection(it) }

        // Process result into list of user data objects
        val userList = mutableListOf<User>()
        if (querySnapshot != null) {
            for (document in querySnapshot.documents) {
                // Extract data from query snapshot
                val data = document.data

                // Grab URL to profile picture
                profilePictureURL = data?.get("profilePictureURL") as? String
            }
        }
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
                            text = "Profile",
                            style = Typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    val pickImageLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.GetContent()
                    ) { uri: Uri? ->
                        uri?.let {
                            currentUserEmail?.let { it1 ->
                                uploadImageToFirebase(
                                    navController,
                                    coroutineScope,
                                    it1, uri
                                )
                            }
                        }
                    }
                    IconButton(
                        onClick = {
                            // TODO disabling profile picture upload due to issue with Firebase App Check services
                            //pickImageLauncher.launch("image/*")
                                  },
                        modifier = Modifier.size(96.dp)
                    ) {
                        if (profilePictureURL == null || profilePictureURL == "") {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_account_circle_24),
                                contentDescription = "Profile Picture",
                                modifier = Modifier.size(72.dp)
                            )
                        } else {
                            Icon(
                                painter = rememberImagePainter(
                                    data = profilePictureURL,
                                    builder = {
                                        transformations(CircleCropTransformation())
                                    }
                                ),
                                contentDescription = "Profile Picture",
                                modifier = Modifier.size(72.dp)
                            )
                        }
                    }

                    // Display profile items
                    val text by remember { mutableStateOf("Email: **********") }
                    var showEmail by remember { mutableStateOf(false) }
                    ProfileItem(
                        text = if (showEmail) {
                            "Email:\n" + currentUserEmail as String
                        } else {
                            text
                        },
                        onClick = {
                            // Toggle the state on click
                            showEmail = !showEmail
                        }
                    )
                    ProfileItem(
                        text = "Log Out",
                        fontColor = Color.Red,
                        onClick = {
                            // Sign out
                            auth.signOut()

                            // Navigate to login screen
                            navController.navigate("LoginScreen")
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
fun PreviewProfileScreen() {
    IceBreakerTheme {
        val navController = rememberNavController()
        val coroutineScope = rememberCoroutineScope()
        ProfileScreen(navController, coroutineScope)
    }
}

@Composable
fun ProfileItem(text: String, fontColor: Color? = null, onClick: () -> Unit) {
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
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = text,
                    color = fontColor ?: LocalContentColor.current,
                    style = Typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun InnerMenuScreenBottomNavBar(navController: NavController) {
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
                selectedTab = "Menu"
                navController.navigate("MenuScreen")

            },
            selected = selectedTab == "Menu",
        )
    }
}

fun uploadImageToFirebase(
    navController: NavController,
    coroutineScope: CoroutineScope,
    currentUserEmail: String,
    uri: Uri
) {
    val storageRef = FirebaseStorage.getInstance().reference
    val profileImagesRef = storageRef.child("profilePictures/${UUID.randomUUID()}")

    val uploadTask = profileImagesRef.putFile(uri)
    uploadTask.addOnSuccessListener {
        profileImagesRef.downloadUrl.addOnSuccessListener { downloadUri ->
            // Save URL to profile picture in firestore database
            coroutineScope.launch {
                FirestoreQueries.updateUserData(
                    currentUserEmail,
                    "profilePictureURL",
                    downloadUri.toString()
                )

                // Trigger recomposition of ProfileScreen to update UI
                navController.navigate("ProfileScreen")
            }
        }
    }.addOnFailureListener {
        // Handle unsuccessful uploads
    }
}

