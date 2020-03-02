package com.colan.kindercare.bindiingUtils.module

import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.colan.kindercare.R
import com.colan.kindercare.bindiingUtils.implementaion.IImageViewBinding
import com.colan.kindercare.utils.Constants

class BaseImageViewBinding : IImageViewBinding {

    override fun setLeaveImageResources(imageView: AppCompatImageView, fileType: String) {
        val requestOptions = RequestOptions().centerCrop()
        var filePath: Int = R.drawable.ic_annual_leave
        when (fileType) {
            Constants.SICK_LEAVE -> filePath = R.drawable.ic_medical_leave
            Constants.CASUAL_LEAVE -> filePath = R.drawable.ic_annual_leave
            Constants.LOP -> filePath = R.drawable.ic_other_leave
        }
        Glide
            .with(imageView.context)
            .load(filePath)
            .apply(requestOptions)
            .into(imageView)
    }

    override fun setImageFromDrawable(imageView: ImageView, filePath: Int?) {
        Glide.with(imageView.context).load(filePath).into(imageView)
    }

    override fun setImageFromUrl(imageView: ImageView, filePath: String?) {
        val option: RequestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            //.placeholder(R.drawable.cleopatra_face_dark)
            .fitCenter()

        Glide.with(imageView.context)
            .load(filePath)
            .apply(option)
            .into(imageView)
    }

    override fun goneUnless(view: View, str: String?) {
        view.visibility = if (str != null) {

            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

}