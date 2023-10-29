package com.mikekorel.timetracker.home

import com.mikekorel.timetracker.core.UiEffect
import com.mikekorel.timetracker.core.UiEvent
import com.mikekorel.timetracker.core.UiState
import com.mikekorel.timetracker.models.UserActivity

interface HomeScreenContract {

    sealed interface Event: UiEvent {
        data class OnActivityNameChange(val newActName: String): Event
        object OnClickCreate: Event
        object OnBackPressedWhenSheetVisible: Event
        object OnAddClicked : Event
        data class OnItemClicked(val itemClicked: UserActivity) : Event
    }

    data class State(
        val activityList: List<UserActivity> = listOf(),
        val activeActivity: UserActivity? = null,
        val activeTime: Long? = null,

        val sheetActivityToCreate: UserActivity = UserActivity(),

        override var isLoading: Boolean = false,
        override var hasError: Boolean = false,
    ): UiState {

        sealed interface Effect : UiEffect {
            data class ShowOrHideSheet(val show: Boolean) : Effect
        }
    }
}