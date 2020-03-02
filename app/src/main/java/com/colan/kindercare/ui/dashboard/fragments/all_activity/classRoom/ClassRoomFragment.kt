package com.colan.kindercare.ui.dashboard.fragments.all_activity.classRoom


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR

import com.colan.kindercare.R
import com.colan.kindercare.databinding.FragmentClassRoomBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.dashboard.fragments.all_activity.AllActivityViewModel
import com.colan.kindercare.ui.modules.common.message.attachment.AttachmentActivity
import com.colan.kindercare.utils.Singleton
import com.colan.kindercare.utils.bundle
import com.colan.kindercare.utils.navigateTo
import kotlinx.android.synthetic.main.activity_compose.*
import kotlinx.android.synthetic.main.attachment_layout.view.*

/**
 * A simple [Fragment] subclass.
 */
class ClassRoomFragment :BaseFragment<FragmentClassRoomBinding,AllActivityViewModel>(),BaseNavigator {

    override val bindingVariable: Int
        get() = BR.allActivityVM
    override val layoutId: Int
        get() = R.layout.fragment_class_room
    override val viewModel: AllActivityViewModel
        get() = ViewModelProviders.of(this).get(AllActivityViewModel::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.attachmentLayout?.view_all_tv?.setOnClickListener {
            goTo(AttachmentActivity::class.java, bundle)
        }
    }

    override fun onClickView(v: View?) {
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        navigateTo(context!!,clazz, null)
    }
}
