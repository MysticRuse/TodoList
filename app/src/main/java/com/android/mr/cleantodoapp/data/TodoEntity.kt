package com.android.mr.cleantodoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mytodos")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val isDone: Boolean = false

)
