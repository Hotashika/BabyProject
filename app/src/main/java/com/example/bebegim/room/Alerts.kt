package com.example.bebegim.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alerts")
data class Alerts(
    @PrimaryKey
    val alert_id: String, // UUID as String
    val baby_id: String,  // UUID as String

    val alert_type: String,

    val title: String,
    val message: String,

    val priority: String,

    val is_read: Boolean,
    val is_dismissed: Boolean,

    val scheduled_for: String, // You can use Instant or Long if preferred
    val created_at: String     // You can use Instant or Long if preferred
)
