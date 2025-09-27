package com.aeterna.core.ui.performance

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

/** Performance optimization utilities for Compose UI */

/** Remember a stable reference to prevent unnecessary recompositions */
@Composable
inline fun <T> rememberStable(calculation: @DisallowComposableCalls () -> T): T {
    return remember { calculation() }
}

/** Stable wrapper for callbacks to prevent recompositions */
@Stable class StableCallback<T>(val callback: (T) -> Unit)

@Composable
fun <T> rememberStableCallback(callback: (T) -> Unit): StableCallback<T> {
    return remember { StableCallback(callback) }
}

/** Optimized state collection that only recomposes when value actually changes */
@Composable
fun <T> Flow<T>.collectAsStateOptimized(
        initial: T,
        context: kotlin.coroutines.CoroutineContext = kotlin.coroutines.EmptyCoroutineContext
): State<T> {
    return distinctUntilChanged().collectAsState(initial = initial, context = context)
}

/** Density-aware size calculations */
@Composable
fun rememberDensityAwareSizes(
        smallDp: androidx.compose.ui.unit.Dp,
        mediumDp: androidx.compose.ui.unit.Dp,
        largeDp: androidx.compose.ui.unit.Dp
): Triple<androidx.compose.ui.unit.Dp, androidx.compose.ui.unit.Dp, androidx.compose.ui.unit.Dp> {
    val density = LocalDensity.current
    return remember(density) { Triple(smallDp, mediumDp, largeDp) }
}

/** Lazy loading state for images and content */
@Stable
data class LazyLoadingState(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val isLoaded: Boolean = false
)

@Composable
fun rememberLazyLoadingState(): MutableState<LazyLoadingState> {
    return remember { mutableStateOf(LazyLoadingState()) }
}

/** Reduced motion preferences */
@Composable
fun rememberReducedMotion(reduceMotion: Boolean): Boolean {
    return remember(reduceMotion) { reduceMotion }
}

/** Animation duration based on accessibility preferences */
@Composable
fun rememberAnimationDuration(
        normalDuration: Int = 300,
        reducedDuration: Int = 0,
        reduceMotion: Boolean = false
): Int {
    return remember(reduceMotion) { if (reduceMotion) reducedDuration else normalDuration }
}

/** Stable data class for song information to prevent unnecessary recompositions */
@Stable
data class StableSongInfo(
        val id: String,
        val title: String,
        val artist: String,
        val duration: Long,
        val albumArt: String?
)

/** Stable data class for playlist information */
@Stable
data class StablePlaylistInfo(
        val id: String,
        val name: String,
        val songCount: Int,
        val isPrivate: Boolean,
        val coverArt: String?
)

/** Memory-efficient image loading state */
@Stable
data class ImageLoadingState(
        val isLoading: Boolean = true,
        val hasError: Boolean = false,
        val placeholder: Any? = null
)

@Composable
fun rememberImageLoadingState(url: String?): MutableState<ImageLoadingState> {
    return remember(url) { mutableStateOf(ImageLoadingState(isLoading = url != null)) }
}

/** Debounced search state to prevent excessive API calls */
@Composable
fun rememberDebouncedSearchState(
        initialQuery: String = "",
        debounceTimeMs: Long = 300L
): Pair<String, (String) -> Unit> {
    var query by remember { mutableStateOf(initialQuery) }
    var debouncedQuery by remember { mutableStateOf(initialQuery) }

    LaunchedEffect(query) {
        kotlinx.coroutines.delay(debounceTimeMs)
        debouncedQuery = query
    }

    return debouncedQuery to { newQuery -> query = newQuery }
}

/** Viewport-aware loading for large lists */
@Stable
data class ViewportState(
        val visibleItemCount: Int,
        val firstVisibleIndex: Int,
        val lastVisibleIndex: Int
)

/** Efficient list state management */
@Composable
fun <T> rememberLazyListOptimization(items: List<T>, viewportSize: Int = 20): List<T> {
    return remember(items, viewportSize) {
        if (items.size <= viewportSize) {
            items
        } else {
            items.take(viewportSize)
        }
    }
}
