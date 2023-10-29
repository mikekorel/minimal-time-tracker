package com.mikekorel.timetracker.models

data class UserActivity(
    val name: String = "",
    var totalTimeActive: Long = 0,    // total time logged as active (in seconds)
    var activeSince: Long? = null,  // timestamp if currently active, null otherwise
)
