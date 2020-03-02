package com.colan.kindercare.utils

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

class Alert {
    companion object {
        fun createYesNoAlert(context: Context,
                             title: String?, message: String, listener: OnAlertClickListener?): AlertDialog.Builder {
            val dialog = AlertDialog.Builder(context)
            if (title != null)
                dialog.setTitle(title)
            else
                dialog.setTitle("Information")
            dialog.setMessage(message)
            dialog.setPositiveButton(android.R.string.yes) { dialogInterface, _ ->
                listener?.onPositive(dialogInterface)
            }
            dialog.setNegativeButton(android.R.string.no) { dialogInterface, _ ->
                listener?.onNegative(dialogInterface)
            }
            dialog.show()
            return dialog
        }
        fun createOkAlert(context: Context,
                          title: String?, message: String, listener: OnAlertClickListener?): AlertDialog.Builder {
            val dialog = AlertDialog.Builder(context)
            if (title != null)
                dialog.setTitle(title)
            else
                dialog.setTitle("Information")
            dialog.setMessage(message)
            dialog.setPositiveButton(android.R.string.yes) { dialogInterface, _ ->
                listener?.onPositive(dialogInterface)
            }
            dialog.show()
            return dialog
        }
    }
    interface OnAlertClickListener {

        fun onPositive(dialog: DialogInterface)

        fun onNegative(dialog: DialogInterface)
    }
}