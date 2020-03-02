package com.colan.kindercare.ui.modules.admin.enroll.details

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.data.remote.data.Status
import com.colan.kindercare.databinding.ActivityEnrollmentDetailsBinding
import com.colan.kindercare.ui.base.BaseActivity
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.modules.admin.enroll.EnrollmentEnquiryVM
import com.colan.kindercare.ui.modules.admin.enroll.addEnquiry.AddEnquiryActivity
import com.colan.kindercare.utils.Constants
import com.colan.kindercare.utils.bundle
import com.colan.kindercare.utils.navigateTo
import kotlinx.android.synthetic.main.custom_toolbar.view.*


class EnrollmentDetailsActivity :
    BaseActivity<ActivityEnrollmentDetailsBinding, EnrollmentEnquiryVM>(),
    BaseNavigator {


    override fun getBindingVariable(): Int {
        return BR.enrollmentEnquiryVM
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_enrollment_details
    }

    override fun getViewModel(): EnrollmentEnquiryVM {
        return ViewModelProviders.of(this).get(EnrollmentEnquiryVM::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel?.setNavigator(this)
        getViewDataBinding().toolbar.title_toolbar.text = getString(R.string.enrollment_enquiry)
        getViewDataBinding().toolbar.nav_icon_iv.setOnClickListener { onBackPressed() }
        setUpData()
        setObserver()
    }

    private fun setObserver() {

        mViewModel!!.deleteListResponse.observe(this, Observer {
            when(it.status){
                Status.SUCCESS-> {
                    mViewModel!!.mShowProgressBar.value = false
                    setResult(Constants.ENROLLCALLBACK,  Intent())
                    finish()
                }
                else -> {
                    mViewModel!!.mShowProgressBar.value = false

                }
            }
        })

        mViewModel!!.mShowProgressBar.observe(this, Observer {
            when {
                it -> showLoadingIndicator()
                else -> dismissLoadingIndicator()
            }
        })
    }

    private fun setUpData() {
        bundle.let {
            mViewModel!!.childAge.set(bundle.getString(Constants.CHILD_AGE))
            mViewModel!!.childName.set(bundle.getString(Constants.CHILD_NAME))
            mViewModel!!.childParentEmail.set(bundle.getString(Constants.CHILD_PARENT_EMAIL))
            mViewModel!!.childParentMobile.set(bundle.getString(Constants.CHILD_PARENT_MOBILE))
            mViewModel!!.childParentName.set(bundle.getString(Constants.CHILD_FATHER_NAME)+" "+bundle.getString(Constants.CHILD_MOTHER_NAME))
            mViewModel!!.childStatus.set(bundle.getString(Constants.CHILD_STATUS))
            mViewModel!!.childMotherName.set(bundle.getString(Constants.CHILD_MOTHER_NAME))
            mViewModel!!.childClass.set(bundle.getString(Constants.CHILD_CLASS))
            mViewModel!!.childDob.set(bundle.getString(Constants.CHILD_DOB))
            mViewModel!!.childPurpose.set(bundle.getString(Constants.CHILD_PURPOSE))
            mViewModel!!.childID.set(bundle.getString(Constants.CHILD_ID))
        }
    }

    override fun onClickView(v: View?) {
        when (v?.id) {
            R.id.edit_enquiry -> {
                if (mViewModel!!.isSuperAdmin.get()!!) {
                    mViewModel!!.deleteEnroll()
                } else {
                    goTo(AddEnquiryActivity::class.java, bundle)
                }
            }
        }
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        navigateTo(this, clazz, null)
    }
}
