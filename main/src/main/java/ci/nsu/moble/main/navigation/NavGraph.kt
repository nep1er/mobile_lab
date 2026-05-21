package ci.nsu.moble.main.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable data object Main : Screen()
    @Serializable data object Stage1 : Screen()
    @Serializable data object Stage2 : Screen()
    @Serializable data object Result : Screen()
    @Serializable data object History : Screen()
}