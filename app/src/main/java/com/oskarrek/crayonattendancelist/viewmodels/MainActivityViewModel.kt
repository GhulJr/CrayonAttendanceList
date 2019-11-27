package com.oskarrek.crayonattendancelist.viewmodels

import android.app.Application
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.oskarrek.crayonattendancelist.models.AttendanceList
import com.oskarrek.crayonattendancelist.models.Participant
import com.oskarrek.crayonattendancelist.repositories.DatabaseRepo
import com.oskarrek.crayonattendancelist.repositories.StorageRepo
import java.io.File
import java.io.FileInputStream
import java.io.InputStream





class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val repoDB : DatabaseRepo = DatabaseRepo.getInstance(application)
    private val storageRepo: StorageRepo = StorageRepo.getInstance(application)

    val attendanceLists: LiveData<List<AttendanceList>>


    init {
        attendanceLists = repoDB.getAttendanceLists()
    }

    fun addAttendanceList(list : AttendanceList) {
        repoDB.insertAttendanceLists(list)
    }

    fun loadParticipantsFromExcel() {

        val list = storageRepo.loadParticipantsFromExcel()

        for(participant in list){
            repoDB.insertParticipants(participant)
        }
    }
}