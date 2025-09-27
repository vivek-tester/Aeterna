package com.aeterna.aeterna.ui.adaptive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aeterna.aeterna.ui.BottomNavItem
import com.aeterna.aeterna.ui.HomeScreen
import com.aeterna.aeterna.ui.LibraryScreen
import com.aeterna.aeterna.ui.SearchScreen

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AdaptiveMainLayout(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier
) {
    val navigationType = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> NavigationType.BOTTOM_NAVIGATION
        WindowWidthSizeClass.Medium -> NavigationType.NAVIGATION_RAIL
        WindowWidthSizeClass.Expanded -> NavigationType.PERMANENT_NAVIGATION_DRAWER
        else -> NavigationType.BOTTOM_NAVIGATION
    }
    
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Library
    )
    
    when (navigationType) {
        NavigationType.BOTTOM_NAVIGATION -> {
            // Use existing compact layout with bottom navigation
            CompactLayout(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                tabs = tabs,
                modifier = modifier
            )
        }
        
        NavigationType.NAVIGATION_RAIL -> {
            // Medium screens - use navigation rail
            MediumLayout(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                tabs = tabs,
                modifier = modifier
            )
        }
        
        NavigationType.PERMANENT_NAVIGATION_DRAWER -> {
            // Large screens - use permanent navigation drawer
            ExpandedLayout(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                tabs = tabs,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun CompactLayout(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    tabs: List<BottomNavItem>,
    modifier: Modifier = Modifier
) {
    // This would use the existing MainScreen implementation
    // with bottom navigation for phone screens
    Column(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> HomeScreen()
                1 -> SearchScreen()
                2 -> LibraryScreen()
            }
        }
        // Bottom navigation would be here
    }
}

@Composable
private fun MediumLayout(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    tabs: List<BottomNavItem>,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxSize()) {
        // Navigation Rail
        NavigationRail(
            modifier = Modifier.width(80.dp)
        ) {
            tabs.forEachIndexed { index, tab ->
                NavigationRailItem(
                    selected = selectedTab == index,
                    onClick = { onTabSelected(index) },
                    icon = { 
                        androidx.compose.material3.Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.label
                        )
                    },
                    label = {
                        androidx.compose.material3.Text(tab.label)
                    }
                )
            }
        }
        
        // Content Area
        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> HomeScreen()
                1 -> SearchScreen()
                2 -> LibraryScreen()
            }
        }
    }
}

@Composable
private fun ExpandedLayout(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    tabs: List<BottomNavItem>,
    modifier: Modifier = Modifier
) {
    // For large screens, we could implement a three-pane layout
    // Navigation | Content | Detail (e.g., now playing)
    Row(modifier = modifier.fillMaxSize()) {
        // Navigation Drawer
        Column(
            modifier = Modifier
                .width(240.dp)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            tabs.forEachIndexed { index, tab ->
                NavigationDrawerItem(
                    selected = selectedTab == index,
                    onClick = { onTabSelected(index) },
                    icon = tab.icon,
                    label = tab.label
                )
            }
        }
        
        // Main content area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            when (selectedTab) {
                0 -> HomeScreen()
                1 -> SearchScreen()
                2 -> LibraryScreen()
            }
        }
        
        // Optional: Now Playing panel for large screens
        if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded) {
            NowPlayingPanel(
                modifier = Modifier.width(320.dp)
            )
        }
    }
}

@Composable
private fun NavigationDrawerItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.Surface(
        selected = selected,
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            androidx.compose.material3.Icon(
                imageVector = icon,
                contentDescription = label
            )
            androidx.compose.foundation.layout.Spacer(
                modifier = Modifier.width(16.dp)
            )
            androidx.compose.material3.Text(
                text = label,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
private fun NowPlayingPanel(
    modifier: Modifier = Modifier
) {
    // Side panel showing current song, mini player controls, and queue
    androidx.compose.material3.Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            androidx.compose.material3.Text(
                text = \"Now Playing\",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Mini player content would go here
            androidx.compose.material3.Text(
                text = \"Queue and current song details\",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

enum class NavigationType {
    BOTTOM_NAVIGATION,
    NAVIGATION_RAIL,
    PERMANENT_NAVIGATION_DRAWER
}"