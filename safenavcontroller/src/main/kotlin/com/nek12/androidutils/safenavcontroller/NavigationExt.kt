@file:Suppress("unused")

package com.nek12.androidutils.safenavcontroller

import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController

internal const val Deprecation = """
    XML is deprecated.
    The maintainer team no longer uses XML and cannot provide support for this module anymore.
    Please either: 
      - Migrate to Compose and use KMPUtils https://github.com/respawn-app/KMPUtils
      - Copy and paste these extensions into your project
"""

@Deprecated(Deprecation)
fun Fragment.popBackStack() = findNavController().popBackStack()

@Deprecated(Deprecation)
fun Fragment.findSafeNavController(@IdRes currentFragmentNavId: Int): SafeNavController =
    SafeNavController(currentFragmentNavId, findNavController())

@Deprecated(Deprecation)
fun Fragment.navigateUp() = findNavController().navigateUp()

@Deprecated(Deprecation)
fun Fragment.tryNavigate(directions: NavDirections, navOptions: NavOptions? = null) =
    findNavController().tryNavigate(directions, navOptions)

@Deprecated(Deprecation)
fun Fragment.tryNavigate(directions: NavDirections, extras: Navigator.Extras) =
    findNavController().tryNavigate(directions, extras)

@Deprecated(Deprecation)
fun Fragment.tryNavigate(
    @IdRes to: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) = findNavController().tryNavigate(to, args, navOptions, navigatorExtras)

@Deprecated(Deprecation)
fun Fragment.canNavigateUp() = findNavController().canNavigateUp

@Deprecated(Deprecation)
fun NavController.tryNavigate(directions: NavDirections, navOptions: NavOptions? = null): Boolean =
    tryLogging { navigate(directions, navOptions) }

@Deprecated(Deprecation)
fun NavController.tryNavigate(directions: NavDirections, extras: Navigator.Extras): Boolean =
    tryLogging { navigate(directions, extras) }

/**
 * @return true if navigation was successful
 */
@Deprecated(Deprecation)
fun NavController.tryNavigate(
    @IdRes to: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
): Boolean = tryLogging { navigate(to, args, navOptions, navigatorExtras) }

val NavController.canNavigateUp get() = previousBackStackEntry != null

private fun tryLogging(block: () -> Unit): Boolean =
    runCatching { block() }.onFailure { Log.e("tryNavigate", "Unable to navigate", it) }.isSuccess
