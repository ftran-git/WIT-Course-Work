package com.example.seniorproject_icebreaker.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seniorproject_icebreaker.data.firestore.FirestoreQueries
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

class QuestionBasedActivityViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _name = MutableStateFlow<String?>(null)
    val name: StateFlow<String?> get() = _name

    private val _startDate = MutableStateFlow<Timestamp?>(null)
    val startDate: StateFlow<Timestamp?> get() = _startDate

    private val _completed = MutableStateFlow<Map<String, Boolean>?>(emptyMap())
    val completed: StateFlow<Map<String, Boolean>?> = _completed

    private val _question = MutableStateFlow<Any?>(null)
    val question: StateFlow<Any?> get() = _question

    private val _answer = MutableStateFlow<Map<String, String>?>(emptyMap())
    val answer: StateFlow<Map<String, String>?> = _answer

    fun checkName(currentUserEmail: String, friendEmail: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Grab name from database
                val name = FirestoreQueries.retrieveCurrentActivityData(currentUserEmail, friendEmail, "name")

                // Update name value in view model
                if (name == null) {
                    _name.value = null
                } else {
                    _name.value = name.toString()
                }
            } catch (e: Exception) {
                // Update name value to null in view model
                _name.value = null

                // Log error or handle it appropriately
                Log.e("ActivityScreenViewModel", "Error checking name: $e.message", e)
            }
        }
    }

    fun checkStartDate(currentUserEmail: String, friendEmail: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Grab start date from database
                val startDate = FirestoreQueries.retrieveCurrentActivityData(currentUserEmail, friendEmail, "startDate")

                // Update start date value in view model
                if (startDate == null) {
                    _startDate.value = null
                } else {
                    _startDate.value = startDate as Timestamp
                }
            } catch (e: Exception) {
                // Update  start date value to null in view model
                _startDate.value = null

                // Log error or handle it appropriately
                Log.e("ActivityScreenViewModel", "Error checking start date: $e.message", e)
            }
        }
    }

    fun checkCompleted(currentUserEmail: String, friendEmail: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Grab completed from database
                val completed = FirestoreQueries.retrieveCurrentActivityData(currentUserEmail, friendEmail, "completed")

                // Update completed value in view model
                if (completed == null) {
                    _completed.value = null
                } else{
                    _completed.value = completed as Map<String, Boolean>
                }
            } catch (e: Exception) {
                // Update completed value to null in view model
                _completed.value = null

                // Log error or handle it appropriately
                Log.e("ActivityScreenViewModel", "Error checking completed: $e.message", e)
            }
        }
    }

    fun checkQuestion(currentUserEmail: String, friendEmail: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Grab question from database
                val question = FirestoreQueries.retrieveCurrentActivityData(currentUserEmail, friendEmail, "question")

                // Update question value in view model
                if (question == null) {
                    _question.value = null
                } else {
                    _question.value = question as Any
                }
            } catch (e: Exception) {
                // Update question value to null in view model
                _question.value = null

                // Log error or handle it appropriately
                Log.e("ActivityScreenViewModel", "Error checking question: $e.message", e)
            }
        }
    }

    fun checkAnswer(currentUserEmail: String, friendEmail: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Grab answer from database
                val answer = FirestoreQueries.retrieveCurrentActivityData(currentUserEmail, friendEmail, "answer")

                // Update answer value in view model
                if (answer == null) {
                    _answer.value = null
                } else {
                    _answer.value = answer as Map<String, String>
                }
            } catch (e: Exception) {
                // Update answer value to null in view model
                _answer.value = null

                // Log error or handle it appropriately
                Log.e("ActivityScreenViewModel", "Error checking answer: $e.message", e)
            }
        }
    }

    fun checkData(currentUserEmail: String, friendEmail: String) {
        // Set isLoading to true before starting data loading
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Perform each data check sequentially
                checkName(currentUserEmail, friendEmail)
                checkStartDate(currentUserEmail, friendEmail)
                checkCompleted(currentUserEmail, friendEmail)
                checkQuestion(currentUserEmail, friendEmail)
                checkAnswer(currentUserEmail, friendEmail)
            } catch (e: Exception) {
                // Log or handle the exception appropriately
                Log.e("ActivityScreenViewModel", "Error checking data: $e")
            } finally {
                // Once data loading is complete, set isLoading back to false
                _isLoading.value = false
            }
        }
    }
}
