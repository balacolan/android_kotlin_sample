package com.colan.kindercare.ui.modules.teacher.dailyActivity.AllActivity.fragment.bathroom

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR

import com.colan.kindercare.R
import com.colan.kindercare.databinding.FragmentBathRoomDetailBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.modules.teacher.dailyActivity.DailyActivityVM
import kotlinx.android.synthetic.main.custom_toolbar.view.*


class BathRoomDetailFragment : BaseFragment<FragmentBathRoomDetailBinding, DailyActivityVM>(),
BaseNavigator{

    override val bindingVariable: Int
        get() = BR.bathRoomDetailVM
    override val layoutId: Int
        get() = R.layout.fragment_bath_room_detail
    override val viewModel: DailyActivityVM
        get() = ViewModelProviders.of(this).get(DailyActivityVM::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.toolbar?.title_toolbar?.setText(getString(R.string.activity_detail))
        viewDataBinding?.toolbar?.nav_icon_iv?.setOnClickListener {
            activity?.finish()
            replaceFragment(R.id.activity_container, BathRoomStudentFragment(),"Bathroom","Bathroom")

        }
    }

    override fun onClickView(v: View?) {

    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {

    }

}
