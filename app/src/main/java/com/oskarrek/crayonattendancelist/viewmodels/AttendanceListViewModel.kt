package com.oskarrek.crayonattendancelist.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.oskarrek.crayonattendancelist.models.AttendanceList
import com.oskarrek.crayonattendancelist.repositories.DatabaseRepo

class AttendanceListViewModel(application: Application) : AndroidViewModel(application) {
    private val repoDB : DatabaseRepo = DatabaseRepo.getInstance(application)
    val attendanceLists: LiveData<List<AttendanceList>>

    init {
        attendanceLists = repoDB.getAttendanceLists()
    }
}