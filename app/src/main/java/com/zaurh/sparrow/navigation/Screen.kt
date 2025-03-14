package com.zaurh.sparrow.navigation

sealed class Screen(val route: String) {
    data object SignInScreen : Screen("sign_in_screen")
    data object SignUpScreen : Screen("sign_up_screen")
    data object HomeScreen: Screen("home_screen")
    data object ImageScreen: Screen("image_screen/{postId}/{authorUsername}/{authorImageUrl}")
    data object UserScreen: Screen("user_screen/{userId}")
    data object FollowersScreen: Screen("followers_screen/{userId}")
    data object FollowingScreen: Screen("following_screen/{userId}")
    data object ChatScreen: Screen("chat_screen/{receiverId}/{receiverUsername}/{receiverImage}")
    data object EditAccountScreen: Screen("edit_account_screen")
    data object SettingsScreen: Screen("settings_screen")

}