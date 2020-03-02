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
import com.colan.kindercare.data.model.StudentModel
import com.colan.kindercare.databinding.FragmentClassroomDailyActivityBinding
import com.colan.kindercare.databinding.StudentListItemsBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.dashboard.IToolbar
import com.colan.kindercare.ui.modules.common.dailyactivity.DailyActivityVM


class ClassRoomFragmentDailyActivity : BaseFragment<FragmentClassroomDailyActivityBinding, DailyActivityVM>(),BaseNavigator
, OnDataBindCallback<StudentListItemsBinding> {

    private lateinit var mToolbarManager: IToolbar
    private var baseRecyclerAdapter: BaseRecyclerViewAdapter<StudentModel, StudentListItemsBinding>? =
        null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        mToolbarManager.changeToolbarTitle(getString(R.string.class_tittle))

        setUpRecyclerview()
        observeResponse()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mToolbarManager = context as IToolbar
    }

    override val bindingVariable: Int
        get() = BR.classRoomVM
    override val layoutId: Int
        get() = R.layout.fragment_classroom_daily_activity
    override val viewModel: DailyActivityVM
        get() = ViewModelProviders.of(this).get(DailyActivityVM::class.java)

    override fun onClickView(v: View?) {
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
    }
    private fun observeResponse() {
        viewModel.studentModel.observe(this, Observer {
            viewDataBinding?.courseItemRv?.adapter = baseRecyclerAdapter
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
    }

}
