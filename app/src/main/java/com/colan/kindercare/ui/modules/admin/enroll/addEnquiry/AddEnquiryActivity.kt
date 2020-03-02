package com.colan.kindercare.ui.modules.admin.enroll.addEnquiry

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.data.remote.data.Status
import com.colan.kindercare.databinding.ActivityAddEnquiryBinding
import com.colan.kindercare.ui.base.BaseActivity
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.modules.admin.enroll.EnrollmentEnquiryVM
import com.colan.kindercare.utils.Constants
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import java.text.SimpleDateFormat
import java.util.*


class AddEnquiryActivity : BaseActivity<ActivityAddEnquiryBinding, EnrollmentEnquiryVM>(),
    BaseNavigator {

    override fun getBindingVariable(): Int {
        return BR.enrollmentEnquiryVM
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_add_enquiry
    }

    override fun getViewModel(): EnrollmentEnquiryVM {
        return ViewModelProviders.of(this).get(EnrollmentEnquiryVM::class.java)
    }
    override fun onClickView(v: View?){
        when(v?.id){
            R.id.cancel_btn -> {
                onBackPressed()
            }
            R.id.submit_btn -> {
                    mViewModel!!.addEnrollApi()
            }
        }

    }


    private fun observeResponse() {
        mViewModel?.mShowProgressBar?.observeEvent(this, androidx.lifecycle.Observer {
            when {
                it -> showLoadingIndicator()
                else -> dismissLoadingIndicator()
            }
        })
        mViewModel?.spinnerArrayListClass?.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        mViewModel!!.mShowProgressBar.value = true
                    }
                    Status.SUCCESS -> {
                        mViewModel!!.mShowProgressBar.value = false
                        mViewModel!!.mClassSpinnerDataList.addAll(it.data?.data!!)
                        val classSpinnerList =
                            it.data.data.map { it.className!! }
                        mViewModel!!.mClassSpinnerList.addAll(classSpinnerList)
                        Log.e("section","fff${mViewModel!!.mClassSpinnerList}")

                    }
                    Status.ERROR, Status.FAILURE -> {
                        mViewModel!!.mShowProgressBar.value = false
                        putToast(""+it.data?.errors)
                    }
                }

            }
        }
        )

        mViewModel?.addEnrollResponse?.observe(this,androidx.lifecycle.Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        mViewModel?.mShowProgressBar?.value = true
                    }
                    Status.SUCCESS -> {
                        try {
                            if (it.data!=null) {
                                mViewModel?.mShowProgressBar?.value = false
                                Toast.makeText(this, "Enrollment Updated", Toast.LENGTH_SHORT).show()
                                setResult(Constants.ENROLLCALLBACK,  Intent())
                                finish()
                            }else {
                                mViewModel?.mShowProgressBar?.value = false
                                Toast.makeText(this, "Error getting User Information", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } catch (e: java.lang.Exception) {
                            mViewModel?.mShowProgressBar?.value = false
                            Toast.makeText(this, "" + e.message, Toast.LENGTH_SHORT).show()
                        }

                    }
                    Status.ERROR, Status.FAILURE -> {
                        mViewModel?.mShowProgressBar?.value = false
                        putToast(""+it.data?.errors)
                    }
                }

            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel?.setNavigator(this)
        mViewModel?.loadEnrollClass()
        getViewDataBinding().toolbar.title_toolbar.text = getString(R.string.enrollment_enquiry)
        getViewDataBinding().toolbar.nav_icon_iv.setOnClickListener { onBackPressed() }
        observeResponse()
        getViewDataBinding().leaveTillEt.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                val drawableRight = 2
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= getViewDataBinding().leaveTillEt.right - getViewDataBinding().leaveTillEt.compoundDrawables[drawableRight].bounds.width()) {
                        val myCalendar = Calendar.getInstance()
                        val date =
                            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                                // TODO Auto-generated method stub
                                myCalendar.set(Calendar.YEAR, year)
                                myCalendar.set(Calendar.MONTH, monthOfYear)
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                                val myFormat = "yyyy-MM-dd" // your format
                                val sdf = SimpleDateFormat(myFormat, Locale.getDefault())

                                getViewDataBinding().leaveTillEt.setText(sdf.format(myCalendar.getTime()))
                            }
                        DatePickerDialog(
                            this@AddEnquiryActivity,
                            date,
                            myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                        return true
                    }
                }
                return false
            }
        })
    }



    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
    }
}
