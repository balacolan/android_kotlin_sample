package com.colan.kindercare.ui.modules.common.weeklyMenu


import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.adapter.BaseRecyclerViewAdapter
import com.colan.kindercare.adapter.OnDataBindCallback
import com.colan.kindercare.data.model.MealModel
import com.colan.kindercare.databinding.FragmentWeeklyMenuBinding
import com.colan.kindercare.databinding.MealListItemsBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.cusome_ui.PersianDatePicker
import com.colan.kindercare.ui.cusome_ui.models.Calendars
import com.colan.kindercare.ui.cusome_ui.models.Day
import com.colan.kindercare.ui.cusome_ui.models.YearMonth
import com.colan.kindercare.ui.dashboard.IToolbar
import com.colan.kindercare.ui.modules.admin.addWeeklyMenu.AddWeeklyMenuActivity
import com.colan.kindercare.utils.Constants
import com.colan.kindercare.utils.Singleton
import com.colan.kindercare.utils.bundle
import com.colan.kindercare.utils.navigateTo
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class WeeklyMenuFragment : BaseFragment<FragmentWeeklyMenuBinding,WeeklyMenuVM>(),BaseNavigator,
    OnDataBindCallback<MealListItemsBinding> {


    private var baseRecyclerAdapter: BaseRecyclerViewAdapter<MealModel, MealListItemsBinding>? = null
    private lateinit var mToobarManagner: IToolbar

    override val bindingVariable: Int
        get() = BR.weeklyVM
    override val layoutId: Int
        get() = com.colan.kindercare.R.layout.fragment_weekly_menu
    override val viewModel: WeeklyMenuVM
        get() = ViewModelProviders.of(this).get(WeeklyMenuVM::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        mToobarManagner.changeToolbarTitle(getString(com.colan.kindercare.R.string.weekly_menu))
        setUpRecyclerview()
        observeResponse()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding?.addNewActivityIv?.setOnClickListener {
            bundle.clear()
            bundle.putString(Constants.ACTIVITY_TYPE,"Meal")
            goTo(AddWeeklyMenuActivity::class.java, bundle)
        }
        viewDataBinding?.editTv?.setOnClickListener {
            bundle.clear()
            bundle.putString(Constants.ACTIVITY_TYPE,"Meal")
            goTo(AddWeeklyMenuActivity::class.java, bundle)
        }

        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        viewDataBinding?.persianDatePicker
            ?.setYearMonths(Calendars(date).yearMonths)
            ?.setDefaultItemBackgroundColor(com.colan.kindercare.R.color.white)  // background color of non-selected item
            ?.setDefaultItemTextColor(com.colan.kindercare.R.color.defaultTextColor)  // text color of non-selected item
            ?.setSelectedItemBackgroundColor(com.colan.kindercare.R.color.colorPrimary) // background color of selected item
            ?.setSelectedItemTextColor(com.colan.kindercare.R.color.selectText)   // text color of selected item
            //                .setSelectedItemBackground(R.drawable.orange_gradient)
            ?.setListener(object : PersianDatePicker.OnDaySelectListener {
                override fun onDaySelect(yearMonth: YearMonth, day: Day) {
                    // right your code here when item is selected

                    putToast(""+yearMonth.getYear() + "-" + yearMonth.getMonthNumber() + "-" + day.getNumberString())

                }
            })
            ?.setItemElevation(4f)  // default is 0
            ?.setItemRadius(2f)  // default is 0
            ?.hasAnimation(false) // Animation for item selection . default is false
            ?.load()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Singleton.isDashBoardFragmentVisible =true
        mToobarManagner = context as IToolbar
    }

    private fun observeResponse() {
        viewModel.mealModel.observe(this, Observer {
            viewDataBinding?.menuRv?.adapter=baseRecyclerAdapter
            baseRecyclerAdapter?.addDataSet(it)
            baseRecyclerAdapter?.notifyDataSetChanged()
        })
    }

    private fun setUpRecyclerview() {
        baseRecyclerAdapter = BaseRecyclerViewAdapter(
            com.colan.kindercare.R.layout.meal_list_items,
            BR.mealItem,
            ArrayList(),
            null,
            this
        )
    }


    override fun onItemClick(view: View, position: Int, v: MealListItemsBinding) {
    }

    override fun onDataBind(v: MealListItemsBinding, onClickListener: View.OnClickListener) {
    }



    override fun onClickView(v: View?) {
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        navigateTo(context!!,clazz,mExtras)
    }
}
