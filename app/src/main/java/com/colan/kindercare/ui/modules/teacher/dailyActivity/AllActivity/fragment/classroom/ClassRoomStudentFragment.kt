package com.colan.kindercare.ui.modules.teacher.dailyActivity.AllActivity.fragment.classroom

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.adapter.BaseRecyclerViewAdapter
import com.colan.kindercare.adapter.OnDataBindCallback
import com.colan.kindercare.data.model.StudentModel
import com.colan.kindercare.databinding.FragmentClassRoomStudentBinding
import com.colan.kindercare.databinding.StudentListItemsBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.modules.teacher.dailyActivity.DailyActivityVM
import com.colan.kindercare.ui.modules.teacher.dailyActivity.selectStudentBottomSheet.SelectStudentBottomSheet
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class ClassRoomStudentFragment  : BaseFragment<FragmentClassRoomStudentBinding, DailyActivityVM>(),
    OnDataBindCallback<StudentListItemsBinding>,BaseNavigator,
    SelectStudentBottomSheet.OnUserSlectedListener {
    override fun selectedUserList(userList: ArrayList<String>) {
        putToast("dssdfd")
    }

    override val bindingVariable: Int
        get() = BR.selectedStudentVM
    override val layoutId: Int
        get() = R.layout.fragment_class_room_student
    override val viewModel: DailyActivityVM
        get() = ViewModelProviders.of(this).get(DailyActivityVM::class.java)


    private var baseRecyclerAdapter: BaseRecyclerViewAdapter<StudentModel, StudentListItemsBinding>? =
        null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        setUpRecyclerview()
        observeResponse()
    }


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.toolbar?.title_toolbar?.setText(getString(R.string.selected_student))
        viewDataBinding?.toolbar?.nav_icon_iv?.setOnClickListener {
            viewModel.onBack()
            replaceFragment(R.id.activity_container, ClassRoomDetailFragment(),"Classroom","Classroom")
        }
        viewDataBinding?.addRemove?.setOnClickListener {
            val bottomSheetFragment = SelectStudentBottomSheet(this)
            bottomSheetFragment.show(fragmentManager!!, bottomSheetFragment.getTag())
        }
    }

    override fun onItemClick(view: View, position: Int, v: StudentListItemsBinding) {

    }

    override fun onDataBind(v: StudentListItemsBinding, onClickListener: View.OnClickListener) {

    }

    override fun onClickView(v: View?) {

    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {

    }

}