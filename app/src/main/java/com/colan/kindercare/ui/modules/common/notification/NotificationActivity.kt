package com.colan.kindercare.ui.modules.common.notification

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.adapter.BaseRecyclerViewAdapter
import com.colan.kindercare.adapter.OnDataBindCallback
import com.colan.kindercare.data.model.NotificationModel
import com.colan.kindercare.data.remote.data.Status
import com.colan.kindercare.data.remote.data.response.message.MessageListResponse
import com.colan.kindercare.data.remote.data.response.notification.NotificationResponse
import com.colan.kindercare.databinding.ActivityNotificationBinding
import com.colan.kindercare.databinding.NotificationListItemBinding
import com.colan.kindercare.ui.base.BaseActivity
import kotlinx.android.synthetic.main.custom_toolbar2.view.*

class NotificationActivity : BaseActivity<ActivityNotificationBinding, NotificationViewModel>(),
    OnDataBindCallback<NotificationListItemBinding> {

    private var baseRecyclerAdapter: BaseRecyclerViewAdapter<NotificationResponse, NotificationListItemBinding>? =
        null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeResponse()
        setUpRecyclerview()
        getViewDataBinding().toolbar.title_toolbar.text = getString(R.string.notification)
        getViewDataBinding().toolbar.nav_icon_iv.setOnClickListener { onBackPressed() }
        getViewDataBinding().toolbar.notification_clear_tv.setOnClickListener { mViewModel?.notificationList?.clear()
        baseRecyclerAdapter?.notifyDataSetChanged()
        }
    }

    override fun getBindingVariable(): Int {
        return BR.notificationVM
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_notification
    }

    override fun getViewModel(): NotificationViewModel {
        return ViewModelProviders.of(this).get(NotificationViewModel::class.java)
    }


    private fun observeResponse() {
        mViewModel?.mShowProgressBar?.observeEvent(this, Observer {
            when {
                it -> showLoadingIndicator()
                else -> dismissLoadingIndicator()
            }
        })

        mViewModel?.notificationListResponse?.observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        mViewModel?.mShowProgressBar?.value = true
                    }
                    Status.SUCCESS -> {
                        mViewModel?.mShowProgressBar?.value = false
                        try {
                            mViewModel?.getNotificationListApiJob?.cancel()
                            it.data?.data?.let {
                                    it1 -> mViewModel?.notificationList?.addAll(it1) }
                            mViewModel?.notificationList?.let { it1 -> setUpRecyclerview(it1) }
                        } catch (e: Exception) {
                            Log.i("catch Error", "" + e.message)
                        }
                    }
                    Status.ERROR, Status.FAILURE -> {
                        mViewModel?.mShowProgressBar?.value = false
                    }
                }

            }
        })


    }


    private fun setUpRecyclerview(notificationList: ArrayList<NotificationResponse>) {
        baseRecyclerAdapter = BaseRecyclerViewAdapter(
            R.layout.notification_list_item,
            BR.notificationItem,
            notificationList,
            null,
            this
        )

        getViewDataBinding().notificationRv?.adapter=baseRecyclerAdapter
        baseRecyclerAdapter?.notifyDataSetChanged()
    }

    private fun setUpRecyclerview() {
        baseRecyclerAdapter = BaseRecyclerViewAdapter(
            R.layout.notification_list_item, BR.notificationItem, ArrayList(), null,
            this
        )
    }

    override fun onItemClick(view: View, position: Int, v: NotificationListItemBinding) {
    }

    override fun onDataBind(v: NotificationListItemBinding, onClickListener: View.OnClickListener) {
    }
}
