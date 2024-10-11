package com.example.seniorproject_icebreaker.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.seniorproject_icebreaker.R
import com.example.seniorproject_icebreaker.data.firestore.FirestoreQueries
import com.example.seniorproject_icebreaker.ui.theme.AppPadding
import com.example.seniorproject_icebreaker.ui.theme.IceBreakerTheme
import com.example.seniorproject_icebreaker.ui.theme.Typography
import com.example.seniorproject_icebreaker.ui.viewmodel.ChatScreenViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@Composable
fun ChatScreen(
    navController: NavController, coroutineScope: CoroutineScope, viewModel: ChatScreenViewModel,
    friendEmail: String
) {
    // Define to interact with Firebase Authentication
    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    // Define to store messages and new message text
    val messagesState = viewModel.messages.observeAsState(initial = emptyList())
    val newMessage = remember { mutableStateOf("") }

    // Fetch and listen for messages each time the screen is composed
    LaunchedEffect(Unit) {
        val currentUserEmail = auth.currentUser?.email
        if (currentUserEmail != null) {
            viewModel.fetchMessages(currentUserEmail, friendEmail)
            delay(1000) // Delay is needed if chat document needs to be created while fetching messages
            viewModel.listenForMessages(currentUserEmail, friendEmail)
        }
    }

    // LazyListState to control the scroll position of the LazyColumn
    val listState = rememberLazyListState()

    // Scroll to the bottom whenever messagesState changes
    LaunchedEffect(messagesState.value) {
        if (messagesState.value.isNotEmpty()) {
            listState.animateScrollToItem(messagesState.value.size - 1)
        }
    }

    // Surface to contain elements
    Surface {
        // Column to arrange elements vertically
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(AppPadding.large)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppPadding.large),
                verticalAlignment = Alignment.CenterVertically,
            ) {
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

                Text(
                    text = friendEmail,
                    style = Typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
            }

            // LazyColumn to display messages
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f)
            ) {
                items(messagesState.value) { message ->
                    val currentUserEmail = auth.currentUser?.email
                    if (currentUserEmail != null) {
                        val senderId = message["senderId"] as? String
                        val messageText = message["text"] as? String
                        val timestamp = message["timestamp"] as? Timestamp

                        if (senderId != null && messageText != null && timestamp != null) {
                            MessageItem(
                                currentUserEmail,
                                senderId = senderId,
                                messageText = messageText,
                                timestamp = timestamp
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.padding(AppPadding.large),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value = newMessage.value,
                    onValueChange = { newMessage.value = it },
                    label = { Text("Message") },
                    modifier = Modifier
                        .weight(1f),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.None
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            coroutineScope.launch {
                                val currentUserEmail = auth.currentUser?.email
                                if (currentUserEmail != null) {
                                    FirestoreQueries.sendMessage(
                                        currentUserEmail,
                                        friendEmail,
                                        newMessage.value
                                    )
                                    newMessage.value = ""
                                }
                            }
                        }
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            val currentUserEmail = auth.currentUser?.email
                            if (currentUserEmail != null) {
                                FirestoreQueries.sendMessage(
                                    currentUserEmail,
                                    friendEmail,
                                    newMessage.value
                                )
                                newMessage.value = ""
                            }
                        }
                    }
                )
                {
                    Text(text = "Send")
                }
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewChatScreen() {
    IceBreakerTheme {
        val navController = rememberNavController()
        val coroutineScope = rememberCoroutineScope()
        val chatScreenViewModel = remember { ChatScreenViewModel() }
        val friendEmail = "example@email.com"
        ChatScreen(navController, coroutineScope, chatScreenViewModel, friendEmail)
    }
}

@Composable
fun MessageItem(
    currentUserId: String,
    senderId: String,
    messageText: String,
    timestamp: Timestamp,
    modifier: Modifier = Modifier
) {
    // Parse timestamp object to display
    val localTimeZone = TimeZone.getDefault()
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    timeFormat.timeZone = localTimeZone
    val time = remember(timestamp) {
        val date = timestamp.toDate()
        timeFormat.format(date)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .align(if (senderId == currentUserId) Alignment.TopEnd else Alignment.TopStart)
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(
                        if (senderId == currentUserId) Color.Cyan else Color.LightGray,
                        RoundedCornerShape(16.dp)
                    )
                    .padding(8.dp)
            ) {
                Text(
                    text = messageText,
                    style = Typography.bodyLarge,
                    modifier = Modifier.padding(
                        vertical = 4.dp,
                        horizontal = 8.dp
                    ) // Decrease the padding here
                )
                Text(
                    text = time,
                    style = Typography.labelSmall,
                    modifier = Modifier.padding(
                        vertical = 4.dp,
                        horizontal = 8.dp
                    ) // Decrease the padding here
                )
            }
        }
    }
}

