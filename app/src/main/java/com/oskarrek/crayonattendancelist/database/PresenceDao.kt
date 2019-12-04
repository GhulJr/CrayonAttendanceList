package com.oskarrek.crayonattendancelist.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.oskarrek.crayonattendancelist.models.Presence

@Dao
interface PresenceDao {

    @Query("SELECT * FROM presence")
    fun getPresenceList() : LiveData<List<Presence>>


    @Query("SELECT * FROM presence WHERE attendanceListId = :listId")
    fun getPresenceByAttendanceListId(listId : Int) : LiveData<List<Presence>>

    @Delete
    fun deletePresences(vararg p : Presence)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPresences(vararg p : Presence)
}