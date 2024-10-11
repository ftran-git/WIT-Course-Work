package com.example.seniorproject_icebreaker.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seniorproject_icebreaker.data.firestore.FirestoreQueries
import kotlinx.coroutines.launch

class ChatScreenViewModel : ViewModel() {
    private val _messages = MutableLiveData<List<Map<String, Any>>>()
    val messages: LiveData<List<Map<String, Any>>> get() = _messages
    private val _error = MutableLiveData<String>()

    fun fetchMessages(currentUserEmail: String, friendEmail: String) {
        viewModelScope.launch {
            try {
                val messagesList = FirestoreQueries.fetchMessages(currentUserEmail, friendEmail)
                _messages.value = messagesList

            } catch (e: Exception) {
                Log.e("ChatScreenViewModel", "Error fetching messages: $e.message", e)
                _error.value = e.message
            }
        }
    }

    fun listenForMessages(currentUserEmail: String, friendEmail: String) {
        viewModelScope.launch {
            try {
                FirestoreQueries.listenForMessages(currentUserEmail, friendEmail,
                    onMessagesReceived = { messagesList ->
                        // Update messages LiveData with new messages
                        _messages.postValue(messagesList)
                    },
                    onError = { e ->
                        // Log error and propagate it if necessary to UI layer
                        Log.e("ChatScreenViewModel", "Error listening for messages: ${e.message}", e)
                        _error.postValue("Error listening for messages: ${e.message}")
                    }
                )
            } catch (e: Exception) {
                // Handle any exceptions that occur during the process
                Log.e("ChatScreenViewModel", "Error listening for messages: ${e.message}", e)
                _error.postValue("Error listening for messages messages: ${e.message}")
            }
        }
    }
}