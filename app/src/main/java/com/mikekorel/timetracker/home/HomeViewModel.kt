package com.mikekorel.timetracker.home

import com.mikekorel.timetracker.core.CoreViewModel
import com.mikekorel.timetracker.home.HomeScreenContract.Event
import com.mikekorel.timetracker.home.HomeScreenContract.State
import com.mikekorel.timetracker.home.HomeScreenContract.State.Effect
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : CoreViewModel<Event, State, Effect>(State()) {
    override suspend fun handleEvent(event: Event) {
        when(event) {
            is Event.OnNameChange -> {
                setState { copy(
                    name = event.newName
                ) }
            }
        }
    }
}