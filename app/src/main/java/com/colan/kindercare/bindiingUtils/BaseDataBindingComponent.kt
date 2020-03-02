package com.colan.kindercare.bindiingUtils

import androidx.databinding.DataBindingComponent
import com.colan.kindercare.bindiingUtils.implementaion.IImageViewBinding
import com.colan.kindercare.bindiingUtils.implementaion.ISpinnerBinding
import com.colan.kindercare.bindiingUtils.module.BaseImageViewBinding
import com.colan.kindercare.bindiingUtils.module.BaseSpinnerBinding

class BaseDataBindingComponent : DataBindingComponent {

    override fun getISpinnerBinding(): ISpinnerBinding {
        return BaseSpinnerBinding()
    }

    override fun getIImageViewBinding(): IImageViewBinding {
        return BaseImageViewBinding()
    }

}