package com.colan.kindercare.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class SchoolSelectionBroadCast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (schoolLister != null) {
            schoolLister!!.onTriggerSchoolSelection()
        }
    }

    companion object {
        var schoolLister: SchoolSelectionListner? = null
    }
}