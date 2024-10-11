package com.example.seniorproject_icebreaker.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seniorproject_icebreaker.data.firestore.FirestoreQueries
import com.example.seniorproject_icebreaker.data.model.FriendStatus
import com.example.seniorproject_icebreaker.data.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchUserViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    init {
        searchUsers("")
    }

    fun setSearchText(text: String) {
        _searchText.value = text
    }

    fun resetSearchViewModel() {
        _searchText.value = ""
        _users.value = emptyList()
    }

    fun searchUsers(query: String) {
        // Set isLoading to true before starting data loading
        _isLoading.value = true

        // Perform data search
        viewModelScope.launch {
            try {
                // Grab current user's UID
                val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
                val currentUserEmail = auth.currentUser?.email

                // Grab search results
                val querySnapshot = FirestoreQueries.searchUsersCollection(query)

                // Process result into list of user data objects
                val userList = mutableListOf<User>()
                if (querySnapshot != null) {
                    for (document in querySnapshot.documents) {
                        // Extract data from query snapshot
                        val data = document.data
                        val email = data?.get("email") as? String

                        // Skip if user is current user
                        if (email == auth.currentUser?.email) {
                            continue
                        }

                        // Check friend status
                        if (currentUserEmail != null && email != null) {
                            // Other user is not a friend so check for incoming or outgoing friend requests
                            if (!FirestoreQueries.checkUserIsFriend(currentUserEmail, email)) {
                                if (FirestoreQueries.checkUserIsIncFriendReq(
                                        currentUserEmail,
                                        email
                                    )
                                ) {
                                    // Incoming request found from other user
                                    userList.add(User(email = email, FriendStatus.INCOMING_REQUEST))
                                } else if (FirestoreQueries.checkUserIsOutFriendReq(
                                        currentUserEmail,
                                        email
                                    )
                                ) {
                                    // Outgoing request found for other user
                                    userList.add(User(email = email, FriendStatus.OUTGOING_REQUEST))
                                } else {
                                    // Other user is not a friend
                                    userList.add(User(email = email, FriendStatus.NOT_FRIEND))
                                }
                            } else {
                                // Other user is a friend
                                userList.add(User(email = email, FriendStatus.FRIEND))
                            }
                        }
                    }
                }

                // Update state flow with the list of users
                _users.value = userList
            } catch (e: Exception) {
                // Handle any errors that occur during the search operation
                Log.e("SearchUserViewModel", "Error searching users: $e")
            } finally {
                // Once data loading is complete, set isLoading back to false
                _isLoading.value = false
            }
        }
    }
}