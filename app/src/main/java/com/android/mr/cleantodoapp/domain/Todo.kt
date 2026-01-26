package com.android.mr.cleantodoapp.domain

/**
 * This is different from TodoEntity in the data package tie to Room and DB.
 * This purely focuses on the business logic
 * Core business model of app.
 * Independent of DB
 */
data class Todo(
    val title: String,
    val description: String,
    var isDone: Boolean = false, // Whether it is completed
    val id: Long = 0,
)
