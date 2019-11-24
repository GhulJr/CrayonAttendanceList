package com.oskarrek.crayonattendancelist.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.oskarrek.crayonattendancelist.database.CrayonDatabase
import com.oskarrek.crayonattendancelist.models.AttendanceList
import com.oskarrek.crayonattendancelist.models.Participant
import com.oskarrek.crayonattendancelist.utils.SingletonHolder
import java.lang.Exception
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

    fun getAttendanceLists(): LiveData<List<AttendanceList>> {
        return database.attendanceListDao.getAllLists()
    }

    fun insertAttendanceLists(vararg lists : AttendanceList) {
        executor.execute {
            database.attendanceListDao.insertLists(*lists)
        }
    }

    fun getParticipants(): LiveData<List<Participant>> {
        return database.participantDao.getParticipants()
    }

    fun insertParticipants(vararg array : Participant) {
        executor.execute {
            database.participantDao.insertParticipants(*array)
        }
    }
}