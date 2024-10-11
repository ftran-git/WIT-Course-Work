package com.example.seniorproject_icebreaker.data.model

enum class FriendStatus {
    FRIEND,
    NOT_FRIEND,
    INCOMING_REQUEST,
    OUTGOING_REQUEST
}

data class User(val email: String, val friendStatus: FriendStatus)