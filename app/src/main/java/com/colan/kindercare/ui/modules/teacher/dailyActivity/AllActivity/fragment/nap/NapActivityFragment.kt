package com.colan.kindercare.ui.modules.teacher.dailyActivity.AllActivity.fragment.nap

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
import com.colan.kindercare.databinding.FragmentNapActivityBinding
import com.colan.kindercare.databinding.StudentListItemsBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.modules.teacher.dailyActivity.DailyActivityVM
import kotlinx.android.synthetic.main.custom_toolbar.view.*

/**
 * A simple [Fragment] subclass.
 */
class NapActivityFragment : BaseFragment<FragmentNapActivityBinding, DailyActivityVM>(),
    BaseNavigator, OnDataBindCallback<StudentListItemsBinding> {

    private var baseRecyclerAdapter: BaseRecyclerViewAdapter<StudentModel, StudentListItemsBinding>? = null

    override val bindingVariable: Int
        get() = BR.dailyActivtyVM
    override val layoutId: Int
        get() = R.layout.fragment_nap_activity
    override val viewModel: DailyActivityVM
        get() = ViewModelProviders.of(this).get(DailyActivityVM::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        setUpRecyclerview()
        observeResponse()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.toolbar?.title_toolbar?.setText(getString(R.string.selected_student))
        viewDataBinding?.toolbar?.nav_icon_iv?.setOnClickListener {
            activity?.finish()
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
            R.layout.student_list_items,
            BR.studentItem,
            ArrayList(),
            null,
            this
        )
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
