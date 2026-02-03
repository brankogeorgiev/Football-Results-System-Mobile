package com.brankogeorgiev.presentation.screen.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brankogeorgiev.data.auth.ApiClient
import com.brankogeorgiev.data.auth.UserSession
import com.brankogeorgiev.util.RequestState
import com.brankogeorgiev.util.Result
import kotlinx.coroutines.launch

class HomeViewModel(
    private val client: ApiClient,
    private val userSession: UserSession?
) : ViewModel() {
    private var _uiState: MutableState<HomeUiState> = mutableStateOf(HomeUiState())
    val uiState: State<HomeUiState> = _uiState

    init {
        loadMatches()
    }

    private fun loadMatches() {
        _uiState.value = _uiState.value.copy(matches = RequestState.Loading)

        viewModelScope.launch {
            when (val response = client.fetchMatches()) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        matches = RequestState.Success(data = response.data)
                    )
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        matches = RequestState.Error(message = response.error.name)
                    )
                }
            }
        }
    }
}