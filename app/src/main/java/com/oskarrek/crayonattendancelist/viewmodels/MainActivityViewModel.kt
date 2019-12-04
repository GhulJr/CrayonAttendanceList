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


    val listsLiveData: LiveData<List<AttendanceList>>

    init {
        listsLiveData = databaseRepo.getAttendanceListsLiveData()
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
    fun loadParticipantsFromCsv() {

        val list = storageRepo.loadParticipantsFromCsv()

        for(participant in list){
            databaseRepo.insertParticipants(participant)
        }
    }

    fun saveParticipantsToCsv() {
        //TODO: do something with this method (repo used inside repo, sounds awful).
        storageRepo.saveParticipantsToCsv({databaseRepo.getAttendanceLists()},{databaseRepo.getParticipants()},{ participantId -> databaseRepo.getPresenceByParticipantId(participantId)} )
    }

        /** Storage */

    fun createAppFolderIfMissing() {
        storageRepo.createAppFolder()
    }



}