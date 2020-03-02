package com.colan.kindercare.ui.modules.parent.payment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.databinding.FragmentPaymentBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.dashboard.IToolbar
import com.colan.kindercare.utils.Singleton


class PaymentFragment : BaseFragment<FragmentPaymentBinding, PaymentVM>(), IRadioListener {

    private lateinit var mToobarManagner: IToolbar
    private val invoiceFragment=PaymentInvoiceFragment()
    private val transcationFragment=PaymentTranscationFragment()
    override val bindingVariable: Int
        get() = BR.paymentVM
    override val layoutId: Int
        get() = R.layout.fragment_payment
    override val viewModel: PaymentVM
        get() = ViewModelProviders.of(this).get(PaymentVM::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.mInvoiceRb.set(true)
        viewModel.setNavigator(this)
        mToobarManagner.changeToolbarTitle(getString(R.string.payment))
        viewModel.setPaymentInvoiceResponseData()
        showFragment(invoiceFragment)
    }

    private fun showFragment(fragment: Fragment) {
     activity!!.supportFragmentManager.beginTransaction().replace(R.id.paymentContainer_fl,fragment).commit()
    }


    override fun onRadioButtonClick(id: Int) {
        when (id) {
            R.id.invoice_rb -> {
                showFragment(invoiceFragment)
            }
            R.id.transcation_rb -> {
                showFragment(transcationFragment)
            }
        }
    }

    override fun onClickView(v: View?) {

    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Singleton.isDashBoardFragmentVisible =true
        mToobarManagner = context as IToolbar
    }


}
