package com.example.seniorproject_icebreaker.data.model

enum class NotificationType {
    ACTIVITY,
    FRIEND_REQUEST
}

data class Notification(val type: NotificationType, val message: String)