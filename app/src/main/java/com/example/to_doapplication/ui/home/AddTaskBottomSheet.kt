package com.example.to_doapplication.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.to_doapplication.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskBottomSheet(
    onDismiss: () -> Unit,
    onSave: (title: String, description: String, categoryId: String?, priority: Int?, dueDate: Long?, dueTime: Long?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    
    var priority by remember { mutableStateOf<Int?>(null) }
    var categoryId by remember { mutableStateOf<String?>(null) }
    var dueDate by remember { mutableStateOf<Long?>(null) }
    var dueTime by remember { mutableStateOf<Long?>(null) }

    var showPriorityDialog by remember { mutableStateOf(false) }
    var showCategoryDialog by remember { mutableStateOf(false) }
    var showDateDialog by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Priority Grid Dialog matching screenshot
    if (showPriorityDialog) {
        AlertDialog(
            onDismissRequest = { showPriorityDialog = false },
            containerColor = UpTodoSurface,
            title = {
                Text(
                    text = "Edit Task Priority",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            text = {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.height(220.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items((1..10).toList()) { p ->
                        val isSelected = priority == p
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    color = if (isSelected) UpTodoPrimary else UpTodoBackground,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { priority = p },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "⚑", color = if (isSelected) Color.White else UpTodoHint, fontSize = 14.sp)
                                Text(text = "$p", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = { showPriorityDialog = false }) {
                        Text("Cancel", color = UpTodoPrimary)
                    }
                    Button(
                        onClick = { showPriorityDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = UpTodoPrimary)
                    ) {
                        Text("Save")
                    }
                }
            }
        )
    }

    // Category Grid Dialog matching screenshot
    if (showCategoryDialog) {
        val categories = listOf(
            Triple("Grocery", CategoryGrocery, Color.Black),
            Triple("Work", CategoryWork, Color.Black),
            Triple("Sport", CategorySport, Color.Black),
            Triple("Design", CategoryDesign, Color.Black),
            Triple("University", CategoryUniversity, Color.Black),
            Triple("Social", CategorySocial, Color.Black),
            Triple("Music", CategoryMusic, Color.Black),
            Triple("Health", CategoryHealth, Color.Black),
            Triple("Movie", CategoryMovie, Color.Black),
            Triple("Home", CategoryHome, Color.Black)
        )
        AlertDialog(
            onDismissRequest = { showCategoryDialog = false },
            containerColor = UpTodoSurface,
            title = {
                Text(
                    text = "Choose Category",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            text = {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.height(280.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(categories) { (catName, catColor, _) ->
                        val isSelected = categoryId == catName
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable { categoryId = catName }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .background(catColor, RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = catName.take(3).uppercase(), color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = catName, color = if (isSelected) UpTodoPrimary else Color.White, fontSize = 12.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = { showCategoryDialog = false }) {
                        Text("Cancel", color = UpTodoPrimary)
                    }
                    Button(
                        onClick = { showCategoryDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = UpTodoPrimary)
                    ) {
                        Text("Select")
                    }
                }
            }
        )
    }

    // Material 3 DatePickerDialog
    if (showDateDialog) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDateDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            dueDate = it
                            dueTime = it
                        }
                        showDateDialog = false
                    }
                ) {
                    Text("Select", color = UpTodoPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDateDialog = false }) {
                    Text("Cancel", color = UpTodoHint)
                }
            },
            colors = DatePickerDefaults.colors(containerColor = UpTodoSurface)
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = UpTodoSurface,
                    selectedDayContainerColor = UpTodoPrimary,
                    selectedDayContentColor = Color.White,
                    todayContentColor = UpTodoPrimary,
                    todayDateBorderColor = UpTodoPrimary,
                    dayContentColor = Color.White,
                    yearContentColor = Color.White,
                    subheadContentColor = Color.White,
                    headlineContentColor = Color.White
                )
            )
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = UpTodoSurface,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "Add Task",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("Title", color = UpTodoHint) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = UpTodoPrimary,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = UpTodoPrimary
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Description", color = UpTodoHint) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = UpTodoPrimary,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = UpTodoPrimary
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    IconButton(onClick = { showDateDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Timer",
                            tint = if (dueDate != null) UpTodoPrimary else Color.White
                        )
                    }
                    IconButton(onClick = { showCategoryDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Tag",
                            tint = if (categoryId != null) UpTodoPrimary else Color.White
                        )
                    }
                    IconButton(onClick = { showPriorityDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Flag",
                            tint = if (priority != null) UpTodoPrimary else Color.White
                        )
                    }
                }
                IconButton(
                    onClick = {
                        if (title.isNotBlank()) {
                            onSave(title, description, categoryId, priority, dueDate, dueTime)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = UpTodoPrimary
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
