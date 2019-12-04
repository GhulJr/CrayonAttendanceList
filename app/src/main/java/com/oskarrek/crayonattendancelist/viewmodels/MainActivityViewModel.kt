package com.oskarrek.crayonattendancelist.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.oskarrek.crayonattendancelist.models.AttendanceList
import com.oskarrek.crayonattendancelist.repositories.DatabaseRepo
import com.oskarrek.crayonattendancelist.repositories.StorageRepo


class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val databaseRepo : DatabaseRepo = DatabaseRepo.getInstance(application)
    private val storageRepo: StorageRepo = StorageRepo.getInstance(application)

    val attendanceLists: LiveData<List<AttendanceList>>

    init {
        attendanceLists = databaseRepo.getAttendanceLists()
    }

        /** Attendance List */

    fun addAttendanceList(list : AttendanceList) {
        databaseRepo.insertAttendanceLists(list)
    }

    fun updateAttendanceList(list: AttendanceList) {
        databaseRepo.updateAttendanceList(list)
    }

    fun deleteAttendanceList(list: AttendanceList) {
        databaseRepo.deleteAttendanceList(list)
    }

        /** Participant */

    @Throws(Exception::class)
    fun loadParticipantsFromExcel() {

        val list = storageRepo.loadParticipantsFromExcel()

        for(participant in list){
            databaseRepo.insertParticipants(participant)
        }
    }
        /** Storage */

    fun createAppFolderIfMissing() {
        storageRepo.createAppFolder()
    }


}