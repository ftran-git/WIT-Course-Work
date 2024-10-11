package com.example.seniorproject_icebreaker.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seniorproject_icebreaker.data.firestore.FirestoreQueries
import com.example.seniorproject_icebreaker.data.model.Notification
import com.example.seniorproject_icebreaker.data.model.NotificationType
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationsViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications

    init {
        checkNotifications()
    }

    fun checkNotifications() {
        // Set isLoading to true before starting data loading
        _isLoading.value = true

        // Perform data search of notifications
        viewModelScope.launch {
            // Incoming Friend Requests
            try {
                // Grab current user's UID
                val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
                val currentUserEmail = auth.currentUser?.email

                // Search current user's notifications collection
                val querySnapshot = currentUserEmail?.let {
                    FirestoreQueries.searchNotificationsCollection(
                        it
                    )
                }

                // Process result into list of user data objects
                val notificationsList = mutableListOf<Notification>()
                if (querySnapshot != null) {
                    for (document in querySnapshot.documents) {
                        // Extract data from query snapshot
                        val data = document.data
                        val type = data?.get("type") as? String
                        val message = data?.get("message") as? String
                        if (type != null && message != null) {
                            if (type == "ACTIVITY") {
                                notificationsList.add(Notification(NotificationType.ACTIVITY, message))
                            } else {
                                notificationsList.add(Notification(NotificationType.FRIEND_REQUEST, message))
                            }
                        }
                    }
                }

                // Update state flow with the list of users
                _notifications.value = notificationsList
            } catch (e: Exception) {
                // Handle any errors that occur during the search operation
                Log.e("FriendRequestsViewModel", "Error checking incoming friend requests: $e")
            } finally {
                // Once data loading is complete, set isLoading back to false
                _isLoading.value = false
            }
        }
    }
}