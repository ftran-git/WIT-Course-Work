package com.example.seniorproject_icebreaker

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.seniorproject_icebreaker.ui.screen.AccountRecoveryScreen
import com.example.seniorproject_icebreaker.ui.screen.ChatScreen
import com.example.seniorproject_icebreaker.ui.screen.FriendRequestsScreen
import com.example.seniorproject_icebreaker.ui.screen.FriendsListScreen
import com.example.seniorproject_icebreaker.ui.screen.HomeScreen
import com.example.seniorproject_icebreaker.ui.screen.LoginScreen
import com.example.seniorproject_icebreaker.ui.screen.MenuScreen
import com.example.seniorproject_icebreaker.ui.screen.NotificationsScreen
import com.example.seniorproject_icebreaker.ui.screen.ProfileScreen
import com.example.seniorproject_icebreaker.ui.screen.RegisterScreen
import com.example.seniorproject_icebreaker.ui.screen.SearchUserScreen
import com.example.seniorproject_icebreaker.ui.screen.activity.ActivityScreen
import com.example.seniorproject_icebreaker.ui.screen.activity.game.FourInARowActivity
import com.example.seniorproject_icebreaker.ui.screen.activity.game.TicTacToeActivity
import com.example.seniorproject_icebreaker.ui.screen.activity.question.HypotheticalActivity
import com.example.seniorproject_icebreaker.ui.screen.activity.question.WouldYouRatherActivity
import com.example.seniorproject_icebreaker.ui.theme.AppPadding
import com.example.seniorproject_icebreaker.ui.theme.IceBreakerTheme
import com.example.seniorproject_icebreaker.ui.viewmodel.QuestionBasedActivityViewModel
import com.example.seniorproject_icebreaker.ui.viewmodel.ChatScreenViewModel
import com.example.seniorproject_icebreaker.ui.viewmodel.FourInARowViewModel
import com.example.seniorproject_icebreaker.ui.viewmodel.FriendRequestsViewModel
import com.example.seniorproject_icebreaker.ui.viewmodel.FriendsListViewModel
import com.example.seniorproject_icebreaker.ui.viewmodel.NotificationsViewModel
import com.example.seniorproject_icebreaker.ui.viewmodel.SearchUserViewModel
import com.example.seniorproject_icebreaker.ui.viewmodel.TicTacToeViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    // Singleton object to access Firebase Database
    object FirestoreInstance {
        @SuppressLint("StaticFieldLeak")
        val db = Firebase.firestore
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Register the broadcast receiver to request notification permission from user
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(permissionReceiver, IntentFilter("REQUEST_NOTIFICATION_PERMISSION"),
                RECEIVER_NOT_EXPORTED
            )
        }

        // Check if it's the first run of the application
        val isFirstRun = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            .getBoolean("isFirstRun", true)

        if (isFirstRun) {
            // Check for POST_NOTIFICATIONS permission and request it if needed
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermission()
            }

            // Update shared preferences to mark that the permission has been requested
            getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                .edit()
                .putBoolean("isFirstRun", false)
                .apply()
        }

        // Create notification channel for daily reminder notification
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Daily Reminder Notification"
            val description = "Channel for daily reminder notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("Daily_Reminder_Channel_ID", name, importance)
            channel.description = description
            notificationManager.createNotificationChannel(channel)
        }

        // Set content and define navigation between screens
        enableEdgeToEdge()
        setContent {
            IceBreakerTheme {
                // Create NavController object to management navigation within the app
                val navController = rememberNavController()
                // Create CoroutineScope object to create and manage coroutines
                val coroutineScope = rememberCoroutineScope()

                // Create SearchUserViewModel to retrieve associated data
                val searchUserViewModel = remember { SearchUserViewModel() }
                // Create FriendListViewModel to retrieve associated data
                val friendsListViewModel = remember { FriendsListViewModel() }
                // Create ChatScreenViewModel to retrieve associated data
                val chatScreenViewModel = remember { ChatScreenViewModel() }
                // Create ActivityScreenViewModel to retrieve associated data
                val activityScreenViewModel = remember { QuestionBasedActivityViewModel() }
                // Create TicTacToeViewModel to retrieve associated data
                val ticTacToeViewModel = remember { TicTacToeViewModel() }
                // Create FourInARowViewModel to retrieve associated data
                val fourInARowViewModel = remember { FourInARowViewModel() }
                // Create FriendRequestsViewModel to retrieve associated data
                val friendRequestsViewModel = remember { FriendRequestsViewModel() }
                // Create NotificationsViewModel to retrieve associated data
                val notificationsViewModel = remember { NotificationsViewModel() }

                // Create NavBackStackEntry to pass data from one composable to another
                val navBackStackEntry by navController.currentBackStackEntryAsState()

                // Define NavController mapping
                NavHost(navController, startDestination = "MainScreen") {
                    composable("MainScreen") {
                        BackHandler(true) {
                            // Do nothing
                        }
                        MainScreen(navController)
                    }
                    composable("RegisterScreen") {
                        BackHandler(true) {
                            navController.navigate("LoginScreen")
                        }
                        RegisterScreen(navController)
                    }
                    composable("LoginScreen") {
                        BackHandler(true) {
                            // Do nothing
                        }
                        LoginScreen(navController, coroutineScope)
                    }
                    composable("AccountRecoveryScreen") {
                        BackHandler(true) {
                            navController.navigate("LoginScreen")
                        }
                        AccountRecoveryScreen(navController)
                    }
                    composable("HomeScreen") {
                        BackHandler(true) {
                            // Do nothing
                        }
                        HomeScreen(navController)
                    }
                    composable("SearchUserScreen") {
                        BackHandler(true) {
                            // Do nothing
                        }
                        SearchUserScreen(navController, searchUserViewModel, coroutineScope)
                    }
                    composable("FriendsListScreen") {
                        BackHandler(true) {
                            // Do nothing
                        }
                        FriendsListScreen(navController, friendsListViewModel)
                    }
                    composable("MenuScreen") {
                        BackHandler(true) {
                            // Do nothing
                        }
                        MenuScreen(navController)
                    }
                    composable("ChatScreen/{key}") {
                        val friendEmail = navBackStackEntry?.arguments?.getString("key")
                        BackHandler(true) {
                            navController.navigate("FriendsListScreen")
                        }
                        if (friendEmail != null) {
                            ChatScreen(navController, coroutineScope, chatScreenViewModel, friendEmail)
                        }
                    }
                    composable("ActivityScreen/{key}") {
                        val friendEmail = navBackStackEntry?.arguments?.getString("key")
                        BackHandler(true) {
                            navController.navigate("FriendsListScreen")
                        }
                        if (friendEmail != null) {
                            ActivityScreen(navController, friendEmail)
                        }
                    }
                    composable("HypotheticalActivity/{key}") {
                        val friendEmail = navBackStackEntry?.arguments?.getString("key")
                        BackHandler(true) {
                            navController.navigate("FriendsListScreen")
                        }
                        if (friendEmail != null) {
                            HypotheticalActivity(navController, coroutineScope, activityScreenViewModel, friendEmail)
                        }
                    }
                    composable("WouldYouRatherActivity/{key}") {
                        val friendEmail = navBackStackEntry?.arguments?.getString("key")
                        BackHandler(true) {
                            navController.navigate("FriendsListScreen")
                        }
                        if (friendEmail != null) {
                            WouldYouRatherActivity(navController, coroutineScope, activityScreenViewModel, friendEmail)
                        }
                    }
                    composable("FourInARowActivity/{key}") {
                        val friendEmail = navBackStackEntry?.arguments?.getString("key")
                        BackHandler(true) {
                            navController.navigate("FriendsListScreen")
                        }
                        if (friendEmail != null) {
                            FourInARowActivity(navController, coroutineScope, fourInARowViewModel, friendEmail)
                        }
                    }
                    composable("TicTacToeActivity/{key}") {
                        val friendEmail = navBackStackEntry?.arguments?.getString("key")
                        BackHandler(true) {
                            navController.navigate("FriendsListScreen")
                        }
                        if (friendEmail != null) {
                            TicTacToeActivity(navController, coroutineScope, ticTacToeViewModel, friendEmail)
                        }
                    }
                    composable("ProfileScreen") {
                        BackHandler(true) {
                            navController.navigate("MenuScreen")
                        }
                        ProfileScreen(navController, coroutineScope)
                    }
                    composable("FriendRequestsScreen") {
                        BackHandler(true) {
                            navController.navigate("MenuScreen")
                        }
                        FriendRequestsScreen(navController, coroutineScope, friendRequestsViewModel)
                    }
                    composable("NotificationsScreen") {
                        BackHandler(true) {
                            navController.navigate("MenuScreen")
                        }
                        NotificationsScreen(navController, notificationsViewModel)
                    }
                }
            }
        }
    }

    // Companion object to hold static value for permission request
    companion object {
        private const val REQUEST_POST_NOTIFICATIONS = 1
    }

    // Define receiver for requesting notification permission
    private val permissionReceiver = object : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "REQUEST_NOTIFICATION_PERMISSION") {
                requestNotificationPermission()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the broadcast receiver
        unregisterReceiver(permissionReceiver)
    }

    // Method to request notification permission
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_POST_NOTIFICATIONS
            )
        }
    }

    // Method to handle users response to notification permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_POST_NOTIFICATIONS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission was granted, you can now show notifications
                    Log.d("REQUEST_POST_NOTIFICATION", "Permission was granted")
                } else {
                    // Permission denied, you cannot show notifications
                    Log.d("REQUEST_POST_NOTIFICATION", "Permission was not granted")
                }
                return
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavController) {
    // Define to interact with Firebase Authentication
    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    // Surface to contain elements
    Surface {
        // Column to arrange elements vertically
        Column(
            // Fill parent space and assign padding
            modifier = Modifier
                .fillMaxSize()
                .padding(AppPadding.large),
            // Center vertically and horizontally
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_banner_polar_blue_dark),
                contentDescription = "App Logo",
                modifier = Modifier.size(300.dp)
            )
        }
    }

    // Start a coroutine to delay the navigation to LoginScreen
    LaunchedEffect(Unit) {
        delay(2000) // 2-second delay

        val user = auth.currentUser
        if (user == null) {
            navController.navigate("LoginScreen")
        } else {
            navController.navigate("HomeScreen")
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewMainScreen() {
    IceBreakerTheme {
        val navController = rememberNavController()
        MainScreen(navController)
    }
}





