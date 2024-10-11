package com.example.seniorproject_icebreaker.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seniorproject_icebreaker.data.firestore.FirestoreQueries
import com.example.seniorproject_icebreaker.data.model.FriendStatus
import com.example.seniorproject_icebreaker.data.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FriendRequestsViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _incomingFriendRequests = MutableStateFlow<List<User>>(emptyList())
    val incomingFriendRequests: StateFlow<List<User>> = _incomingFriendRequests

    private val _outgoingFriendRequests = MutableStateFlow<List<User>>(emptyList())
    val outgoingFriendRequests: StateFlow<List<User>> = _outgoingFriendRequests

    init {
        checkFriendRequests()
    }

    fun checkIncomingFriendRequests() {
        // Perform data search of incoming friend requests
        viewModelScope.launch {
            // Incoming Friend Requests
            try {
                // Grab current user's UID
                val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
                val currentUserEmail = auth.currentUser?.email

                // Search current user's incomingFriendRequests collection
                val querySnapshot = currentUserEmail?.let {
                    FirestoreQueries.searchIncomingFriendRequests(
                        it
                    )
                }

                // Process result into list of user data objects
                val userList = mutableListOf<User>()
                if (querySnapshot != null) {
                    for (document in querySnapshot.documents) {
                        // Extract data from query snapshot
                        val data = document.data
                        val email = data?.get("email") as? String
                        if (email != null) {
                            userList.add(User(email = email, FriendStatus.INCOMING_REQUEST))
                        }
                    }
                }

                // Update state flow with the list of users
                _incomingFriendRequests.value = userList
            } catch (e: Exception) {
                // Handle any errors that occur during the search operation
                Log.e("FriendRequestsViewModel", "Error checking incoming friend requests: $e")
            } finally {
                // Once data loading is complete, set isLoading back to false
                _isLoading.value = false
            }
        }
    }

    fun checkOutgoingFriendRequests() {
        // Perform data search of outgoing friend requests
        viewModelScope.launch {
            // Outgoing Friend Requests
            try {
                // Grab current user's UID
                val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
                val currentUserEmail = auth.currentUser?.email

                // Search current user's outgoingFriendRequests collection
                val querySnapshot = currentUserEmail?.let {
                    FirestoreQueries.searchOutgoingFriendRequests(
                        it
                    )
                }

                // Process result into list of user data objects
                val userList = mutableListOf<User>()
                if (querySnapshot != null) {
                    for (document in querySnapshot.documents) {
                        // Extract data from query snapshot
                        val data = document.data
                        val email = data?.get("email") as? String
                        if (email != null) {
                            userList.add(User(email = email, FriendStatus.OUTGOING_REQUEST))
                        }
                    }
                }

                // Update state flow with the list of users
                _outgoingFriendRequests.value = userList
            } catch (e: Exception) {
                // Handle any errors that occur during the search operation
                Log.e("FriendRequestsViewModel", "Error checking outgoing friend requests: $e")
            } finally {
                // Once data loading is complete, set isLoading back to false
                _isLoading.value = false
            }
        }
    }

    fun checkFriendRequests() {
        // Set isLoading to true before starting data loading
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Perform each data check sequentially
                checkIncomingFriendRequests()
                checkOutgoingFriendRequests()
            } catch (e: Exception) {
                // Log or handle the exception appropriately
                Log.e("FriendRequestsViewModel", "Error checking friend requests: $e")
            } finally {
                // Once data loading is complete, set isLoading back to false
                _isLoading.value = false
            }
        }
    }
}