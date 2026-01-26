package com.android.mr.cleantodoapp.domain

import kotlinx.coroutines.flow.Flow

/**
 * Defines actions app can do with Todos - bi=ut without knowing how it can be done.
 * Contract between data and domain
 *
 * Implementation of this interface happen in data layer
 */
interface TodoRepository {
    fun getTodos(): Flow<List<Todo>> // Flow is a stream of data and it updates as data changes. Can be collected.

    // Modify asynchronously
    suspend fun addTodo(todo: Todo)
    suspend fun updateTodo(todo: Todo)
    suspend fun deleteTodo(todo: Todo)
}