package com.vend.photobucket.ui.photo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.vend.photobucket.R
import com.vend.photobucket.application.PhotoApplication
import com.vend.photobucket.data.RealmHelper
import com.vend.photobucket.data.SharedPreferenceHelper
import com.vend.photobucket.model.Image
import com.vend.photobucket.model.User
import com.vend.photobucket.ui.authentication.AuthenticationActivity
import com.vend.photobucket.ui.photo.addphoto.AddPhotoFragment
import com.vend.photobucket.ui.photo.details.DetailsFragment
import io.realm.RealmList
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class PhotoActivity : AppCompatActivity() {

    @Inject
    lateinit var addPhotoFragment: AddPhotoFragment

    @Inject
    lateinit var sharedPreferenceHelper: SharedPreferenceHelper
    @Inject
    lateinit var realmHelper: RealmHelper

    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar

    private lateinit var rvAdapter: PhotoAdapter

    private var isList = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        (applicationContext as PhotoApplication).getAppComponent().inject(this)

        navigationView = drawer
        drawerLayout = drawer_layout
        toolbar = toolbar_layout


        setDrawerLayoutToggle()
        setupToolbar()
        setNavigationListener()

        var user: User?
        val phoneNumber = sharedPreferenceHelper.getSession()
        phoneNumber?.let{
            user = realmHelper.getUser(it)
            val str = "${user?.firstName} ${user?.lastName}"
            supportActionBar?.title = str
        }

        setupButtons()
        setupRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.btnAdd -> {
                supportFragmentManager
                        .beginTransaction()
                        .replace(android.R.id.content, addPhotoFragment)
                        .addToBackStack("PhotoActivity")
                        .commit()

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showImageDetails(image: Image){
        val detailsFragment = DetailsFragment.newInstance(image)

        supportFragmentManager
                .beginTransaction()
                .replace(android.R.id.content, detailsFragment)
                .addToBackStack("PhotoActivity")
                .commit()


        Toast.makeText(this, "$image", Toast.LENGTH_SHORT)
                .show()
    }

    private fun setDrawerLayoutToggle(){
        val actionBarToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
                                                    R.string.app_name,
                                                    R.string.app_name)
        drawerLayout.addDrawerListener(actionBarToggle)
    }

    private fun setupToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar.apply {
            this?.setDisplayHomeAsUpEnabled(true)
            this?.setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        toolbar.setNavigationOnClickListener{
            if(drawerLayout.isDrawerOpen(Gravity.START))
                drawerLayout.closeDrawer(Gravity.START)
            else
                drawerLayout.openDrawer(Gravity.START)
        }
    }

    private fun setNavigationListener(){
        navigationView.setNavigationItemSelectedListener {
            drawerLayout.closeDrawers()

            when(it.itemId){

                R.id.photos ->{}
                R.id.logout ->{
                    sharedPreferenceHelper.clearSession()

                    startActivity(Intent(this, AuthenticationActivity::class.java))
                    finish()
                }
                else->{}
            }
            true
        }
    }

    private fun setupButtons(){

        btnSelectAll.apply{
            this.visibility = View.VISIBLE // todo: handle this
            setOnClickListener { rvAdapter.selectAll() }
        }

        btnDelete.apply {
            this.visibility = View.VISIBLE // todo: handle this
            setOnClickListener {
                if(!rvAdapter.delete())
                    Toast.makeText(this.context, "Please select image(s) first", Toast.LENGTH_LONG)
                            .show()
            }
        }

        ibSwitchView.apply {
            setImageResource(R.drawable.ic_view_list_black_24dp)

            setOnClickListener {
                isList = !isList

                if(isList){
                    rvImages.layoutManager = LinearLayoutManager(context)
                    setImageResource(R.drawable.ic_grid_on_black_24dp)
                }
                else{
                    rvImages.layoutManager = GridLayoutManager(context, 2)
                    setImageResource(R.drawable.ic_view_list_black_24dp)
                }
            }
        }
    }

    fun updateItem(image: Image){

        Toast.makeText(this, "$image", Toast.LENGTH_SHORT)
                .show()
    }

    private fun setupRecyclerView(){

        rvAdapter = PhotoAdapter(getImages(), this)

        val recyclerView = rvImages
        recyclerView.apply {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(context)


        }
    }

    private fun getImages(): RealmList<Image>{
        val arrayList: RealmList<Image> = RealmList()

        var i:Long = 0
        arrayList.add(Image(i++,"111111111","Title $i", "Desc", ""))
        arrayList.add(Image(i++,"111111111","Title $i", "Desc", ""))
        arrayList.add(Image(i++,"111111111","Title $i", "Desc", ""))
        arrayList.add(Image(i++,"111111111","Title $i", "Desc", ""))
        arrayList.add(Image(i++,"111111111","Title $i", "Desc", ""))
        arrayList.add(Image(i++,"111111111","Title $i", "Desc", ""))
        arrayList.add(Image(i++,"111111111","Title $i", "Desc", ""))
        arrayList.add(Image(i++,"111111111","Title $i", "Desc", ""))
        arrayList.add(Image(i++,"111111111","Title $i", "Desc", ""))
        arrayList.add(Image(i++,"111111111","Title $i", "Desc", ""))
        arrayList.add(Image(i++,"111111111","Title $i", "Desc", ""))
        arrayList.add(Image(i++,"111111111","Title $i", "Desc", ""))
        arrayList.add(Image(i++,"111111111","Title $i", "Desc", ""))
        arrayList.add(Image(i++,"111111111","Title $i", "Desc", ""))
        arrayList.add(Image(i++,"111111111","Title $i", "Desc", ""))
        arrayList.add(Image(i++,"111111111","Title $i", "Desc", ""))
        arrayList.add(Image(i++,"111111111","Title $i", "Desc", ""))
        arrayList.add(Image(i++,"111111111","Title $i", "Desc", ""))
        arrayList.add(Image(i++,"111111111","Title $i", "Desc", ""))
        arrayList.add(Image(i++,"111111111","Title $i", "Desc", ""))

        return arrayList
    }
}
