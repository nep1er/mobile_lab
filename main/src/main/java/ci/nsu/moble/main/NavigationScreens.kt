package ci.nsu.moble.main

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object SecondNav : Screen("second_nav")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
}