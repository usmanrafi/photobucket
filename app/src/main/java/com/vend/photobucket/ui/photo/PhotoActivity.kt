package com.vend.photobucket.ui.photo

import android.Manifest
import android.annotation.TargetApi
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.PermissionChecker
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.vend.photobucket.R
import com.vend.photobucket.application.PhotoApplication
import com.vend.photobucket.model.Image
import com.vend.photobucket.ui.authentication.AuthenticationActivity
import com.vend.photobucket.ui.photo.addphoto.AddPhotoFragment
import com.vend.photobucket.ui.photo.addphoto.DatabaseImageListener
import com.vend.photobucket.ui.photo.details.DetailsFragment
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class PhotoActivity : AppCompatActivity(), PhotoAdapterListener, DatabaseImageListener {
    private val PERMISSIONS = 30

    @Inject
    lateinit var photoViewModel: PhotoViewModel

    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar

    private lateinit var rvAdapter: PhotoAdapter

    private var isList = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        (applicationContext as PhotoApplication).getAppComponent().inject(this)

        checkForPermissions()

        navigationView = drawer
        drawerLayout = drawer_layout
        toolbar = toolbar_layout

        initViews()

        subscribe()

        photoViewModel.checkSession()

    }

    private fun initViews() {
        setDrawerLayoutToggle()
        setupToolbar()
        setNavigationListener()

        setupButtons()
        setupSpinner()

        setupRecyclerView()
        setupSearch()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.btnAdd -> {
                supportFragmentManager
                        .beginTransaction()
                        .replace(android.R.id.content, AddPhotoFragment())
                        .addToBackStack("PhotoActivity")
                        .commit()

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun imageAdded() {
        photoViewModel.updateData()
    }

    fun updateImage(image: Image) {
        photoViewModel.updateImage(image)
        rvAdapter.notifyDataSetChanged()
    }

    override fun deleteImages(images: ArrayList<Image>) {
        photoViewModel.deleteImages(images)
    }

    override fun showImageDetails(image: Image) {
        val detailsFragment = DetailsFragment.newInstance(image)

        supportFragmentManager
                .beginTransaction()
                .replace(android.R.id.content, detailsFragment)
                .addToBackStack("PhotoActivity")
                .commit()
    }

    override fun getFilteredImages(constraint: String, images: ArrayList<Image>): ArrayList<Image> {
        return photoViewModel.getFilteredImages(constraint, images)
    }

    private fun subscribe() {
        photoViewModel.getUser().observe(this, Observer {
            val str = "${it?.firstName} ${it?.lastName}"
            supportActionBar?.title = str
        })

        photoViewModel.getData().observe(this, Observer {
            it?.let {
                rvAdapter.setData(it)
                rvAdapter.notifyDataSetChanged()


                tvAutoCompleteSearch.setAdapter(ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, rvAdapter.getSearchSuggestions()))
            }
        })
    }

    private fun setupSearch() {
        tvAutoCompleteSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                rvAdapter.filter.filter(p0.toString())

                // shuffle images
                spinnerSort.setSelection(1)
                spinnerSort.setSelection(0)
            }
        })

        btnSearch.setOnClickListener {
            rvAdapter.filter.filter(tvAutoCompleteSearch.text.toString())
        }
    }

    private fun setDrawerLayoutToggle() {
        val actionBarToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
                                                    R.string.app_name,
                                                    R.string.app_name)
        drawerLayout.addDrawerListener(actionBarToggle)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar.apply {
            this?.setDisplayHomeAsUpEnabled(true)
            this?.setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        toolbar.setNavigationOnClickListener {
            if (drawerLayout.isDrawerOpen(Gravity.START))
                drawerLayout.closeDrawer(Gravity.START)
            else
                drawerLayout.openDrawer(Gravity.START)
        }
    }

    private fun setNavigationListener() {
        navigationView.setNavigationItemSelectedListener {
            drawerLayout.closeDrawers()

            when (it.itemId) {

                R.id.photos -> {
                }
                R.id.logout -> {
                    photoViewModel.clearSession()

                    startActivity(Intent(this, AuthenticationActivity::class.java))
                    finish()
                }
                else -> {
                }
            }
            true
        }
    }

    private fun setupButtons() {

        btnSelectAll.apply {
            this.visibility = View.VISIBLE
            setOnClickListener { rvAdapter.selectAll() }
        }

        btnDelete.apply {
            this.visibility = View.VISIBLE
            setOnClickListener {
                if (!rvAdapter.delete())
                    Toast.makeText(this.context, "Please select image(s) first", Toast.LENGTH_LONG)
                            .show()
            }
        }

        btnCapitalize.setOnClickListener {
            photoViewModel.convertImageTitlesToUpperCase()
            photoViewModel.updateData()

            // sort by title
            spinnerSort.setSelection(1)
        }

        ibSwitchView.apply {
            setImageResource(R.drawable.ic_view_list_black_24dp)

            setOnClickListener {
                isList = !isList

                if (isList) {
                    rvImages.layoutManager = LinearLayoutManager(context)
                    setImageResource(R.drawable.ic_grid_on_black_24dp)
                } else {
                    rvImages.layoutManager = GridLayoutManager(context, 2)
                    setImageResource(R.drawable.ic_view_list_black_24dp)
                }
            }
        }
    }

    private fun setupSpinner() {
        val items = resources.getStringArray(R.array.spinner_sort_items)
        spinnerSort.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items)

        spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                shuffleImages()
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p2) {
                    0 -> shuffleImages()
                    1 -> sortImagesByTitle()
                    2 -> sortImagesByDate()
                }
            }

        }
    }

    private fun shuffleImages() {
        rvAdapter.shuffle()
    }

    private fun sortImagesByTitle() {
        rvAdapter.sortByTitle()
    }

    private fun sortImagesByDate() {
        rvAdapter.sortByDate(ascending = false)
    }

    private fun setupRecyclerView() {

        rvAdapter = PhotoAdapter(ArrayList(), this as PhotoAdapterListener, this as Context)

        val recyclerView = rvImages
        recyclerView.apply {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun checkForPermissions() {
        if (Build.VERSION.SDK_INT >= 23 &&
                PermissionChecker.checkSelfPermission(this, Manifest.permission.RECEIVE_BOOT_COMPLETED)
                != PackageManager.PERMISSION_GRANTED
                && PermissionChecker.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && PermissionChecker.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                && PermissionChecker.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions()

        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private fun requestPermissions() {
        requestPermissions(arrayOf(
                android.Manifest.permission.RECEIVE_BOOT_COMPLETED,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_CONTACTS
        ), PERMISSIONS)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSIONS) {
            if (!grantResults.isEmpty()
                    && !(grantResults[0] == PackageManager.PERMISSION_GRANTED
                            && grantResults[1] == PackageManager.PERMISSION_GRANTED
                            && grantResults[2] == PackageManager.PERMISSION_GRANTED
                            && grantResults[3] == PackageManager.PERMISSION_GRANTED))
                requestPermissions()
        }
    }
}
