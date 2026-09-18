package com.example.to_doapplication.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.to_doapplication.R
import com.example.to_doapplication.ui.theme.*

@Composable
fun OnboardingScreen(
    onSkip: () -> Unit,
    onGetStarted: () -> Unit
) {
    var currentPage by remember { mutableIntStateOf(0) }
    val pages = listOf(
        Pair("Manage your tasks", "You can easily manage all of your daily tasks in DoMe for free"),
        Pair("Create daily routine", "In Uptodo you can create your personalized routine to stay productive"),
        Pair("Organize your tasks", "You can organize your daily tasks by adding your tasks into separate categories")
    )
    val images = listOf(
        R.drawable.frame_161,
        R.drawable.frame_162,
        R.drawable.frame_182,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UpTodoBackground)
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            TextButton(onClick = onSkip) {
                Text("SKIP", color = UpTodoOnBackground.copy(alpha = 0.5f))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(UpTodoSurface),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = images[currentPage]),
                contentDescription = "Onboarding Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = pages[currentPage].first,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = UpTodoOnBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = pages[currentPage].second,
            fontSize = 16.sp,
            color = UpTodoOnBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = { if (currentPage > 0) currentPage-- },
                enabled = currentPage > 0
            ) {
                Text(
                    text = "BACK", 
                    color = if (currentPage > 0) UpTodoOnBackground.copy(alpha = 0.5f) else Color.Transparent
                )
            }

            Button(
                onClick = {
                    if (currentPage < pages.size - 1) {
                        currentPage++
                    } else {
                        onGetStarted()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = UpTodoPrimary)
            ) {
                Text(
                    text = if (currentPage == pages.size - 1) "GET STARTED" else "NEXT", 
                    color = UpTodoOnPrimary
                )
            }
        }
    }
}
