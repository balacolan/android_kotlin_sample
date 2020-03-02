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
import com.colan.kindercare.databinding.FragmentClassRoomPreviewBinding
import com.colan.kindercare.databinding.StudentListItemsBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.modules.common.message.attachment.AttachmentActivity
import com.colan.kindercare.ui.modules.teacher.dailyActivity.AllActivity.fragment.PhotoActivityFragment
import com.colan.kindercare.ui.modules.teacher.dailyActivity.DailyActivityVM
import com.colan.kindercare.ui.modules.teacher.dailyActivity.selectStudentBottomSheet.SelectStudentBottomSheet
import com.colan.kindercare.utils.bundle
import com.colan.kindercare.utils.navigateTo
import kotlinx.android.synthetic.main.attachment_layout_white_bg.view.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class ClassRoomPreviewFragment  : BaseFragment<FragmentClassRoomPreviewBinding, DailyActivityVM>(),
    OnDataBindCallback<StudentListItemsBinding>,BaseNavigator {
    override val bindingVariable: Int
        get() = BR.previewVM
    override val layoutId: Int
        get() = R.layout.fragment_class_room_preview
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.toolbar?.title_toolbar?.setText(getString(R.string.preview))
        viewDataBinding?.toolbar?.nav_icon_iv?.setOnClickListener {
            viewModel.onBack()
            replaceFragment(R.id.activity_container, ClassRoomStudentFragment(),"Classroom","Classroom")
        }
        viewDataBinding?.attachmentView?.view_attachement_tv?.setOnClickListener {
            goTo(AttachmentActivity::class.java, bundle)
        }
        viewDataBinding?.editTv?.setOnClickListener {
            replaceFragment(R.id.activity_container,ClassRoomDetailFragment(),"Classroom","Classroom")
        }
        viewDataBinding?.editCourseItemTv?.setOnClickListener {
            replaceFragment(R.id.activity_container,ClassRoomStudentFragment(),"Classroom","Classroom")
        }

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
            R.layout.student_list_items, BR.studentItem, ArrayList(), null, this
        )
    }

    override fun onItemClick(view: View, position: Int, v: StudentListItemsBinding) {

    }

    override fun onDataBind(v: StudentListItemsBinding, onClickListener: View.OnClickListener) {

    }

    override fun onClickView(v: View?) {

    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        navigateTo(context!!,clazz, null)
    }
}
