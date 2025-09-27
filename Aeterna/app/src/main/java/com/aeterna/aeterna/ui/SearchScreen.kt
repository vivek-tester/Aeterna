package com.aeterna.aeterna.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.aeterna.core.domain.Song

@Composable
fun SearchScreen(
        onSongClick: (Song) -> Unit = {},
        onNavigateToFullResults: (String, String) -> Unit = { _, _ -> },
        modifier: Modifier = Modifier
) {
    // Enhanced search implementation coming soon
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Universal Search - Coming Soon")
    }
}
