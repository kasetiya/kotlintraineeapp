package com.softices.kotlintraineeapp.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import com.softices.kotlintraineeapp.R
import com.softices.kotlintraineeapp.adapters.ContactsAdapter
import com.softices.kotlintraineeapp.extra.L
import com.softices.kotlintraineeapp.models.ContactModel

class ContactsActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private var arrayList: ArrayList<ContactModel> = ArrayList()
    lateinit var context: Context
    private lateinit var adapter: ContactsAdapter
    /** \
     * set view
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        init()
    }

    /***
     * initialize activity.
     */
    private fun init() {
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        context = this
        listView = findViewById(R.id.contact_listview)

    }

    /** \
     * fetch all contacts.
     */
    private fun retrieveContacts() {
        val phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")
        if (phones != null && phones.moveToFirst()) {
            do {
                val contact = ContactModel()
                contact.contactName = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                contact.contactNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                contact.contactEmail = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS))
                arrayList.add(contact)
            } while (phones.moveToNext())
        } else {
            Log.e("data", "no contacts")
        }
        phones!!.close()
    }

    /**
     * check permission given.
     */
    override fun onResume() {
        super.onResume()
        checkPermission()
    }

    /**
     * check pemission given for read contact
     * and version above 23.
     */
    private fun checkPermission() {
        if (Build.VERSION.SDK_INT < 23) {
            LoadContacts().execute()
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                LoadContacts().execute()
            } else
                ActivityCompat.requestPermissions(this, arrayOf(Manifest
                        .permission.READ_CONTACTS), 152)
        }
    }

    /**
     * @param permissions
     * @param requestCode
     * @param grantResults
     *
     * when runs when permission dialog close
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size > 0) {
            when (requestCode) {
                152 -> goWithCameraPermission(grantResults)
            }
        }
    }

    private fun goWithCameraPermission(grantResults: IntArray) {
        if (grantResults.get(0) == PackageManager.PERMISSION_GRANTED) {
            LoadContacts().execute()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest
                    .permission.READ_CONTACTS), 152)
        }
    }

    /**
     * load contacts using asynk task.
     * its not necessary to use asynk task.
     */
    inner class LoadContacts() : AsyncTask<Void, Void, Void>() {
        override fun onPreExecute() {
            super.onPreExecute()
            L.showProgressDialog(this@ContactsActivity)
        }

        override fun doInBackground(vararg p0: Void?): Void? {
            retrieveContacts()
            return null;
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            L.hideProgressDialog()
            adapter = ContactsAdapter(arrayList, this@ContactsActivity)
            listView.adapter = adapter
        }
    }

    /**
     * create search menu
     * @param menu menu object
     * @return Boolean is menu visible
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)
        val menuItem = menu!!.findItem(R.id.action_search)
        val searchViewActionBar: SearchView = MenuItemCompat.getActionView(menuItem) as SearchView
        searchViewActionBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchViewActionBar.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter(newText)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * back arrow pressed
     */
    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(menuItem)
    }
}