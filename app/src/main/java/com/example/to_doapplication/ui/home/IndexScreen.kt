package com.example.to_doapplication.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.to_doapplication.data.Task
import com.example.to_doapplication.ui.theme.UpTodoBackground
import com.example.to_doapplication.ui.theme.UpTodoHint
import com.example.to_doapplication.ui.theme.UpTodoPrimary
import com.example.to_doapplication.ui.theme.UpTodoSurface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndexScreen(
    tasks: List<Task> = emptyList(),
    onTaskClick: (Task) -> Unit = {},
    onToggleCompletion: (Task) -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var filterOption by remember { mutableStateOf("Today") }
    var expanded by remember { mutableStateOf(false) }
    
    val displayTasks = if (searchQuery.isNotBlank()) {
        tasks.filter { 
            it.title.contains(searchQuery, ignoreCase = true) || 
            it.description.contains(searchQuery, ignoreCase = true) 
        }
    } else {
        when (filterOption) {
            "Today" -> tasks.filter { !it.isCompleted }
            "Completed" -> tasks.filter { it.isCompleted }
            else -> tasks
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UpTodoBackground)
            .padding(top = 24.dp, start = 24.dp, end = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color.White
                )
            }
            Text(
                text = "Index",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                tint = Color.White,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(UpTodoSurface)
                    .padding(6.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Search Bar matching screenshot
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search for your task...", color = UpTodoHint) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = UpTodoHint)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = UpTodoSurface,
                unfocusedContainerColor = UpTodoSurface,
                disabledContainerColor = UpTodoSurface,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = UpTodoPrimary
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Filter Dropdown Button matching screenshot ("Today v")
        Box {
            Surface(
                color = UpTodoSurface,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.clickable { expanded = true }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = filterOption, color = Color.White, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Dropdown", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(UpTodoSurface)
            ) {
                DropdownMenuItem(
                    text = { Text("Today (Active)", color = Color.White) },
                    onClick = { filterOption = "Today"; expanded = false }
                )
                DropdownMenuItem(
                    text = { Text("Completed", color = Color.White) },
                    onClick = { filterOption = "Completed"; expanded = false }
                )
                DropdownMenuItem(
                    text = { Text("All Tasks", color = Color.White) },
                    onClick = { filterOption = "All"; expanded = false }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (tasks.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .background(UpTodoSurface.copy(alpha = 0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Illustration Placeholder", color = UpTodoHint)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "What do you want to do today?",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tap + to add your tasks",
                        color = UpTodoHint,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else if (displayTasks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(UpTodoSurface.copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "No task pending",
                            tint = UpTodoHint,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No task pending",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(displayTasks) { task ->
                    TaskItem(
                        task = task,
                        onTaskClick = onTaskClick,
                        onToggleCompletion = onToggleCompletion
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onTaskClick: (Task) -> Unit = {},
    onToggleCompletion: (Task) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(UpTodoSurface, shape = MaterialTheme.shapes.small)
            .clickable { onTaskClick(task) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = task.isCompleted,
            onClick = { onToggleCompletion(task) },
            colors = RadioButtonDefaults.colors(
                selectedColor = UpTodoPrimary,
                unselectedColor = UpTodoHint
            )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = task.title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Today At 16:45",
                color = UpTodoHint,
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!task.categoryId.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .background(UpTodoPrimary.copy(alpha = 0.2f), MaterialTheme.shapes.small)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = task.categoryId, color = UpTodoPrimary, fontSize = 12.sp)
                }
            }
            if (task.priority != null) {
                Surface(
                    color = UpTodoBackground,
                    shape = MaterialTheme.shapes.small
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "⚑", color = UpTodoHint, fontSize = 10.sp)
                        Text(text = "${task.priority}", color = Color.White, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
