package com.colan.kindercare.ui.modules.teacher.dailyActivity.AllActivity.fragment.meal


import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.databinding.FragmentMealWeeklyDetailBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.modules.admin.addWeeklyMenu.AddWeeklyMenuVM
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_meal_weekly_detail.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class MealWeeklyDetailFragment :BaseFragment<FragmentMealWeeklyDetailBinding,AddWeeklyMenuVM>(),BaseNavigator {

    override val bindingVariable: Int
        get() = BR.addWeeklyMenuVM
    override val layoutId: Int
        get() = R.layout.fragment_meal_weekly_detail
    override val viewModel:AddWeeklyMenuVM
        get() = ViewModelProviders.of(this).get(AddWeeklyMenuVM::class.java)

    override fun onClickView(v: View?) {

    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.toolbar?.title_toolbar?.text = getString(R.string.add_weekly_menu)
        viewDataBinding?.toolbar?.nav_icon_iv?.setOnClickListener {
            activity?.finish()

        }
    }



}
