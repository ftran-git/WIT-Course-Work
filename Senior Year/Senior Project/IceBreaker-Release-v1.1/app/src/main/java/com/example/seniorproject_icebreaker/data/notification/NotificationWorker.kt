package com.example.seniorproject_icebreaker.data.notification

/* FCM IS USED INSTEAD FOR PUSH NOTIFICATIONS,
KEEPING CODE FOR FUTURE REFERENCE IF NEEDED */

//
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.content.Context
//import android.os.Build
//import androidx.core.app.NotificationCompat
//import androidx.work.CoroutineWorker
//import androidx.work.WorkerParameters
//import com.example.seniorproject_icebreaker.R
//import com.example.seniorproject_icebreaker.data.firestore.FirestoreQueries
//import com.example.seniorproject_icebreaker.data.model.FriendStatus
//import com.example.seniorproject_icebreaker.data.model.User
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//
//class NotificationWorker(context: Context, params: WorkerParameters) :
//    CoroutineWorker(context, params) {
//    // Define to interact with Firebase Authentication
//    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
//    override suspend fun doWork(): Result {
//        // Get user's sign in status
//        val isUserSignedIn = checkUserSignInStatus()
//
//        // Perform the task if the user is signed in
//        return if (isUserSignedIn) {
//            // Retrieve the currentUserEmail from the input data
//            val currentUserEmail = inputData.getString("currentUserEmail") ?: return Result.failure()
//
//            // Get list of all friends by searching current user's friend collection
//            val querySnapshot = withContext(Dispatchers.IO) {
//                FirestoreQueries.searchFriendsCollection(currentUserEmail)
//            }
//
//            // Process result into list of user data objects
//            val userList = mutableListOf<User>()
//            querySnapshot?.let { snapshot ->
//                for (document in snapshot.documents) {
//                    // Extract data from query snapshot
//                    val data = document.data
//                    val email = data?.get("email") as? String
//                    if (email != null) {
//                        // Assuming User class has an 'email' field
//                        userList.add(User(email = email, FriendStatus.FRIEND))
//                    }
//                }
//            }
//
//            // Calculate time since last sent message for each friend
//            for (friend in userList) {
//                // Get duration and convert from milliseconds to hours
//                val duration = FirestoreQueries.calculateTimeSinceLastSentMessage(currentUserEmail, friend.email)
//                val hours = duration?.let { it / (1000 * 60 * 60) }
//
//                // Check if it has been more than 24 hours
//                if (hours != null) {
//                    if (hours > 24) {
//                        // Send notification
//                        sendDailyReminderNotification(friend.email)
//                    } else {
//                        // Do nothing
//                    }
//                }
//            }
//
//            Result.success()
//        } else {
//            // Do nothing
//            Result.failure()
//        }
//    }
//
//    private fun checkUserSignInStatus(): Boolean {
//        val currentUser: FirebaseUser? = auth.currentUser
//        return currentUser != null
//    }
//
//    private fun sendDailyReminderNotification(friendEmail: String) {
//        // Grab notification manager
//        val notificationManager =
//            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        // Create the NotificationChannel
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name: CharSequence = "Daily Reminder Notification"
//            val description = "Channel for daily reminder notifications"
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel(DAILY_REMINDER_CHANNEL_ID, name, importance)
//            channel.description = description
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        // Generate a unique notification ID using the current timestamp
//        val uniqueNotificationId = System.currentTimeMillis().toInt()
//
//        // Build the notification
//        val builder: NotificationCompat.Builder =
//            NotificationCompat.Builder(applicationContext, DAILY_REMINDER_CHANNEL_ID)
//                .setSmallIcon(R.drawable.baseline_notifications_24) // Ensure you have this icon in your drawable resources
//                .setContentTitle("Daily Reminder")
//                .setContentText("This is your daily notification to text $friendEmail!")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setAutoCancel(true)
//
//        // Show the notification
//        notificationManager.notify(uniqueNotificationId, builder.build())
//    }
//
//    companion object {
//        private const val DAILY_REMINDER_CHANNEL_ID = "Daily_Reminder_Channel_ID"
//    }
//}