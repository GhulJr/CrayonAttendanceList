package com.oskarrek.crayonattendancelist.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.oskarrek.crayonattendancelist.models.AttendanceList
import com.oskarrek.crayonattendancelist.models.Participant
import com.oskarrek.crayonattendancelist.models.Presence

@Database(entities = [Participant::class, AttendanceList::class, Presence::class], version = 1)
abstract class CrayonDatabase : RoomDatabase() {
    abstract val participantDao     : ParticipantDao
    abstract val attendanceListDao  : AttendanceListDao
    abstract val presenceDao        : PresenceDao
}