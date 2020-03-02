package com.colan.kindercare.ui.modules.common.leaveapproval

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.adapter.BaseRecyclerViewAdapter
import com.colan.kindercare.adapter.OnDataBindCallback
import com.colan.kindercare.data.remote.data.Status
import com.colan.kindercare.data.remote.data.response.leaveApproval.LeaveApprovalListResponse
import com.colan.kindercare.databinding.FragmentLeaveApprovalBinding
import com.colan.kindercare.databinding.LeaveApprovalListItemBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.dashboard.IToolbar
import com.colan.kindercare.ui.modules.common.leaveapproval.details.LeaveApprovalActivity
import com.colan.kindercare.ui.modules.parent.payment.IRadioListener
import com.colan.kindercare.utils.*
import com.colan.kindercare.utils.Singleton.type


class LeaveApprovalFragment : BaseFragment<FragmentLeaveApprovalBinding, LeaveApprovalVM>(),
    IRadioListener,OnDataBindCallback<LeaveApprovalListItemBinding>, SchoolSelectionListner {


    private lateinit var mToolbarManager: IToolbar
    private var baseRecyclerAdapter: BaseRecyclerViewAdapter<LeaveApprovalListResponse, LeaveApprovalListItemBinding>? = null

    override val bindingVariable: Int
        get() = BR.leaveApprovalVM
    override val layoutId: Int
        get() = R.layout.fragment_leave_approval
    override val viewModel: LeaveApprovalVM
        get() = ViewModelProviders.of(this).get(LeaveApprovalVM::class.java)

    override fun onClickView(v: View?) {
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        navigateTo(context!!, clazz, null)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Singleton.isDashBoardFragmentVisible =true
        mToolbarManager = context as IToolbar
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        viewModel.setNavigator(this)
        mToolbarManager.changeToolbarTitle(getString(R.string.leave_approval))
        SchoolSelectionBroadCast.schoolLister =this
        observeResponse()
        type=Constants.TEACHER_TYPE
        viewModel.loadLeaveApproval(Constants.TEACHER_TYPE)
        return viewDataBinding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when {
            viewModel.isAdmin.get()!! -> viewDataBinding?.group?.visibility=View.VISIBLE
            viewModel.isSuperAdmin.get()!! -> viewDataBinding?.group?.visibility=View.VISIBLE
            viewModel.isteacher.get()!!->{
                val layoutParams = view.layoutParams as MarginLayoutParams
                layoutParams.setMargins(3, 0, 3, 0)
            }
            else -> viewDataBinding?.group?.visibility=View.GONE
        }
    }
    private fun observeResponse() {
        viewModel.mShowProgressBar.observeEvent(this, Observer {
            when {
                it -> showLoadingIndicator()
                else -> dismissLoadingIndicator()
            }
        })

        viewModel.approvalListResponse.observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        viewModel.mShowProgressBar.value=true
                    }
                    Status.SUCCESS -> {
                        viewModel.mShowProgressBar.value=false
                        try {
                            viewModel.getLeaveApprovalListApiJob?.cancel()
                            it.data?.data?.let { it1 -> viewModel.leaveApprovalList.addAll(it1) }
                            setUpRecyclerview(viewModel.leaveApprovalList)
                        }catch (e: Exception) {
                            Log.i("catch Error", ""+e.message)
                        }


                    }
                    Status.ERROR, Status.FAILURE -> {
                        viewModel.mShowProgressBar.value=false
                        viewModel.leaveApprovalList.clear()
                        baseRecyclerAdapter?.notifyDataSetChanged()
                    }
                }

            }
        })

        viewModel.approvalResponse.observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        viewModel.mShowProgressBar.value=true
                    }
                    Status.SUCCESS -> {
                        viewModel.mShowProgressBar.value=false
                        viewModel.leaveApprovalList.clear()
                        viewModel.loadLeaveApproval(type)

                    }
                    Status.ERROR, Status.FAILURE -> {
                        viewModel.mShowProgressBar.value=false
                        putToast("Error")
                    }
                }

            }
        })


        viewModel.rejectResponse.observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        viewModel.mShowProgressBar.value=true
                    }
                    Status.SUCCESS -> {
                        viewModel.mShowProgressBar.value=false
                        viewModel.leaveApprovalList.clear()
                        viewModel.loadLeaveApproval(type)
                    }
                    Status.ERROR, Status.FAILURE -> {
                        viewModel.mShowProgressBar.value=false
                        putToast("Error")
                    }
                }

            }
        })
    }

    private fun setUpRecyclerview(approvalList: ArrayList<LeaveApprovalListResponse>) {
        baseRecyclerAdapter = BaseRecyclerViewAdapter(
            R.layout.leave_approval_list_item,
            BR.leaveApprovalItem,
            approvalList,
            null,
            this
        )

        viewDataBinding?.leaveApprovalRv?.adapter=baseRecyclerAdapter
        baseRecyclerAdapter?.notifyDataSetChanged()
    }

    override fun onItemClick(view: View, position: Int, v: LeaveApprovalListItemBinding) {
        when(view){
            v.leaveStatusCard->{
                bundle.clear()
                viewModel.itemId.set(viewModel.leaveApprovalList[position].id.toString())
                setUpDetailsPageData(viewModel.leaveApprovalList[position])
                bundle.putString(Constants.LEAVE_APPROVAL_ITEM_ID,viewModel.leaveApprovalList[position].id.toString())
                bundle.putString(Constants.LEAVE_APPROVAL_NAME,viewModel.leaveApprovalList[position].name)
                viewModel.leaveApprovalList[position].status?.let {
                    bundle.putInt(Constants.APPROVED_STATUS,
                        it
                    )
                }
                bundle.putString(Constants.LEAVE_APPROVAL_CLASS,viewModel.leaveApprovalList[position].name)
                bundle.putString(Constants.LEAVE_APPROVAL_REQUEST_DATE,viewModel.leaveApprovalList[position].requestedDate)
                bundle.putString(Constants.LEAVE_APPROVAL_LEAVE_DAYS,viewModel.leaveApprovalList[position].totDays.toString())
                bundle.putString(Constants.LEAVE_APPROVAL_LEAVE_FROM,viewModel.leaveApprovalList[position].fromDate)
                bundle.putString(Constants.LEAVE_APPROVAL_LEAVE_TILL,viewModel.leaveApprovalList[position].toDate)
                bundle.putString(Constants.LEAVE_APPROVAL_LEAVE_TYPE,viewModel.leaveApprovalList[position].reason)
                bundle.putString(Constants.LEAVE_APPROVAL_LEAVE_REASON,viewModel.leaveApprovalList[position].reason)
                bundle.putString(Constants.LEAVE_APPROVAL_CONTACTS,viewModel.leaveApprovalList[position].contactNo)
                goTo(LeaveApprovalActivity::class.java, bundle)
            }
            v.approvedBtn->{
                viewModel.itemId.set(viewModel.leaveApprovalList[position].id.toString())
                viewModel.doCallApproveLeaveRequestAPI()
            }
            v.rejectedBtn->{
                viewModel.itemId.set(viewModel.leaveApprovalList[position].id.toString())
                viewModel.doCallRejectLeaveRequestAPI()
            }
        }
    }


    override fun onRadioButtonClick(id: Int) {
       when(id){
           R.id.admin_rb->{
               type=Constants.ADMIN_TYPE
               viewModel.leaveApprovalList.clear()
               viewModel.loadLeaveApproval(Constants.ADMIN_TYPE)
           }
           R.id.teacher_rb->{
               type=Constants.TEACHER_TYPE
               viewModel.leaveApprovalList.clear()
               viewModel.loadLeaveApproval(Constants.TEACHER_TYPE)
           }
           R.id.student_rb->{
               type=Constants.STUDENT_TYPE
               viewModel.leaveApprovalList.clear()
               viewModel.loadLeaveApproval(Constants.STUDENT_TYPE)
           }
       }
    }



    private fun setUpDetailsPageData(leaveApprovalListResponse: LeaveApprovalListResponse) {
        viewModel.name.set(leaveApprovalListResponse.name)
        viewModel.mClass.set(leaveApprovalListResponse.name)
        viewModel.requestDate.set(leaveApprovalListResponse.requestedDate)
        viewModel.leaveDays.set(leaveApprovalListResponse.totDays.toString())
        viewModel.leaveFrom.set(leaveApprovalListResponse.fromDate)
        viewModel.leaveTill.set(leaveApprovalListResponse.toDate)
        viewModel.leaveType.set(leaveApprovalListResponse.reason)
        viewModel.reason.set(leaveApprovalListResponse.reason)
        viewModel.contacts.set(leaveApprovalListResponse.contactNo)

    }

    override fun onDataBind(v: LeaveApprovalListItemBinding, onClickListener: View.OnClickListener) {
        v.leaveStatusCard.setOnClickListener(onClickListener)
        v.approvedBtn.setOnClickListener(onClickListener)
        v.rejectedBtn.setOnClickListener(onClickListener)
    }

    override fun onTriggerSchoolSelection() {
        viewModel.leaveApprovalList.clear()
        viewModel.loadLeaveApproval(type)
    }

}
