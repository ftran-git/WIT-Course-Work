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

class FriendsListViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _friends = MutableStateFlow<List<User>>(emptyList())
    val friends: StateFlow<List<User>> = _friends

    init {
        grabFriends()
    }

    fun grabFriends() {
        // Set isLoading to true before starting data loading
        _isLoading.value = true

        // Perform data search
        viewModelScope.launch {
            try {
                // Grab current user's UID
                val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
                val currentUserEmail = auth.currentUser?.email

                // Search current user's friend collection
                val querySnapshot = currentUserEmail?.let {
                    FirestoreQueries.searchFriendsCollection(
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
                            userList.add(User(email = email, FriendStatus.FRIEND))
                        }
                    }
                }

                // Update state flow with the list of users
                _friends.value = userList
            } catch (e: Exception) {
                // Handle any errors that occur during the search operation
                Log.e("FriendsListViewModel", "Error filling friends list: $e")
            } finally {
                // Once data loading is complete, set isLoading back to false
                _isLoading.value = false
            }
        }
    }
}