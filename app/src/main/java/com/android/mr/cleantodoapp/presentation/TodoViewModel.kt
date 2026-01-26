package com.android.mr.cleantodoapp.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mr.cleantodoapp.domain.AddTodoUsecase
import com.android.mr.cleantodoapp.domain.Todo
import com.android.mr.cleantodoapp.domain.TodoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Bridge between the app's UI and the clean architecture layer underneath.
 */
open class TodoViewModel(private val todoRepository: TodoRepository): ViewModel() {

    private val addUsecase = AddTodoUsecase(todoRepository)

    open val todos = todoRepository.getTodos().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = emptyList()
    )

    // User actions
    // Note: Calling AddUseCase and not TodoRepositoryImpl directly
    fun addTodo(title: String, description: String) {
        viewModelScope.launch {
            addUsecase.execute(title, description)
        }
    }

    // Note: Calling TodoRepositoryImpl directly - could be via a Usecase too.
    fun toggleTodoDone(todo: Todo) {
        viewModelScope.launch{
            todoRepository.updateTodo(todo.copy(isDone = !todo.isDone))
        }
    }

    // Note: Calling TodoRepositoryImpl directly - could be via a Usecase too.
    fun editTodo(todo: Todo, newTitle: String?, newDescription: String?) {
        viewModelScope.launch {
            todoRepository.updateTodo(todo.copy(title = newTitle ?: todo.title, description = newDescription ?: todo.description))
        }
    }

    // Note: Calling TodoRepositoryImpl directly - could be via a Usecase too.
    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            todoRepository.deleteTodo(todo)
        }
    }
}