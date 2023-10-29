package com.mikekorel.timetracker.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mikekorel.timetracker.R
import com.mikekorel.timetracker.home.HomeScreenContract.*
import com.mikekorel.timetracker.models.UserActivity

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddActivityBottomSheet(
    sheetActivity: UserActivity,
    state: ModalBottomSheetState,
    onEvent: (Event) -> Unit,
    screenContent: @Composable () -> Unit,
) {
    ModalBottomSheetLayout(
        sheetContent = {
            SheetContent(sheetActivity = sheetActivity, onEvent = onEvent)
        },
        content = screenContent,
        sheetState = state
    )
}

@Composable
fun ColumnScope.SheetContent(
    sheetActivity: UserActivity,
    onEvent: (Event) -> Unit,
) {
    TextField(
        value = sheetActivity.name,
        onValueChange = {
            onEvent(Event.OnActivityNameChange(it))
        },
        placeholder = {
            Text(text = stringResource(R.string.enter_new_activity_name))
        }
    )
    Button(
        onClick = { onEvent(Event.OnClickCreate) }
    ) {
        Text(text = stringResource(R.string.create), maxLines = 1)
    }
}

@Preview(showSystemUi = true)
@Composable
fun SheetPreview() {
    val sheetActivity = UserActivity("Mike")
    Column {
        SheetContent(sheetActivity = sheetActivity, onEvent = { })
    }
}
