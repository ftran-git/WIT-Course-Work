package com.example.seniorproject_icebreaker.ui.screen.activity.game

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
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
import com.example.seniorproject_icebreaker.data.model.TicTacToeMark
import com.example.seniorproject_icebreaker.ui.theme.AppPadding
import com.example.seniorproject_icebreaker.ui.theme.Colors
import com.example.seniorproject_icebreaker.ui.theme.IceBreakerTheme
import com.example.seniorproject_icebreaker.ui.theme.Typography
import com.example.seniorproject_icebreaker.ui.viewmodel.TicTacToeViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TicTacToeActivity(
    navController: NavController,
    coroutineScope: CoroutineScope,
    viewModel: TicTacToeViewModel,
    friendEmail: String
) {
    // Define to interact with Firebase Authentication
    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    // Surface to contain elements
    Surface {
        // Get the system's dark mode state
        val isDarkTheme = isSystemInDarkTheme()

        // To hold states and values
        var currentUserEmail by remember { mutableStateOf<String?>(null) }
        var currentPlayerMark by remember { mutableStateOf<TicTacToeMark?>(null) }
        var opponentPlayerMark by remember { mutableStateOf<TicTacToeMark?>(null) }
        var selectedBox by remember { mutableStateOf<Pair<Int, Int>?>(null) }

        // Observe the state from the ViewModel
        val isViewModelLoading by viewModel.isLoading.collectAsState()
        val currentActivityName by viewModel.name.collectAsState()
        val currentActivityStartDate by viewModel.startDate.collectAsState()
        val players by viewModel.players.collectAsState()
        val boxStates by viewModel.boxStates.collectAsState()
        val turn by viewModel.turn.collectAsState()
        val winner by viewModel.winner.collectAsState()

        // LaunchedEffect to perform asynchronous operation
        LaunchedEffect(Unit) {
            // Grab current user's email
            currentUserEmail = auth.currentUser?.email

            // Update view model
            viewModel.checkData(currentUserEmail!!, friendEmail)
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
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
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
                        text = "Tic-Tac-Toe",
                        style = Typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Determine current player and opponent's mark
                    if (currentUserEmail == players?.get("playerOne")) {
                        currentPlayerMark = TicTacToeMark.X
                        opponentPlayerMark = TicTacToeMark.O
                    } else {
                        currentPlayerMark = TicTacToeMark.O
                        opponentPlayerMark = TicTacToeMark.X
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = if (currentPlayerMark == TicTacToeMark.X) {
                                R.drawable.baseline_clear_24
                            } else {
                                R.drawable.outline_circle_24
                            }
                            ),
                            contentDescription = "Current Player",
                            modifier = Modifier.size(24.dp),
                        )
                        Text(text = "You", style = Typography.titleLarge, fontWeight = FontWeight.Bold)

                        Spacer(modifier = Modifier.width(16.dp))

                        Icon(
                            imageVector = ImageVector.vectorResource(id = if (opponentPlayerMark == TicTacToeMark.X) {
                                R.drawable.baseline_clear_24
                            } else {
                                R.drawable.outline_circle_24
                            }
                            ),
                            contentDescription = "Opponent Player",
                            modifier = Modifier.size(24.dp),
                        )
                        Text(text = "Opponent", style = Typography.titleLarge, fontWeight = FontWeight.Bold)
                    }

                    if (winner?.isNotEmpty() == true) {
                        // Load Tic-Tac-Toe board
                        boxStates?.let {
                            TicTacToeBoard(
                                playersTurn = false,
                                boxStates = it,
                                mark = currentPlayerMark!!,
                                onBoxClicked = { newSelectedBox ->
                                    selectedBox = newSelectedBox
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Check if a player won or if there was a draw
                        if (winner == "DRAW") {
                            Text(
                                text = "It is a draw!",
                                style = Typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        } else {
                            if (currentUserEmail == winner) {
                                Text(
                                    text = "You win!",
                                    style = Typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            } else {
                                Text(
                                    text = "You lose!",
                                    style = Typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        // Toggle proper view depending on which player's turn it is
                        turn?.let {
                            if (turn == currentUserEmail) {
                                // Load Tic-Tac-Toe board
                                boxStates?.let {
                                    TicTacToeBoard(
                                        playersTurn = true,
                                        boxStates = it,
                                        mark = currentPlayerMark!!,
                                        onBoxClicked = { newSelectedBox ->
                                            selectedBox = newSelectedBox // Update selectedPosition
                                        }
                                    )
                                }

                                Text(
                                    text = "Select a box!",
                                    style = Typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )

                                // Spacer to push the Confirm button to the bottom
                                Spacer(modifier = Modifier.weight(1f))

                                // Confirm button
                                Button(
                                    onClick = {
                                        coroutineScope.launch {
                                            // Copy boxStates
                                            val updatedBoxStates = boxStates?.toMutableMap()

                                            // Update selected box to new value according to their player mark
                                            if (currentPlayerMark == TicTacToeMark.X) {
                                                updatedBoxStates?.set(
                                                    selectedBox.toString(),
                                                    TicTacToeMark.X.value
                                                )
                                            } else {
                                                updatedBoxStates?.set(
                                                    selectedBox.toString(),
                                                    TicTacToeMark.O.value
                                                )
                                            }

                                            // Check win condition and update winner if the condition is met
                                            var winnerUpdate = ""
                                            updatedBoxStates?.let { states ->
                                                currentPlayerMark?.let { mark ->
                                                    when {
                                                        checkTicTacToeWinCondition(states, mark) -> winnerUpdate =
                                                            currentUserEmail.toString()
                                                        checkForTicTacToeDraw(states) -> winnerUpdate = "DRAW"
                                                    }
                                                }
                                            }

                                            // Define updates which includes modified boxStates and turn
                                            val updates = mapOf(
                                                "name" to currentActivityName,
                                                "startDate" to currentActivityStartDate as Timestamp,
                                                "players" to players,
                                                "boxStates" to updatedBoxStates,
                                                "turn" to friendEmail,
                                                "winner" to winnerUpdate
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
                                            when (winnerUpdate) {
                                                currentUserEmail -> {
                                                    FirestoreQueries.addNotification(
                                                        Notification(NotificationType.ACTIVITY,"$currentUserEmail has won the game!"),
                                                        friendEmail
                                                    )
                                                }
                                                "DRAW" -> {
                                                    FirestoreQueries.addNotification(
                                                        Notification(NotificationType.ACTIVITY,"$currentUserEmail has drew the game!"),
                                                        friendEmail
                                                    )
                                                }
                                                else -> {
                                                    FirestoreQueries.addNotification(
                                                        Notification(NotificationType.ACTIVITY,"$currentUserEmail has made a move!"),
                                                        friendEmail
                                                    )
                                                }
                                            }

                                            // Reload composable
                                            navController.navigate("ActivityScreen/$friendEmail")
                                        }
                                    },
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                ) {
                                    Text(text = "Confirm Move")
                                }
                            } else {
                                // Load Tic-Tac-Toe board
                                boxStates?.let {
                                    TicTacToeBoard(
                                        playersTurn = false,
                                        boxStates = it,
                                        mark = currentPlayerMark!!,
                                        onBoxClicked = { newSelectedBox ->
                                            selectedBox = newSelectedBox // Update selectedPosition
                                        }
                                    )
                                }

                                Text(
                                    text = "Waiting for opponent's turn!",
                                    style = Typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
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
fun PreviewTicTacToeActivity() {
    IceBreakerTheme {
        val navController = rememberNavController()
        val coroutineScope = rememberCoroutineScope()
        val viewModel = remember { TicTacToeViewModel() }
        val friendEmail = "example@email.com"
        TicTacToeActivity(navController, coroutineScope, viewModel, friendEmail)
    }
}

@Composable
fun TicTacToeBoard(
    playersTurn: Boolean,
    boxStates: Map<String, String>,
    mark: TicTacToeMark,
    onBoxClicked: (Pair<Int, Int>?) -> Unit,
) {
    // Grab boxes that can be selected
    val validBoxes = boxStates.filter { it.value.isEmpty() }.map { it.key }

    // Grab boxes that have already been selected
    val invalidBoxes = boxStates.filter { it.value.isNotEmpty() }.map { it.key }

    // State to track the selected box
    var selectedBox by remember { mutableStateOf<Pair<Int, Int>?>(null) }

    Box(
        modifier = Modifier
            .size(250.dp)
            .background(if (isSystemInDarkTheme()) Colors.DarkSchemePolarBlue else Colors.LightSchemePolarBlue)
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            for (row in 0 until 3) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (col in 0 until 3) {
                        val coordinates = (row to col).toString()
                        val isClickable = playersTurn && validBoxes.contains(coordinates)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .border(width = 1.dp, color = Color.White)
                                .background(
                                    if (isSystemInDarkTheme()) {
                                        Colors.DarkSchemePolarBlue
                                    } else {
                                        Colors.LightSchemePolarBlue
                                    }
                                )
                                .clickable(
                                    enabled = isClickable,
                                    onClick = {
                                        // Logic for selecting and unselecting boxes
                                        if (selectedBox == row to col) {
                                            selectedBox = null
                                            onBoxClicked(null)
                                        } else {
                                            selectedBox = row to col
                                            onBoxClicked(row to col)
                                        }
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            // Display icons for boxes that have already been selected
                            if (invalidBoxes.contains(coordinates)) {
                                if (boxStates[coordinates] == TicTacToeMark.X.value) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_clear_24),
                                        contentDescription = TicTacToeMark.X.value,
                                        modifier = Modifier.size(24.dp)
                                    )
                                } else if (boxStates[coordinates] == TicTacToeMark.O.value) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.outline_circle_24),
                                        contentDescription = TicTacToeMark.O.value,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            } else if (row to col == selectedBox) {
                                // Display proper icon for currently selected box
                                if (mark == TicTacToeMark.X) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_clear_24),
                                        contentDescription = mark.value,
                                        modifier = Modifier.size(24.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.outline_circle_24),
                                        contentDescription = mark.value,
                                        modifier = Modifier.size(24.dp)
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

private fun checkTicTacToeWinCondition(board: Map<String, String>, mark: TicTacToeMark): Boolean {
    val winningCombinations = listOf(
        // Rows
        listOf("(0, 0)", "(0, 1)", "(0, 2)"),
        listOf("(1, 0)", "(1, 1)", "(1, 2)"),
        listOf("(2, 0)", "(2, 1)", "(2, 2)"),
        // Columns
        listOf("(0, 0)", "(1, 0)", "(2, 0)"),
        listOf("(0, 1)", "(1, 1)", "(2, 1)"),
        listOf("(0, 2)", "(1, 2)", "(2, 2)"),
        // Diagonals
        listOf("(0, 0)", "(1, 1)", "(2, 2)"),
        listOf("(0, 2)", "(1, 1)", "(2, 0)")
    )

    return winningCombinations.any { combination ->
        combination.all { board[it] == mark.value }
    }
}

private fun checkForTicTacToeDraw(boxStates: Map<String, String>): Boolean {
    return boxStates.values.all { it.isNotEmpty() }
}