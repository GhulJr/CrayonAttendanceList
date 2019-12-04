package com.oskarrek.crayonattendancelist.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.oskarrek.crayonattendancelist.models.AttendanceList

@Dao
interface AttendanceListDao {

    @Query("SELECT * FROM attendance_lists")
    fun getAllListsLiveData() : LiveData<List<AttendanceList>>

    @Query("SELECT * FROM attendance_lists ORDER BY date_in_millis ASC")
    fun getLists() : List<AttendanceList>

    @Insert
    fun insertLists(vararg lists : AttendanceList)

    @Delete
    fun deleteLists(vararg lists : AttendanceList)

    @Update
    fun updateLists(vararg  lists : AttendanceList)
}