package com.aeterna.aeterna

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aeterna.core.data.datastore.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val authDataStore: AuthDataStore
) : ViewModel() {

    val isAuthenticated: StateFlow<Boolean> = authDataStore.getAuthStateFlow()
        .map { it != null }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )
}