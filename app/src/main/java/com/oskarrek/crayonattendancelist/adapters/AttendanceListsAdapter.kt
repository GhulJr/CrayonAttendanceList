package com.oskarrek.crayonattendancelist.adapters

import android.annotation.TargetApi
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oskarrek.crayonattendancelist.R
import com.oskarrek.crayonattendancelist.models.AttendanceList
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class AttendanceListsAdapter() :
    RecyclerView.Adapter<AttendanceListsAdapter.AttendanceListViewHolder>() {

    lateinit var list : ArrayList<AttendanceList>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceListViewHolder
            = AttendanceListViewHolder(LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_attendance_list, parent, false))


    override fun getItemCount(): Int = if(::list.isInitialized) list.size else 0


    override fun onBindViewHolder(holder: AttendanceListViewHolder, position: Int) {
        val currentList = list[position]
        holder.title.text = currentList.title
        holder.date.text = formatDate(currentList.dateInMillis)
    }

    @TargetApi(Build.VERSION_CODES.O)
    fun formatDate(inMillis : Long) : String{
        val date = Date(inMillis)
        val simpleDateFormat = SimpleDateFormat.getDateInstance()
        return simpleDateFormat.format(date)
    }

    class AttendanceListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title : TextView = view.findViewById(R.id.attendanceList_title)
        var date : TextView = view.findViewById(R.id.attendanceList_date)
    }
}