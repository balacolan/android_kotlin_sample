package com.colan.kindercare.ui.modules.common.leavestatus

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.data.remote.data.Status
import com.colan.kindercare.databinding.ActivityRequestLeaveBinding
import com.colan.kindercare.ui.base.BaseActivity
import com.colan.kindercare.ui.base.BaseNavigator
import kotlinx.android.synthetic.main.activity_request_leave.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import java.util.*


class RequestLeaveActivity : BaseActivity<ActivityRequestLeaveBinding, LeaveStatusVM>(),
    BaseNavigator {


    private lateinit var viewModel: LeaveStatusVM


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        observeResponse()
        getViewDataBinding().toolbar.title_toolbar.text =
            getString(com.colan.kindercare.R.string.request_leave)
        getViewDataBinding().toolbar.nav_icon_iv.setOnClickListener { onBackPressed() }

    }

    override fun getBindingVariable(): Int {
        return BR.requestLeaveVM
    }

    override fun getLayoutId(): Int {
        return com.colan.kindercare.R.layout.activity_request_leave
    }


    var mYear: Int = 0
    var mMonth: Int = 0
    var mDay: Int = 0


    override fun getViewModel(): LeaveStatusVM {
        viewModel = ViewModelProviders.of(this).get(LeaveStatusVM::class.java)
        return viewModel
    }

    override fun onClickView(v: View?) {
        when (v?.id) {
            R.id.child_dob_et -> {
                datepicker()
            }
            R.id.leave_till_et -> {
                datepicker2()
            }
            R.id.apply_btn -> {
                viewModel.doCallApproveLeaveRequestAPI()

            }
            R.id.cancel_btn -> {
                onBackPressed()
            }
        }
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
    }
    private fun observeResponse() {
        mViewModel?.mShowProgressBar?.observeEvent(this, androidx.lifecycle.Observer {
            when {
                it -> showLoadingIndicator()
                else -> dismissLoadingIndicator()
            }
        })

        mViewModel?.addLeaveApplicationResponse?.observe(this,androidx.lifecycle.Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        mViewModel?.mShowProgressBar?.value = true
                    }
                    Status.SUCCESS -> {
                        try {
                            if (it.data!=null) {
                                mViewModel?.mShowProgressBar?.value = false
                                finish()
                                Toast.makeText(this, "Request Added", Toast.LENGTH_SHORT).show()
                            }else {
                                mViewModel?.mShowProgressBar?.value = false
                                Toast.makeText(this, "Error getting Request Information", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } catch (e: java.lang.Exception) {
                            mViewModel?.mShowProgressBar?.value = false
                            Toast.makeText(this, "" + e.message, Toast.LENGTH_SHORT).show()
                        }

                    }
                    Status.ERROR, Status.FAILURE -> {
                        mViewModel?.mShowProgressBar?.value = false
                        putToast(""+it.message)
                    }
                }

            }
        })
    }

    fun datepicker() {

        // Get Current Date
        val c = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                child_dob_et.setText(
                    year.toString() + "-" + (monthOfYear + 1) + "-" +dayOfMonth.toString()
                    //dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                )
            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()

    }

    fun datepicker2() {

        // Get Current Date
        val c = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                leave_till_et.setText(
                    //dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                    year.toString() + "-" + (monthOfYear + 1) + "-" +dayOfMonth.toString()
                    //dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                )
            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()

    }

}
