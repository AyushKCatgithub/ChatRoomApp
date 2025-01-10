package com.example.chatroom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatroom.screen.ChatRoomListScreen
import com.example.chatroom.screen.ChatScreen
import com.example.chatroom.screen.LoginScreen
import com.example.chatroom.screen.Screen
import com.example.chatroom.screen.SignUpScreen
import com.example.chatroom.ui.theme.ChatRoomTheme
import com.example.chatroom.viewmodel.AuthViewModel


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val authViewModel: AuthViewModel = viewModel()

            ChatRoomTheme {
            Surface (modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background){
                NavigationGraph(navController = navController , authViewModel = authViewModel)

            }


            }
        }
    }
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SignupScreen.route
    ) {
        composable(Screen.SignupScreen.route) {
            SignUpScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }
        composable(Screen.LoginScreen.route) {
            LoginScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }
        composable(Screen.ChatRoomsScreen.route) {
            ChatRoomListScreen(){
                navController.navigate(Screen.ChatScreen.route + "/${it.id}")
            }
        }
        composable("${Screen.ChatScreen.route}/{roomId}") {
            val roomId: String = it
                .arguments?.getString("roomId") ?: ""
            ChatScreen(roomId = roomId)
        }
    }

}