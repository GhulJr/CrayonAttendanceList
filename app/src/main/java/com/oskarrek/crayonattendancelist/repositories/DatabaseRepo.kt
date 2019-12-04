package com.oskarrek.crayonattendancelist.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.oskarrek.crayonattendancelist.database.CrayonDatabase
import com.oskarrek.crayonattendancelist.models.AttendanceList
import com.oskarrek.crayonattendancelist.models.Participant
import com.oskarrek.crayonattendancelist.models.Presence
import com.oskarrek.crayonattendancelist.utils.SingletonHolder
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class DatabaseRepo private constructor(context: Context) {

    private val executor : Executor
    private val database : CrayonDatabase

    // Singleton of repository
    companion object : SingletonHolder<DatabaseRepo, Context>(::DatabaseRepo)

    init {
        executor = Executors.newSingleThreadExecutor()
        database = Room.databaseBuilder(context, CrayonDatabase::class.java, "CrayonDatabase").build()
    }

    /** Attendance list */

    fun getAttendanceListsLiveData(): LiveData<List<AttendanceList>> = database.attendanceListDao.getAllListsLiveData()

    fun insertAttendanceLists(vararg lists : AttendanceList) {
        executor.execute {
            database.attendanceListDao.insertLists(*lists)
        }
    }

    fun updateAttendanceList(list: AttendanceList) {
        executor.execute {
            database.attendanceListDao.updateLists(list)
        }
    }

    fun deleteAttendanceList(list: AttendanceList) {
        executor.execute {
            database.attendanceListDao.deleteLists(list)
        }
    }

    /** Participant */

    fun getParticipantsLiveData(): LiveData<List<Participant>> = database.participantDao.getParticipantsLiveData()


    fun getParticipants() : List<Participant> = database.participantDao.getParticipants()


    fun insertParticipants(participant: Participant) {
        executor.execute {
            val currentParticipant : Participant? = database.participantDao.getParticipantsBuStringId(participant.stringId)
            if(currentParticipant == null)
                database.participantDao.insertParticipant(participant)
        }
    }

    /** Presence */

    fun getPresencesLiveData(listId : Int) : LiveData<List<Presence>> = database.presenceDao.getPresenceByAttendanceListId(listId)


    fun getPresenceByParticipantId(participantId : Int) : List<Presence> = database.presenceDao.getPresenceByParticipantId(participantId)

    fun deletePresence(p: Presence) {
        executor.execute {
            database.presenceDao.deletePresences(p)
        }
    }

    fun insertPresence(presence: Presence) {
        executor.execute {
            database.presenceDao.insertPresences(presence)
        }
    }

    fun getAttendanceLists(): List<AttendanceList> = database.attendanceListDao.getLists()


}