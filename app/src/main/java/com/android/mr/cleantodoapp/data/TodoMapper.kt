package com.android.mr.cleantodoapp.data

import com.android.mr.cleantodoapp.domain.Todo


/**
 * Converts a TodoEntity from the data layer to a Todo in the domain layer
 */
fun TodoEntity.toDomain(): Todo {
    return Todo(
        title = this.title,
        description = this.description,
        isDone = this.isDone,
        id = this.id,
    )
}

/**
 * Converts a list of TodoEntity to a list of Todo
 */
fun List<TodoEntity>.toDomain(): List<Todo> {
    return this.map { it.toDomain() }
}

/**
 * Converts a Todo from the domain layer to a TodoEntity in the data layer.
 * Needed for adding or updating items.
 */
fun Todo.toEntity(): TodoEntity {
    return TodoEntity(
        title = this.title,
        description = this.description,
        isDone = this.isDone,
        id = this.id,
    )
}