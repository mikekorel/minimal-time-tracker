package com.mikekorel.timetracker.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mikekorel.timetracker.common.collectAsEffect
import com.mikekorel.timetracker.home.HomeScreenContract.Event
import com.mikekorel.timetracker.home.HomeScreenContract.State
import com.mikekorel.timetracker.ui.theme.TimeTrackerTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state = viewModel.state().collectAsState()
    viewModel.effect().collectAsEffect { effect ->
//        when (effect) {
//
//        }
    }
    
    HomeScreenContent(
        currState = state.value,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun HomeScreenContent(
    currState: State,
    onEvent: (Event) -> Unit,
) {
    Box {
        Column(
            modifier = Modifier
                .padding(all = 16.dp)
        ) {
            Text(text = "Insert new habit to track:")
            Row(
                modifier = Modifier
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = currState.name,
                    onValueChange = {
                        onEvent(Event.OnNameChange(it))
                    },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .width(IntrinsicSize.Min)
                        .weight(1f)
                )
                Button(onClick = { }) {
                    Text(text = "Create", maxLines = 1)
                }
            }
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