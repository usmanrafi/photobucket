package com.vend.photobucket.dagger

import android.content.Context
import android.content.SharedPreferences
import com.vend.photobucket.application.PhotoApplication
import com.vend.photobucket.data.RealmHelper
import com.vend.photobucket.data.SharedPreferenceHelper
import dagger.Module
import dagger.Provides
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Singleton

@Module
class AppModule(app: PhotoApplication) {

    private val photoApplication: PhotoApplication = app

    @Provides
    @Singleton
    fun provideApplicationContext() = photoApplication as Context

    @Provides
    @Singleton
    fun provideRealm() = Realm.getInstance(
            RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()
                    .build()
    )!!

    @Provides
    @Singleton
    fun provideRealmHelper(realm: Realm) = RealmHelper(realm)

    private val PREF_NAME = "vend.photobucket"
    @Provides
    @Singleton
    fun provideSharedPreferences(app: Context) = app.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)!!

    @Provides
    @Singleton
    fun provideSharedPreferenceHelper(sharedPreferences: SharedPreferences) =
            SharedPreferenceHelper(sharedPreferences)

}