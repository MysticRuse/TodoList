package com.android.mr.cleantodoapp.domain

class AddTodoUsecase(private val todoRepo: TodoRepository) {

    suspend fun execute(title: String, description: String) {
        todoRepo.addTodo(Todo(title, description, isDone = false))
    }
}