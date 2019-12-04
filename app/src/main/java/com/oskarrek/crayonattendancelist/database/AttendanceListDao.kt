package com.oskarrek.crayonattendancelist.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.oskarrek.crayonattendancelist.models.AttendanceList

@Dao
interface AttendanceListDao {

    @Query("SELECT * FROM attendance_lists")
    fun getAllLists() : LiveData<List<AttendanceList>>

    @Insert
    fun insertLists(vararg lists : AttendanceList)

    @Delete
    fun deleteLists(vararg lists : AttendanceList)

    @Update
    fun updateLists(vararg  lists : AttendanceList)
}