package com.colan.kindercare.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView

class LoadingIndicatorView(context: Context) : Dialog(context) {

    lateinit var messageText: TextView
    lateinit var layout: LinearLayout
    var loadingText = ""
    var colorCode = "#20B8AB"

    init {
        initIndicatorView(context)
    }

    fun setColor(color: String) {
        colorCode=color
    }

    fun setText(message: String) {
        loadingText = message
    }

    private fun initIndicatorView(context: Context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        this.layout = LinearLayout(context)
        this.layout.orientation = LinearLayout.VERTICAL
        val progress = ProgressBar(context)
        progress.isIndeterminate=false
        progress.indeterminateDrawable
            .setColorFilter(Color.parseColor(colorCode), PorterDuff.Mode.SRC_IN)
        this.layout.addView(progress, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        this.messageText = TextView(context)
        this.messageText.text = loadingText
        this.messageText.setTextColor(Color.WHITE)
        this.layout.gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL
        this.layout.addView(messageText)
        this.addContentView(layout, ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT))
        this.setCancelable(false)
    }
}