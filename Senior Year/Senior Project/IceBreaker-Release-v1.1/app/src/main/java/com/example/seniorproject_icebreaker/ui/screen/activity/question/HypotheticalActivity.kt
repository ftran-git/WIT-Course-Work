package com.example.seniorproject_icebreaker.ui.screen.activity.question

import android.annotation.SuppressLint
import android.content.res.Configuration
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.seniorproject_icebreaker.R
import com.example.seniorproject_icebreaker.data.firestore.FirestoreQueries
import com.example.seniorproject_icebreaker.data.model.Notification
import com.example.seniorproject_icebreaker.data.model.NotificationType
import com.example.seniorproject_icebreaker.ui.theme.AppPadding
import com.example.seniorproject_icebreaker.ui.theme.IceBreakerTheme
import com.example.seniorproject_icebreaker.ui.theme.Typography
import com.example.seniorproject_icebreaker.ui.viewmodel.QuestionBasedActivityViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HypotheticalActivity(
    navController: NavController,
    coroutineScope: CoroutineScope,
    viewModel: QuestionBasedActivityViewModel,
    friendEmail: String,
) {
    // Define to interact with Firebase Authentication
    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    // Surface to contain elements
    Surface {
        // Get the system's dark mode state
        val isDarkTheme = isSystemInDarkTheme()

        // To hold states and values
        var currentUserEmail by remember { mutableStateOf<String?>(null) }
        var textFieldValue by remember { mutableStateOf("") }

        // Observe the state from the ViewModel
        val isViewModelLoading by viewModel.isLoading.collectAsState()
        val currentActivityName by viewModel.name.collectAsState()
        val currentActivityStartDate by viewModel.startDate.collectAsState()
        val currentActivityCompleted by viewModel.completed.collectAsState()
        val currentActivityQuestion by viewModel.question.collectAsState()
        val currentActivityAnswer by viewModel.answer.collectAsState()

        // Misc properties
        val focusManager = LocalFocusManager.current
        val focusRequester = remember { FocusRequester() }
        val keyboardController = LocalSoftwareKeyboardController.current

        // LaunchedEffect to perform the asynchronous operation
        LaunchedEffect(Unit) {
            // Grab current user's email
            currentUserEmail = auth.currentUser?.email

            // Update view model
            viewModel.checkData(currentUserEmail!!, friendEmail)

            // Wait for view model check to complete
            while (isViewModelLoading) {
                delay(100)
            }
        }

        // Box to contain all elements
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(AppPadding.large)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = AppPadding.large,
                        vertical = AppPadding.medium
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Back button
                    IconButton(
                        onClick = {
                            navController.navigate("FriendsListScreen")
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.back_arrow),
                            contentDescription = "Back"
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Flexible space for email text
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = friendEmail,
                            style = Typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1, // Limit to one line
                            overflow = TextOverflow.Ellipsis // Add ellipsis for overflow
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Refresh button
                    IconButton(
                        onClick = {
                            // Update view model
                            viewModel.checkData(currentUserEmail!!, friendEmail)

                            // Reload UI
                            navController.navigate("ActivityScreen/$friendEmail")
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_change_circle_24),
                            contentDescription = "Refresh"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Image(
                        painter = painterResource(id = if (isDarkTheme) R.drawable.app_banner_polar_blue_dark else R.drawable.app_banner_polar_blue_light),
                        contentDescription = "App Logo",
                        modifier = Modifier.size(150.dp)
                    )

                    Text(
                        text = "Hypothetical",
                        style = Typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = currentActivityQuestion.toString(),
                        style = Typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    if (currentActivityCompleted?.get(currentUserEmail) != true) {
                        TextField(
                            value = textFieldValue,
                            onValueChange = { textFieldValue = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = AppPadding.large)
                                .focusRequester(focusRequester),
                            textStyle = Typography.labelSmall,
                            label = { Text(text = "Enter your answer") },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                }
                            )
                        )
                    }

                    // Display UI element depending on completed status of current user and friend
                    if (currentActivityCompleted?.get(currentUserEmail) == true && currentActivityCompleted?.get(
                            friendEmail
                        ) == true
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))

                        Column(
                            modifier = Modifier
                                .background(color = Color.Gray, shape = RoundedCornerShape(8.dp))
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {

                            // Show friend's answer if both users completed activity
                            Text(
                                text = "You said: ${currentActivityAnswer?.get(currentUserEmail)}",
                                style = Typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "They said: ${currentActivityAnswer?.get(friendEmail)}",
                                style = Typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    } else if (currentActivityCompleted?.get(currentUserEmail) == true && currentActivityCompleted?.get(
                            friendEmail
                        ) == false
                    ) {
                        Box(
                            modifier = Modifier
                                .background(color = Color.Gray, shape = RoundedCornerShape(8.dp))
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Spacer(modifier = Modifier.height(20.dp))

                            // Hide friend's answer if they have not completed activity
                            Text(
                                text = "Friend has not answered yet!",
                                style = Typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Spacer to push the submit button to the bottom
                    Spacer(modifier = Modifier.weight(1f))

                    // Submit button
                    if (currentActivityCompleted?.get(currentUserEmail) != true) {
                        Button(
                            onClick = {
                                // Check if text field input is empty
                                if (textFieldValue.isEmpty()) {
                                    return@Button
                                } else {
                                    coroutineScope.launch {
                                        // Query for friends completed and answer data
                                        val friendCompleted = currentUserEmail?.let {
                                            FirestoreQueries.retrieveCurrentActivityData(
                                                it, friendEmail, "completed"
                                            ) as Map<*, *>
                                        }
                                        val friendAnswer = currentUserEmail?.let {
                                            FirestoreQueries.retrieveCurrentActivityData(
                                                it, friendEmail, "answer"
                                            ) as Map<*, *>
                                        }

                                        // Define update data
                                        val updates = mapOf(
                                            "name" to currentActivityName,
                                            "startDate" to currentActivityStartDate as Timestamp,
                                            "completed" to mapOf(
                                                currentUserEmail to true,
                                                friendEmail to friendCompleted?.get(friendEmail),
                                            ),
                                            "question" to currentActivityQuestion,
                                            "answer" to mapOf(
                                                currentUserEmail to textFieldValue,
                                                friendEmail to friendAnswer?.get(friendEmail)
                                            )
                                        )

                                        // Update database
                                        if (currentUserEmail != null) {
                                            FirestoreQueries.updateCurrentActivityData(
                                                currentUserEmail!!,
                                                friendEmail,
                                                updates
                                            )
                                        }

                                        // Update view model
                                        if (currentUserEmail != null) {
                                            viewModel.checkData(
                                                currentUserEmail!!,
                                                friendEmail
                                            )
                                        }

                                        // Wait for view model check to complete
                                        while (isViewModelLoading) {
                                            delay(100)
                                        }

                                        // Add notification document to friend's notification collection
                                        FirestoreQueries.addNotification(
                                            Notification(NotificationType.ACTIVITY,"$currentUserEmail has submitted their answer!"),
                                            friendEmail
                                        )

                                        // Reload composable
                                        navController.navigate("ActivityScreen/$friendEmail")
                                    }
                                }
                            },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text(text = "Submit Answer")
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
fun PreviewHypotheticalActivity() {
    IceBreakerTheme {
        val navController = rememberNavController()
        val coroutineScope = rememberCoroutineScope()
        val viewModel = remember { QuestionBasedActivityViewModel() }
        val friendEmail = "example@email.com"
        HypotheticalActivity(navController, coroutineScope, viewModel, friendEmail)
    }
}