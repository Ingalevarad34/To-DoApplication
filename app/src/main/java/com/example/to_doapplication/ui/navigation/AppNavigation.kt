package com.example.to_doapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.to_doapplication.auth.AuthManager
import com.example.to_doapplication.ui.TaskViewModel
import com.example.to_doapplication.ui.auth.LoginScreen
import com.example.to_doapplication.ui.auth.RegisterScreen
import com.example.to_doapplication.ui.auth.StartScreen
import com.example.to_doapplication.ui.main.MainScreen
import com.example.to_doapplication.ui.onboarding.IntroScreen
import com.example.to_doapplication.ui.onboarding.OnboardingScreen
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    object Intro : Screen("intro")
    object Onboarding : Screen("onboarding")
    object Start : Screen("start")
    object Login : Screen("login")
    object Register : Screen("register")
    object Main : Screen("main")
}

@Composable
fun AppNavigation(
    viewModel: TaskViewModel,
    authManager: AuthManager,
    startDestination: String = Screen.Intro.route
) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Intro.route) {
            IntroScreen(onTimeout = {
                navController.navigate(Screen.Onboarding.route) {
                    popUpTo(Screen.Intro.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onGetStarted = {
                    navController.navigate(Screen.Start.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                },
                onSkip = {
                    navController.navigate(Screen.Start.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Start.route) {
            StartScreen(
                onBack = { navController.popBackStack() },
                onLoginClick = { navController.navigate(Screen.Login.route) },
                onCreateAccountClick = { navController.navigate(Screen.Register.route) }
            )
        }
        composable(Screen.Login.route) {
            var loginError by remember { mutableStateOf<String?>(null) }
            LoginScreen(
                onBack = { navController.popBackStack() },
                onLoginClick = { email, password ->
                    scope.launch {
                        val error = authManager.loginWithEmail(email, password)
                        if (error == null) {
                            viewModel.loadTasks()
                            navController.navigate(Screen.Main.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        } else {
                            loginError = error
                        }
                    }
                },
                onGoogleLoginClick = {
                    scope.launch {
                        val success = authManager.signInWithGoogle()
                        if (success) {
                            viewModel.loadTasks()
                            navController.navigate(Screen.Main.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        } else {
                            loginError = "Google sign in failed"
                        }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                errorMessage = loginError
            )
        }
        composable(Screen.Register.route) {
            var registerError by remember { mutableStateOf<String?>(null) }
            RegisterScreen(
                onBack = { navController.popBackStack() },
                onRegisterClick = { email, password ->
                    scope.launch {
                        val error = authManager.registerWithEmail(email, password)
                        if (error == null) {
                            viewModel.loadTasks()
                            navController.navigate(Screen.Main.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        } else {
                            registerError = error
                        }
                    }
                },
                onGoogleRegisterClick = {
                    scope.launch {
                        val success = authManager.signInWithGoogle()
                        if (success) {
                            viewModel.loadTasks()
                            navController.navigate(Screen.Main.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        } else {
                            registerError = "Google sign in failed"
                        }
                    }
                },
                onLoginClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                errorMessage = registerError
            )
        }
        composable(Screen.Main.route) {
            MainScreen(viewModel, authManager)
        }
    }
}
