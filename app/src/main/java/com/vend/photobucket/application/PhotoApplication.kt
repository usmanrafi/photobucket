package com.vend.photobucket.application

import android.app.Application
import com.vend.photobucket.dagger.AppComponent
import com.vend.photobucket.dagger.AppModule
import com.vend.photobucket.dagger.DaggerAppComponent
import io.realm.Realm

class PhotoApplication: Application(){

    private lateinit var appComponent: AppComponent

    @Override
    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
        appComponent.inject(this)

    }

    fun getAppComponent() = appComponent
}