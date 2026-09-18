package com.example.to_doapplication.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.to_doapplication.data.Task
import com.example.to_doapplication.ui.home.TaskItem
import com.example.to_doapplication.ui.theme.*
import java.util.Calendar
import java.util.Locale

data class CalendarDay(
    val dateMillis: Long,
    val dayOfWeek: String,
    val dayOfMonth: String,
    val year: Int,
    val month: Int,
    val day: Int
)

@Composable
fun CalendarScreen(
    tasks: List<Task> = emptyList(),
    onTaskClick: (Task) -> Unit = {},
    onToggleCompletion: (Task) -> Unit = {}
) {
    // Generate days (30 days in past to 30 days in future)
    val calendarDays = remember {
        val list = mutableListOf<CalendarDay>()
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -30)
        for (i in 0..60) {
            val millis = cal.timeInMillis
            val dayOfWeekStr = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())?.uppercase() ?: ""
            val dayOfMonthStr = cal.get(Calendar.DAY_OF_MONTH).toString()
            val y = cal.get(Calendar.YEAR)
            val m = cal.get(Calendar.MONTH)
            val d = cal.get(Calendar.DAY_OF_MONTH)
            list.add(CalendarDay(millis, dayOfWeekStr, dayOfMonthStr, y, m, d))
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }
        list
    }

    // Default selected day is today
    var selectedDayMillis by remember {
        val today = Calendar.getInstance()
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)
        mutableStateOf(today.timeInMillis)
    }

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Today", "Completed")

    val selectedCal = Calendar.getInstance().apply { timeInMillis = selectedDayMillis }
    val selYear = selectedCal.get(Calendar.YEAR)
    val selMonth = selectedCal.get(Calendar.MONTH)
    val selDay = selectedCal.get(Calendar.DAY_OF_MONTH)

    val baseFilteredTasks = tasks.filter { task ->
        val matchesTab = if (selectedTabIndex == 0) !task.isCompleted else task.isCompleted
        
        val matchesDate = if (task.dueDate != null && task.dueDate > 0) {
            val taskCal = Calendar.getInstance().apply { timeInMillis = task.dueDate }
            taskCal.get(Calendar.YEAR) == selYear &&
            taskCal.get(Calendar.MONTH) == selMonth &&
            taskCal.get(Calendar.DAY_OF_MONTH) == selDay
        } else {
            val todayCal = Calendar.getInstance()
            todayCal.get(Calendar.YEAR) == selYear && todayCal.get(Calendar.MONTH) == selMonth && todayCal.get(Calendar.DAY_OF_MONTH) == selDay
        }

        matchesTab && matchesDate
    }

    // If viewing Completed tab and date filtering yields nothing, fallback to showing all completed tasks
    val filteredTasks = if (baseFilteredTasks.isEmpty() && selectedTabIndex == 1) {
        tasks.filter { it.isCompleted }
    } else {
        baseFilteredTasks
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UpTodoBackground)
            .padding(16.dp)
    ) {
        // Top Bar
        Text(
            text = "Calendar",
            color = UpTodoOnBackground,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Interactive Horizontal Calendar Strip
        Surface(
            color = UpTodoSurface,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            val listState = rememberLazyListState(initialFirstVisibleItemIndex = 25)
            LazyRow(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(calendarDays) { day ->
                    val isSelected = day.year == selYear && day.month == selMonth && day.day == selDay
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .background(
                                color = if (isSelected) UpTodoPrimary else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { selectedDayMillis = day.dateMillis }
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = day.dayOfWeek,
                            color = if (isSelected) UpTodoOnPrimary else UpTodoHint,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = day.dayOfMonth,
                            color = if (isSelected) UpTodoOnPrimary else UpTodoOnSurface,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(UpTodoSurface, RoundedCornerShape(4.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            tabs.forEachIndexed { index, title ->
                val isSelected = selectedTabIndex == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = if (isSelected) UpTodoPrimary else Color.Transparent,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(vertical = 12.dp)
                        .clickable { selectedTabIndex = index },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        color = if (isSelected) UpTodoOnPrimary else UpTodoOnSurface,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tasks List or Empty State
        if (filteredTasks.isEmpty()) {
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
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredTasks) { task ->
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
