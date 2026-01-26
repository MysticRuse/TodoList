package com.android.mr.cleantodoapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Database for the app
 */
@Database(
    entities = [TodoEntity::class],
    version = 1,
    exportSchema = false // Room wont save the data in folder
)
abstract class TodoDatabase: RoomDatabase() {

    // Connects todoDao to the database
    abstract fun todoDao(): TodoDao

}