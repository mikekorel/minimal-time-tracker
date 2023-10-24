package com.mikekorel.timetracker.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mikekorel.timetracker.common.collectAsEffect
import com.mikekorel.timetracker.home.HomeScreenContract.Event
import com.mikekorel.timetracker.home.HomeScreenContract.State
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

    // override back behavior only if bottom sheet is visible
    BackHandler(bsState.isVisible) {
        viewModel.onEvent(Event.OnBackPressedWhenSheetVisible)
    }

    viewModel.effect().collectAsEffect { effect ->
        when (effect) {
            is State.Effect.ShowOrHideSheet -> {
                coroutineScope.launch {
                    if (effect.show) {
                        bsState.show()
                    } else {
                        bsState.hide()
                    }
                }
            }
        }
    }

    HomeScreenContent(
        currState = state.value,
        onEvent = viewModel::onEvent
    )

    AddActivityBottomSheet(
        data = state.value.sheetData,
        onEvent = viewModel::onEvent,
        state = bsState
    )
}

@Composable
fun HomeScreenContent(
    currState: State,
    onEvent: (Event) -> Unit,
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
        ) {
            if (currState.activeActivity != null) {
                Row(modifier = Modifier
                    .padding(vertical = 16.dp, horizontal = 8.dp)
                    .background(Color.Cyan)
                ) {
                    Text(
                        text = currState.activeActivity.name,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                onEvent(Event.OnItemClicked(currState.activeActivity))
                            }
                    )
                    Text(text = currState.activeTime.toString())
                    Text(text = "   Total: ${currState.activeActivity.totalTimeActive}")
                }
            }
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp),
            ) {
                items(currState.activityList) { activity ->
                    val isActive = activity != currState.activeActivity
                    Text(
                        text = activity.name,
                        modifier = Modifier
                            .padding(8.dp)
                            .background(if (!isActive) Color.Green else Color.Magenta)
                            .clickable {
                                onEvent(Event.OnItemClicked(activity))
                            }
                            .padding(16.dp)
                    )
                }
            }
        }

        Button(
            onClick = { onEvent(Event.OnAddClicked) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(4.dp)
        ) {
            Text("ADD")
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MainActivityContentPreview() {
    TimeTrackerTheme {
        HomeScreen()
    }
}