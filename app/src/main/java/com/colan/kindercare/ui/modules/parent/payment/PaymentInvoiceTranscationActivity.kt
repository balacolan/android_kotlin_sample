package com.colan.kindercare.ui.modules.parent.payment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.databinding.ActivityPaymentInvoiceTranscationBinding
import com.colan.kindercare.ui.base.BaseActivity
import com.colan.kindercare.utils.Constants
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class PaymentInvoiceTranscationActivity : BaseActivity<ActivityPaymentInvoiceTranscationBinding, PaymentVM>(),IRadioListener {


    override fun getBindingVariable(): Int {
        return BR.paymentInvoiceDetailVM
    }

    private lateinit var viewModel: PaymentVM


    override fun getLayoutId(): Int {
        return R.layout.activity_payment_invoice_transcation
    }

    override fun getViewModel(): PaymentVM {
        viewModel = ViewModelProviders.of(this).get(PaymentVM::class.java)
        return viewModel
    }

    override fun onClickView(v: View?) {
    }
    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
    }
    override fun onRadioButtonClick(id: Int) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        val intentData= intent.getStringExtra(Constants.PAYMENT_EXTRA)
        intentData.let {
            if(intentData.equals(Constants.INVOICE)){
                viewModel.isPending.set(true)
                viewModel.mStatus.set("Paid")
            }else{
                viewModel.mStatus.set("Pending")
            }

            if(viewModel.isPending.get()!!){
                getViewDataBinding().toolbar.title_toolbar.text=getString(R.string.payment)
            }else{
                getViewDataBinding().toolbar.title_toolbar.text=getString(R.string.invoice_details)
            }

        }
        getViewDataBinding().toolbar.nav_icon_iv.setOnClickListener { onBackPressed() }
    }
}
