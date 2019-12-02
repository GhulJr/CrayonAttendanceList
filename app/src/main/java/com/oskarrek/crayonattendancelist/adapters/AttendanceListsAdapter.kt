package com.oskarrek.crayonattendancelist.adapters

import android.view.*
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.oskarrek.crayonattendancelist.R
import com.oskarrek.crayonattendancelist.interfaces.IOnListMenuListener
import com.oskarrek.crayonattendancelist.models.AttendanceList
import kotlinx.android.synthetic.main.item_attendance_list.view.*
import kotlin.collections.ArrayList

class AttendanceListsAdapter(val onMenuItemListener : IOnListMenuListener, val clickListener: (AttendanceList) -> Unit) :
    RecyclerView.Adapter<AttendanceListsAdapter.AttendanceListViewHolder>() {

    lateinit var list : ArrayList<AttendanceList>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceListViewHolder
            = AttendanceListViewHolder(LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_attendance_list, parent, false))


    override fun getItemCount(): Int = if(::list.isInitialized) list.size else 0


    override fun onBindViewHolder(holder: AttendanceListViewHolder, position: Int) {
        val currentList = list[position]
        holder.bind(currentList, onMenuItemListener, clickListener)
        //holder.title.text = currentList.title
        //holder.date.text = formatDate(currentList.dateInMillis)
    }


    class AttendanceListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        //TODO: Maybe collapse menuListener and clickListener?
        fun bind(attendanceList: AttendanceList, menuListener: IOnListMenuListener ,clickListener: (AttendanceList) -> Unit) {
            itemView.apply {
                attendanceList_title.text = attendanceList.title
                attendanceList_date.text = attendanceList.getDateAsString()
                setOnClickListener{ clickListener(attendanceList) }
                setOnLongClickListener {createPopupMenu(menuListener, attendanceList)}
            }
        }

        private fun createPopupMenu(menuListener: IOnListMenuListener,attendanceList: AttendanceList) : Boolean {
            PopupMenu(itemView.context, itemView).apply {
                inflate(R.menu.menu_attendance_list)
                gravity = Gravity.END

                setOnMenuItemClickListener { item ->
                    return@setOnMenuItemClickListener when(item.itemId) {
                        R.id.action_edit_list -> {
                            menuListener.onEdit(attendanceList)
                            true
                        }

                        R.id.action_delete_list -> {
                            menuListener.onDelete(attendanceList)
                            true
                        }


                        else -> false
                    }
                }
            }.show()
            return true

        }

    }
}