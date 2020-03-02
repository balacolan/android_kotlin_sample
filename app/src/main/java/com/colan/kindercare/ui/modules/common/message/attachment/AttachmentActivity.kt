package com.colan.kindercare.ui.modules.common.message.attachment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.adapter.BaseRecyclerViewAdapter
import com.colan.kindercare.adapter.OnDataBindCallback
import com.colan.kindercare.data.model.PhotosModel
import com.colan.kindercare.databinding.ActivityAttachmentBinding
import com.colan.kindercare.databinding.PhotoDownloadItemsBinding
import com.colan.kindercare.ui.base.BaseActivity
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.modules.common.message.MessageVM
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class AttachmentActivity :BaseActivity<ActivityAttachmentBinding,MessageVM>(),BaseNavigator,
    OnDataBindCallback<PhotoDownloadItemsBinding> {



    private var baseRecyclerAdapter: BaseRecyclerViewAdapter<PhotosModel, PhotoDownloadItemsBinding>? = null

    override fun getBindingVariable(): Int {
        return BR.messageVM
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_attachment
    }

    override fun getViewModel(): MessageVM {
        return ViewModelProviders.of(this).get(MessageVM::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getViewDataBinding().toolbar.title_toolbar.text=getString(R.string.attachment_title)
        getViewDataBinding().toolbar.nav_icon_iv.setOnClickListener { onBackPressed() }
        setUpRecyclerview()
        observeResponse()
    }

    private fun observeResponse() {
        mViewModel?.phtosdModel?.observe(this, Observer {
            getViewDataBinding().attachmentRv?.adapter=baseRecyclerAdapter
            baseRecyclerAdapter?.addDataSet(it)
            baseRecyclerAdapter?.notifyDataSetChanged()
        })
    }

    private fun setUpRecyclerview() {
        baseRecyclerAdapter = BaseRecyclerViewAdapter(
            R.layout.photo_download_items,
            BR.photosItem,
            ArrayList(),
            null,
            this
        )
    }

    override fun onItemClick(view: View, position: Int, v: PhotoDownloadItemsBinding) {
    }

    override fun onDataBind(v: PhotoDownloadItemsBinding, onClickListener: View.OnClickListener) {
    }


    override fun onClickView(v: View?) {
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
    }


}
