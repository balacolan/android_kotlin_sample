package com.colan.kindercare.data.model

import java.util.ArrayList

data class PaymentInvoiceResponse(var item:String?="",var visibility:Boolean=false, var list: ArrayList<PaymentInvoiceDetailResponse>? = null)