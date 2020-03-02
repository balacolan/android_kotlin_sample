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
import com.colan.kindercare.data.model.StudentModel
import com.colan.kindercare.databinding.FragmentMealWeeklyBinding
import com.colan.kindercare.databinding.StudentListItemsBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.modules.admin.addWeeklyMenu.AddWeeklyMenuVM
import com.colan.kindercare.ui.modules.teacher.dailyActivity.selectStudentBottomSheet.SelectStudentBottomSheet
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import com.colan.kindercare.ui.modules.admin.addWeeklyMenu.AddWeeklyMenuActivity

/**
 * A simple [Fragment] subclass.
 */
class MealWeeklyFragment : BaseFragment<FragmentMealWeeklyBinding, AddWeeklyMenuVM>(),
    BaseNavigator, OnDataBindCallback<StudentListItemsBinding>,
    SelectStudentBottomSheet.OnUserSlectedListener {

    private var baseRecyclerAdapter: BaseRecyclerViewAdapter<StudentModel, StudentListItemsBinding>? = null

    override val bindingVariable: Int
        get() = BR.addWeeklyMenuVM
    override val layoutId: Int
        get() = R.layout.fragment_meal_weekly
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
        viewDataBinding?.addStudent?.setOnClickListener {
            val bottomSheetFragment = SelectStudentBottomSheet(this)
            bottomSheetFragment.show(fragmentManager!!, bottomSheetFragment.getTag())
        }
    }

    override fun selectedUserList(userList: ArrayList<String>) {
        putToast("dkdjfkd")
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
    }

    private fun setUpRecyclerView() {
        baseRecyclerAdapter = BaseRecyclerViewAdapter(
            R.layout.student_list_items,
            BR.studentItem,
            ArrayList(),
            null,
            this
        )
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)

    }

    override fun onResume() {
        super.onResume()
        AddWeeklyMenuVM(activity!!.application)
    }

    override fun onClickView(v: View?) {

    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {

    }



    override fun onItemClick(view: View, position: Int, v: StudentListItemsBinding) {
    }

    override fun onDataBind(v: StudentListItemsBinding, onClickListener: View.OnClickListener) {
    }



}
