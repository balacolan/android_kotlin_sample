package com.colan.kindercare.utils

import android.content.IntentFilter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.colan.kindercare.R


object Singleton {

    var isDashBoardFragmentVisible: Boolean=false
    var isNetworkConnected: Boolean = false
    var isInboxClicked:Boolean=false
    var isSentClicked:Boolean=false
    var isBackPressFromMessage:Boolean=false
    var sendToList=ArrayList<String>()
    var userListId=ArrayList<String>()
    var userListEmail=ArrayList<String>()

    var type:String=""
    var mIntentFilter = IntentFilter()
    @BindingAdapter("ImageBinding")
    @JvmStatic
    fun ImageBinding(image: ImageView, imageUrl: Int) {
        Glide.with(image.context)
            .load(imageUrl)
            .placeholder(R.drawable.ic_choose_photo)
            .into(image)
    }

    @BindingAdapter("layout_marginTop")
    @JvmStatic
    fun setMarginTop(view: View, margin: Int) {
        val params = view.layoutParams
        if (params is ViewGroup.MarginLayoutParams) {
            params.topMargin = margin
            view.requestLayout()
        }
    }
}