package com.colan.kindercare.ui.modules.common.dailyactivity.details

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.adapter.BaseRecyclerViewAdapter
import com.colan.kindercare.adapter.OnDataBindCallback
import com.colan.kindercare.data.model.MealModel
import com.colan.kindercare.data.model.StudentModel
import com.colan.kindercare.databinding.FragmentDailyActivityMealBinding
import com.colan.kindercare.databinding.MealListItemsBinding
import com.colan.kindercare.databinding.StudentListItemsBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.dashboard.IToolbar
import com.colan.kindercare.ui.modules.admin.addWeeklyMenu.AddWeeklyMenuActivity
import com.colan.kindercare.ui.modules.common.dailyactivity.DailyActivityVM
import com.colan.kindercare.ui.modules.teacher.dailyActivity.selectStudentBottomSheet.SelectStudentBottomSheet
import kotlinx.android.synthetic.main.custom_toolbar.view.*


class MealFragment : BaseFragment<FragmentDailyActivityMealBinding, DailyActivityVM>(),
    BaseNavigator, OnDataBindCallback<StudentListItemsBinding> {

    override fun onClickView(v: View?) {
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
    }

    private lateinit var mToolbarManager: IToolbar
    private var baseRecyclerAdapter: BaseRecyclerViewAdapter<StudentModel, StudentListItemsBinding>? =
        null

    private var baseRecyclerAdapterCourseItem: BaseRecyclerViewAdapter<MealModel, MealListItemsBinding>? = null


    override val bindingVariable: Int
        get() = BR.mealViewModel
    override val layoutId: Int
        get() = R.layout.fragment_daily_activity_meal
    override val viewModel: DailyActivityVM
        get() = ViewModelProviders.of(this).get(DailyActivityVM::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        mToolbarManager.changeToolbarTitle(getString(R.string.meal_tittle))
        setUpRecyclerview()
        observeResponse()
        setUpRecyclerviewCourseItems()
        observeResponseCourseItems()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mToolbarManager = context as IToolbar
    }

   /* override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding?.editTv?.setOnClickListener {
            val bottomSheetFragment = SelectStudentBottomSheet()
            bottomSheetFragment.show(fragmentManager!!, bottomSheetFragment.getTag())
        }
    }*/

    private fun observeResponse() {
        viewModel.studentModel.observe(this, Observer {
            viewDataBinding?.studentsRv?.adapter = baseRecyclerAdapter
            baseRecyclerAdapter?.addDataSet(it)
            baseRecyclerAdapter?.notifyDataSetChanged()
        })
    }

    private fun setUpRecyclerview() {
        baseRecyclerAdapter = BaseRecyclerViewAdapter(
            R.layout.student_list_items,
            BR.studentItem,
            ArrayList(),
            null,
            this
        )
    }

    override fun onItemClick(view: View, position: Int, v: StudentListItemsBinding) {
    }

    override fun onDataBind(v: StudentListItemsBinding, onClickListener: View.OnClickListener) {
        v.rootLayout.setOnClickListener(onClickListener)
    }

    private fun observeResponseCourseItems() {
        viewModel.mealModel.observe(this, Observer {
            viewDataBinding?.courseItemRv?.adapter=baseRecyclerAdapterCourseItem
            baseRecyclerAdapterCourseItem?.addDataSet(it)
            baseRecyclerAdapterCourseItem?.notifyDataSetChanged()
        })
    }

    private fun setUpRecyclerviewCourseItems() {
        baseRecyclerAdapterCourseItem = BaseRecyclerViewAdapter(R.layout.meal_list_items, BR.mealItem, ArrayList(), null,
            object :OnDataBindCallback<MealListItemsBinding>{
                override fun onItemClick(view: View, position: Int, v: MealListItemsBinding) {
                }
                override fun onDataBind(v: MealListItemsBinding, onClickListener: View.OnClickListener) {
                }
            }
        )
    }

}
