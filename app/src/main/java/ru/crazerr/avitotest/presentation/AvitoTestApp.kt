package ru.crazerr.avitotest.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.crazerr.avitotest.R
import ru.crazerr.avitotest.utils.navigation.BottomNavItem
import ru.crazerr.avitotest.utils.navigation.Destination
import ru.crazerr.avitotest.utils.navigation.RootNavHost

@Composable
fun AvitoTestApp(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    val bottomNavItems = listOf(
        BottomNavItem(
            label = R.string.bottom_item_local,
            icon = R.drawable.ic_audiotrack,
            destination = Destination.LocalTracks
        ),
        BottomNavItem(
            label = R.string.bottom_item_api,
            icon = R.drawable.ic_cloud,
            destination = Destination.ApiTracks
        )
    )

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            val route = currentDestination?.route?.substringAfterLast(".")

            if (bottomNavItems.map { it.destination.toString() }.contains(route)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                ) {
                    bottomNavItems.forEach { bottomNavItem ->
                        BottomNavigationItem(
                            modifier = Modifier.weight(1f),
                            icon = painterResource(bottomNavItem.icon),
                            text = stringResource(bottomNavItem.label),
                            selected = route == bottomNavItem.destination.toString(),
                            onClick = {
                                navController.navigate(bottomNavItem.destination) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            RootNavHost(navController = navController)
        }
    }
}

@Composable
private fun BottomNavigationItem(
    modifier: Modifier = Modifier,
    icon: Painter,
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(top = 4.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = if (selected) MaterialTheme.colorScheme.primary else LocalContentColor.current
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall.copy(color = if (selected) MaterialTheme.colorScheme.primary else LocalContentColor.current)
        )
    }
}