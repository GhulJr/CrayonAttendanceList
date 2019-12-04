package com.oskarrek.crayonattendancelist

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.oskarrek.crayonattendancelist.activities.CheckPresenceActivity
import com.oskarrek.crayonattendancelist.adapters.AttendanceListsAdapter
import com.oskarrek.crayonattendancelist.dialogs.AddEditListDialogFragment
import com.oskarrek.crayonattendancelist.interfaces.IOnListDialogListener
import com.oskarrek.crayonattendancelist.interfaces.IOnListMenuListener
import com.oskarrek.crayonattendancelist.models.AttendanceList
import com.oskarrek.crayonattendancelist.viewmodels.MainActivityViewModel

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity(), IOnListDialogListener, IOnListMenuListener{

    private val TAG = MainActivity::class.java.simpleName
    private val STORARGE_REQUEST = 1001
    var STORAGE_PERMISSIONS = Array(2){Manifest.permission.READ_EXTERNAL_STORAGE; Manifest.permission.WRITE_EXTERNAL_STORAGE}


    private lateinit var listsAdapter : AttendanceListsAdapter
    private lateinit var viewModel: MainActivityViewModel

    //TODO: Should be owned by AttendanceList class.
    companion object {
        val ATTENDANCELIST_ID = "101"
        val ATTENDANCELIST_TITLE = "102"
        val ATTENDANCELIST_TIMESPAN = "103"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(main_activity_toolbar)
        fab.setOnClickListener { startAddEditListDialog(Bundle()) }

        if(checkPermission()) {
            setupViewModel()
            setupRecyclerView()
            viewModel.createAppFolderIfMissing()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item : MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_read_data -> {
                loadParticipantsFromCsv()
                true
            }
            R.id.action_save_data -> {
                saveParticipantsToCsv()
                true
            }
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
        intent.putExtra(ATTENDANCELIST_ID, attendanceList.id)
        intent.putExtra(ATTENDANCELIST_TITLE, attendanceList.title)
        startActivity(intent)
    }

    /**Utils classes.*/

    private fun onStoragePermissionGranted() {
        setupViewModel()
        setupRecyclerView()
        loadParticipantsFromCsv()
    }

    private fun setupRecyclerView() {
        listsAdapter = AttendanceListsAdapter(this) { attendanceList -> onAttendanceListClick(attendanceList) }

        attendanceList_recyclerView.apply {
            this.layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = listsAdapter
        }
    }

    /*override fun onResume() {
        super.onResume()
        listsAdapter.notifyDataSetChanged()
    }*/

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        viewModel.listsLiveData.observe(this, Observer { list ->
            listsAdapter.list = ArrayList(list) // Might cause a problem.
            listsAdapter.notifyDataSetChanged()
        })

       // viewModel.loadParticipantsFromCsv()
    }

    private fun startAddEditListDialog(bundle : Bundle) {
        val dialogFragment = AddEditListDialogFragment().apply {
            arguments = bundle
            show(supportFragmentManager, "AddEditListDialogFragment")
        }
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
                    STORAGE_PERMISSIONS,
                    STORARGE_REQUEST
                )

            return false
        }

        return true
    }

    private fun loadParticipantsFromCsv() {
      try {
          viewModel.loadParticipantsFromCsv()
      } catch (e : Exception) {
          Log.e(TAG, "Unable to load from csv file.", e)
          Toast.makeText(this, R.string.missing_csv_file, Toast.LENGTH_LONG).show()
      }

        Toast.makeText(this, R.string.participants_data_updated, Toast.LENGTH_LONG).show()

    }

    private fun saveParticipantsToCsv() {
        viewModel.saveParticipantsToCsv()
    }

    /**Interfaces.*/

    override fun onCreate(list : AttendanceList) {
        viewModel.addAttendanceList(list)
    }

    override fun onUpdate(list: AttendanceList) {
       viewModel.updateAttendanceList(list)
    }

    override fun onDelete(list: AttendanceList) {
        viewModel.deleteAttendanceList(list)
        listsAdapter.notifyDataSetChanged()
    }

    override fun onEdit(list: AttendanceList) {
        val bundle = Bundle().apply {
            putInt(ATTENDANCELIST_ID, list.id)
            putString(ATTENDANCELIST_TITLE, list.title)
            putLong(ATTENDANCELIST_TIMESPAN, list.dateInMillis)
        }
        startAddEditListDialog(bundle)
    }
}
