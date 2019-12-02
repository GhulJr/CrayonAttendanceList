package com.oskarrek.crayonattendancelist.interfaces

import com.oskarrek.crayonattendancelist.models.AttendanceList

interface IOnListMenuListener {
    fun onDelete(list : AttendanceList)
    fun onEdit(list : AttendanceList)
}