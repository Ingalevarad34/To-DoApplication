package com.example.to_doapplication.data

import com.google.firebase.database.PropertyName
import java.util.UUID

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val description: String = "",
    @get:PropertyName("completed")
    @set:PropertyName("completed")
    var isCompleted: Boolean = false,
    val userId: String = "", // Added userId to support user specific tasks
    val timestamp: Long = System.currentTimeMillis(),
    val categoryId: String? = null,
    val priority: Int? = null,
    val dueDate: Long? = null,
    val dueTime: Long? = null,
) {
    // Empty constructor for Firebase Realtime Database
    constructor() : this("", "", "", false, "", 0L, null, null, null, null)
}
