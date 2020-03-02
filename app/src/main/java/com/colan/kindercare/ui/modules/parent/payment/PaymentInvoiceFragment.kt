package com.colan.kindercare.ui.modules.parent.payment


import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.adapter.BaseRecyclerViewAdapter
import com.colan.kindercare.adapter.OnDataBindCallback
import com.colan.kindercare.data.model.PaymentInvoiceResponse
import com.colan.kindercare.databinding.FragmentPaymentInvoiceBinding
import com.colan.kindercare.databinding.ItemPaymentDetailInvoiceBinding
import com.colan.kindercare.databinding.ItemPaymentInvoiceBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.utils.Constants
import kotlinx.android.synthetic.main.item_payment_detail_invoice.*
import kotlinx.android.synthetic.main.item_payment_detail_invoice.view.*
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import android.text.style.UnderlineSpan
import android.text.SpannableString

class PaymentInvoiceFragment : BaseFragment<FragmentPaymentInvoiceBinding, PaymentVM>(),
    IRadioListener,
    OnDataBindCallback<ItemPaymentInvoiceBinding> {
    private var mAdapter: BaseRecyclerViewAdapter<PaymentInvoiceResponse, ItemPaymentInvoiceBinding>? =
        null
    private var mAdapterChild: BaseRecyclerViewAdapter<PaymentInvoiceResponse, ItemPaymentDetailInvoiceBinding>? =
        null
    private var paymentInvoiceList = ArrayList<PaymentInvoiceResponse>()
    override val bindingVariable: Int
        get() = BR.paymentInvoiceVM
    override val layoutId: Int
        get() = com.colan.kindercare.R.layout.fragment_payment_invoice
    override val viewModel: PaymentVM
        get() = ViewModelProviders.of(this).get(PaymentVM::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.mInvoiceRb.set(true)
        viewModel.setNavigator(this)
        setupAdapter()
        observerResponse()

        /*val content = SpannableString(underlineData)
        content.setSpan(UnderlineSpan(), 0, underlineData.length(), 0)
// 0 specify start index and underlineData.length() specify end index of styling
        myTextView.setText(content)*/

    }

    private fun setupAdapter() {
        mAdapter = BaseRecyclerViewAdapter(
            com.colan.kindercare.R.layout.item_payment_invoice,
            BR.itemPaymentInvoiceVM, ArrayList(),
            null, this)

        mAdapterChild= BaseRecyclerViewAdapter(
            com.colan.kindercare.R.layout.item_payment_detail_invoice,
            BR.itemPaymentDetailInvoiceVM, ArrayList(),
            null, object :OnDataBindCallback<ItemPaymentDetailInvoiceBinding>{
                override fun onItemClick(
                    view: View,
                    position: Int,
                    v: ItemPaymentDetailInvoiceBinding
                ) {
                }

                override fun onDataBind(
                    v: ItemPaymentDetailInvoiceBinding,
                    onClickListener: View.OnClickListener
                ) {

                }
            })
        viewModel.setPaymentInvoiceResponseData()
    }

    private fun observerResponse() {
        viewModel.paymentInvoiceResponse.observe(this, Observer {
            viewDataBinding?.rcvPaymentInvoice?.adapter = mAdapter
            paymentInvoiceList=it
            mAdapter?.addDataSet(it)
            mAdapter?.notifyDataSetChanged()

        })
    }
    override fun onItemClick(view: View, position: Int, v: ItemPaymentInvoiceBinding) {
        when(view){
            v.rcvPaymentInvoiceDetail->{
                     view.tag.toString().let {
                         if(view.tag != "Pending"){
                             val extra =Bundle()
                             viewModel.isPending.set(true)
                             extra.putString(Constants.PAYMENT_EXTRA,Constants.INVOICE)
                             goTo(PaymentInvoiceTranscationActivity::class.java,extra)
                         }else{
                             val extra =Bundle()
                             viewModel.isPending.set(false)
                             extra.putString(Constants.PAYMENT_EXTRA,Constants.TRANSCATION)
                             goTo(PaymentInvoiceTranscationActivity::class.java,extra)
                         }

                     }

            }
        }
        when(view.id){

            com.colan.kindercare.R.id.main_cdv->{
            }
            com.colan.kindercare.R.id.arrow_imv->{

                if(mAdapter?.getItems()!![position].visibility){
                    mAdapter?.getItems()!![position].visibility = false
                }else{
                    mAdapter?.getItems()?.filter { it.visibility }?.forEach {
                        it.visibility = false
                    }
                    mAdapter?.getItems()!![position].visibility = true
                }


                mAdapter?.notifyDataSetChanged()
            }
        }
    }

    override fun onDataBind(v: ItemPaymentInvoiceBinding, onClickListener: View.OnClickListener) {
        v.mainCdv.setOnClickListener(onClickListener)
        v.arrowImv.setOnClickListener(onClickListener)
        v.rcvPaymentInvoiceDetail.setOnClickListener(onClickListener)

    }
    override fun onRadioButtonClick(id: Int) {

    }

    override fun onClickView(v: View?) {

    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        val intent = Intent(activity, clazz)
        mExtras?.let { intent.putExtras(it) }
        startActivity(intent)
        activity!!.overridePendingTransition(com.colan.kindercare.R.anim.anim_enter, com.colan.kindercare.R.anim.anim_exit)
    }


}
