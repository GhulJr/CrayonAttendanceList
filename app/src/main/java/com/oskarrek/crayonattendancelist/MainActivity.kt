package com.oskarrek.crayonattendancelist

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.oskarrek.crayonattendancelist.activities.CheckPresenceActivity
import com.oskarrek.crayonattendancelist.adapters.AttendanceListsAdapter
import com.oskarrek.crayonattendancelist.dialogs.AddEditListDialogFragment
import com.oskarrek.crayonattendancelist.interfaces.IOnCreateListListener
import com.oskarrek.crayonattendancelist.models.AttendanceList
import com.oskarrek.crayonattendancelist.viewmodels.AttendanceListViewModel

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), IOnCreateListListener {


    private lateinit var listsAdapter : AttendanceListsAdapter
    private lateinit var viewModel: AttendanceListViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            addAttendanceList()
        }

        setupViewModel()
        setupRecyclerView()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item : MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    /** Listeners methods. */

    private fun onAttendanceListClick(attendanceList: AttendanceList) {
        val intent = Intent(this@MainActivity, CheckPresenceActivity::class.java)
        startActivity(intent)
        //TODO: Extend intent to start activity with list data.
    }

    /**Utils classes.*/

    private fun setupRecyclerView() {
        listsAdapter = AttendanceListsAdapter { attendanceList -> onAttendanceListClick(attendanceList) }

        attendanceList_recyclerView.apply {
            this.layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = listsAdapter
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(AttendanceListViewModel::class.java)
        viewModel.attendanceLists.observe(this, Observer {list ->
            listsAdapter.list = ArrayList(list) // Might cause a problem.
            listsAdapter.notifyDataSetChanged()
        })
    }

    // TODO:(O) Create separate class from it/move this method to ViewModel.
    private fun addAttendanceList() {
        val dialogFragment = AddEditListDialogFragment()
        dialogFragment.show(supportFragmentManager, "AddEditListDialogFragment")
    }

    private fun getDummyData() : ArrayList<AttendanceList>{
        val dummyList = ArrayList<AttendanceList>()

        for(i in 0..20) {
            dummyList.add(AttendanceList("Podstawy Html", 1573937709230))
        }

        return dummyList
    }

    /**Interfaces.*/

    override fun onCreate(list : AttendanceList) {
        viewModel.addAttendanceList(list)
    }
}
