package com.mikekorel.timetracker.home

import com.mikekorel.timetracker.core.UiEffect
import com.mikekorel.timetracker.core.UiEvent
import com.mikekorel.timetracker.core.UiState
import com.mikekorel.timetracker.models.UserActivity

interface HomeScreenContract {

    sealed interface Event: UiEvent {
        data class OnNameChange(val newName: String): Event
        data class OnActivityNameChange(val newActName: String): Event
        object OnClickCreate: Event
        object OnBackPressedWhenSheetVisible: Event
        object OnAddClicked : Event
    }

    data class State(
        val name: String = "",
        val activities: List<UserActivity> = listOf(),

        val sheetData: SheetData = SheetData(),

        override var isLoading: Boolean = false,
        override var hasError: Boolean = false,
    ): UiState {

        sealed interface Effect : UiEffect {
            data class ShowOrHideSheet(val show: Boolean) : Effect
        }
    }
}