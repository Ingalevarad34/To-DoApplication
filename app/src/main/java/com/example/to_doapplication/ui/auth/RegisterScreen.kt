package com.example.to_doapplication.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.to_doapplication.ui.theme.*

@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onRegisterClick: (String, String) -> Unit,
    onGoogleRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
    errorMessage: String? = null
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UpTodoBackground)
            .padding(24.dp)
    ) {
        IconButton(onClick = onBack, modifier = Modifier.offset(x = (-12).dp)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = UpTodoIcon)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Register",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = UpTodoOnBackground
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text("Email", color = UpTodoOnBackground)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = UpTodoOnBackground,
                unfocusedTextColor = UpTodoOnBackground,
                focusedBorderColor = UpTodoPrimary,
                unfocusedBorderColor = UpTodoHint
            ),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Password", color = UpTodoOnBackground)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = UpTodoOnBackground,
                unfocusedTextColor = UpTodoOnBackground,
                focusedBorderColor = UpTodoPrimary,
                unfocusedBorderColor = UpTodoHint
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Confirm Password", color = UpTodoOnBackground)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = UpTodoOnBackground,
                unfocusedTextColor = UpTodoOnBackground,
                focusedBorderColor = UpTodoPrimary,
                unfocusedBorderColor = UpTodoHint
            ),
            singleLine = true
        )

        val displayError = localError ?: errorMessage
        if (displayError != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = displayError, color = UpTodoError, fontSize = 14.sp)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = {
                if (password != confirmPassword) {
                    localError = "Passwords do not match"
                } else if (email.isBlank() || password.isBlank()) {
                    localError = "Fields cannot be blank"
                } else {
                    localError = null
                    onRegisterClick(email.trim(), password)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = UpTodoPrimary)
        ) {
            Text("Register", color = UpTodoOnPrimary)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f).height(1.dp).background(UpTodoHint))
            Text(" or ", color = UpTodoHint, modifier = Modifier.padding(horizontal = 8.dp))
            Spacer(modifier = Modifier.weight(1f).height(1.dp).background(UpTodoHint))
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        OutlinedButton(
            onClick = onGoogleRegisterClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = UpTodoOnBackground)
        ) {
            Text("Register with Google", color = UpTodoOnBackground)
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Already have an account? ", color = UpTodoHint)
            TextButton(onClick = onLoginClick) {
                Text("Login", color = UpTodoOnBackground)
            }
        }
    }
}
