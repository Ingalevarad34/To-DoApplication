package com.example.to_doapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_doapplication.auth.AuthManager
import com.example.to_doapplication.data.Task
import com.example.to_doapplication.data.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TaskViewModel(
    private val repository: TaskRepository,
    private val authManager: AuthManager,
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _isUserLoggedIn = MutableStateFlow(authManager.getCurrentUserId() != null)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn.asStateFlow()
    
    @Suppress("BooleanLiteralArgument")
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadTasks()
    }

    fun loadTasks() {
        val userId = authManager.getCurrentUserId()
        if (userId != null) {
            _isUserLoggedIn.value = true
            viewModelScope.launch {
                _isLoading.value = true
                repository.syncTasksFromFirebase(userId)
                repository.getTasksForUser(userId).collectLatest {
                    _tasks.value = it
                    _isLoading.value = false
                }
            }
        } else {
            _isUserLoggedIn.value = false
            _tasks.value = emptyList()
        }
    }

    fun addTask(
        title: String,
        description: String,
        categoryId: String? = null,
        priority: Int? = null,
        dueDate: Long? = null,
        dueTime: Long? = null
    ) {
        val userId = authManager.getCurrentUserId() ?: return
        val task = Task(
            title = title,
            description = description,
            userId = userId,
            categoryId = categoryId,
            priority = priority,
            dueDate = dueDate,
            dueTime = dueTime
        )
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun toggleTaskCompletion(task: Task) {
        val updatedTask = task.copy(isCompleted = !task.isCompleted)
        viewModelScope.launch {
            repository.updateTask(updatedTask)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun signOut() {
        authManager.signOut()
        _isUserLoggedIn.value = false
        _tasks.value = emptyList()
    }
}
