@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.nek12.androidutils.safenavcontroller

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

/**
 * SafeNavController class is a wrapper around navController that takes care of that
 * DestinationNotFound exception for you. It happens when the user makes multiple fast attempts
 * to navigate, especially if there are animations involved. Wrap your nav controller using
 * findNavController() with this class and use it to navigate or use an extension function.
 * If navigation is not possible, the operation will be simply canceled.
 */
@Deprecated(Deprecation)
class SafeNavController(
    @IdRes val currentFragmentId: Int,
    private val navController: NavController,
) {

    /**
     * @param from The navigation ID of the current fragment you want to navigate from
     * @param to The id of the fragment to navigate
     */
    @Deprecated(Deprecation)
    fun navigate(@IdRes to: Int) = navigate(to, null)

    /**
     * @param from The navigation ID of the current fragment you want to navigate from
     * @param to The id of the fragment to navigate
     * @param bundle Unsafe navigation arguments to pass
     */
    @Deprecated(Deprecation)
    fun navigate(@IdRes to: Int, bundle: Bundle?) =
        navigate(to, bundle, null, null)

    /**
     * @param from The navigation ID of the current fragment you want to navigate from
     * @param to The id of the fragment to navigate
     */
    @Deprecated(Deprecation)
    fun navigate(
        @IdRes to: Int,
        bundle: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?,
    ) {
        if (navController.currentDestination?.id == currentFragmentId) {
            navController.navigate(to, bundle, navOptions, navigatorExtras)
        }
    }

    /**
     * @param from The navigation ID of the current fragment you want to navigate from
     * @param directions Generated NavDirections class. Pass argunments to them if you wish.
     */
    @Deprecated(Deprecation)
    fun navigate(
        directions: NavDirections,
        extras: Navigator.Extras,
    ) {
        if (navController.currentDestination?.id == currentFragmentId) {
            navController.navigate(directions, extras)
        }
    }

    @Deprecated(Deprecation)
    fun navigate(
        directions: NavDirections,
    ) {
        if (navController.currentDestination?.id == currentFragmentId) {
            navController.navigate(directions)
        }
    }

    @Deprecated(Deprecation)
    fun navigateUp() = navController.navigateUp()

    @Deprecated(Deprecation)
    fun popBackStack() = navController.popBackStack()
}
