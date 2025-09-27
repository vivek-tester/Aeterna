package com.aeterna.aeterna.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aeterna.core.domain.Song
import com.aeterna.core.domain.usecase.YouTubeMusicUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

data class SearchState(
        val isLoading: Boolean = false,
        val query: String = "",
        val results: Map<String, List<Any>> = emptyMap(),
        val suggestions: List<String> = emptyList(),
        val error: String? = null
)

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(private val youtubeMusicUseCases: YouTubeMusicUseCases) :
        ViewModel() {

    private val _searchState = MutableStateFlow(SearchState())
    val searchState: StateFlow<SearchState> = _searchState.asStateFlow()

    private val _searchHistory = MutableStateFlow<List<String>>(emptyList())
    val searchHistory: StateFlow<List<String>> = _searchHistory.asStateFlow()

    private val _trendingSearches = MutableStateFlow<List<String>>(emptyList())
    val trendingSearches: StateFlow<List<String>> = _trendingSearches.asStateFlow()

    private val searchQueryFlow = MutableStateFlow("")

    init {
        // Set up reactive search with debouncing
        viewModelScope.launch {
            searchQueryFlow
                    .debounce(300)
                    .distinctUntilChanged()
                    .filter { it.isNotEmpty() }
                    .flatMapLatest { query -> performSearchInternal(query) }
                    .catch { error ->
                        _searchState.value =
                                _searchState.value.copy(isLoading = false, error = error.message)
                    }
                    .collect { results ->
                        _searchState.value =
                                _searchState.value.copy(
                                        isLoading = false,
                                        results = results,
                                        error = null
                                )
                    }
        }

        loadTrendingSearches()
    }

    fun searchContent(query: String) {
        _searchState.value = _searchState.value.copy(query = query, isLoading = query.isNotEmpty())
        searchQueryFlow.value = query
    }

    fun performSearch(query: String) {
        searchContent(query)
    }

    private suspend fun performSearchInternal(query: String) =
            try {
                val searchResults = youtubeMusicUseCases.searchYouTubeMusic(query)
                searchResults.collect { results ->
                    // Categorize results
                    val categorizedResults = categorizeSearchResults(results)
                    categorizedResults
                }
            } catch (e: Exception) {
                flowOf(emptyMap<String, List<Any>>())
            }

    private fun categorizeSearchResults(results: List<Any>): Map<String, List<Any>> {
        return results.groupBy { item ->
            when (item) {
                is Song -> "Songs"
                // Add other types when available
                else -> "Other"
            }
        }
    }

    fun addToSearchHistory(query: String) {
        viewModelScope.launch {
            val currentHistory = _searchHistory.value.toMutableList()
            currentHistory.remove(query) // Remove if already exists
            currentHistory.add(0, query) // Add to top
            if (currentHistory.size > 20) {
                currentHistory.removeAt(currentHistory.size - 1)
            }
            _searchHistory.value = currentHistory
        }
    }

    fun removeFromSearchHistory(query: String) {
        viewModelScope.launch { _searchHistory.value = _searchHistory.value.filter { it != query } }
    }

    fun clearSearch() {
        _searchState.value = SearchState()
        searchQueryFlow.value = ""
    }

    private fun loadTrendingSearches() {
        viewModelScope.launch {
            // Mock trending searches - in real app, fetch from API
            _trendingSearches.value =
                    listOf(
                            "Taylor Swift",
                            "The Weeknd",
                            "Billie Eilish",
                            "Ed Sheeran",
                            "Ariana Grande",
                            "Drake",
                            "Olivia Rodrigo",
                            "Post Malone",
                            "Dua Lipa",
                            "Harry Styles"
                    )
        }
    }
}
