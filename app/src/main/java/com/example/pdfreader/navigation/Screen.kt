package com.example.pdfreader.navigation

sealed class Screen(val route: String) {
    data object Main: Screen(route = "main_screen")
    data object Reader: Screen(route = "reader_screen")
}