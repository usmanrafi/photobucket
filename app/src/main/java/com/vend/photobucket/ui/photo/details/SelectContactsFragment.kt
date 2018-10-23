package com.vend.photobucket.ui.photo.details


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.vend.photobucket.R
import com.vend.photobucket.model.Image
import kotlinx.android.synthetic.main.fragment_select_contacts.*
import java.io.File


private const val ARG_IMAGE = "image"
private const val PERMISSIONS = 30

class SelectContactsFragment : Fragment() {

    private lateinit var image: Image
    private lateinit var rvAdapter: SelectContactsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val param = it.getString(ARG_IMAGE)
            val arr = param.split("|")
            image = Image(arr[0].toLong(), arr[1], arr[2], arr[3], arr[4])
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_select_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        setupButtons()
    }

    companion object {
        @JvmStatic
        fun newInstance(image: Image) =
                SelectContactsFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_IMAGE, image.toString())
                    }
                }
    }

    fun sendImage(list: ArrayList<String>) {
        val path = Uri.fromFile(File(image.path))

        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "vnd.android.cursor.dir/email"

        val to = Array(list.size, { _ -> "" })
        var i = 0
        list.forEach {
            to[i++] = (it.subSequence(it.indexOf('(') + 1, it.indexOf(')'))).toString()
        }
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to)

        emailIntent.putExtra(Intent.EXTRA_STREAM, path)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject")
        ContextCompat.startActivity(context!!, Intent.createChooser(emailIntent, "Send email..."), null)
    }

    private fun setupRecyclerView() {
        checkForPermissions()

        rvAdapter = SelectContactsAdapter(getContacts(), this)

        val recyclerView = rvContacts
        recyclerView.apply {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupButtons() {
        btnSelectAll.setOnClickListener {
            rvAdapter.selectAll()
        }

        btnSendEmail.setOnClickListener {
            if (!rvAdapter.send()) {
                Toast.makeText(this.context, "Please select contact(s) first", Toast.LENGTH_LONG)
                        .show()
            }
        }
    }

    private fun getContacts(): ArrayList<String> {

        val names = ArrayList<String>()

        val cr = activity!!.contentResolver
        val cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        if (cur!!.count > 0) {
            while (cur.moveToNext()) {
                val id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))
                val cur1 = cr.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        arrayOf<String>(id), null)
                while (cur1!!.moveToNext()) {
                    //to get the contact names
                    val name = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val email = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
                    if (email != null) {
                        val contact = "$name ($email)"
                        if (!names.contains(contact))
                            names.add(contact)
                    }
                }
                cur1.close()
            }
        }
        return names
    }

    private fun checkForPermissions() {
        if (Build.VERSION.SDK_INT >= 23 &&
                PermissionChecker.checkSelfPermission(this.context!!, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions()
        }
    }


    private fun requestPermissions() {
        requestPermissions(arrayOf(
                android.Manifest.permission.READ_CONTACTS), PERMISSIONS)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSIONS) {
            if (grantResults.isEmpty()
                    || grantResults[0] == PackageManager.PERMISSION_DENIED)
                requestPermissions()
        }
    }
}
