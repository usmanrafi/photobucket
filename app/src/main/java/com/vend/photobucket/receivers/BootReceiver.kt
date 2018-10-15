package com.vend.photobucket.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.vend.photobucket.ui.authentication.AuthenticationActivity

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if(intent.action == Intent.ACTION_BOOT_COMPLETED){
            val intent = Intent(context, AuthenticationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }
}
