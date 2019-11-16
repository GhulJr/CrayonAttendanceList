package com.oskarrek.crayonattendancelist

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.oskarrek.crayonattendancelist.adapters.AttendanceListsAdapter
import com.oskarrek.crayonattendancelist.models.AttendanceList
import com.oskarrek.crayonattendancelist.viewmodels.AttendanceListViewModel

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var listsAdapter : AttendanceListsAdapter
    private lateinit var viewModel: AttendanceListViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        setupViewModel()
        setupRecyclerView()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }


    /**Utils classes.*/

    private fun setupRecyclerView() {
        listsAdapter = AttendanceListsAdapter().apply {
            list = getDummyData()
        }

        attendanceList_recyclerView.apply {
            this.layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = listsAdapter
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(AttendanceListViewModel::class.java)
        viewModel.attendanceLists.observe(this, Observer {list ->
            listsAdapter.list = list
            listsAdapter.notifyDataSetChanged()
        })
    }

    private fun getDummyData() : ArrayList<AttendanceList>{
        val dummyList = ArrayList<AttendanceList>()

        for(i in 0..20) {
            dummyList.add(AttendanceList("Podstawy Html", 1573937709230))
        }

        return dummyList
    }
}
