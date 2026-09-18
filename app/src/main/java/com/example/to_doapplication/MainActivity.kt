package com.example.to_doapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.to_doapplication.auth.AuthManager
import com.example.to_doapplication.data.TaskRepository
import com.example.to_doapplication.ui.TaskViewModel
import com.example.to_doapplication.ui.navigation.AppNavigation
import com.example.to_doapplication.ui.navigation.Screen
import com.example.to_doapplication.ui.theme.ToDoApplicationTheme
import com.google.firebase.database.FirebaseDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val authManager = AuthManager(this)
        val firebaseDatabase = FirebaseDatabase.getInstance().reference
        val repository = TaskRepository(firebaseDatabase)
        
        // Manual ViewModel instantiation for simplicity
        val viewModel = TaskViewModel(repository, authManager)

        setContent {
            ToDoApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val isUserLoggedIn by viewModel.isUserLoggedIn.collectAsState()
                    val startDestination = if (isUserLoggedIn) {
                        Screen.Main.route
                    } else {
                        Screen.Intro.route
                    }
                    AppNavigation(viewModel, authManager, startDestination)
                }
            }
        }
    }
}
