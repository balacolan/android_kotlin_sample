package com.colan.kindercare.ui.modules.common.leavestatus


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.adapter.BaseRecyclerViewAdapter
import com.colan.kindercare.adapter.OnDataBindCallback
import com.colan.kindercare.data.local.sharedPreference.ISharedPreferenceService
import com.colan.kindercare.data.remote.data.Status
import com.colan.kindercare.data.remote.data.response.leaveApplication.LeaveApplicationListResponse
import com.colan.kindercare.databinding.FragmentLeaveStatusBinding
import com.colan.kindercare.databinding.LeaveStatusAdapterBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.dashboard.IToolbar
import com.colan.kindercare.ui.modules.common.leavestatus.bottomsheetfragment.BottomSheetFragment
import com.colan.kindercare.utils.Constants
import com.colan.kindercare.utils.Singleton
import kotlinx.android.synthetic.main.leave_status_adapter.*
import org.koin.android.ext.android.inject


class LeaveStatusFragment : BaseFragment<FragmentLeaveStatusBinding, LeaveStatusVM>(),
    BaseNavigator,
    BottomSheetFragment.OnLeaveFilter, OnDataBindCallback<LeaveStatusAdapterBinding> {

    private val iSharedPreferenceService: ISharedPreferenceService by inject()

    private var baseRecyclerAdapter: BaseRecyclerViewAdapter<LeaveApplicationListResponse, LeaveStatusAdapterBinding>? =
        null

    override val bindingVariable: Int
        get() = BR.leaveStatusVM
    override val layoutId: Int
        get() = R.layout.fragment_leave_status
    override val viewModel: LeaveStatusVM
        get() = ViewModelProviders.of(this).get(LeaveStatusVM::class.java)

    private lateinit var mToobarManagner: IToolbar

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Singleton.isDashBoardFragmentVisible = true
        mToobarManagner = context as IToolbar
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        observeResponse()
        viewModel.loadLeaveApplicationListData()

    }

    private fun observeResponse() {
        viewModel.mShowProgressBar.observeEvent(this, Observer {
            when {
                it -> showLoadingIndicator()
                else -> dismissLoadingIndicator()
            }
        })

        viewModel.getLeaveFilterListResponse.observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        viewModel.mShowProgressBar.value = true
                    }
                    Status.SUCCESS -> {
                        viewModel.mShowProgressBar.value = false
                        try {
                            if(it.data?.data.isNullOrEmpty()){
                                putToast("No Data Found")
                            }else{
                                baseRecyclerAdapter?.cleatDataSet()
                                it.data?.data?.let { it1 -> viewModel.getLeaveFilterResult.addAll(it1) }
                                setUpRecyclerview(viewModel.getLeaveFilterResult)
                            }
                        } catch (e: Exception) {
                            Log.i("catch Error", "" + e.message)
                        }

                    }
                    Status.ERROR, Status.FAILURE -> {
                        viewModel.mShowProgressBar.value = false
                    }
                }

            }
        })
    }

    private fun setUpRecyclerview(filterLeaveList: ArrayList<LeaveApplicationListResponse>) {

        baseRecyclerAdapter = BaseRecyclerViewAdapter(
            R.layout.leave_status_adapter,
            BR.leaveAdapter,
            filterLeaveList,
            null,
            this
        )
        viewDataBinding?.leaveRequestRv?.adapter = baseRecyclerAdapter
        baseRecyclerAdapter?.notifyDataSetChanged()

    }

    fun getStatusTypeByName(statusType: String): String? {
        when (statusType) {
            Constants.PENDING_LEAVE_STATUS -> {
                return resources.getString(R.string.pending)
            }
            Constants.APPROVED_LEAVE_STATUS -> {
                return resources.getString(R.string.approved)
            }
            Constants.REJECTED_LEAVE_STATUS -> {
                return resources.getString(R.string.rejected)
            }
        }
        return ""
    }

    override fun onItemClick(view: View, position: Int, v: LeaveStatusAdapterBinding) {
        when (view.id) {
            R.id.leave_status_card -> {
                val intent = Intent(activity, LeaveStatusActivity::class.java)
                val itemID = v.leaveAdapter?.id
                iSharedPreferenceService.setStringValue(Constants.CARD_ITEM_ID,""+itemID)
                startActivity(intent)
            }
        }
    }

    override fun onDataBind(v: LeaveStatusAdapterBinding, onClickListener: View.OnClickListener) {
        v.leaveStatusCard.setOnClickListener(onClickListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        viewModel.setNavigator(this)


        mToobarManagner.changeToolbarTitle(getString(R.string.leave_application))

        return viewDataBinding?.root
    }

    override fun getLeaveFilterAppliedData(fromDate: String, toDate: String, userType: String?) {
        viewModel.mFromDate.set(fromDate)
        viewModel.mToDate.set(toDate)
        viewModel.mLeaveType.set(userType)

        viewModel.loadLeavesList()

    }

    override fun onResume() {
        super.onResume()
        viewModel.loadLeaveApplicationListData()
    }

    override fun onClickView(v: View?) {
        when (v?.id) {
            R.id.leave_request_btn -> {
                goTo(RequestLeaveActivity::class.java, null)
            }
            R.id.filter -> {
                val bottomSheetFragment = BottomSheetFragment(this)
                bottomSheetFragment.show(fragmentManager!!, bottomSheetFragment.tag)
            }
        }
    }


    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        val intent = Intent(activity, clazz)
        startActivity(intent)
        activity!!.overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit)
    }


}
