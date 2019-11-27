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

    fun getAttendanceLists(): LiveData<List<AttendanceList>> {
        return database.attendanceListDao.getAllLists()
    }

    fun insertAttendanceLists(vararg lists : AttendanceList) {
        executor.execute {
            database.attendanceListDao.insertLists(*lists)
        }
    }

    /** Participant */

    fun getParticipants(): LiveData<List<Participant>> {
        return database.participantDao.getParticipants()
    }

    fun insertParticipants(participant: Participant) {
        executor.execute {
            val currentParticipant : Participant? = database.participantDao.getParticipantsBuStringId(participant.stringId)
            if(currentParticipant == null)
                database.participantDao.insertParticipant(participant)
        }
    }

    /** Presence */
    fun getPresences(listId : Int) : LiveData<List<Presence>> {
        return database.presenceDao.getPresenceByAttenanceListId(listId)
    }

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
}