package com.android.mr.cleantodoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.android.mr.cleantodoapp.data.TodoDatabase
import com.android.mr.cleantodoapp.data.TodoRepositoryImpl
import com.android.mr.cleantodoapp.presentation.TodoViewModel
import com.android.mr.cleantodoapp.presentation.TodoViewModelFactory
import com.android.mr.cleantodoapp.ui.screens.TodoScreen
import com.android.mr.cleantodoapp.ui.theme.CleanTodoAppTheme

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            TodoDatabase::class.java,
            "todos.db"
        ).build()
    }

    private val todoRepository by lazy { TodoRepositoryImpl(db.todoDao()) }
    private val todoViewModel: TodoViewModel by viewModels {
        TodoViewModelFactory(todoRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CleanTodoAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    //Apply the innerPadding to your screen's container
                    Box(modifier = Modifier.padding(innerPadding)) {
                        TodoScreen(todoViewModel = todoViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CleanTodoAppTheme {
        Greeting("Android")
    }
}