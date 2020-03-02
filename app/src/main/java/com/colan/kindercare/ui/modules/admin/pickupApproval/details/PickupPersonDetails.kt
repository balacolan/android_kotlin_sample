package com.colan.kindercare.ui.modules.admin.pickupApproval.details

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.data.remote.data.Status
import com.colan.kindercare.databinding.ActivityPickupPersonDetailsBinding
import com.colan.kindercare.ui.base.BaseActivity
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.modules.admin.AdminDashboardVM
import com.colan.kindercare.ui.modules.admin.pickupApproval.PickupPersonVM
import com.colan.kindercare.utils.Constants
import com.colan.kindercare.utils.bundle
import com.colan.kindercare.utils.navigateTo
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.reject_approv_layout.view.*

class PickupPersonDetails :  BaseActivity<ActivityPickupPersonDetailsBinding, PickupPersonVM>(),
    BaseNavigator {



    override fun getBindingVariable(): Int {
        return  BR.pickUpDetailsVM
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_pickup_person_details
    }

    override fun getViewModel(): PickupPersonVM {
        return ViewModelProviders.of(this).get(PickupPersonVM::class.java)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel?.setNavigator(this)
        setUpData()
        observeResponse()
        setupApproveRejectButtonClick()
        getViewDataBinding().toolbar.title_toolbar.text=getString(R.string.pickup_person_details)
        getViewDataBinding().toolbar.nav_icon_iv.setOnClickListener { onBackPressed() }
    }

    private fun observeResponse() {
        mViewModel?.mShowProgressBar?.value=false
        mViewModel?.mShowProgressBar?.observeEvent(this, Observer {
            when {
                it -> showLoadingIndicator()
                else -> dismissLoadingIndicator()
            }
        })

        mViewModel?.statusResponse?.observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        mViewModel?.mShowProgressBar?.value=true
                    }
                    Status.SUCCESS -> {
                        mViewModel?.mShowProgressBar?.value=false
                        try {
                            putToast("Success")
                        }catch (e: Exception) {
                            Log.i("catch Error", ""+e.message)
                        }

                    }
                    Status.ERROR, Status.FAILURE -> {
                        mViewModel?.mShowProgressBar?.value=false
                        putToast("Error")
                    }
                }

            }
        })

    }

    private fun setupApproveRejectButtonClick() {
        getViewDataBinding().bottomLayout.cancel_btn.setOnClickListener {
            mViewModel?.doCallApproveRejectApi(Constants.REJECT)
        }
        getViewDataBinding().bottomLayout.submit_btn.setOnClickListener {mViewModel?.doCallApproveRejectApi(Constants.APPROVE)  }
    }

    override fun onClickView(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {

    }

    private fun setUpData() {
        bundle.let {
            mViewModel?.itemId?.set(bundle.getString(Constants.ITEM_ID))
            mViewModel?.approvedStatus?.set(bundle.getString(Constants.APPROVED_STATUS))
            mViewModel?.studentName?.set(bundle.getString(Constants.STUDENT_NAME))
            mViewModel?.classSection?.set(bundle.getString(Constants.STUDENT_CLASS))
            mViewModel?.fathersName?.set(bundle.getString(Constants.FATHERS_NAME))
            mViewModel?.mothersName?.set(bundle.getString(Constants.MOTHERS_NAME))
            mViewModel?.relationShip?.set(bundle.getString(Constants.RELATIONSHIP))
            mViewModel?.pickupPerson?.set(bundle.getString(Constants.PICKUP_PERSON_NAME))
            mViewModel?.contacts?.set(bundle.getString(Constants.CONTACTS))

        }

    }
}
