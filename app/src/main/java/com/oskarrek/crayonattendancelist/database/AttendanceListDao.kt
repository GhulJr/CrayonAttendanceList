package com.oskarrek.crayonattendancelist.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.oskarrek.crayonattendancelist.models.AttendanceList

@Dao
interface AttendanceListDao {

    @Query("SELECT * FROM attendance_lists")
    fun getAllLists() : LiveData<List<AttendanceList>>

    @Insert
    fun insertLists(vararg lists : AttendanceList)

    @Delete
    fun deleteLists(vararg lists : AttendanceList)
}