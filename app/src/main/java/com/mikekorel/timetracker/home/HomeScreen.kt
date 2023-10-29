package com.mikekorel.timetracker.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mikekorel.timetracker.R
import com.mikekorel.timetracker.common.collectAsEffect
import com.mikekorel.timetracker.home.HomeScreenContract.Event
import com.mikekorel.timetracker.home.HomeScreenContract.State
import com.mikekorel.timetracker.models.UserActivity
import com.mikekorel.timetracker.ui.theme.TimeTrackerTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state = viewModel.state().collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val bsState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    viewModel.effect().collectAsEffect { effect ->
        when (effect) {
            is State.Effect.ShowOrHideSheet -> {
                coroutineScope.launch {
                    if (effect.show) {
                        bsState.show()
                        focusRequester.requestFocus()
                        keyboardController?.show()
                    } else {
                        bsState.hide()
                        focusRequester.freeFocus()
                        keyboardController?.hide()
                    }
                }
            }
        }
    }

    // override back behavior only if bottom sheet is visible
    BackHandler(bsState.isVisible) {
        viewModel.onEvent(Event.OnBackPressedWhenSheetVisible)
    }

    LaunchedEffect(bsState) {
        // observe sheet state as a flow
        snapshotFlow { bsState.isVisible }.collect { isVisible ->
            if (!isVisible) {
                keyboardController?.hide()
            }
        }
    }

    AddActivityBottomSheet(
        sheetActivity = state.value.sheetActivityToCreate,
        onEvent = viewModel::onEvent,
        state = bsState,
        focusRequester = focusRequester,
        screenContent = {
            HomeScreenContent(
                currState = state.value,
                onEvent = viewModel::onEvent
            )
        },
    )
}

@Composable
fun HomeScreenContent(
    currState: State,
    onEvent: (Event) -> Unit,
) {
    val hasActivities = currState.activityList.isNotEmpty()

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .padding(horizontal = 4.dp)
        ) {
            if (hasActivities) {
                if (currState.activeActivity != null) {
                    Text(
                        text = stringResource(R.string.current_activity),
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .padding(top = 16.dp)
                    )
                    Row(
                        modifier = Modifier
                            .padding(vertical = 16.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = currState.activeActivity.name,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFF1B262C),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .background(Color(0xFF3282B8))
                                .clickable {
                                    onEvent(Event.OnItemClicked(currState.activeActivity))
                                }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                        Text(
                            text = currState.activeTime.toString(),
                            modifier = Modifier
                                .padding(start = 16.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp
                        )
                    }
                }

                Text(
                    text = stringResource(R.string.your_activities_tap_to_start_tracking),
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .padding(horizontal = 4.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 128.dp),
                ) {
                    items(currState.activityList) { activity ->
                        val isActive = activity == currState.activeActivity
                        Text(
                            text = activity.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFF1B262C),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .background(if (!isActive) Color(0xFFBBE1FA) else Color(0xFF3282B8))
                                .clickable {
                                    onEvent(Event.OnItemClicked(activity))
                                }
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }

        if (!hasActivities) {
            Text(
                text = stringResource(R.string.start_by_adding_a_new_activity_to_track),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 32.dp)
                    .padding(bottom = 80.dp),
                color = Color.Black,
            )
        }

        Button(
            onClick = { onEvent(Event.OnAddClicked) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
        ) {
            Text("ADD")
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NoActivitiesPreview() {
    val mockState = State()
    TimeTrackerTheme {
        HomeScreenContent(
            currState = mockState,
            onEvent = { }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun WithActivitiesPreview() {
    val mockActive = UserActivity(name = "Cinema")
    val mockState = State(
        activityList = listOf(
            UserActivity(
                name = "Work",
            ),
            UserActivity(
                name = "Rest",
            ),
            UserActivity(
                name = "Games",
            ),
            UserActivity(
                name = "Gym",
            ),
            mockActive
        ),
        activeActivity = mockActive,
        activeTime = 36,
    )
    TimeTrackerTheme {
        HomeScreenContent(
            currState = mockState,
            onEvent = { }
        )
    }
}