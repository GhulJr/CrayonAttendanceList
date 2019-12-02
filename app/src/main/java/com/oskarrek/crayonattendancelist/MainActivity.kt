package com.oskarrek.crayonattendancelist

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
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

    private val TAG = MainActivity.javaClass.simpleName
    private val STORARGE_REQUEST = 1001

    private lateinit var listsAdapter : AttendanceListsAdapter
    private lateinit var viewModel: MainActivityViewModel

    //TODO: Should be owned by AttendanceList class.
    companion object {
        val ATTENDANCELIST_ID = "101"
        val ATTENDANCELIST_TITLE = "102"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(main_activity_toolbar)
        fab.setOnClickListener { addAttendanceList() }

        if(checkPermission()) {
            setupViewModel()
            setupRecyclerView()
            viewModel.createAppFolderIfMissing()
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?,menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_attendance_list, menu)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_edit_list -> true
            R.id.action_delete_list -> true
            else -> super.onContextItemSelected(item)
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
                loadParticipants()
                true
            }
            R.id.action_save_data -> {
                Toast.makeText(this, "[DEBUG] in progress", Toast.LENGTH_SHORT).show()
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
        loadParticipants()
    }

    private fun setupRecyclerView() {
        listsAdapter = AttendanceListsAdapter(this) { attendanceList -> onAttendanceListClick(attendanceList) }

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
                    Array(1) { Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORARGE_REQUEST
                )

            return false
        }

        return true
    }

    private fun  loadParticipants() {
      try {
          viewModel.loadParticipantsFromExcel()
      } catch (e : Exception) {
          Log.e(TAG, "Unable to load from csv file.", e)
          Toast.makeText(this, R.string.missing_csv_file, Toast.LENGTH_LONG).show()
      }

        Toast.makeText(this, R.string.participants_data_updated, Toast.LENGTH_LONG).show()

    }

    /**Interfaces.*/

    override fun onCreate(list : AttendanceList) {
        viewModel.addAttendanceList(list)
    }

    override fun onDelete(list: AttendanceList) {
        viewModel.deleteAttendanceList(list)
        listsAdapter.notifyDataSetChanged()
    }

    override fun onEdit(list: AttendanceList) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
