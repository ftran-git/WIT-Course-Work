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

class TicTacToeViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _name = MutableStateFlow<String?>(null)
    val name: StateFlow<String?> get() = _name

    private val _startDate = MutableStateFlow<Timestamp?>(null)
    val startDate: StateFlow<Timestamp?> get() = _startDate

    /* Game State */

    private val _players = MutableStateFlow<Map<String, String>?>(null)
    val players: StateFlow<Map<String, String>?> = _players

    private val _boxStates= MutableStateFlow<Map<String, String>?>(null)
    val boxStates: StateFlow<Map<String, String>?> = _boxStates

    private val _turn = MutableStateFlow<String?>(null)
    val turn: StateFlow<String?> get() = _turn

    private val _winner = MutableStateFlow<String?>(null)
    val winner: StateFlow<String?> get() = _winner

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
                Log.e("TicTacToeViewModel", "Error checking name: $e.message", e)
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
                // Update start date value to null in view model
                _startDate.value = null

                // Log error or handle it appropriately
                Log.e("TicTacToeViewModel", "Error checking start date: $e.message", e)
            }
        }
    }

    fun checkPlayers(currentUserEmail: String, friendEmail: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Grab players from database
                val players = FirestoreQueries.retrieveCurrentActivityData(currentUserEmail, friendEmail, "players")

                // Update players value in view model
                if (players == null) {
                    _players.value = null
                } else {
                    _players.value = players as Map<String, String>
                }
            } catch (e: Exception) {
                // Update players value to null in view model
                _players.value = null

                // Log error or handle it appropriately
                Log.e("TicTacToeViewModel", "Error checking players: $e.message", e)
            }
        }
    }

    fun checkBoxStates(currentUserEmail: String, friendEmail: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Grab box states from database
                val boxStates = FirestoreQueries.retrieveCurrentActivityData(currentUserEmail, friendEmail, "boxStates")

                // Update box states value in view model
                if (boxStates == null) {
                    _boxStates.value = null
                } else {
                    _boxStates.value = boxStates as Map<String, String>
                }
            } catch (e: Exception) {
                // Update box states value to null in view model
                _boxStates.value = null

                // Log error or handle it appropriately
                Log.e("TicTacToeViewModel", "Error checking box states: $e.message", e)
            }
        }
    }

    fun checkTurn(currentUserEmail: String, friendEmail: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Grab turn from database
                val turn = FirestoreQueries.retrieveCurrentActivityData(currentUserEmail, friendEmail, "turn")

                // Update turn value in view model
                if (turn == null) {
                    _turn.value = null
                } else {
                    _turn.value = turn as String
                }
            } catch (e: Exception) {
                // Update turn value to null in view model
                _turn.value = null

                // Log error or handle it appropriately
                Log.e("TicTacToeViewModel", "Error checking turn: $e.message", e)
            }
        }
    }

    fun checkWinner(currentUserEmail: String, friendEmail: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Grab winner from database
                val winner = FirestoreQueries.retrieveCurrentActivityData(currentUserEmail, friendEmail, "winner")

                // Update winner value in view model
                if (winner == null) {
                    _winner.value = null
                } else {
                    _winner.value = winner as String
                }
            } catch (e: Exception) {
                // Update winner value to null in view model
                _winner.value = null

                // Log error or handle it appropriately
                Log.e("TicTacToeViewModel", "Error checking winner: $e.message", e)
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
                checkPlayers(currentUserEmail, friendEmail)
                checkBoxStates(currentUserEmail, friendEmail)
                checkTurn(currentUserEmail, friendEmail)
                checkWinner(currentUserEmail, friendEmail)
            } catch (e: Exception) {
                // Log or handle the exception appropriately
                Log.e("TicTacToeViewModel", "Error checking data: $e")
            } finally {
                // Once data loading is complete, set isLoading back to false
                _isLoading.value = false
            }
        }
    }
}
