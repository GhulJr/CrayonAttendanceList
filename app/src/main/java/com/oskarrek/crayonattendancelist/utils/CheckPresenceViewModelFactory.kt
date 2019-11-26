package com.oskarrek.crayonattendancelist.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.oskarrek.crayonattendancelist.models.AttendanceList
import com.oskarrek.crayonattendancelist.viewmodels.CheckPresenceViewModel

class CheckPresenceViewModelFactory(
    val application: Application,
    val attendanceListId : Int
) : ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return  CheckPresenceViewModel(application, attendanceListId) as (T)
    }
}