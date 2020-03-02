package com.colan.kindercare.ui.modules.parent.payment

import android.app.Application
import android.graphics.Paint
import android.view.View
import android.widget.CompoundButton
import android.widget.RadioGroup
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.R
import com.colan.kindercare.data.model.PaymentInvoiceDetailResponse
import com.colan.kindercare.data.model.PaymentInvoiceResponse
import com.colan.kindercare.data.model.SelectChildModel
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.base.BaseViewModel
import java.util.*

class PaymentVM(application: Application) : BaseViewModel<IRadioListener>(application)
{
    val mInvoiceRb = ObservableField<Boolean>(false)
    var paymentInvoiceResponse = MutableLiveData<ArrayList<PaymentInvoiceResponse>>()
    private var paymentInvoiceList = ArrayList<PaymentInvoiceResponse>()
    private var list = ArrayList<PaymentInvoiceDetailResponse>()
    var isPending=ObservableField(false)
    var mStatus=ObservableField("")
    fun onClickAction(view: View?) {
        getNavigator().onClickView(view)
    }

    fun onRbChanged(button: RadioGroup,id:Int) {
        getNavigator().onRadioButtonClick(id)
      }


     fun setPaymentInvoiceResponseData(){
         paymentInvoiceResponse.value = loadListData()
     }

    private fun loadListData(): ArrayList<PaymentInvoiceResponse> {
        val dataDetail=PaymentInvoiceDetailResponse("Term 1","Paid",false)
        list.add(dataDetail)
        val dataDetail1=PaymentInvoiceDetailResponse("Term 2","Paid",false)
        list.add(dataDetail1)
        val dataDetail2=PaymentInvoiceDetailResponse("Term 3","Pending",true)
        list.add(dataDetail2)

        val data=PaymentInvoiceResponse("School Fees",false,list)
        paymentInvoiceList.add(data)
        val data1=PaymentInvoiceResponse("Book Fees",false,list)
        paymentInvoiceList.add(data1)
        val data2=PaymentInvoiceResponse("Uniform Fees",false,list)
        paymentInvoiceList.add(data2)
        return this.paymentInvoiceList
    }



}