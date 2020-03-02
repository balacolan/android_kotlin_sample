package com.colan.kindercare.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.data.model.PaymentInvoiceDetailResponse
import kotlinx.android.synthetic.main.item_payment_detail_invoice.view.*


class UserAdapter(val data: List<PaymentInvoiceDetailResponse>) : RecyclerView.Adapter<UserAdapter.UserHolder>(){// ,ListBinding<LitterModel> {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val layoutInflater  = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_payment_detail_invoice, parent, false)

        return UserHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.invoice_item_term_tv.setOnClickListener {
            //data[position].isClicked.set(false)
            val rv = holder.itemView.parent as RecyclerView
            rv.tag = data[position].status
            rv.performClick()
        }

        holder.itemView.invoice_item_status_tv.setOnClickListener {
            //data[position].isClicked.set(false)
            val rv = holder.itemView.parent as RecyclerView
            rv.tag = data[position].status
            rv.performClick()
        }

    }

    class UserHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Any) {
            binding.setVariable(BR.itemPaymentDetailInvoiceVM, data)
            binding.executePendingBindings()
        }
    }
}