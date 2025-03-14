package com.zaurh.sparrow.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.zaurh.sparrow.presentation.account.followers.FollowersScreen
import com.zaurh.sparrow.presentation.account.following.FollowingScreen
import com.zaurh.sparrow.presentation.auth.sign_up.SignUpScreen
import com.zaurh.sparrow.presentation.auth.sign_in.SignInScreen
import com.zaurh.sparrow.presentation.home.HomeScreen
import com.zaurh.sparrow.presentation.image.ImageScreen
import com.zaurh.sparrow.presentation.account.profile.ProfileViewModel
import com.zaurh.sparrow.presentation.account.user.UserScreen
import com.zaurh.sparrow.presentation.chat.ChatScreen
import com.zaurh.sparrow.presentation.settings.edit_account.EditAccountScreen
import com.zaurh.sparrow.presentation.settings.SettingsScreen
import com.zaurh.sparrow.shared.AuthViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String,
) {
    val authViewModel = hiltViewModel<AuthViewModel>()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            route = Screen.HomeScreen.route,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it })
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it })
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it })
            }
        ) {
            HomeScreen(
                modifier = modifier,
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(
            route = Screen.ImageScreen.route,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }) // <--
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }) // -->
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }) // <--
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }) // -->
            },
            arguments = listOf(
                navArgument("postId") { type = NavType.StringType },
                navArgument("authorUsername") { type = NavType.StringType },
                navArgument("authorImageUrl") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId") ?: ""
            val authorUsername = backStackEntry.arguments?.getString("authorUsername") ?: ""
            val authorImageUrl = backStackEntry.arguments?.getString("authorImageUrl") ?: ""

            ImageScreen(
                authorUsername = authorUsername,
                authorImageUrl = authorImageUrl,
                postId = postId,
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(
            route = Screen.ChatScreen.route,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }) // <--
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }) // -->
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }) // <--
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }) // -->
            },
            arguments = listOf(
                navArgument("receiverId") { type = NavType.StringType },
                navArgument("receiverUsername") { type = NavType.StringType },
                navArgument("receiverImage") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val receiverId = backStackEntry.arguments?.getString("receiverId") ?: ""
            val receiverUsername = backStackEntry.arguments?.getString("receiverUsername") ?: ""
            val receiverImage = backStackEntry.arguments?.getString("receiverImage") ?: ""

            ChatScreen(
                receiverId = receiverId,
                receiverUsername = receiverUsername,
                receiverImage = receiverImage,
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(
            route = Screen.UserScreen.route,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }) // <--
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }) // -->
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }) // <--
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }) // -->
            },
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val profileViewModel = backStackEntry.sharedViewModel<ProfileViewModel>(navController)

            UserScreen(
                userId = userId,
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(
            route = Screen.EditAccountScreen.route,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }) // <--
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }) // -->
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }) // <--
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }) // -->
            }
        ) {
            EditAccountScreen(
                authViewModel = authViewModel,
                navController = navController
            )
        }

        composable(
            route = Screen.SettingsScreen.route,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }) // <--
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }) // -->
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }) // <--
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }) // -->
            }
        ) {
            SettingsScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }


        composable(
            route = Screen.FollowersScreen.route,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }) // <--
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }) // -->
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }) // <--
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }) // -->
            },
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""

            FollowersScreen(
                userId = userId,
                navController = navController
            )
        }

        composable(
            route = Screen.FollowingScreen.route,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }) // <--
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }) // -->
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }) // <--
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }) // -->
            },
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""

            FollowingScreen(
                userId = userId,
                navController = navController
            )
        }



        composable(
            route = Screen.SignInScreen.route,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }) // <--
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }) // -->
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }) // <--
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }) // -->
            }
        ) {
            SignInScreen(
                modifier = modifier,
                navController = navController,
                authViewModel = authViewModel
            )
        }
        composable(
            route = Screen.SignUpScreen.route,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it })
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it })
            }
        ) {
            SignUpScreen(
                modifier = modifier,
                navController = navController,
                authViewModel = authViewModel
            )
        }

    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavController,
): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry  = remember(this){
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}