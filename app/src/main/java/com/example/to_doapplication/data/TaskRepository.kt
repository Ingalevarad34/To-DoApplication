package com.example.to_doapplication.data

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class TaskRepository(
    private val databaseReference: DatabaseReference,
) {
    fun getTasksForUser(userId: String): Flow<List<Task>> = callbackFlow {
        val tasksRef = databaseReference.child("users").child(userId).child("tasks")
        
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tasks = mutableListOf<Task>()
                for (childSnapshot in snapshot.children) {
                    val task = childSnapshot.getValue(Task::class.java)
                    task?.let { 
                        if (it.userId.isBlank()) {
                            tasks.add(it.copy(userId = userId))
                        } else {
                            tasks.add(it)
                        }
                    }
                }
                trySend(tasks)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        
        tasksRef.addValueEventListener(listener)
        awaitClose { tasksRef.removeEventListener(listener) }
    }

    fun getCategoriesForUser(userId: String): Flow<List<Category>> = callbackFlow {
        val categoriesRef = databaseReference.child("users").child(userId).child("categories")
        
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val categories = mutableListOf<Category>()
                for (childSnapshot in snapshot.children) {
                    val category = childSnapshot.getValue(Category::class.java)
                    category?.let { categories.add(it) }
                }
                trySend(categories)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        
        categoriesRef.addValueEventListener(listener)
        awaitClose { categoriesRef.removeEventListener(listener) }
    }

    suspend fun insertCategory(category: Category) {
        databaseReference.child("users").child(category.userId).child("categories").child(category.id).setValue(category).await()
    }

    suspend fun syncTasksFromFirebase(userId: String) {
        // Obsolete as getTasksForUser is now directly listening to Realtime Database
    }

    suspend fun insertTask(task: Task) {
        databaseReference.child("users").child(task.userId).child("tasks").child(task.id).setValue(task).await()
    }

    suspend fun updateTask(task: Task) {
        databaseReference.child("users").child(task.userId).child("tasks").child(task.id).setValue(task).await()
    }

    suspend fun deleteTask(task: Task) {
        databaseReference.child("users").child(task.userId).child("tasks").child(task.id).removeValue().await()
    }
}
