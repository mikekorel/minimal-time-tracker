package com.mikekorel.timetracker.home

import com.mikekorel.timetracker.core.UiEffect
import com.mikekorel.timetracker.core.UiEvent
import com.mikekorel.timetracker.core.UiState

interface HomeScreenContract {

    sealed interface Event: UiEvent {
        data class OnNameChange(val newName: String): Event
    }

    data class State(
        val name: String = "",

        override var isLoading: Boolean = false,
        override var hasError: Boolean = false,
    ): UiState {

        sealed interface Effect: UiEffect {

        }
    }
}