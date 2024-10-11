package com.example.seniorproject_icebreaker.ui.screen.activity

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.seniorproject_icebreaker.data.firestore.FirestoreQueries
import com.example.seniorproject_icebreaker.data.model.Activity
import com.example.seniorproject_icebreaker.data.model.ActivityName
import com.example.seniorproject_icebreaker.data.model.ActivityType
import com.example.seniorproject_icebreaker.data.model.GameActivity
import com.example.seniorproject_icebreaker.data.model.QuestionActivity
import com.example.seniorproject_icebreaker.data.model.WouldYouRatherQuestion
import com.example.seniorproject_icebreaker.data.model.toMap
import com.example.seniorproject_icebreaker.ui.theme.IceBreakerTheme
import com.example.seniorproject_icebreaker.ui.viewmodel.QuestionBasedActivityViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import java.util.Date
import java.util.concurrent.TimeUnit

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ActivityScreen(
    navController: NavController,
    friendEmail: String
) {
    // Define to interact with Firebase Authentication
    val auth by remember { mutableStateOf(FirebaseAuth.getInstance()) }

    // Mutable variables
    var currentUserEmail by remember { mutableStateOf<String?>(null) }
    var currentActivityName by remember { mutableStateOf<String?>(null) }
    var currentActivityStartDate by remember { mutableStateOf<Timestamp?>(null) }

    // Process activity selection
    LaunchedEffect(Unit) {
        // Grab current user email
        currentUserEmail = auth.currentUser?.email

        // Grab current activity name from database
        var data =
            currentUserEmail?.let {
                FirestoreQueries.retrieveCurrentActivityData(
                    it,
                    friendEmail,
                    "name"
                )
            }
        if (data != null) {
            currentActivityName = data.toString()
        }

        // Grab current activity start date from database
        data =
            currentUserEmail?.let {
                FirestoreQueries.retrieveCurrentActivityData(
                    it,
                    friendEmail,
                    "startDate"
                )
            }
        if (data != null) {
            currentActivityStartDate = data as Timestamp
        }

        // Display activity based on current state
        when (currentActivityName) {
            null -> {
                // Get new activity
                val newActivity = getRandomActivity()

                // Define data updates
                val updates: MutableMap<String, Any> = mutableMapOf(
                    "name" to newActivity.activityName.toString(),
                    "startDate" to Timestamp.now(),
                )

                // Add question and answer key-field to data if activity is question type
                if (newActivity.activityType == ActivityType.QUESTION) {
                    // Get question
                    val question = getRandomQuestion(newActivity)

                    // Check for null
                    if (question != null) {
                        // Add data to map
                        updates["completed"] = mutableMapOf(
                            currentUserEmail to false,
                            friendEmail to false
                        )
                        if (newActivity.activityName.toString() == QuestionActivity.WOULD_YOU_RATHER.toString()) {
                            updates["question"] = question as Map<*, *>
                        } else {
                            updates["question"] = question as String

                        }
                        updates["answer"] = mutableMapOf(
                            currentUserEmail to "",
                            friendEmail to ""
                        )
                    }
                }

                // Setup database accordingly if activity is Tic-Tac-Toe game
                if (newActivity.activityName is ActivityName.Game && newActivity.activityName.gameActivity == GameActivity.TIC_TAC_TOE) {
                    updates["players"] = mutableMapOf(
                        "playerOne" to currentUserEmail,
                        "playerTwo" to friendEmail
                    )
                    updates["boxStates"] = mutableMapOf(
                        "(0, 0)" to "",
                        "(0, 1)" to "",
                        "(0, 2)" to "",
                        "(1, 0)" to "",
                        "(1, 1)" to "",
                        "(1, 2)" to "",
                        "(2, 0)" to "",
                        "(2, 1)" to "",
                        "(2, 2)" to ""
                    )
                    updates["turn"] = currentUserEmail as String
                    updates["winner"] = ""
                }

                // Setup database accordingly if activity is Four in a Row game
                if (newActivity.activityName is ActivityName.Game && newActivity.activityName.gameActivity == GameActivity.FOUR_IN_A_ROW) {
                    updates["players"] = mutableMapOf(
                        "playerOne" to currentUserEmail,
                        "playerTwo" to friendEmail
                    )
                    updates["boxStates"] = mutableMapOf(
                        "(0, 0)" to "",
                        "(0, 1)" to "",
                        "(0, 2)" to "",
                        "(0, 3)" to "",
                        "(0, 4)" to "",
                        "(0, 5)" to "",
                        "(0, 6)" to "",
                        "(1, 0)" to "",
                        "(1, 1)" to "",
                        "(1, 2)" to "",
                        "(1, 3)" to "",
                        "(1, 4)" to "",
                        "(1, 5)" to "",
                        "(1, 6)" to "",
                        "(2, 0)" to "",
                        "(2, 1)" to "",
                        "(2, 2)" to "",
                        "(2, 3)" to "",
                        "(2, 4)" to "",
                        "(2, 5)" to "",
                        "(2, 6)" to "",
                        "(3, 0)" to "",
                        "(3, 1)" to "",
                        "(3, 2)" to "",
                        "(3, 3)" to "",
                        "(3, 4)" to "",
                        "(3, 5)" to "",
                        "(3, 6)" to "",
                        "(4, 0)" to "",
                        "(4, 1)" to "",
                        "(4, 2)" to "",
                        "(4, 3)" to "",
                        "(4, 4)" to "",
                        "(4, 5)" to "",
                        "(4, 6)" to "",
                        "(5, 0)" to "",
                        "(5, 1)" to "",
                        "(5, 2)" to "",
                        "(5, 3)" to "",
                        "(5, 4)" to "",
                        "(5, 5)" to "",
                        "(5, 6)" to "",
                    )
                    updates["turn"] = currentUserEmail as String
                    updates["winner"] = ""
                }


                // Update database
                FirestoreQueries.updateCurrentActivityData(
                    currentUserEmail!!,
                    friendEmail,
                    updates
                )

                // Display UI for new selected activity
                loadActivityUI(
                    newActivity,
                    navController,
                    friendEmail
                )
            }

            else -> {
                // Check time since activity's start date
                if (has24HoursPassed(currentActivityStartDate!!)) {
                    Log.d("ActivityScreen", "24 hours has passed for this current activity!")

                    // Get a new activity that is not the same as the current
                    val newActivity = getRandomActivityExcluding(currentActivityName!!)

                    // Define data
                    val updates: MutableMap<String, Any> = mutableMapOf(
                        "name" to newActivity.activityName.toString(),
                        "startDate" to Timestamp.now(),
                    )

                    // Add question and answer key-field to data if activity is question type
                    if (newActivity.activityType == ActivityType.QUESTION) {
                        // Get question
                        val question = getRandomQuestion(newActivity)

                        // Check for null
                        if (question != null) {
                            // Add data to map
                            updates["completed"] = mutableMapOf(
                                currentUserEmail to false,
                                friendEmail to false
                            )
                            if (newActivity.activityName.toString() == QuestionActivity.WOULD_YOU_RATHER.toString()) {
                                updates["question"] = question as Map<*, *>
                            } else {
                                updates["question"] = question as String

                            }
                            updates["answer"] = mutableMapOf(
                                currentUserEmail to "",
                                friendEmail to ""
                            )
                        }
                    }

                    // Setup database accordingly if activity is Tic-Tac-Toe game
                    if (newActivity.activityName is ActivityName.Game && newActivity.activityName.gameActivity == GameActivity.TIC_TAC_TOE) {
                        updates["players"] = mutableMapOf(
                            "playerOne" to currentUserEmail,
                            "playerTwo" to friendEmail
                        )
                        updates["boxStates"] = mutableMapOf(
                            "(0, 0)" to "",
                            "(0, 1)" to "",
                            "(0, 2)" to "",
                            "(1, 0)" to "",
                            "(1, 1)" to "",
                            "(1, 2)" to "",
                            "(2, 0)" to "",
                            "(2, 1)" to "",
                            "(2, 2)" to ""
                        )
                        updates["turn"] = currentUserEmail as String
                        updates["winner"] = ""
                    }

                    // Setup database accordingly if activity is Four in a Row game
                    if (newActivity.activityName is ActivityName.Game && newActivity.activityName.gameActivity == GameActivity.FOUR_IN_A_ROW) {
                        updates["players"] = mutableMapOf(
                            "playerOne" to currentUserEmail,
                            "playerTwo" to friendEmail
                        )
                        updates["boxStates"] = mutableMapOf(
                            "(0, 0)" to "",
                            "(0, 1)" to "",
                            "(0, 2)" to "",
                            "(0, 3)" to "",
                            "(0, 4)" to "",
                            "(0, 5)" to "",
                            "(0, 6)" to "",
                            "(1, 0)" to "",
                            "(1, 1)" to "",
                            "(1, 2)" to "",
                            "(1, 3)" to "",
                            "(1, 4)" to "",
                            "(1, 5)" to "",
                            "(1, 6)" to "",
                            "(2, 0)" to "",
                            "(2, 1)" to "",
                            "(2, 2)" to "",
                            "(2, 3)" to "",
                            "(2, 4)" to "",
                            "(2, 5)" to "",
                            "(2, 6)" to "",
                            "(3, 0)" to "",
                            "(3, 1)" to "",
                            "(3, 2)" to "",
                            "(3, 3)" to "",
                            "(3, 4)" to "",
                            "(3, 5)" to "",
                            "(3, 6)" to "",
                            "(4, 0)" to "",
                            "(4, 1)" to "",
                            "(4, 2)" to "",
                            "(4, 3)" to "",
                            "(4, 4)" to "",
                            "(4, 5)" to "",
                            "(4, 6)" to "",
                            "(5, 0)" to "",
                            "(5, 1)" to "",
                            "(5, 2)" to "",
                            "(5, 3)" to "",
                            "(5, 4)" to "",
                            "(5, 5)" to "",
                            "(5, 6)" to "",
                        )
                        updates["turn"] = currentUserEmail as String
                        updates["winner"] = ""
                    }

                    // Update database
                    FirestoreQueries.updateCurrentActivityData(
                        currentUserEmail!!,
                        friendEmail,
                        updates
                    )

                    // Display UI for new selected activity
                    loadActivityUI(
                        newActivity,
                        navController,
                        friendEmail
                    )
                } else {
                    Log.d(
                        "ActivityScreen",
                        "24 hours has not passed for this current activity!"
                    )

                    // Display UI for same current activity
                    val activity = getActivityByName(currentActivityName!!)
                    loadActivityUI(
                        activity,
                        navController,
                        friendEmail
                    )
                }
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewActivityScreen() {
    IceBreakerTheme {
        val navController = rememberNavController()
        val chatScreenViewModel = remember { QuestionBasedActivityViewModel() }
        val friendEmail = "example@email.com"
        ActivityScreen(navController, friendEmail)
    }
}

fun has24HoursPassed(timestamp: Timestamp): Boolean {
    // Convert Firestore Timestamp to Date
    val timestampDate = timestamp.toDate()

    // Get current time
    val currentTime = Date()

    // Calculate the difference in milliseconds
    val diffInMillis = currentTime.time - timestampDate.time

    // Convert milliseconds to hours
    val diffInHours = TimeUnit.MILLISECONDS.toHours(diffInMillis)

    // Check if 24 hours have passed
    return diffInHours >= 24
}

fun getRandomActivityExcluding(excludedActivityName: String): Activity {
    val filteredActivities =
        Activity.allActivities.filter { it.activityName.toString() != excludedActivityName }
    if (filteredActivities.isEmpty()) {
        throw IllegalArgumentException("No activities available to select from")
    }
    return filteredActivities.random()
}

fun getRandomActivity(): Activity {
    val activities = Activity.allActivities
    if (activities.isEmpty()) {
        throw IllegalArgumentException("No activities available to select from")
    }
    return activities.random()
}

fun getActivityByName(activityName: String): Activity {
    return Activity.allActivities.first { it.activityName.toString() == activityName }
}

fun getRandomQuestion(activity: Activity): Any? {
    val questionActivity = activity.activityName as? ActivityName.Question
    return questionActivity?.let {
        when (it.questionActivity) {
            QuestionActivity.HYPOTHETICAL -> {
                QuestionActivity.HYPOTHETICAL.questions.random() as String
            }

            QuestionActivity.WOULD_YOU_RATHER -> {
                val wouldYouRatherQuestion =
                    QuestionActivity.WOULD_YOU_RATHER.questions.random() as WouldYouRatherQuestion
                wouldYouRatherQuestion.toMap()
            }
        }
    }
}

fun loadActivityUI(
    activity: Activity,
    navController: NavController,
    friendEmail: String
) {
    when (activity.activityType) {
        ActivityType.GAME -> {
            val gameActivity = activity.activityName as? ActivityName.Game
            gameActivity?.let {
                when (it.gameActivity) {
                    GameActivity.FOUR_IN_A_ROW -> navController.navigate("FourInARowActivity/$friendEmail")
                    GameActivity.TIC_TAC_TOE -> navController.navigate("TicTacToeActivity/$friendEmail")
                }
            }
        }

        ActivityType.QUESTION -> {
            val questionActivity = activity.activityName as? ActivityName.Question
            questionActivity?.let {
                // Logic to load proper screen
                when (it.questionActivity) {
                    QuestionActivity.HYPOTHETICAL -> navController.navigate("HypotheticalActivity/$friendEmail")
                    QuestionActivity.WOULD_YOU_RATHER -> navController.navigate("WouldYouRatherActivity/$friendEmail")
                }
            }
        }
    }
}

