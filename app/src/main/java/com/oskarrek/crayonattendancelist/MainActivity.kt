package com.oskarrek.crayonattendancelist

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.oskarrek.crayonattendancelist.activities.CheckPresenceActivity
import com.oskarrek.crayonattendancelist.adapters.AttendanceListsAdapter
import com.oskarrek.crayonattendancelist.dialogs.AddEditListDialogFragment
import com.oskarrek.crayonattendancelist.interfaces.IOnCreateListListener
import com.oskarrek.crayonattendancelist.models.AttendanceList
import com.oskarrek.crayonattendancelist.viewmodels.MainActivityViewModel

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), IOnCreateListListener {

    private val STORARGE_REQUEST = 1001

    private lateinit var listsAdapter : AttendanceListsAdapter
    private lateinit var viewModel: MainActivityViewModel

    companion object {
        val ATTENDANCE_CHECK = "101"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { addAttendanceList() }

        if(checkPermission()) {
            setupViewModel()
            setupRecyclerView()
        }
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when(requestCode)  {
            STORARGE_REQUEST -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onStoragePermissionGranted()
                } else {
                    finish()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /** Listeners methods. */

    private fun onAttendanceListClick(attendanceList: AttendanceList) {
        val intent = Intent(this@MainActivity, CheckPresenceActivity::class.java)
        intent.putExtra(ATTENDANCE_CHECK, attendanceList.id)
        startActivity(intent)
    }

    /**Utils classes.*/

    private fun onStoragePermissionGranted() {
        setupViewModel()
        setupRecyclerView()
        viewModel.loadParticipantsFromExcel()
    }

    private fun setupRecyclerView() {
        listsAdapter = AttendanceListsAdapter { attendanceList -> onAttendanceListClick(attendanceList) }

        attendanceList_recyclerView.apply {
            this.layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = listsAdapter
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        viewModel.attendanceLists.observe(this, Observer {list ->
            listsAdapter.list = ArrayList(list) // Might cause a problem.
            listsAdapter.notifyDataSetChanged()
        })

       // viewModel.loadParticipantsFromExcel()
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

    private fun checkPermission() : Boolean{

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                    this,
                    Array(1) { Manifest.permission.READ_EXTERNAL_STORAGE },
                    STORARGE_REQUEST
                )

            return false
        }

        return true
    }


    /**Interfaces.*/

    override fun onCreate(list : AttendanceList) {
        viewModel.addAttendanceList(list)
    }
}
