package com.colan.kindercare.ui.modules.common.leavestatus

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.data.remote.data.Status
import com.colan.kindercare.databinding.ActivityLeaveStatusBinding
import com.colan.kindercare.ui.base.BaseActivity
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.utils.Alert
import com.colan.kindercare.utils.Constants
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class LeaveStatusActivity : BaseActivity<ActivityLeaveStatusBinding, LeaveStatusVM>(),
    BaseNavigator {


    private lateinit var viewModel: LeaveStatusVM

    override fun getBindingVariable(): Int {
        return BR.leaveStatusViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_leave_status
    }

    override fun getViewModel(): LeaveStatusVM {
        viewModel = ViewModelProviders.of(this).get(LeaveStatusVM::class.java)
        return viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        observeResponse()

        viewModel.loadItemIdData()

        getViewDataBinding().toolbar.title_toolbar.text = getString(R.string.leave_status)
        getViewDataBinding().toolbar.nav_icon_iv.setOnClickListener { onBackPressed() }
    }

    private fun observeResponse() {
        mViewModel?.mShowProgressBar?.observeEvent(this, androidx.lifecycle.Observer {
            when {
                it -> showLoadingIndicator()
                else -> dismissLoadingIndicator()
            }
        })

        mViewModel?.getLeaveSelectedIdResponse?.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        mViewModel?.mShowProgressBar?.value = true
                    }
                    Status.SUCCESS -> {
                        it.data?.data?.let { it1 ->
                            viewModel.getLeaveItemIdData.addAll(it1)
                            viewModel.mLeaveTypeData.set(it1[0].leaveType)
                            viewModel.mFromDateData.set(it1[0].getCreateDateFrom())
                            viewModel.mToDateData.set(it1[0].getCreateDateTo())
                            viewModel.mLeaveDaysData.set(it1[0].leaveDays + " " +"Days")
                            viewModel.mReasonData.set(it1[0].reason)
                            viewModel.mContactNo.set(it1[0].contact)
                            viewModel.mLeaveApproveStatusData.set(it1[0].status?.let { it2 ->
                                getStatusTypeByName(
                                    it2
                                )
                            })
                            Log.d("status_leave_type", "" + viewModel.mLeaveApproveStatusData)
                            //let { getClassTypeByName(it) }
                        }
                        try {
                            mViewModel?.mShowProgressBar?.value = false
                            Toast.makeText(
                                this,
                                "Success",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } catch (e: java.lang.Exception) {
                            mViewModel?.mShowProgressBar?.value = false
                            Toast.makeText(this, "" + e.message, Toast.LENGTH_SHORT).show()
                        }


                    }
                    Status.ERROR, Status.FAILURE -> {
                        mViewModel?.mShowProgressBar?.value = false
                        putToast("" + it.message)
                    }
                }

            }
        })

        mViewModel?.deleteLeaveApplicationData?.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        mViewModel?.mShowProgressBar?.value = true
                    }
                    Status.SUCCESS -> {
                        try {
                            mViewModel?.mShowProgressBar?.value = false
                            finish()
                            Toast.makeText(
                                this,
                                "Request Deleted",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } catch (e: java.lang.Exception) {
                            mViewModel?.mShowProgressBar?.value = false
                            Toast.makeText(this, "" + e.message, Toast.LENGTH_SHORT).show()
                        }


                    }
                    Status.ERROR, Status.FAILURE -> {
                        mViewModel?.mShowProgressBar?.value = false
                        putToast("" + it.message)
                    }
                }

            }
        })
    }

    private fun getStatusTypeByName(statusType: String): String? {
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

    override fun onClickView(v: View?) {
        when (v?.id) {
            R.id.btn_delete-> {
               callDeleteApiAlert()
            }
        }
    }

    fun callDeleteApiAlert(){
        Alert.createYesNoAlert(this,"Do You Want To Delete The Request?","",object :
            Alert.OnAlertClickListener{
            override fun onPositive(dialog: DialogInterface) {
                viewModel.loadItemIdDelete()
                dialog.dismiss()
            }

            override fun onNegative(dialog: DialogInterface) {
                dialog.dismiss()
            }

        })
    }



    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
