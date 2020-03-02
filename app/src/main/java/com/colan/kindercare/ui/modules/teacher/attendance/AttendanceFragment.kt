package com.colan.kindercare.ui.modules.teacher.attendance


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR

import com.colan.kindercare.R
import com.colan.kindercare.adapter.BaseRecyclerViewAdapter
import com.colan.kindercare.adapter.OnDataBindCallback
import com.colan.kindercare.data.model.AttendanceModel
import com.colan.kindercare.databinding.AttendanceListItemsBinding
import com.colan.kindercare.databinding.FragmentAttendanceBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.dashboard.IToolbar
import com.colan.kindercare.ui.modules.teacher.TeacherDashboardVM
import com.colan.kindercare.utils.*

/**
 * A simple [Fragment] subclass.
 */
class AttendanceFragment : BaseFragment<FragmentAttendanceBinding, TeacherDashboardVM>(),
    BaseNavigator,
    OnDataBindCallback<AttendanceListItemsBinding> {

    private var baseRecyclerAdapter: BaseRecyclerViewAdapter<AttendanceModel, AttendanceListItemsBinding>? =
        null

    private lateinit var mToobarManagner: IToolbar


    override val bindingVariable: Int
        get() = BR.teacherVM
    override val layoutId: Int
        get() = R.layout.fragment_attendance
    override val viewModel: TeacherDashboardVM
        get() = ViewModelProviders.of(this).get(TeacherDashboardVM::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        mToobarManagner.changeToolbarTitle(getString(R.string.daily_attendance_title))
        setUpRecyclerview()
        observeResponse()
    }

    private fun observeResponse() {
        viewModel.attendanceModel.observe(this, Observer {
            viewDataBinding?.attendanceRv?.adapter = baseRecyclerAdapter
            baseRecyclerAdapter?.addDataSet(it)
            baseRecyclerAdapter?.notifyDataSetChanged()
        })
    }

    private fun setUpRecyclerview() {
        baseRecyclerAdapter = BaseRecyclerViewAdapter(
            R.layout.attendance_list_items,
            BR.attendanceItem,
            ArrayList(),
            null,
            this
        )
    }


    override fun onItemClick(view: View, position: Int, v: AttendanceListItemsBinding) {
    }

    override fun onDataBind(v: AttendanceListItemsBinding, onClickListener: View.OnClickListener) {
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Singleton.isDashBoardFragmentVisible = true
        mToobarManagner = context as IToolbar
    }


    override fun onClickView(v: View?) {
        when (v?.id) {
            R.id.date_previous_iv -> {
                val date = viewModel.currentDate.get()?.let { StringToDateConvert(it) }
                val decreamentDate = date?.let { decrementDateByOne(it) }
                val dateInString = decreamentDate?.toString("dd MMMM yyyy")
                viewModel.currentDate.set(dateInString)
            }
            R.id.date_next_iv -> {
                val date = viewModel.currentDate.get()?.let { StringToDateConvert(it) }
                val incrementDate = date?.let { incrementDateByOne(it) }
                val dateInString = incrementDate?.toString("dd MMMM yyyy")
                viewModel.currentDate.set(dateInString)
            }
        }
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
    }

}
