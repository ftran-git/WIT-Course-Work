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
import com.example.seniorproject_icebreaker.data.model.FourInARowColor
import com.example.seniorproject_icebreaker.data.model.Notification
import com.example.seniorproject_icebreaker.data.model.NotificationType
import com.example.seniorproject_icebreaker.ui.theme.AppPadding
import com.example.seniorproject_icebreaker.ui.theme.Colors
import com.example.seniorproject_icebreaker.ui.theme.IceBreakerTheme
import com.example.seniorproject_icebreaker.ui.theme.Typography
import com.example.seniorproject_icebreaker.ui.viewmodel.FourInARowViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FourInARowActivity(
    navController: NavController,
    coroutineScope: CoroutineScope,
    viewModel: FourInARowViewModel,
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
        var currentPlayerColor by remember { mutableStateOf<FourInARowColor?>(null) }
        var opponentPlayerColor by remember { mutableStateOf<FourInARowColor?>(null) }
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
                        text = "Four in a Row",
                        style = Typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Determine current player and opponent's color
                    if (currentUserEmail == players?.get("playerOne")) {
                        currentPlayerColor = FourInARowColor.RED
                        opponentPlayerColor = FourInARowColor.YELLOW
                    } else {
                        currentPlayerColor = FourInARowColor.YELLOW
                        opponentPlayerColor = FourInARowColor.RED
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.four_in_a_row_game_piece),
                            contentDescription = "Current Player",
                            modifier = Modifier.size(24.dp),
                            tint = getColorFromString(currentPlayerColor!!.value)
                        )
                        Text(
                            text = "You",
                            style = Typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.four_in_a_row_game_piece),
                            contentDescription = "Opponent Player",
                            modifier = Modifier.size(24.dp),
                            tint = getColorFromString(opponentPlayerColor!!.value)
                        )
                        Text(
                            text = "Opponent",
                            style = Typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (winner?.isNotEmpty() == true) {
                        // Load Four in a Row board
                        boxStates?.let {
                            FourInARowBoard(
                                playersTurn = false,
                                boxStates = it,
                                color = currentPlayerColor!!,
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
                                // Load Four in a Row board
                                boxStates?.let {
                                    FourInARowBoard(
                                        playersTurn = true,
                                        boxStates = it,
                                        color = currentPlayerColor!!,
                                        onBoxClicked = { newSelectedBox ->
                                            selectedBox = newSelectedBox
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
                                            if (currentPlayerColor == FourInARowColor.RED) {
                                                updatedBoxStates?.set(
                                                    selectedBox.toString(),
                                                    FourInARowColor.RED.value
                                                )
                                            } else {
                                                updatedBoxStates?.set(
                                                    selectedBox.toString(),
                                                    FourInARowColor.YELLOW.value
                                                )
                                            }

                                            // Check win condition and update winner if the condition is met
                                            var winnerUpdate = ""
                                            updatedBoxStates?.let { states ->
                                                currentPlayerColor?.let { color ->
                                                    when {
                                                        checkFourInARowWinCondition(
                                                            states,
                                                            color
                                                        ) -> winnerUpdate =
                                                            currentUserEmail.toString()

                                                        checkForFourInARowDraw(states) -> winnerUpdate =
                                                            "DRAW"
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
                                // Load Four in a Row board
                                boxStates?.let {
                                    FourInARowBoard(
                                        playersTurn = false,
                                        boxStates = it,
                                        color = currentPlayerColor!!,
                                        onBoxClicked = { newSelectedBox ->
                                            selectedBox = newSelectedBox
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
fun PreviewFourInARowActivity() {
    IceBreakerTheme {
        val navController = rememberNavController()
        val coroutineScope = rememberCoroutineScope()
        val viewModel = remember { FourInARowViewModel() }
        val friendEmail = "example@email.com"
        FourInARowActivity(navController, coroutineScope, viewModel, friendEmail)
    }
}

@Composable
fun FourInARowBoard(
    playersTurn: Boolean,
    boxStates: Map<String, String>,
    color: FourInARowColor,
    onBoxClicked: (Pair<Int, Int>?) -> Unit,
) {
    // Grab boxes that can be selected
    val validBoxes = boxStates.filter { it.value.isEmpty() }.map { it.key }

    // Grab boxes that have already been selected
    val invalidBoxes = boxStates.filter { it.value.isNotEmpty() }.map { it.key }

    // State to track the selected box
    var selectedBox by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    // State to track which column has been selected
    val selectedColumn = remember { mutableStateOf<Int?>(null) }

    Box(
        modifier = Modifier
            .aspectRatio(7f / 6f)
            .size(250.dp)
            .background(if (isSystemInDarkTheme()) Colors.DarkSchemePolarBlue else Colors.LightSchemePolarBlue)
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            // Render the board
            for (row in 0 until 6) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (col in 0 until 7) {
                        val coordinates = (row to col).toString()
                        val isClickable = playersTurn && validBoxes.contains(coordinates)

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .border(width = 1.dp, color = Color.White)
                                .background(
                                    if (col == selectedColumn.value) {
                                        Colors.LightGray
                                    } else {
                                        if (isSystemInDarkTheme()) {
                                            Colors.DarkSchemePolarBlue
                                        } else {
                                            Colors.LightSchemePolarBlue
                                        }
                                    }
                                )
                                .clickable(
                                    enabled = isClickable,
                                    onClick = {
                                        // Logic for selecting and unselecting boxes
                                        if (selectedBox == row to col) {
                                            selectedBox = null
                                            onBoxClicked(null)
                                            selectedColumn.value = null
                                        } else {
                                            // Bottom most empty box from a given column will be selected
                                            selectedBox = row to col
                                            selectedBox = findBottomMostEmptyBox(selectedBox!!.second, boxStates)
                                            onBoxClicked(selectedBox)
                                            selectedColumn.value = col
                                        }
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            // Display icons for boxes that have already been selected
                            if (invalidBoxes.contains(coordinates)) {
                                if (boxStates[coordinates] == FourInARowColor.RED.value) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.four_in_a_row_game_piece),
                                        contentDescription = FourInARowColor.RED.value,
                                        modifier = Modifier.size(24.dp),
                                        tint = Color.Red
                                    )
                                } else if (boxStates[coordinates] == FourInARowColor.YELLOW.value) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.four_in_a_row_game_piece),
                                        contentDescription = FourInARowColor.YELLOW.value,
                                        modifier = Modifier.size(24.dp),
                                        tint = Color.Yellow
                                    )
                                }
                            } else if (row to col == selectedBox) {
                                // Display proper icon for currently selected box
                                if (color == FourInARowColor.RED) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.four_in_a_row_game_piece),
                                        contentDescription = color.value,
                                        modifier = Modifier.size(24.dp),
                                        tint = Color.Red
                                    )
                                } else {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.four_in_a_row_game_piece),
                                        contentDescription = color.value,
                                        modifier = Modifier.size(24.dp),
                                        tint = Color.Yellow
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

fun getColorFromString(colorString: String?): Color {
    return when (colorString) {
        "Red" -> Color.Red
        "Yellow" -> Color.Yellow
        else -> Color.Gray // Default color if no match is found
    }
}

fun findBottomMostEmptyBox(col: Int, boxStates: Map<String, String>): Pair<Int, Int>? {
    for (row in 5 downTo 0) { // Starting from the bottom row
        val coordinates = (row to col).toString()
        if (boxStates[coordinates].isNullOrEmpty()) {
            return row to col
        }
    }
    return null
}

private fun checkFourInARowWinCondition(
    board: Map<String, String>,
    color: FourInARowColor
): Boolean {
    val winningCombinations = listOf(
        // Horizontal
        listOf("(0, 0)", "(0, 1)", "(0, 2)", "(0, 3)"),
        listOf("(0, 1)", "(0, 2)", "(0, 3)", "(0, 4)"),
        listOf("(0, 2)", "(0, 3)", "(0, 4)", "(0, 5)"),
        listOf("(0, 3)", "(0, 4)", "(0, 5)", "(0, 6)"),
        listOf("(1, 0)", "(1, 1)", "(1, 2)", "(1, 3)"),
        listOf("(1, 1)", "(1, 2)", "(1, 3)", "(1, 4)"),
        listOf("(1, 2)", "(1, 3)", "(1, 4)", "(1, 5)"),
        listOf("(1, 3)", "(1, 4)", "(1, 5)", "(1, 6)"),
        listOf("(2, 0)", "(2, 1)", "(2, 2)", "(2, 3)"),
        listOf("(2, 1)", "(2, 2)", "(2, 3)", "(2, 4)"),
        listOf("(2, 2)", "(2, 3)", "(2, 4)", "(2, 5)"),
        listOf("(2, 3)", "(2, 4)", "(2, 5)", "(2, 6)"),
        listOf("(3, 0)", "(3, 1)", "(3, 2)", "(3, 3)"),
        listOf("(3, 1)", "(3, 2)", "(3, 3)", "(3, 4)"),
        listOf("(3, 2)", "(3, 3)", "(3, 4)", "(3, 5)"),
        listOf("(3, 3)", "(3, 4)", "(3, 5)", "(3, 6)"),
        listOf("(4, 0)", "(4, 1)", "(4, 2)", "(4, 3)"),
        listOf("(4, 1)", "(4, 2)", "(4, 3)", "(4, 4)"),
        listOf("(4, 2)", "(4, 3)", "(4, 4)", "(4, 5)"),
        listOf("(4, 3)", "(4, 4)", "(4, 5)", "(4, 6)"),
        listOf("(5, 0)", "(5, 1)", "(5, 2)", "(5, 3)"),
        listOf("(5, 1)", "(5, 2)", "(5, 3)", "(5, 4)"),
        listOf("(5, 2)", "(5, 3)", "(5, 4)", "(5, 5)"),
        listOf("(5, 3)", "(5, 4)", "(5, 5)", "(5, 6)"),
        // Vertical
        listOf("(0, 0)", "(1, 0)", "(2, 0)", "(3, 0)"),
        listOf("(1, 0)", "(2, 0)", "(3, 0)", "(4, 0)"),
        listOf("(2, 0)", "(3, 0)", "(4, 0)", "(5, 0)"),
        listOf("(0, 1)", "(1, 1)", "(2, 1)", "(3, 1)"),
        listOf("(1, 1)", "(2, 1)", "(3, 1)", "(4, 1)"),
        listOf("(2, 1)", "(3, 1)", "(4, 1)", "(5, 1)"),
        listOf("(0, 2)", "(1, 2)", "(2, 2)", "(3, 2)"),
        listOf("(1, 2)", "(2, 2)", "(3, 2)", "(4, 2)"),
        listOf("(2, 2)", "(3, 2)", "(4, 2)", "(5, 2)"),
        listOf("(0, 3)", "(1, 3)", "(2, 3)", "(3, 3)"),
        listOf("(1, 3)", "(2, 3)", "(3, 3)", "(4, 3)"),
        listOf("(2, 3)", "(3, 3)", "(4, 3)", "(5, 3)"),
        listOf("(0, 4)", "(1, 4)", "(2, 4)", "(3, 4)"),
        listOf("(1, 4)", "(2, 4)", "(3, 4)", "(4, 4)"),
        listOf("(2, 4)", "(3, 4)", "(4, 4)", "(5, 4)"),
        listOf("(0, 5)", "(1, 5)", "(2, 5)", "(3, 5)"),
        listOf("(1, 5)", "(2, 5)", "(3, 5)", "(4, 5)"),
        listOf("(2, 5)", "(3, 5)", "(4, 5)", "(5, 5)"),
        listOf("(0, 6)", "(1, 6)", "(2, 6)", "(3, 6)"),
        listOf("(1, 6)", "(2, 6)", "(3, 6)", "(4, 6)"),
        listOf("(2, 6)", "(3, 6)", "(4, 6)", "(5, 6)"),
        // Diagonal (descending)
        listOf("(0, 0)", "(1, 1)", "(2, 2)", "(3, 3)"),
        listOf("(1, 1)", "(2, 2)", "(3, 3)", "(4, 4)"),
        listOf("(2, 2)", "(3, 3)", "(4, 4)", "(5, 5)"),
        listOf("(0, 1)", "(1, 2)", "(2, 3)", "(3, 4)"),
        listOf("(1, 2)", "(2, 3)", "(3, 4)", "(4, 5)"),
        listOf("(2, 3)", "(3, 4)", "(4, 5)", "(5, 6)"),
        listOf("(0, 2)", "(1, 3)", "(2, 4)", "(3, 5)"),
        listOf("(1, 3)", "(2, 4)", "(3, 5)", "(4, 6)"),
        listOf("(0, 3)", "(1, 4)", "(2, 5)", "(3, 6)"),
        listOf("(1, 0)", "(2, 1)", "(3, 2)", "(4, 3)"),
        listOf("(2, 1)", "(3, 2)", "(4, 3)", "(5, 4)"),
        listOf("(0, 4)", "(1, 3)", "(2, 2)", "(3, 1)"),
        listOf("(1, 3)", "(2, 2)", "(3, 1)", "(4, 0)"),
        listOf("(2, 2)", "(3, 1)", "(4, 0)", "(5, 6)"),
        listOf("(0, 5)", "(1, 4)", "(2, 3)", "(3, 2)"),
        listOf("(1, 4)", "(2, 3)", "(3, 2)", "(4, 1)"),
        listOf("(2, 3)", "(3, 2)", "(4, 1)", "(5, 0)"),
        listOf("(0, 6)", "(1, 5)", "(2, 4)", "(3, 3)"),
        listOf("(1, 5)", "(2, 4)", "(3, 3)", "(4, 2)"),
        listOf("(2, 4)", "(3, 3)", "(4, 2)", "(5, 1)"),
        // Diagonal (ascending)
        listOf("(3, 0)", "(2, 1)", "(1, 2)", "(0, 3)"),
        listOf("(4, 0)", "(3, 1)", "(2, 2)", "(1, 3)"),
        listOf("(5, 0)", "(4, 1)", "(3, 2)", "(2, 3)"),
        listOf("(3, 1)", "(2, 2)", "(1, 3)", "(0, 4)"),
        listOf("(4, 1)", "(3, 2)", "(2, 3)", "(1, 4)"),
        listOf("(5, 1)", "(4, 2)", "(3, 3)", "(2, 4)"),
        listOf("(3, 2)", "(2, 3)", "(1, 4)", "(0, 5)"),
        listOf("(4, 2)", "(3, 3)", "(2, 4)", "(1, 5)"),
        listOf("(5, 2)", "(4, 3)", "(3, 4)", "(2, 5)"),
        listOf("(3, 3)", "(2, 4)", "(1, 5)", "(0, 6)"),
        listOf("(4, 3)", "(3, 4)", "(2, 5)", "(1, 6)"),
        listOf("(4, 0)", "(3, 1)", "(2, 2)", "(1, 3)"),
        listOf("(5, 0)", "(4, 1)", "(3, 2)", "(2, 3)"),
        listOf("(5, 0)", "(4, 1)", "(3, 2)", "(2, 3)"),
        listOf("(5, 1)", "(4, 2)", "(3, 3)", "(2, 4)"),
        listOf("(5, 1)", "(4, 2)", "(3, 3)", "(2, 4)"),
        listOf("(5, 2)", "(4, 3)", "(3, 4)", "(2, 5)"),
        listOf("(5, 2)", "(4, 3)", "(3, 4)", "(2, 5)"),
        listOf("(5, 3)", "(4, 4)", "(3, 5)", "(2, 6)")
    )

    return winningCombinations.any { combination ->
        combination.all { board[it] == color.value }
    }
}

private fun checkForFourInARowDraw(boxStates: Map<String, String>): Boolean {
    return boxStates.values.all { it.isNotEmpty() }
}
