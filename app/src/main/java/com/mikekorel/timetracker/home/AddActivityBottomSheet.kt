package com.mikekorel.timetracker.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikekorel.timetracker.R
import com.mikekorel.timetracker.home.HomeScreenContract.*
import com.mikekorel.timetracker.models.UserActivity

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddActivityBottomSheet(
    sheetActivity: UserActivity,
    state: ModalBottomSheetState,
    onEvent: (Event) -> Unit,
    focusRequester: FocusRequester,
    screenContent: @Composable () -> Unit,
) {
    ModalBottomSheetLayout(
        sheetContent = {
            SheetContent(sheetActivity = sheetActivity, onEvent = onEvent, focusRequester = focusRequester)
        },
        content = screenContent,
        sheetState = state
    )
}

@Composable
fun ColumnScope.SheetContent(
    sheetActivity: UserActivity,
    onEvent: (Event) -> Unit,
    focusRequester: FocusRequester,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = sheetActivity.name,
            onValueChange = {
                onEvent(Event.OnActivityNameChange(it))
            },
            placeholder = {
                Text(text = stringResource(R.string.enter_new_activity_name))
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp),
            shape = RoundedCornerShape(8.dp),
            maxLines = 1,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )
        Button(
            onClick = { onEvent(Event.OnClickCreate) },
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.create),
                maxLines = 1,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Preview(showSystemUi = false)
@Composable
fun SheetPreview() {
    val sheetActivity = UserActivity("Mike")
    Column(
        modifier = Modifier.background(Color.White)
    ) {
        SheetContent(sheetActivity = sheetActivity, onEvent = { }, focusRequester = FocusRequester())
    }
}
