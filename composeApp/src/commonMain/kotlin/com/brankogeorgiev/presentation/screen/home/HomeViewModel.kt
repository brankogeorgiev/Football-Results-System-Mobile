package com.brankogeorgiev.presentation.screen.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brankogeorgiev.data.network.ApiClient
import com.brankogeorgiev.util.Result
import kotlinx.coroutines.launch

class HomeViewModel(
    private val client: ApiClient
) : ViewModel() {
    private var _uiState: MutableState<HomeUIState> = mutableStateOf(HomeUIState())
    val uiState: State<HomeUIState> = _uiState

    init {
        viewModelScope.launch {
            loadMatches()
        }
    }

    suspend fun loadMatches() {
        when (val response = client.fetchMatches()) {
            is Result.Success -> {
                _uiState.value = _uiState.value.copy(matches = response.data)
            }

            is Result.Error<*> -> {
                TODO()
            }
        }
    }
}