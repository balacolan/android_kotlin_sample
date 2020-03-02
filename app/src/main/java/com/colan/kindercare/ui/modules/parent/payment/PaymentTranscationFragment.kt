package com.colan.kindercare.ui.modules.parent.payment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR

import com.colan.kindercare.R
import com.colan.kindercare.adapter.BaseRecyclerViewAdapter
import com.colan.kindercare.adapter.OnDataBindCallback
import com.colan.kindercare.data.model.PaymentInvoiceResponse
import com.colan.kindercare.databinding.FragmentPaymentTranscationBinding
import com.colan.kindercare.databinding.ItemPaymentTranscationBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.utils.Constants


class PaymentTranscationFragment : BaseFragment<FragmentPaymentTranscationBinding, PaymentVM>(),
    IRadioListener, OnDataBindCallback<ItemPaymentTranscationBinding> {
    private var mAdapter: BaseRecyclerViewAdapter<PaymentInvoiceResponse, ItemPaymentTranscationBinding>? =
        null
    override val bindingVariable: Int
        get() = BR.paymentInvoiceVM
    override val layoutId: Int
        get() = R.layout.fragment_payment_transcation
    override val viewModel: PaymentVM
        get() = ViewModelProviders.of(this).get(PaymentVM::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.mInvoiceRb.set(true)
        viewModel.setNavigator(this)
        setupAdapter()
        observerResponse()
    }

    private fun setupAdapter() {
        mAdapter = BaseRecyclerViewAdapter(
            R.layout.item_payment_transcation,
            BR.itemPaymentTranscationVM, ArrayList(),
            null, this)
        viewModel.setPaymentInvoiceResponseData()
    }

    private fun observerResponse() {
        viewModel.paymentInvoiceResponse.observe(this, Observer {
            viewDataBinding?.rcvPaymentTranscation?.adapter = mAdapter
            mAdapter?.addDataSet(it)
            mAdapter?.notifyDataSetChanged()
        })
    }
    override fun onItemClick(view: View, position: Int, v: ItemPaymentTranscationBinding) {
        when(view.id){
//            R.id.main_cdv->{
//                val extra =Bundle()
//                extra.putString(Constants.PAYMENT_EXTRA, Constants.TRANSCATION)
//                goTo(PaymentInvoiceTranscationActivity::class.java,extra)
//            }
        }
    }

    override fun onDataBind(
        v: ItemPaymentTranscationBinding,
        onClickListener: View.OnClickListener
    ) {
        v.mainCdv.setOnClickListener(onClickListener)
    }
    override fun onRadioButtonClick(id: Int) {

    }

    override fun onClickView(v: View?) {

    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        val intent = Intent(activity, clazz)
        mExtras?.let { intent.putExtras(it) }
        startActivity(intent)
        activity!!.overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit)
    }


}
