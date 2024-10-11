package com.example.seniorproject_icebreaker.data.firestore

import android.util.Log
import androidx.constraintlayout.helper.widget.MotionEffect
import com.example.seniorproject_icebreaker.MainActivity
import com.example.seniorproject_icebreaker.data.model.Notification
import com.example.seniorproject_icebreaker.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

object FirestoreQueries {
    suspend fun createUsersDocument(newUserData: HashMap<String, String>) {
        try {
            // Get FirestoreInstance from MainActivity
            val db = MainActivity.FirestoreInstance.db

            // Create and add document to 'users' collection
            val documentReference = db.collection("users")
                .add(newUserData)
                .await()

            // Log the document ID
            Log.d(MotionEffect.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
        } catch (e: Exception) {
            Log.w("FirestoreQueries", "Error creating document: ${e.message}", e)
        }
    }

    suspend fun searchUsersCollection(email: String): QuerySnapshot? {
        return try {
            // Get FirestoreInstance from MainActivity
            val db = MainActivity.FirestoreInstance.db

            // Search database for specified user
            db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .await()
        } catch (e: Exception) {
            Log.w("FirestoreQueries", "Error searching for user with email $email: ${e.message}", e)
            null
        }
    }

    suspend fun updateUserData(email: String, field: String, value: String) {
        try {
            // Get FirestoreInstance from MainActivity
            val db = MainActivity.FirestoreInstance.db

            // Search database for specified user
            val querySnapshot = db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .await()

            // Check if user exists
            if (!querySnapshot.isEmpty) {
                // Get the document reference of the first matching user
                val documentReference = querySnapshot.documents[0].reference

                // Create a map to update the specified field
                val updateData = hashMapOf<String, Any>(
                    field to value
                )

                // Update the document with the new data
                documentReference.update(updateData).await()
                Log.d("FirestoreQueries", "User data updated with email: $email")
            } else {
                Log.w("FirestoreQueries", "No user found with email: $email")
            }
        } catch (e: Exception) {
            Log.w("FirestoreQueries", "Error updating user data with email $email: ${e.message}", e)
        }
    }


    suspend fun searchFriendsCollection(currentUserEmail: String): QuerySnapshot? {
        return try {
            // Get FirestoreInstance from MainActivity
            val db = MainActivity.FirestoreInstance.db

            // Get current user's Document ID from 'users' collection
            val currentUserDocumentId = getUsersDocumentId(currentUserEmail)

            // Search current user's friends collection
            if (currentUserDocumentId != null) {
                db.collection("users")
                    .document(currentUserDocumentId)
                    .collection("friends")
                    .get()
                    .await()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.w(
                "FirestoreQueries",
                "Error searching for user's friend collection with email $currentUserEmail: ${e.message}",
                e
            )
            null
        }
    }

    suspend fun searchIncomingFriendRequests(currentUserEmail: String): QuerySnapshot? {
        return try {
            // Get FirestoreInstance from MainActivity
            val db = MainActivity.FirestoreInstance.db

            // Get current user's Document ID from 'users' collection
            val currentUserDocumentId = getUsersDocumentId(currentUserEmail)

            // Search current user's incomingFriendRequests collection
            if (currentUserDocumentId != null) {
                db.collection("users")
                    .document(currentUserDocumentId)
                    .collection("incomingFriendRequests")
                    .get()
                    .await()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.w(
                "FirestoreQueries",
                "Error searching for user's incomingFriendRequests collection with email $currentUserEmail: ${e.message}",
                e
            )
            null
        }
    }

    suspend fun searchOutgoingFriendRequests(currentUserEmail: String): QuerySnapshot? {
        return try {
            // Get FirestoreInstance from MainActivity
            val db = MainActivity.FirestoreInstance.db

            // Get current user's Document ID from 'users' collection
            val currentUserDocumentId = getUsersDocumentId(currentUserEmail)

            // Search current user's outgoingFriendRequests collection
            if (currentUserDocumentId != null) {
                db.collection("users")
                    .document(currentUserDocumentId)
                    .collection("outgoingFriendRequests")
                    .get()
                    .await()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.w(
                "FirestoreQueries",
                "Error searching for user's outgoingFriendRequests collection with email $currentUserEmail: ${e.message}",
                e
            )
            null
        }
    }

    suspend fun checkUserIsUnique(fieldName: String, fieldValue: String): Boolean {
        return try {
            // Get FirestoreInstance from MainActivity
            val db = MainActivity.FirestoreInstance.db

            // Query database to check if user is unique
            val querySnapshot =
                db.collection("users")
                    .whereEqualTo(fieldName, fieldValue)
                    .limit(1)
                    .get()
                    .await()
            querySnapshot.isEmpty
        } catch (e: Exception) {
            Log.w(
                "FirestoreQueries",
                "Error checking if user is unique for $fieldName=$fieldValue: ${e.message}",
                e
            )
            false
        }
    }

    private suspend fun getUsersDocumentId(email: String): String? {
        return try {
            // Get FirestoreInstance from MainActivity
            val db = MainActivity.FirestoreInstance.db

            // Query to find user by email
            val querySnapshot =
                db.collection("users")
                    .whereEqualTo("email", email)
                    .limit(1)
                    .get()
                    .await()

            // If querySnapshot is not empty, return the document ID from the first document
            if (!querySnapshot.isEmpty) {
                querySnapshot.documents[0].id
            } else {
                null
            }
        } catch (e: Exception) {
            Log.w("FirestoreQueries", "Error getting Document ID for email $email: ${e.message}", e)
            null
        }
    }

    suspend fun checkUserIsFriend(currentUserEmail: String, otherUserEmail: String): Boolean {
        return try {
            // Get FirestoreInstance from MainActivity
            val db = MainActivity.FirestoreInstance.db

            // Get current user's Document ID from 'users' collection
            val currentUserDocumentId = getUsersDocumentId(currentUserEmail)

            // If currentUserDocumentId is not null, proceed with the query
            if (currentUserDocumentId != null) {
                // Search current user's 'friends' collection for the other user
                val querySnapshot =
                    db.collection("users")
                        .document(currentUserDocumentId)
                        .collection("friends")
                        .whereEqualTo("email", otherUserEmail)
                        .get()
                        .await()

                // Check if the querySnapshot is not empty to determine if the other user is a friend
                !querySnapshot.isEmpty
            } else {
                // If currentUserDocumentId is null, log an error and return false
                Log.w(
                    "FirestoreQueries",
                    "Document ID for current user (email: $currentUserEmail) is null"
                )
                false
            }
        } catch (e: Exception) {
            Log.w(
                "FirestoreQueries",
                "Error checking if user is a friend (current email: $currentUserEmail, other email: $otherUserEmail): ${e.message}",
                e
            )
            false
        }
    }

    suspend fun checkUserIsIncFriendReq(
        currentUserEmail: String,
        otherUserEmail: String
    ): Boolean {
        return try {
            // Get FirestoreInstance from MainActivity
            val db = MainActivity.FirestoreInstance.db

            // Get current user's Document ID from 'users' collection
            val currentUserDocumentId = getUsersDocumentId(currentUserEmail)

            // If currentUserDocumentId is not null, proceed with the query
            if (currentUserDocumentId != null) {
                // Search current user's 'incomingFriendRequests' collection for the other user
                val querySnapshot =
                    db.collection("users")
                        .document(currentUserDocumentId)
                        .collection("incomingFriendRequests")
                        .whereEqualTo("email", otherUserEmail)
                        .get()
                        .await()

                // Check if the querySnapshot is not empty to determine if there is an incoming friend request from other user
                !querySnapshot.isEmpty
            } else {
                // If currentUserDocumentId is null, log an error and return false
                Log.w(
                    "FirestoreQueries",
                    "Document ID for current user (email: $currentUserEmail) is null"
                )
                false
            }
        } catch (e: Exception) {
            Log.w(
                "FirestoreQueries",
                "Error checking incoming friend request (current email: $currentUserEmail, other email: $otherUserEmail): ${e.message}",
                e
            )
            false
        }
    }

    suspend fun checkUserIsOutFriendReq(
        currentUserEmail: String,
        otherUserEmail: String
    ): Boolean {
        return try {
            // Get FirestoreInstance from MainActivity
            val db = MainActivity.FirestoreInstance.db

            // Get current user's Document ID from 'users' collection
            val currentUserDocumentId = getUsersDocumentId(currentUserEmail)

            // If currentUserDocumentId is not null, proceed with the query
            if (currentUserDocumentId != null) {
                // Search current user's 'outgoingFriendRequests' collection for the other user
                val querySnapshot =
                    db.collection("users")
                        .document(currentUserDocumentId)
                        .collection("outgoingFriendRequests")
                        .whereEqualTo("email", otherUserEmail)
                        .get()
                        .await()

                // Check if the querySnapshot is not empty to determine if there is an outgoing friend request for other user
                !querySnapshot.isEmpty
            } else {
                // currentUserDocumentId is null, log an error and return false
                Log.w(
                    "FirestoreQueries",
                    "Document ID for current user (email: $currentUserEmail) is null"
                )
                false
            }
        } catch (e: Exception) {
            Log.w(
                "FirestoreQueries",
                "Error checking outgoing friend request (current UID: $currentUserEmail, other email: $otherUserEmail): ${e.message}",
                e
            )
            false
        }
    }

    suspend fun addUserToCollection(
        collection: String,
        currentUserEmail: String,
        newFriendData: HashMap<String, String>
    ) {
        try {
            // Get FirestoreInstance from MainActivity
            val db = MainActivity.FirestoreInstance.db

            // Get current user's Document ID from 'users' collection
            val currentUserDocumentId = getUsersDocumentId(currentUserEmail)

            // Check if currentUserDocumentId is not null
            if (currentUserDocumentId != null) {
                // Create and add document for friend in specified collection
                val documentReference = db.collection("users")
                    .document(currentUserDocumentId)
                    .collection(collection)
                    .add(newFriendData)
                    .await()

                // Log the document ID
                Log.d(MotionEffect.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
        } catch (e: Exception) {
            Log.w("FirestoreQueries", "Error creating document: ${e.message}", e)
        }
    }

    suspend fun deleteUserFromCollection(
        collection: String,
        currentUserEmail: String,
        newFriendEmail: String
    ) {
        try {
            // Get FirestoreInstance from MainActivity
            val db = MainActivity.FirestoreInstance.db

            // Get current user's Document ID from 'users' collection
            val currentUserDocumentId = getUsersDocumentId(currentUserEmail)

            // Check if currentUserDocumentId is not null
            if (currentUserDocumentId != null) {
                // Query the collection for the document with the specified email
                val querySnapshot: QuerySnapshot = db.collection("users")
                    .document(currentUserDocumentId)
                    .collection(collection)
                    .whereEqualTo("email", newFriendEmail)
                    .get()
                    .await()

                // Check if any documents were found
                if (!querySnapshot.isEmpty) {
                    // There should only be one document for the specified email so grab the first and delete it
                    val doc = querySnapshot.documents[0]
                    val docRef = doc.reference
                    docRef.delete().await()

                    // Log the document ID
                    Log.d("FirestoreQueries", "Document ${docRef.id} successfully deleted")
                } else {
                    Log.w("FirestoreQueries", "No documents found with email: $newFriendEmail")
                }
            } else {
                Log.w("FirestoreQueries", "No Document ID found for user: $currentUserEmail")
            }
        } catch (e: Exception) {
            Log.e("FirestoreQueries", "Error deleting document: ${e.message}", e)
        }
    }

    private suspend fun getChatDocumentId(currentUserEmail: String, friendEmail: String): String? {
        return try {
            // Get FirestoreInstance from MainActivity
            val db = MainActivity.FirestoreInstance.db

            // Query to find chat by first finding documents with current user in array of participants
            val querySnapshot = db.collection("chats")
                .whereArrayContains("participants", currentUserEmail)
                .get()
                .await()

            // Then filter by finding document with friend user in array of participants
            val document = querySnapshot.documents.find { documentSnapshot ->
                val participants = documentSnapshot.get("participants") as? List<*>
                participants?.contains(friendEmail) == true
            }

            // Return the document ID if found, otherwise return null
            document?.id
        } catch (e: Exception) {
            // Log or handle exceptions
            Log.w(
                "FirestoreQueries",
                "Error getting Document ID for chat between $currentUserEmail and $friendEmail: ${e.message}",
                e
            )
            null
        }
    }

    suspend fun sendMessage(currentUserEmail: String, friendEmail: String, text: String) {
        try {
            // Get FirestoreInstance from MainActivity
            val db = MainActivity.FirestoreInstance.db

            // Define data for message document
            val message = hashMapOf(
                "senderId" to currentUserEmail,
                "text" to text,
                "timestamp" to FieldValue.serverTimestamp()
            )

            // Get Document ID to chat between the two users
            val chatDocumentId = getChatDocumentId(currentUserEmail, friendEmail)

            // Add message to specified chat collection
            if (chatDocumentId != null) {
                // If the chat document exists, add the message to the existing chat
                db.collection("chats").document(chatDocumentId).collection("messages")
                    .add(message)
                    .await() // Wait for the message to be added
                Log.d("FirestoreQueries", "Message sent successfully")
            } else {
                // If the chat document doesn't exist, create a new chat document and add the message
                val chatDocRef = db.collection("chats").document()
                chatDocRef.collection("messages")
                    .add(message)
                    .await()
                // Make sure to set participant data to chat document fields
                val participantsData = hashMapOf(
                    "participants" to listOf(currentUserEmail, friendEmail)
                )
                db.collection("chats").document(chatDocRef.id)
                    .set(participantsData)
                    .await() // Wait for the participants data to be added
                Log.d("FirestoreQueries", "Message sent successfully")
            }
        } catch (e: Exception) {
            Log.e("FirestoreQueries", "Error sending message: ${e.message}", e)
        }
    }

    suspend fun fetchMessages(
        currentUserEmail: String,
        friendEmail: String
    ): List<Map<String, Any>> {
        return try {
            // Get FirestoreInstance from MainActivity
            val db = MainActivity.FirestoreInstance.db

            // Get Document ID from 'chats' collection
            var chatDocumentId = getChatDocumentId(currentUserEmail, friendEmail)

            // Create document if it doesn't exist
            if (chatDocumentId == null) {
                val chatDocumentRef = db.collection("chats").document()
                chatDocumentRef.set(mapOf("participants" to listOf(currentUserEmail, friendEmail)))
                    .await()
                chatDocumentId = chatDocumentRef.id
            }

            // Get messages from specified chat Document ID
            val querySnapshot =
                db.collection("chats").document(chatDocumentId).collection("messages")
                    .orderBy("timestamp", Query.Direction.ASCENDING)
                    .get()
                    .await()

            // Store messages in list
            val messages = mutableListOf<Map<String, Any>>()
            for (doc in querySnapshot.documents) {
                doc.data?.let { messages.add(it) }
            }

            // Return result
            messages
        } catch (e: Exception) {
            Log.e("FirestoreQueries", "Error retrieving messages: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun listenForMessages(
        currentUserEmail: String,
        friendEmail: String,
        onMessagesReceived: (List<Map<String, Any>>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        try {
            // Get FirestoreInstance from MainActivity
            val db = MainActivity.FirestoreInstance.db

            // Get Document ID to chat between the two users
            val chatDocumentId = getChatDocumentId(currentUserEmail, friendEmail)

            // Listen for messages
            if (chatDocumentId != null) {
                db.collection("chats").document(chatDocumentId).collection("messages")
                    .orderBy("timestamp", Query.Direction.ASCENDING)
                    .addSnapshotListener { snapshots, e ->
                        if (e != null) {
                            onError(e)
                            return@addSnapshotListener
                        }

                        if (snapshots != null) {
                            val messagesList = mutableListOf<Map<String, Any>>()
                            for (doc in snapshots) {
                                doc.data.let { messagesList.add(it) }
                            }
                            onMessagesReceived(messagesList)
                        }
                    }
            }
        } catch (e: Exception) {
            Log.e("FirestoreQueries", "Error listening to messages: ${e.message}", e)
        }
    }

//    suspend fun calculateTimeSinceLastSentMessage(
//        currentUserEmail: String,
//        friendEmail: String
//    ): Long? {
//        return try {
//            // Get FirestoreInstance from MainActivity
//            val db = MainActivity.FirestoreInstance.db
//
//            // Get Document ID for the chat between the two users
//            val chatDocumentId = getChatDocumentId(currentUserEmail, friendEmail)
//
//            // Use Document ID to check messages
//            if (chatDocumentId != null) {
//                // Navigate to the messages collection and query for messages
//                val messagesQuery = db.collection("chats")
//                    .document(chatDocumentId)
//                    .collection("messages")
//                    .whereEqualTo("senderId", currentUserEmail)
//                    .orderBy("timestamp", Query.Direction.DESCENDING)
//                    .limit(1) // Retrieve only the latest message
//
//                // Execute the query
//                val querySnapshot = messagesQuery.get().await()
//
//                // Check if any messages were found
//                if (!querySnapshot.isEmpty) {
//                    // Get the latest message
//                    val latestMessage = querySnapshot.documents[0]
//
//                    // Get the timestamp of the latest message
//                    val timestamp = latestMessage.getTimestamp("timestamp")
//
//                    // Calculate the time difference in milliseconds
//                    val currentTime = Calendar.getInstance().timeInMillis
//                    val messageTime = timestamp?.toDate()?.time ?: 0
//                    val timeDifference = currentTime - messageTime
//
//                    // Return time difference
//                    timeDifference
//                } else {
//                    // No messages found in the chat
//                    null
//                }
//            } else {
//                // Chat Document ID does not exist
//                null
//            }
//        } catch (e: Exception) {
//            Log.e(
//                "FirestoreQueries",
//                "Error calculating time since last sent message from $currentUserEmail to $friendEmail: ${e.message}",
//                e
//            )
//            null
//        }
//    }

    suspend fun retrieveCurrentActivityData(
        currentUserEmail: String,
        friendEmail: String,
        mapKey: String
    ): Any? {
        return try {
            // Get FirestoreInstance from MainActivity
            val db = MainActivity.FirestoreInstance.db

            // Get Document ID from 'chats' collection
            val chatDocumentId = getChatDocumentId(currentUserEmail, friendEmail)

            // Check document's currentActivity data
            if (chatDocumentId != null) {
                // Grab document snapshot
                val documentSnapshot = db.collection("chats").document(chatDocumentId).get().await()

                // Check if the document contains the specified mapKey data
                if (documentSnapshot.exists() && documentSnapshot.contains("currentActivity")) {
                    val currentActivityMap = documentSnapshot.get("currentActivity") as? Map<*, *>
                    if (currentActivityMap != null) {
                        val currentActivityData = currentActivityMap[mapKey]
                        Log.d(
                            "FirestoreQueries",
                            "Current activity $mapKey with $friendEmail: $currentActivityData"
                        )
                        currentActivityData
                    } else {
                        Log.d("FirestoreQueries", "Current activity field is not a map.")
                        null
                    }
                } else {
                    Log.d("FirestoreQueries", "No current activity with friend.")
                    null
                }
            } else {
                Log.d("FirestoreQueries", "Chat document ID is null.")
                null
            }
        } catch (e: Exception) {
            Log.w(
                "FirestoreQueries",
                "Error retrieving current activity data between $currentUserEmail and $friendEmail): ${e.message}",
                e
            )
            null
        }
    }

    suspend fun updateCurrentActivityData(
        currentUserEmail: String,
        friendEmail: String,
        updates: Map<String, Any?>
    ): Boolean {
        return try {
            // Get FirestoreInstance from MainActivity
            val db = MainActivity.FirestoreInstance.db

            // Get Document ID from 'chats' collection
            val chatDocumentId = getChatDocumentId(currentUserEmail, friendEmail)

            // Check document's currentActivity data
            if (chatDocumentId != null) {
                // Update the document in Firestore with multiple fields
                db.collection("chats").document(chatDocumentId)
                    .update("currentActivity", updates)
                    .addOnSuccessListener {
                        Log.d(
                            "FirestoreQueries",
                            "Updated current activity for $friendEmail with updates: $updates"
                        )
                    }
                    .addOnFailureListener { e ->
                        Log.w(
                            "FirestoreQueries",
                            "Error updating current activity data for $friendEmail: ${e.message}",
                            e
                        )
                    }
                    .await()

                true
            } else {
                Log.d(
                    "FirestoreQueries",
                    "Chat document ID is null for $currentUserEmail and $friendEmail."
                )
                false
            }
        } catch (e: Exception) {
            Log.w(
                "FirestoreQueries",
                "Error updating current activity data between $currentUserEmail and $friendEmail: ${e.message}",
                e
            )
            false
        }
    }

    suspend fun searchNotificationsCollection(currentUserEmail: String): QuerySnapshot? {
        return try {
            // Get FirestoreInstance from MainActivity
            val db = MainActivity.FirestoreInstance.db

            // Get current user's Document ID from 'users' collection
            val currentUserDocumentId = getUsersDocumentId(currentUserEmail)

            // Search current user's notifications collection
            if (currentUserDocumentId != null) {
                db.collection("users")
                    .document(currentUserDocumentId)
                    .collection("notifications")
                    .get()
                    .await()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.w(
                "FirestoreQueries",
                "Error searching for user's notifications collection with email $currentUserEmail: ${e.message}",
                e
            )
            null
        }
    }

    suspend fun addNotification(
        notification: Notification,
        userEmail: String
    ) {
        try {
            // Get FirestoreInstance from MainActivity
            val db = MainActivity.FirestoreInstance.db

            // Get user's Document ID from 'users' collection
            val userDocumentId = getUsersDocumentId(userEmail)

            // Check if userDocumentId is not null
            if (userDocumentId != null) {
                // Define data to add
                val data = mapOf(
                    "type" to notification.type,
                    "message" to notification.message
                )

                // Create and add notification document in notifications collection
                val documentReference = db.collection("users")
                    .document(userDocumentId)
                    .collection("notifications")
                    .add(data)
                    .await()

                // Log the document ID
                Log.d(MotionEffect.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
        } catch (e: Exception) {
            Log.w("FirestoreQueries", "Error creating notification document: ${e.message}", e)
        }
    }
}








