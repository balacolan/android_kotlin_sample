package com.colan.kindercare.bindiingUtils.implementaion

import androidx.databinding.BindingAdapter
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView

interface IImageViewBinding {

    @BindingAdapter("customImageFromDrawable")
    fun setImageFromDrawable(imageView: ImageView, filePath: Int?)

    @BindingAdapter("customImageFromUrl")
    fun setImageFromUrl(imageView: ImageView, filePath: String?)

    @BindingAdapter("goneUnless")
    fun goneUnless(view: View, str: String?)

    @BindingAdapter("setLeaveImageResources")
    fun setLeaveImageResources(imageView: AppCompatImageView, fileType: String)

}