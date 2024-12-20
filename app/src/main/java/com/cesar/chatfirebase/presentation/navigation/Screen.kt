package com.cesar.chatfirebase.presentation.navigation

sealed class Screen(val route:String) {
    object Login: Screen("login_screen")
    object Register: Screen("register_screen")
    object UserList: Screen("user_list_screen")
    object Setting: Screen("setting_screen")
    object Camera: Screen("camera_screen")
    object CreateGroup: Screen("create_group_screen")
    object Chat : Screen("chat_screen/?user={user}"){
        fun createRoute(user:String) = "chat_screen/?user=$user"
    }
}