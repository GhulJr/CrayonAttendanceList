package com.oskarrek.crayonattendancelist.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.oskarrek.crayonattendancelist.models.AttendanceList
import com.oskarrek.crayonattendancelist.models.Participant

@Database(entities = [Participant::class, AttendanceList::class, PresenceDao::class], version = 1)
abstract class CrayonDatabase : RoomDatabase() {
    abstract fun participantDao(): ParticipantDao
    abstract fun attendanceListDao(): AttendanceListDao
    abstract fun presenceDao(): PresenceDao
}