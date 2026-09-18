package com.example.to_doapplication.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.to_doapplication.R
import com.example.to_doapplication.auth.AuthManager
import com.example.to_doapplication.data.Task
import com.example.to_doapplication.ui.TaskViewModel
import com.example.to_doapplication.ui.home.AddTaskBottomSheet
import com.example.to_doapplication.ui.home.IndexScreen
import com.example.to_doapplication.ui.home.TaskDetailScreen
import com.example.to_doapplication.ui.calendar.CalendarScreen
import com.example.to_doapplication.ui.focus.FocusScreen
import com.example.to_doapplication.ui.profile.ProfileScreen
import com.example.to_doapplication.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun MainScreen(viewModel: TaskViewModel, authManager: AuthManager) {
    var selectedTab by remember { mutableStateOf(0) }
    var showAddBottomSheet by remember { mutableStateOf(false) }
    var selectedTaskForDetail by remember { mutableStateOf<Task?>(null) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val tasks by viewModel.tasks.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = UpTodoSurface,
                modifier = Modifier.width(300.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(UpTodoBackground),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.baked_goods_1),
                            contentDescription = "Profile Picture",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = authManager.getCurrentUserDisplayName() ?: "Martha Hays",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            color = UpTodoBackground,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${tasks.count { !it.isCompleted }} Task left",
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            }
                        }
                        Surface(
                            color = UpTodoBackground,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${tasks.count { it.isCompleted }} Task done",
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    DrawerSectionTitle("Settings")
                    DrawerMenuItem(icon = Icons.Default.Settings, text = "App Settings") {
                        scope.launch { drawerState.close() }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    DrawerSectionTitle("Account")
                    DrawerMenuItem(icon = Icons.Default.AccountCircle, text = "Change account name") {
                        scope.launch { drawerState.close() }
                    }
                    DrawerMenuItem(icon = Icons.Default.Lock, text = "Change account password") {
                        scope.launch { drawerState.close() }
                    }
                    DrawerMenuItem(icon = Icons.Default.Create, text = "Change account Image") {
                        scope.launch { drawerState.close() }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    DrawerSectionTitle("Uptodo")
                    DrawerMenuItem(icon = Icons.Default.Info, text = "About US") {
                        scope.launch { drawerState.close() }
                    }
                    DrawerMenuItem(icon = Icons.Default.Info, text = "FAQ") {
                        scope.launch { drawerState.close() }
                    }
                    DrawerMenuItem(icon = Icons.Default.Build, text = "Help & Feedback") {
                        scope.launch { drawerState.close() }
                    }
                    DrawerMenuItem(icon = Icons.Default.ThumbUp, text = "Support US") {
                        scope.launch { drawerState.close() }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                scope.launch { drawerState.close() }
                                viewModel.signOut()
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Log out",
                            tint = UpTodoError,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Log out",
                            color = UpTodoError,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    ) {
        Scaffold(
            containerColor = UpTodoBackground,
            bottomBar = {
                NavigationBar(
                    containerColor = UpTodoSurface,
                    contentColor = Color.White
                ) {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Index") },
                        label = { Text("Index") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = UpTodoPrimary,
                            unselectedIconColor = Color.White,
                            selectedTextColor = UpTodoPrimary,
                            unselectedTextColor = Color.White,
                            indicatorColor = Color.Transparent
                        )
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = { Icon(Icons.Default.DateRange, contentDescription = "Calendar") },
                        label = { Text("Calendar") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = UpTodoPrimary,
                            unselectedIconColor = Color.White,
                            selectedTextColor = UpTodoPrimary,
                            unselectedTextColor = Color.White,
                            indicatorColor = Color.Transparent
                        )
                    )
                    // Spacer for the center FAB
                    NavigationBarItem(
                        selected = false,
                        onClick = { },
                        icon = { Box(modifier = Modifier) },
                        label = { },
                        enabled = false
                    )
                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        icon = { Icon(Icons.Default.CheckCircle, contentDescription = "Focuse") },
                        label = { Text("Focuse") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = UpTodoPrimary,
                            unselectedIconColor = Color.White,
                            selectedTextColor = UpTodoPrimary,
                            unselectedTextColor = Color.White,
                            indicatorColor = Color.Transparent
                        )
                    )
                    NavigationBarItem(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                        label = { Text("Profile") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = UpTodoPrimary,
                            unselectedIconColor = Color.White,
                            selectedTextColor = UpTodoPrimary,
                            unselectedTextColor = Color.White,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showAddBottomSheet = true },
                    shape = CircleShape,
                    containerColor = UpTodoPrimary,
                    contentColor = Color.White,
                    modifier = Modifier.offset(y = 40.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Task")
                }
            },
            floatingActionButtonPosition = FabPosition.Center
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (selectedTab) {
                    0 -> {
                        IndexScreen(
                            tasks = tasks,
                            onTaskClick = { selectedTaskForDetail = it },
                            onToggleCompletion = { viewModel.toggleTaskCompletion(it) },
                            onMenuClick = { scope.launch { drawerState.open() } }
                        )
                    }
                    1 -> {
                        CalendarScreen(
                            tasks = tasks,
                            onTaskClick = { selectedTaskForDetail = it },
                            onToggleCompletion = { viewModel.toggleTaskCompletion(it) }
                        )
                    }
                    2 -> FocusScreen()
                    3 -> {
                        ProfileScreen(
                            tasks = tasks,
                            userName = authManager.getCurrentUserDisplayName(),
                            onLogoutClick = { viewModel.signOut() }
                        )
                    }
                }
            }
        }
    }

    if (selectedTaskForDetail != null) {
        TaskDetailScreen(
            task = selectedTaskForDetail!!,
            onBack = { selectedTaskForDetail = null },
            onUpdateTask = { viewModel.updateTask(it) },
            onDeleteTask = { viewModel.deleteTask(it) }
        )
    }

    if (showAddBottomSheet) {
        AddTaskBottomSheet(
            onDismiss = { showAddBottomSheet = false },
            onSave = { title, description, categoryId, priority, dueDate, dueTime ->
                viewModel.addTask(title, description, categoryId, priority, dueDate, dueTime)
                showAddBottomSheet = false
            }
        )
    }
}

@Composable
fun DrawerSectionTitle(title: String) {
    Text(
        text = title,
        color = UpTodoHint,
        fontSize = 13.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
            textAlign = TextAlign.Start
    )
}

@Composable
fun DrawerMenuItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = UpTodoIcon,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                color = UpTodoOnSurface,
                fontSize = 15.sp
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Navigate",
            tint = UpTodoIcon,
            modifier = Modifier.size(18.dp)
        )
    }
}
