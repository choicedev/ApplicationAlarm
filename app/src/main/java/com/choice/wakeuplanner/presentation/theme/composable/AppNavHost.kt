package com.choice.wakeuplanner.presentation.theme.composable

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.choice.wakeuplanner.core.navigation.Destination
import com.choice.wakeuplanner.presentation.theme.ApplicationTheme

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: Destination = Destination.Home,
    route: String? = null,
    builder: NavGraphBuilder.() -> Unit
) {

    NavHost(
        modifier = modifier.padding(bottom = ApplicationTheme.spacing.gigantic),
        navController = navController,
        startDestination = startDestination.route,
        route = route,
        builder = builder
    )

}

fun NavGraphBuilder.appComposable(
    destination: Destination,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = {
        slideInHorizontally(
            animationSpec = tween(800),
            initialOffsetX = { fullWidth -> fullWidth }
        ) + fadeIn(tween(1000))
    },
    exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = {
        slideOutHorizontally(
            animationSpec = tween(800),
            targetOffsetX = { fullWidth -> -fullWidth }
        ) + fadeOut(tween(1000))
    },
    popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?
    )? = {
        slideInHorizontally(
            animationSpec = tween(800),
            initialOffsetX = { fullWidth -> -fullWidth }
        ) + fadeIn(tween(1000))
    },
    popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?
    )? = {
        slideOutHorizontally(
            animationSpec = tween(800),
            targetOffsetX = { fullWidth -> fullWidth }
        ) + fadeOut(tween(1000))
    },
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {

    composable(
        route = destination.route,
        arguments = arguments,
        deepLinks = deepLinks,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
        content = content
    )

}