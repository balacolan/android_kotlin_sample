package com.colan.kindercare.ui.dashboard.fragments.all_activity.photos


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.adapter.BaseRecyclerViewAdapter
import com.colan.kindercare.adapter.OnDataBindCallback
import com.colan.kindercare.data.model.PhotosModel
import com.colan.kindercare.databinding.FragmentPhotosBinding
import com.colan.kindercare.databinding.PhotoDownloadItemsBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.dashboard.fragments.all_activity.AllActivityViewModel

/**
 * A simple [Fragment] subclass.
 */
class PhotosFragment : BaseFragment<FragmentPhotosBinding,AllActivityViewModel>(),BaseNavigator,
    OnDataBindCallback<PhotoDownloadItemsBinding> {

    private var photosList = ArrayList<PhotosModel>()
    private var baseRecyclerAdapter: BaseRecyclerViewAdapter<PhotosModel, PhotoDownloadItemsBinding>? = null

    override val bindingVariable: Int
        get() = BR.allActivityVM
    override val layoutId: Int
        get() = R.layout.fragment_photos
    override val viewModel: AllActivityViewModel
        get() = ViewModelProviders.of(this).get(AllActivityViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        observeResponse()
    }

    private fun observeResponse() {
        viewModel.phtosdModel.observe(this, Observer {
            if(it!=null && it.isNotEmpty()){
                photosList.addAll(it)
                setUpRecyclerview(photosList)
            }
        })
    }

    private fun setUpRecyclerview(photosList: ArrayList<PhotosModel>) {
        baseRecyclerAdapter = BaseRecyclerViewAdapter(
            R.layout.photo_download_items,
            BR.photosItem,
            photosList,
            null,
            this
        )

       viewDataBinding?.photosRv?.adapter=baseRecyclerAdapter
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
