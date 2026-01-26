package com.android.mr.cleantodoapp.data

import com.android.mr.cleantodoapp.domain.Todo
import com.android.mr.cleantodoapp.domain.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Data movement from data to domain.
 * Data layer is dependent on domain but domain is independent.
 */
class TodoRepositoryImpl(private val dao: TodoDao) : TodoRepository {

    // Data to Domain
    override fun getTodos(): Flow<List<Todo>> {
        return dao.getAllTodos().map { entities ->
            entities.toDomain() }
    }

    // Domain to Data
    override suspend fun addTodo(todo: Todo) {
        dao.insertTodo(todo.toEntity())
    }

    // Domain to Data
    override suspend fun updateTodo(todo: Todo) {
        dao.update(todo.toEntity())
    }

    // Domain to Data
    override suspend fun deleteTodo(todo: Todo) {
        dao.delete(todo.toEntity())
    }
}