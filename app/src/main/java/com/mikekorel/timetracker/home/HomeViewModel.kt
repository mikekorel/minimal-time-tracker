package com.mikekorel.timetracker.home

import com.mikekorel.timetracker.core.CoreViewModel
import com.mikekorel.timetracker.home.HomeScreenContract.Event
import com.mikekorel.timetracker.home.HomeScreenContract.State
import com.mikekorel.timetracker.home.HomeScreenContract.State.Effect
import com.mikekorel.timetracker.models.UserActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer

@HiltViewModel
class HomeViewModel @Inject constructor() : CoreViewModel<Event, State, Effect>(State()) {

    private var timer: Timer? = null
    override suspend fun handleEvent(event: Event) {
        with(event) {
            when (this) {
                is Event.OnActivityNameChange -> {
                    setState { copy(sheetActivityToCreate = sheetActivityToCreate.copy(name = newActName)) }
                }

                Event.OnClickCreate -> {
                    setState { copy(
                        activityList = activityList.plus(sheetActivityToCreate),
                        sheetActivityToCreate = UserActivity()
                    ) }
                    setEffect { Effect.ShowOrHideSheet(false) }
                }

                Event.OnBackPressedWhenSheetVisible -> {
                    setEffect { Effect.ShowOrHideSheet(false) }
                }

                Event.OnAddClicked -> {
                    setEffect { Effect.ShowOrHideSheet(true) }
                }

                is Event.OnItemClicked -> {
                    handleItemClick(itemClicked)
                }
            }
        }
    }

    private fun handleItemClick(itemClicked: UserActivity) {
        val activeItem = state().value.activeActivity
        val activeItemWasPressed = itemClicked == activeItem
        // stop active in any case
        val currTime = System.currentTimeMillis()
        val timeActive = (currTime - (activeItem?.activeSince ?: currTime)) / 1000
        activeItem?.apply {
            totalTimeActive += timeActive
            activeSince = null
        }
        timer?.cancel()
        timer = null
        if (activeItemWasPressed) {
            setState { copy(activeActivity = null) }
        } else {
            // if a non-active activity was pressed, start it
            setState { copy(activeActivity = itemClicked) }
            itemClicked.activeSince = System.currentTimeMillis()
            startTimer(itemClicked)
        }
    }

    private fun startTimer(activity: UserActivity) {
        timer = fixedRateTimer(
            period = 1000,
            daemon = true
        ) {
            activity.activeSince?.let { startTime ->
                setState { copy(activeTime = (System.currentTimeMillis() - startTime) / 1000) }
            }
        }
    }
}