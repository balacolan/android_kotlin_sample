package com.colan.kindercare.ui.modules.common.leaveapproval.details

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.data.remote.data.Status
import com.colan.kindercare.databinding.ActivityLeaveApprovalBinding
import com.colan.kindercare.ui.base.BaseActivity
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.modules.common.leaveapproval.LeaveApprovalVM
import com.colan.kindercare.ui.modules.parent.payment.IRadioListener
import com.colan.kindercare.utils.Constants
import com.colan.kindercare.utils.bundle
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class LeaveApprovalActivity : BaseActivity<ActivityLeaveApprovalBinding, LeaveApprovalVM>(),
    IRadioListener {

    override fun getBindingVariable(): Int {
        return BR.leaveApprovedViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_leave_approval
    }

    override fun getViewModel(): LeaveApprovalVM {
        return ViewModelProviders.of(this).get(LeaveApprovalVM::class.java)
    }

    override fun onClickView(v: View?) {

    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel?.setNavigator(this)
        setUpData()
        observeResponse()
        if (mViewModel?.isAdmin?.get()!! || mViewModel?.isteacher?.get()!! || mViewModel?.isSuperAdmin?.get()!!) {
            getViewDataBinding().toolbar.title_toolbar.text = getString(R.string.leave_approval)
        } else {
            getViewDataBinding().toolbar.title_toolbar.text = getString(R.string.leave_status)
        }

        getViewDataBinding().toolbar.nav_icon_iv.setOnClickListener { onBackPressed() }
    }

    private fun observeResponse() {
        mViewModel?.mShowProgressBar?.value = false
        mViewModel?.mShowProgressBar?.observeEvent(this, Observer {
            when {
                it -> showLoadingIndicator()
                else -> dismissLoadingIndicator()
            }
        })

        mViewModel?.approvalResponse?.observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        mViewModel?.mShowProgressBar?.value = true
                    }
                    Status.SUCCESS -> {
                        mViewModel?.mShowProgressBar?.value = false
                        try {
                            putToast("Success")
                        } catch (e: Exception) {
                            Log.i("catch Error", "" + e.message)
                        }

                    }
                    Status.ERROR, Status.FAILURE -> {
                        mViewModel?.mShowProgressBar?.value = false
                        putToast("Error")
                    }
                }

            }
        })


        mViewModel?.rejectResponse?.observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        mViewModel?.mShowProgressBar?.value = true
                    }
                    Status.SUCCESS -> {
                        mViewModel?.mShowProgressBar?.value = false
                        try {
                            putToast("Success")
                        } catch (e: Exception) {
                            Log.i("catch Error", "" + e.message)
                        }


                    }
                    Status.ERROR, Status.FAILURE -> {
                        mViewModel?.mShowProgressBar?.value = false
                        putToast("Error")
                    }
                }

            }
        })

    }

    private fun setUpData() {
        bundle.let {
            mViewModel?.itemId?.set(bundle.getString(Constants.LEAVE_APPROVAL_ITEM_ID))
            mViewModel?.name?.set(bundle.getString(Constants.LEAVE_APPROVAL_NAME))
            mViewModel?.mClass?.set(bundle.getString(Constants.LEAVE_APPROVAL_CLASS))
            mViewModel?.approvedStatus?.set(bundle.getInt(Constants.APPROVED_STATUS))
            mViewModel?.requestDate?.set(bundle.getString(Constants.LEAVE_APPROVAL_REQUEST_DATE))
            mViewModel?.leaveDays?.set(bundle.getString(Constants.LEAVE_APPROVAL_LEAVE_DAYS))
            mViewModel?.leaveFrom?.set(bundle.getString(Constants.LEAVE_APPROVAL_LEAVE_FROM))
            mViewModel?.leaveTill?.set(bundle.getString(Constants.LEAVE_APPROVAL_LEAVE_TILL))
            mViewModel?.leaveType?.set(bundle.getString(Constants.LEAVE_APPROVAL_LEAVE_TYPE))
            mViewModel?.reason?.set(bundle.getString(Constants.LEAVE_APPROVAL_LEAVE_REASON))
            mViewModel?.contacts?.set(bundle.getString(Constants.LEAVE_APPROVAL_CONTACTS))
        }

    }


    override fun onRadioButtonClick(id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
