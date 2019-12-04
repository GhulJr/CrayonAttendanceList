package com.oskarrek.crayonattendancelist.interfaces

import com.oskarrek.crayonattendancelist.models.AttendanceList

interface IOnListDialogListener {
    fun onCreate(list : AttendanceList)
    fun onUpdate(list : AttendanceList)
}