package com.staygrateful.poi_test.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String) {

    fun withStringKey(key: String): String {
        return "$route/{$key}"
    }

    fun withStringKeys(vararg keys: String): String {
        val sb = StringBuilder(route)
        for (key in keys) {
            sb.append("/{$key}")
        }
        return sb.toString()
    }

    fun withStringArgs(value: String?): String {
        if(value == null) return route
        return "$route/$value"
    }

    fun withStringArgs(vararg values: String): String {
        val sb = StringBuilder(route)
        for (value in values) {
            sb.append("/$value")
        }
        return sb.toString()
    }

    fun withIntArgs(value: Int): String {
        return "$route/$value"
    }

    fun withBooleanArgs(value: Boolean): String {
        return "$route/$value"
    }

    data object RootScreen : Screen(route = "root_screen")

    data object DetailSearchScreen : Screen(route = "detail_search")

    data object ListSearchScreen : Screen(route = "list_search")
}

object Argument {

    fun named(string: String, typeData: NavType<*>): NamedNavArgument {
        return navArgument(string) {
            type = typeData
        }
    }

    fun singleString(key: String): List<NamedNavArgument> {
        return listOf(
            named(key, NavType.StringType)
            //navArgument(key) { type = NavType.StringType }
        )
    }
}