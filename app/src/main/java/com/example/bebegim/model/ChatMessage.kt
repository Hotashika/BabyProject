package com.example.bebegim.model

data class ChatMessage(
    val content: String,
    val type: MessageType
)

enum class MessageType {
    SENT,
    RECEIVED
}
