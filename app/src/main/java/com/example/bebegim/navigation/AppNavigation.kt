package com.example.bebegim.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bebegim.auth.AuthViewModel
import com.example.bebegim.auth.AuthViewModelFactory
import com.example.bebegim.room.AppDatabase
import com.example.bebegim.screens.AdminScreen
import com.example.bebegim.screens.AuthScreen
import com.example.bebegim.screens.CalendarAndNoteScreen
import com.example.bebegim.screens.ChatbotScreen
import com.example.bebegim.screens.HomeScreen
import com.example.bebegim.screens.LoginScreen
import com.example.bebegim.screens.ProfileScreen
import com.example.bebegim.screens.ReportsScreen
import com.example.bebegim.screens.RequestBabyInfo
import com.example.bebegim.screens.SignupScreen
import com.example.bebegim.screens.ThermalScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Chatbot : Screen("chatbot")
    object Reports : Screen("reports")
    object Profile : Screen("profile")
    object Admin : Screen("admin")
    object Signup : Screen("signup")
    object Auth : Screen("auth")
    object CalendarAndNotes : Screen("calendar_and_notes")
    object RequestBabyInfo : Screen("request_baby_info")
    object ThermalCamera : Screen("thermal_camera")
}

@Composable
fun AppNavigation() {
    var loginEmail by remember { mutableStateOf("") }
    val navController = rememberNavController()
    var signupFullName by remember { mutableStateOf("") }
    var signupEmail by remember { mutableStateOf("") }
    var signupPassword by remember { mutableStateOf("") }

    val context = LocalContext.current
    val db = remember { AppDatabase.getInstance(context) }
    val usersDao = db.UsersDao()

        NavHost(navController = navController, startDestination = Screen.Auth.route) {
        composable(Screen.Auth.route) {
            AuthScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                },
                onNavigateToSignup = {
                    navController.navigate(Screen.Signup.route)
                },
                onLoginSuccess = { isAdmin ->
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { isAdmin, email ->
                    loginEmail = email
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(Screen.Signup.route)
                }
            )
        }

        composable(Screen.Signup.route) {
            SignupScreen(
                onSignupSuccess = { fullName, email, password ->
                    signupFullName = fullName
                    signupEmail = email
                    signupPassword = password
                    navController.navigate(Screen.RequestBabyInfo.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.RequestBabyInfo.route) {
            // Use factory for AuthViewModel
            val authViewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = AuthViewModelFactory(usersDao)
            )
            RequestBabyInfo(
                signupFullName   = signupFullName,
                signupEmail      = signupEmail,
                signupPassword   = signupPassword,
                onBabyInfoSubmitted = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                },
                onBabyInfoCancelled = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(Screen.RequestBabyInfo.route) { inclusive = true }
                    }
                },
                onSignupSuccess = { _, _, _ ->
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                },
                authViewModel    = authViewModel,
                onLogout         = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToChatbot = {
                    navController.navigate(Screen.Chatbot.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToReports = {
                    navController.navigate(Screen.Reports.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route + "/$loginEmail") {
                        launchSingleTop = true
                    }
                },
                onNavigateToCalendarAndNotes = {
                    navController.navigate(Screen.CalendarAndNotes.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToThermalCamera = {
                    navController.navigate(Screen.ThermalCamera.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToAdmin = { navController.navigate(Screen.Admin.route) }
            )
        }

        composable(Screen.Chatbot.route) {
            ChatbotScreen(
                onNavigateBack = { navController.navigate(Screen.Home.route) },
                onNavigateToReports = {
                    navController.navigate(Screen.Reports.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToCalendarAndNotes = {
                    navController.navigate(Screen.CalendarAndNotes.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToChatbot = {
                    navController.navigate(Screen.Chatbot.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToThermalCamera = {
                    navController.navigate(Screen.ThermalCamera.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.Reports.route) {
            ReportsScreen(
                onNavigateBack = { navController.navigate(Screen.Home.route) },
                onNavigateToCalendarAndNotes = {
                    navController.navigate(Screen.CalendarAndNotes.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToChatbot = {
                    navController.navigate(Screen.Chatbot.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToThermalCamera = {
                    navController.navigate(Screen.ThermalCamera.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route) {
                        launchSingleTop = true
                    }
                }

            )
        }

        composable(Screen.CalendarAndNotes.route) {
            CalendarAndNoteScreen(
                onNavigateBack = { navController.navigate(Screen.Home.route) },
                onNavigateToChatbot = {
                    navController.navigate(Screen.Chatbot.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToReports = {
                    navController.navigate(Screen.Reports.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToThermalCamera = {
                    navController.navigate(Screen.ThermalCamera.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.ThermalCamera.route) {
            ThermalScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToChatbot = {
                    navController.navigate(Screen.Chatbot.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToReports = {
                    navController.navigate(Screen.Reports.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToCalendarAndNOte = {
                    navController.navigate(Screen.CalendarAndNotes.route) {
                        launchSingleTop = true
                    }
                }
            )
        }


        composable(Screen.Profile.route + "/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: "-"
            val authViewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = AuthViewModelFactory(usersDao)
            )
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToReports = {
                    navController.navigate(Screen.Reports.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToCalendarAndNotes = {
                    navController.navigate(Screen.CalendarAndNotes.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToChatbot = {
                    navController.navigate(Screen.Chatbot.route) {
                        launchSingleTop = true
                    }
                },
                authViewModel = authViewModel,
                email = email
            )
        }

        composable(Screen.Admin.route) {
            AdminScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}
