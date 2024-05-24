package com.cesar.chatfirebase.ui.navigation

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cesar.chatfirebase.ui.chat.ChatScreen
import com.cesar.chatfirebase.ui.login.LoginScreen
import com.cesar.chatfirebase.ui.register.RegisterAccountScreen
import com.cesar.chatfirebase.ui.userList.UserListScreen
import com.cesar.chatfirebase.util.enterTransition
import com.cesar.chatfirebase.util.exitTransition
import com.cesar.chatfirebase.util.popEnterTransition
import com.cesar.domain.model.User
import com.google.gson.Gson


@Composable
fun SetupNavGraph(navController: NavHostController,isLogged : Boolean) {

    val startDestination =  if(isLogged){
        Screen.UserList.route}else{
        Screen.Login.route}

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            route = Screen.Login.route,
            enterTransition = { enterTransition(this) },
            exitTransition = { exitTransition(this) },
            popEnterTransition = {popEnterTransition(this)}
        ) {
            LoginScreen(
                onRegisterUser = {
                    navController.navigate(Screen.Register.route)
                },
                onUserList = {
                    navController.navigate(Screen.UserList.route)
                }
            )
        }

        composable(
            route = Screen.Register.route,
            enterTransition = { enterTransition(this) },
            exitTransition = { exitTransition(this) },
            popEnterTransition = {popEnterTransition(this)}
        ) {
            RegisterAccountScreen(
                onLogin = {
                    navController.navigateUp()
                },
                onUserList = {
                    navController.navigate(Screen.UserList.route)
                }
            )
        }

        composable(
            route = Screen.UserList.route,
            enterTransition = { enterTransition(this) },
            exitTransition = { exitTransition(this) },
            popEnterTransition = {popEnterTransition(this)}
        ) {
            BackHandler(true) {
            }

            UserListScreen(
                onChat =  {
                        val userJson =  Gson().toJson(it)
                        navController.navigate(Screen.Chat.createRoute(userJson))
                },
                onLogin = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        composable(
            route = Screen.Chat.route,
            enterTransition = { enterTransition(this) },
            exitTransition = { exitTransition(this) },
            popEnterTransition = {popEnterTransition(this)},
            arguments = listOf(
                navArgument("user"){defaultValue = "" },
            )

        ) { backStackEntry ->
            val movieJson = backStackEntry.arguments?.getString("user")
            val user =  Gson().fromJson(movieJson, User::class.java)
            requireNotNull(user)
            ChatScreen(
                user = user,
                onUserList = {
                    navController.navigateUp()
                }
            )
        }
    }
}