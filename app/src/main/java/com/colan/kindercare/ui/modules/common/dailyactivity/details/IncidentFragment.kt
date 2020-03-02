package com.colan.kindercare.ui.modules.common.dailyactivity.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.adapter.BaseRecyclerViewAdapter
import com.colan.kindercare.adapter.OnDataBindCallback
import com.colan.kindercare.data.model.StudentModel
import com.colan.kindercare.databinding.FragmentIncidentBinding
import com.colan.kindercare.databinding.StudentListItemsBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.dashboard.IToolbar
import com.colan.kindercare.ui.modules.admin.addWeeklyMenu.AddWeeklyMenuActivity
import com.colan.kindercare.ui.modules.common.dailyactivity.DailyActivityVM
import com.colan.kindercare.ui.modules.common.message.attachment.AttachmentActivity
import com.colan.kindercare.ui.modules.teacher.dailyActivity.ActivtyChoosingActivity
import com.colan.kindercare.ui.modules.teacher.dailyActivity.selectStudentBottomSheet.SelectStudentBottomSheet
import com.colan.kindercare.utils.bundle
import com.colan.kindercare.utils.navigateTo
import kotlinx.android.synthetic.main.custom_toolbar.view.*


class IncidentFragment : BaseFragment<FragmentIncidentBinding, DailyActivityVM>(), BaseNavigator,
    OnDataBindCallback<StudentListItemsBinding> {

    private lateinit var mToolbarManager: IToolbar
    private var baseRecyclerAdapter: BaseRecyclerViewAdapter<StudentModel, StudentListItemsBinding>? =
        null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        mToolbarManager.changeToolbarTitle(getString(R.string.incident))

        setUpRecyclerview()
        observeResponse()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mToolbarManager = context as IToolbar
    }

    override val bindingVariable: Int
        get() = BR.incidentVM
    override val layoutId: Int
        get() = R.layout.fragment_incident
    override val viewModel: DailyActivityVM
        get() = ViewModelProviders.of(this).get(DailyActivityVM::class.java)

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


    override fun onClickView(v: View?) {
        when(v?.id){
            R.id.attachment_view->{
                goTo(AttachmentActivity::class.java, bundle)
            }
        }
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        navigateTo(context!!,clazz, mExtras)
    }

    override fun onItemClick(view: View, position: Int, v: StudentListItemsBinding) {

    }

    override fun onDataBind(v: StudentListItemsBinding, onClickListener: View.OnClickListener) {
    }
}
