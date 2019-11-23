package com.oskarrek.crayonattendancelist.interfaces

import com.oskarrek.crayonattendancelist.models.AttendanceList

interface IOnCreateListListener {
    fun onCreate(list : AttendanceList)
}