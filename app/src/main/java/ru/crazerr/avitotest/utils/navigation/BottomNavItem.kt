package ru.crazerr.avitotest.utils.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class BottomNavItem(
    @StringRes
    val label: Int,
    @DrawableRes
    val icon: Int,
    val destination: Destination,
)
