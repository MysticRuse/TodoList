package com.android.mr.cleantodoapp.ui.screens

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.mr.cleantodoapp.domain.Todo
import com.android.mr.cleantodoapp.domain.TodoRepository
import com.android.mr.cleantodoapp.presentation.TodoViewModel
import com.android.mr.cleantodoapp.ui.theme.CleanTodoAppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(todoViewModel: TodoViewModel) {
    var task by remember { mutableStateOf("") }
    val todoList: List<Todo> by todoViewModel.todos.collectAsStateWithLifecycle() // To observe reactively from ViewModel

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Todo List") }, // Title of the screen
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)
        ) {
            TodoInputBar(
                task = task,
                onTaskChange = { task = it },
                onAddClick = {
                    if (task.isNotBlank()) {
                        todoViewModel.addTodo(task, "")
                        task = ""
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items = todoList, key = { it.id }) { todo ->
                    TodoItem(
                        todo = todo,
                        onToggle = todoViewModel::toggleTodoDone,
                        onEdit = todoViewModel::editTodo,
                        onDelete = todoViewModel::deleteTodo
                    )

                } // Items
            } // End of LazyColumn
        }
    }
}

@Composable
fun TodoItem(
    todo: Todo,
    onToggle: (Todo) -> Unit,
    onEdit: (Todo, String, String) -> Unit,
    onDelete: (Todo) -> Unit,
    modifier: Modifier = Modifier
) {

    // State variables
    var isEditing by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf(todo.title) }
    var newDescription by remember { mutableStateOf(todo.description) }

    // --- ROW 1: The Main Container ---
    // Its job is to hold its children in a horizontal line and fill the screen width.
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        // --- ROW 2: The Expanding Content ---
        // Its only job is to group the Checkbox and Text/TextField and
        // tell them to take up all available space
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Checkbox(
                checked = todo.isDone,
                onCheckedChange = { onToggle(todo) },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            if (isEditing) {
                // Show the editing view
                OutlinedTextField(
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    )
                )
            } else {
                // Show the display view
                Text (
                    text = todo.title,
                    style = if (todo.isDone)
                        LocalTextStyle.current.copy(textDecoration = TextDecoration.LineThrough)
                    else
                        LocalTextStyle.current
                )
            }
        } // end of Row to organize Checkbox and edit text to take up as much space.

        // Show the action buttons - save, edit, delete
        // These are under the main row.
        if (isEditing) {
            // Save Button
            Button(
                onClick = {
                    onEdit(todo, newTitle, newDescription)
                    isEditing = false
                },
                modifier = Modifier.padding(start = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Save")
            }
        } else {
            // Edit icon
            IconButton(onClick = { isEditing = true }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
        }

        // Delete icon
        IconButton(onClick = { onDelete(todo) }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
        }
    }
}
@Composable
fun TodoInputBar(
    task: String,
    onTaskChange: (String) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = task,
            onValueChange = onTaskChange,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(22.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            ),
            placeholder = { Text("Enter your todo here") }
        )

        Button(
            onClick = onAddClick,
            shape = RoundedCornerShape(22.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Add")
        }
    }
}


@Preview(showBackground = true, name = "Todo Screen Preview")
@Composable
fun TodoScreenPreview() {
    // 1. Create a fake repository. This simulates your data source (like a database)
    //    without actually using it.
    val fakeRepository = object : TodoRepository {
        override fun getTodos(): Flow<List<Todo>> {
            // Provide a static list of todos for the preview
            return flowOf(
                listOf(
                    Todo(id = 1, title = "Buy groceries", description = "", isDone = false),
                    Todo(id = 2, title = "Walk the dog", description = "", isDone = true),
                    Todo(id = 3, title = "Fix Compose preview", description = "", isDone = false)
                )
            )
        }

        // Add empty implementations for other repository methods
        override suspend fun addTodo(todo: Todo) {}
        override suspend fun updateTodo(todo: Todo) {}
        override suspend fun deleteTodo(todo: Todo) {}
    }

    // 2. Create a real instance of your ViewModel using the fake repository
    val fakeViewModel = TodoViewModel(fakeRepository)

    // 3. Call your actual TodoScreen composable and pass the fake ViewModel
    // Note: You might want to wrap this in your app's theme for correct styling
    CleanTodoAppTheme {
        TodoScreen(todoViewModel = fakeViewModel)
    }
}
