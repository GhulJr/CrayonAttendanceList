package com.oskarrek.crayonattendancelist.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.oskarrek.crayonattendancelist.models.AttendanceList

class AttendanceListViewModel {
    val attendanceList: LiveData<List<AttendanceList>> = MutableLiveData()
}