package com.example.contactsapplication

import android.database.MergeCursor
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone.ENTERPRISE_CONTENT_FILTER_URI
import android.provider.ContactsContract.Directory.ENTERPRISE_CONTENT_URI
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val searchTerm = Uri.encode("Managed")

        // This part does not work as expected - no results returned if searchterm is empty
        val workContactsUri = ContactsContract.Contacts.ENTERPRISE_CONTENT_FILTER_URI
            .buildUpon()
            .appendQueryParameter(
                ContactsContract.DIRECTORY_PARAM_KEY,
                ContactsContract.Directory.ENTERPRISE_DEFAULT.toString()
            )
            .appendEncodedPath(searchTerm)
            .build()

        val workCursor = contentResolver.query(
            workContactsUri,
            arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.LOOKUP_KEY,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
            ),
            null,
            null,
            null
        )

        Log.i("fucking uri", workContactsUri.toString())
        val nameList: MutableList<String> = mutableListOf()

        workCursor?.let {
            with(it) {
                moveToFirst()
                while (!isAfterLast) {
                    val name = getString(
                        getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                    )
                    val id = getLong(
                        getColumnIndex(ContactsContract.Contacts._ID)
                    )
                    nameList.add(name)
                    moveToNext()
                }
                close()
            }
        }
        Log.i("fucking list count", nameList.count().toString())
    }

}
