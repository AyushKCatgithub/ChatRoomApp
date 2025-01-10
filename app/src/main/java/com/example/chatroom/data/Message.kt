package com.example.chatroom.data

data class Message(
    val id: String = null.toString(), // Unique ID for the message (UUID recommended)
    val senderId: String, // ID of the sender
    val senderFirstName: String, // Name of the sender
    val text: String, // The message text (nullable for other message types)
    val imageUrl: String? = null, // URL of an image (nullable)
    val videoUrl: String? = null, // URL of a video (nullable)
    val timestamp: Long = System.currentTimeMillis(), // Timestamp of the message
    val messageStatus: MessageStatus = MessageStatus.SENDING,// Status of the message
    val isSentByCurrentUser: Boolean = true
)

enum class MessageStatus {
    SENDING, SENT, DELIVERED, READ, FAILED
}

