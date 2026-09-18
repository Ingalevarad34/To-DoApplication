package com.example.to_doapplication.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.to_doapplication.data.Task
import com.example.to_doapplication.ui.theme.*

@Composable
fun TaskDetailScreen(
    task: Task,
    onBack: () -> Unit,
    onUpdateTask: (Task) -> Unit,
    onDeleteTask: (Task) -> Unit
) {
    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf(task.description) }
    var isCompleted by remember { mutableStateOf(task.isCompleted) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UpTodoBackground)
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(
                text = "Task Details",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = {
                onDeleteTask(task)
                onBack()
            }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = UpTodoError)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Task Title", color = UpTodoHint, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = UpTodoPrimary,
                unfocusedBorderColor = UpTodoHint
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Task Description", color = UpTodoHint, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = UpTodoPrimary,
                unfocusedBorderColor = UpTodoHint
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Completed", color = Color.White, fontSize = 16.sp)
            Checkbox(
                checked = isCompleted,
                onCheckedChange = { 
                    isCompleted = it
                    onUpdateTask(task.copy(isCompleted = it))
                },
                colors = CheckboxDefaults.colors(checkedColor = UpTodoPrimary)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                val updated = task.copy(
                    title = title,
                    description = description,
                    isCompleted = isCompleted
                )
                onUpdateTask(updated)
                onBack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = UpTodoPrimary)
        ) {
            Text("Save Changes", color = UpTodoOnPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
