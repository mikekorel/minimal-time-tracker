package com.mikekorel.timetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mikekorel.timetracker.home.HomeScreen
import com.mikekorel.timetracker.ui.theme.TimeTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimeTrackerTheme {
                HomeScreen()
            }
        }
    }
}

