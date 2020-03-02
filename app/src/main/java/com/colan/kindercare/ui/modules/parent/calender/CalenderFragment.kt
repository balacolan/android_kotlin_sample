package com.colan.kindercare.ui.modules.parent.calender


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR

import com.colan.kindercare.R
import com.colan.kindercare.adapter.BaseRecyclerViewAdapter
import com.colan.kindercare.adapter.OnDataBindCallback
import com.colan.kindercare.data.model.CalenderHolydayModel
import com.colan.kindercare.data.model.MealModel
import com.colan.kindercare.databinding.CalenderHolidayListItemsBinding
import com.colan.kindercare.databinding.FragmentCalenderBinding
import com.colan.kindercare.databinding.MealListItemsBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.dashboard.IToolbar
import com.colan.kindercare.ui.modules.common.weeklyMenu.WeeklyMenuVM
import com.colan.kindercare.utils.Singleton

/**
 * A simple [Fragment] subclass.
 */
class CalenderFragment :BaseFragment<FragmentCalenderBinding,CalenderVM>(),BaseNavigator,
    OnDataBindCallback<CalenderHolidayListItemsBinding> {

    private var baseRecyclerAdapter: BaseRecyclerViewAdapter<CalenderHolydayModel, CalenderHolidayListItemsBinding>? = null

    private lateinit var mToobarManagner: IToolbar

    override val bindingVariable: Int
        get() = BR.calenderVM
    override val layoutId: Int
        get() = R.layout.fragment_calender
    override val viewModel: CalenderVM
        get() =  ViewModelProviders.of(this).get(CalenderVM::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        mToobarManagner.changeToolbarTitle(getString(R.string.calender_tittle))
        setUpRecyclerview()
        observeResponse()
    }


    private fun observeResponse() {
        viewModel.holidayModel.observe(this, Observer {
            viewDataBinding?.holidayRv?.adapter=baseRecyclerAdapter
            baseRecyclerAdapter?.addDataSet(it)
            baseRecyclerAdapter?.notifyDataSetChanged()
        })
    }

    private fun setUpRecyclerview() {
        baseRecyclerAdapter = BaseRecyclerViewAdapter(
            R.layout.calender_holiday_list_items,
            BR.holidayItem,
            ArrayList(),
            null,
            this
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Singleton.isDashBoardFragmentVisible =true
        mToobarManagner = context as IToolbar
    }

    override fun onItemClick(view: View, position: Int, v: CalenderHolidayListItemsBinding) {
    }

    override fun onDataBind(
        v: CalenderHolidayListItemsBinding,
        onClickListener: View.OnClickListener
    ) {
    }


    override fun onClickView(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
