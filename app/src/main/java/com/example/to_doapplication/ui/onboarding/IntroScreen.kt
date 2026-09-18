package com.example.to_doapplication.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.to_doapplication.ui.theme.UpTodoBackground
import com.example.to_doapplication.ui.theme.UpTodoOnBackground
import com.example.to_doapplication.ui.theme.UpTodoPrimary

import kotlinx.coroutines.delay

@Composable
fun IntroScreen(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000L)
        onTimeout()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UpTodoBackground),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Logo",
            modifier = Modifier.size(96.dp),
            tint = UpTodoPrimary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "UpTodo",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = UpTodoOnBackground
        )
    }
}
