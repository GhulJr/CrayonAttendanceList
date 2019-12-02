package com.oskarrek.crayonattendancelist.adapters

import android.annotation.TargetApi
import android.os.Build
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oskarrek.crayonattendancelist.R
import com.oskarrek.crayonattendancelist.models.AttendanceList
import com.oskarrek.crayonattendancelist.models.Participant
import kotlinx.android.synthetic.main.item_attendance_list.view.*
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class AttendanceListsAdapter(val clickListener: (AttendanceList) -> Unit) :
    RecyclerView.Adapter<AttendanceListsAdapter.AttendanceListViewHolder>() {

    lateinit var list : ArrayList<AttendanceList>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceListViewHolder
            = AttendanceListViewHolder(LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_attendance_list, parent, false))


    override fun getItemCount(): Int = if(::list.isInitialized) list.size else 0


    override fun onBindViewHolder(holder: AttendanceListViewHolder, position: Int) {
        val currentList = list[position]
        holder.bind(currentList, clickListener)
        //holder.title.text = currentList.title
        //holder.date.text = formatDate(currentList.dateInMillis)
    }


    class AttendanceListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(attendanceList: AttendanceList, clickListener: (AttendanceList) -> Unit) {
            itemView.apply {
                attendanceList_title.text = attendanceList.title
                attendanceList_date.text = attendanceList.getDateAsString()
                setOnClickListener{ clickListener(attendanceList) }
                setOnLongClickListener {createPopupMenu(attendanceList)}
            }
        }

        private fun createPopupMenu(attendanceList: AttendanceList) : Boolean {
            PopupMenu(itemView.context, itemView).apply {
                inflate(R.menu.menu_attendance_list)
            }.show()
            return true

        }

    }
}