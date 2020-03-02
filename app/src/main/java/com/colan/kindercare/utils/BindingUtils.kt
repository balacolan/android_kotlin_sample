package com.colan.kindercare.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.colan.kindercare.adapter.UserAdapter
import com.colan.kindercare.data.model.PaymentInvoiceDetailResponse

@BindingAdapter("data")
fun <T> setRecyclerViewProperties(recyclerView: RecyclerView, items: List<PaymentInvoiceDetailResponse>) {
    recyclerView.adapter = UserAdapter(items)

}