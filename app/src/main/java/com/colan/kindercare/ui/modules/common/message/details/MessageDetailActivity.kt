package com.colan.kindercare.ui.modules.common.message.details

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.data.remote.data.Status
import com.colan.kindercare.databinding.ActivityMessageDetailBinding
import com.colan.kindercare.ui.base.BaseActivity
import com.colan.kindercare.ui.modules.common.message.MessageVM
import com.colan.kindercare.ui.modules.common.message.attachment.AttachmentActivity
import com.colan.kindercare.ui.modules.parent.payment.IRadioListener
import com.colan.kindercare.utils.Constants
import com.colan.kindercare.utils.Singleton
import com.colan.kindercare.utils.Singleton.isInboxClicked
import com.colan.kindercare.utils.bundle
import com.colan.kindercare.utils.navigateTo
import kotlinx.android.synthetic.main.add_attachment_layout.view.*
import kotlinx.android.synthetic.main.attachment_layout_white_bg.view.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class MessageDetailActivity : BaseActivity<ActivityMessageDetailBinding,MessageVM>(),IRadioListener {

    override fun getBindingVariable(): Int {
        return BR.messageVM
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_message_detail
    }

    override fun getViewModel(): MessageVM {
        return ViewModelProviders.of(this).get(MessageVM::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel?.setNavigator(this)
        getViewDataBinding().toolbar.title_toolbar.text=getString(R.string.inbox)
        getViewDataBinding().toolbar.nav_icon_iv.setOnClickListener {  setResult(Constants.ENROLLCALLBACK,  Intent())
            finish() }
        getViewDataBinding().attachmentView.view_attachement_tv.setOnClickListener {
            goTo(AttachmentActivity::class.java, bundle)
        }
        getViewDataBinding().attachmentView.view_attachement_tv.text = getString(R.string.viewAll)
        getViewDataBinding().toolbar.notification_iv.visibility=View.VISIBLE

        if(isInboxClicked){
            getViewDataBinding().toolbar.title_toolbar.text=getString(R.string.inbox)
            mViewModel?.fromInboxMessage?.set(true)
            mViewModel?.msgTpe?.set("3")
        }else{
            getViewDataBinding().toolbar.title_toolbar.text=getString(R.string.sent_message)
            mViewModel?.fromInboxMessage?.set(false)
            mViewModel?.msgTpe?.set("2")
        }

        observeResponse()
    }

    private fun observeResponse() {
        if(!bundle.isEmpty){
            bundle.getString(Constants.MESSAGE_ID)?.let { mViewModel?.doCallViewMessageAPI(it) }
            mViewModel?.profile?.set(bundle.getString(Constants.PROFILE_IMAGE))
            mViewModel?.fromDraftMessage?.value=true
            mViewModel?.mShowProgressBar?.value=true
        }

        mViewModel?.viewMessageResponse?.observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        mViewModel?.mShowProgressBar?.value=true
                    }
                    Status.SUCCESS -> {
                        mViewModel?.mShowProgressBar?.value=false
                        try {
                            mViewModel?.getMessageListApiJob?.cancel()
                            for (Data in it.data?.data!!){
                                mViewModel?.email?.set(Data.name?.joinToString(separator =";"))
                                Data.attachment?.forEach {
                                    mViewModel?.selectedFilePathList?.add(it)
                                }
                                val attachmentText="Attachment("+ mViewModel?.selectedFilePathList?.size+")"
                                //getViewDataBinding().attachmentView.attachment_count_tv.text = attachmentText
                                mViewModel?.subject?.set(Data.subject)
                                mViewModel?.name?.set(Data.from)
                                mViewModel?.type?.set(Data.fromUserType)
                                mViewModel?.composeMessage?.set(Data.message)
                                mViewModel?.date?.set(Data.date)
                            }

                        }catch (e: Exception) {
                            Log.i("catch Error", ""+e.message)
                        }
                    }
                    Status.ERROR, Status.FAILURE -> {
                        mViewModel?.mShowProgressBar?.value=false
                    }
                }

            }
        })

        mViewModel?.deleteMessageResponse?.observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        mViewModel?.mShowProgressBar?.value=true
                    }
                    Status.SUCCESS -> {
                        mViewModel?.mShowProgressBar?.value=false
                        try {
                            mViewModel?.getMessageListApiJob?.cancel()
                            setResult(Constants.ENROLLCALLBACK,  Intent())
                            finish()
                        }catch (e: Exception) {
                            Log.i("catch Error", ""+e.message)
                        }
                    }
                    Status.ERROR, Status.FAILURE -> {
                        mViewModel?.mShowProgressBar?.value=false
                    }
                }

            }
        })
    }

    override fun onRadioButtonClick(id: Int) {
    }

    override fun onClickView(v: View?) {
        when(v?.id){
            R.id.delete_msg_btn->{
                bundle.getString(Constants.MESSAGE_ID)?.let { mViewModel?.doCallDeleteMessageAPI(it) }
            }
        }
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        navigateTo(this,clazz, null)
    }

    override fun onDestroy() {
        super.onDestroy()
        getViewDataBinding().toolbar.notification_iv.visibility=View.GONE
    }



}
