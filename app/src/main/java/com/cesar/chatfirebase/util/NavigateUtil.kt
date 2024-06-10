package com.cesar.chatfirebase.util

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInVertically
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry

fun enterTransition(context:AnimatedContentTransitionScope<NavBackStackEntry>) :EnterTransition {
//   return  fadeIn(
//       animationSpec = tween(
//           300, easing = LinearEasing
//       )
//   ) + context.slideIntoContainer(
//       animationSpec = tween(300, easing = EaseIn),
//       towards = AnimatedContentTransitionScope.SlideDirection.Start
//   )
//    return  slideIn(
//        initialOffset = { IntOffset(0, it.height/2) },
//        animationSpec = tween(50, easing = EaseIn),
//    )
    return EnterTransition.None
}

fun exitTransition(context:AnimatedContentTransitionScope<NavBackStackEntry>) :ExitTransition {
//   return context.slideOutOfContainer(
//        AnimatedContentTransitionScope.SlideDirection.Start, tween(300)
//    )

    return ExitTransition.None
}

fun popEnterTransition(context:AnimatedContentTransitionScope<NavBackStackEntry>) :EnterTransition {
//    return context.slideIntoContainer(
//        AnimatedContentTransitionScope.SlideDirection.End, tween(300)
//    )
    return EnterTransition.None
}