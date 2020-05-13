package com.prajyotmane.healthcare

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater

class LoadingDialogBox(var myactivity: Activity) {
    var dialog:AlertDialog? = null

    fun startLoading(){
        var builder: AlertDialog.Builder = AlertDialog.Builder(myactivity)
        var inflator: LayoutInflater = myactivity.layoutInflater

        builder.setView(inflator.inflate(R.layout.loading_dialog, null))
        builder.setCancelable(false)

        dialog = builder.create()
        dialog?.show()
    }

    fun cancelLoading(){
           dialog?.dismiss()
    }

}