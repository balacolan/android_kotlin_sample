package com.colan.kindercare.ui.modules.teacher.dailyActivity.AllActivity.fragment.meal

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.adapter.BaseRecyclerViewAdapter
import com.colan.kindercare.adapter.OnDataBindCallback
import com.colan.kindercare.data.model.MealModel
import com.colan.kindercare.data.model.StudentModel
import com.colan.kindercare.databinding.FragmentMealWeeklyPreviewBinding
import com.colan.kindercare.databinding.MealListItemsBinding
import com.colan.kindercare.databinding.StudentListItemsBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.modules.admin.addWeeklyMenu.AddWeeklyMenuActivity
import com.colan.kindercare.ui.modules.admin.addWeeklyMenu.AddWeeklyMenuVM
import kotlinx.android.synthetic.main.custom_toolbar.view.*

/**
 * A simple [Fragment] subclass.
 */
class MealWeeklyPreviewFragment : BaseFragment<
        FragmentMealWeeklyPreviewBinding, AddWeeklyMenuVM>(),
    BaseNavigator {

    private var baseRecyclerAdapter: BaseRecyclerViewAdapter<StudentModel, StudentListItemsBinding>? =
        null
    private var baseRecyclerAdapterCourseItem: BaseRecyclerViewAdapter<MealModel, MealListItemsBinding>? =
        null

    override val bindingVariable: Int
        get() = BR.addWeeklyMenuVM
    override val layoutId: Int
        get() = R.layout.fragment_meal_weekly_preview
    override val viewModel: AddWeeklyMenuVM
        get() = ViewModelProviders.of(this).get(AddWeeklyMenuVM::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        setUpRecyclerView()
        observeResponse()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.toolbar?.title_toolbar?.text = getString(R.string.add_weekly_menu)
        viewDataBinding?.toolbar?.nav_icon_iv?.setOnClickListener {
            (activity as AddWeeklyMenuActivity).handleOnBack()
        }
    }

    private fun observeResponse() {
        viewModel.studentModel.observe(this, Observer {
            viewDataBinding?.studentsRv?.adapter = baseRecyclerAdapter
            baseRecyclerAdapter?.addDataSet(it)
            baseRecyclerAdapter?.notifyDataSetChanged()
            viewDataBinding?.teachersRv?.adapter = baseRecyclerAdapter
            baseRecyclerAdapter?.addDataSet(it.shuffled())
            baseRecyclerAdapter?.notifyDataSetChanged()
        })
        viewModel.mealModel.observe(this, Observer {
            viewDataBinding?.courseItemRv?.adapter = baseRecyclerAdapterCourseItem
            baseRecyclerAdapterCourseItem?.addDataSet(it)
            baseRecyclerAdapterCourseItem?.notifyDataSetChanged()

        })

    }

    private fun setUpRecyclerView() {
        baseRecyclerAdapter = BaseRecyclerViewAdapter(
            R.layout.student_list_items,
            BR.studentItem,
            ArrayList(),
            null, object : OnDataBindCallback<StudentListItemsBinding> {
                override fun onItemClick(view: View, position: Int, v: StudentListItemsBinding) {
                }

                override fun onDataBind(
                    v: StudentListItemsBinding,
                    onClickListener: View.OnClickListener
                ) {
                }

            }

        )

        baseRecyclerAdapterCourseItem = BaseRecyclerViewAdapter(
            R.layout.meal_list_items,
            BR.mealItem,
            ArrayList(),
            null,
            object : OnDataBindCallback<MealListItemsBinding> {
                override fun onItemClick(view: View, position: Int, v: MealListItemsBinding) {

                }

                override fun onDataBind(
                    v: MealListItemsBinding,
                    onClickListener: View.OnClickListener
                ) {
                }

            }
        )
    }

    override fun onClickView(v: View?) {

    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {

    }

}
