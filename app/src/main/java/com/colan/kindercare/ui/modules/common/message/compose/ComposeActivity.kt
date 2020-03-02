package com.colan.kindercare.ui.modules.common.message.compose

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.adapter.MultiSelectSpinner
import com.colan.kindercare.data.remote.data.Status
import com.colan.kindercare.databinding.ActivityComposeBinding
import com.colan.kindercare.ui.base.BaseActivity
import com.colan.kindercare.ui.modules.common.message.MessageVM
import com.colan.kindercare.ui.modules.common.message.attachment.AttachmentActivity
import com.colan.kindercare.ui.modules.parent.payment.IRadioListener
import com.colan.kindercare.ui.modules.teacher.dailyActivity.selectStudentBottomSheet.SelectStudentBottomSheet
import com.colan.kindercare.utils.*
import com.colan.kindercare.utils.Singleton.sendToList
import com.colan.kindercare.utils.Singleton.userListId
import kotlinx.android.synthetic.main.add_attachment_layout.view.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import java.io.File


class ComposeActivity : BaseActivity<ActivityComposeBinding, MessageVM>(), IRadioListener,
    MultiSelectSpinner.OnMultipleItemsSelectedListener,
    SelectStudentBottomSheet.OnUserSlectedListener {


    override fun getBindingVariable(): Int {
        return BR.messageVM
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_compose
    }

    override fun getViewModel(): MessageVM {
        return ViewModelProviders.of(this).get(MessageVM::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel?.setNavigator(this)
        setUpToSpinner()
        observeResponse()
        setUpViews()
    }

    private fun observeResponse() {
        if(!bundle.isEmpty){
            bundle.getString(Constants.MESSAGE_ID)?.let { mViewModel?.doCallViewMessageAPI(it) }
            mViewModel?.fromDraftMessage?.value=true
            mViewModel?.mShowProgressBar?.value=true
        }else{
            getViewDataBinding().spinner.setSelection(0)
        }

        mViewModel?.fromDraftMessage?.observe(this, Observer {
            when {
                it -> getViewDataBinding().attachmentLayout.close_label.visibility=View.GONE
            }
        })
        mViewModel?.mShowProgressBar?.observeEvent(this, Observer {
            when {
                it -> showLoadingIndicator()
                else -> dismissLoadingIndicator()
            }
        })
        mViewModel?.composeMessageResponse?.observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        mViewModel?.mShowProgressBar?.value=true
                    }
                    Status.SUCCESS -> {
                        mViewModel?.mShowProgressBar?.value=false
                        try {
                            putToast("Success")
                            finish()
                        }catch (e: Exception) {
                            Log.i("catch Error", ""+e.message)
                        }

                    }
                    Status.ERROR, Status.FAILURE -> {
                        mViewModel?.mShowProgressBar?.value=false
                        putToast("Error")
                    }
                }

            }
        })


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
                                Data.sendTo?.let { it1 -> {
                                    sendToList.addAll(it1)
                                } }

                                    setSpinnerItems(Data.sendTo)

                                mViewModel?.email?.set(Data.name?.joinToString(separator =";"))
                                mViewModel?.subject?.set(Data.subject)
                                mViewModel?.composeMessage?.set(Data.message)
                                Data.userId?.forEach {
                                    userListId.add(it.toString())
                                }
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

    }

    private fun setUpViews() {
        getViewDataBinding().toolbar.title_toolbar.text = getString(R.string.compose_message)
        getViewDataBinding().toolbar.nav_icon_iv.setOnClickListener { onBackPressed() }
        getViewDataBinding().attachmentLayout.attachment_count_tv.setOnClickListener {
            goTo(AttachmentActivity::class.java, bundle)
        }
    }

    private fun setUpToSpinner() {
        getViewDataBinding().spinner.setItems(
            arrayOf(
                "None",
                "Parents",
                "Teachers",
                "Admin"
            )
        )
        getViewDataBinding().spinner.hasNoneOption(true)
        getViewDataBinding().spinner.setListener(this@ComposeActivity)
    }

    override fun onRadioButtonClick(id: Int) {
    }

    override fun onClickView(v: View?) {
        when (v?.id) {
            R.id.email_add_tv -> {
                val bottomSheetFragment = SelectStudentBottomSheet(this)

                bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
            }
            R.id.subject_cc_iv -> {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.type = "*/*"
                startActivityForResult(intent, 1001)
            }
            R.id.submit_btn->{
                mViewModel?.doCallComposeMessageAPI(Constants.SAVE_SEND_TYPE)
            }
            R.id.draft_btn->{
                if(mViewModel?.selectedFilePathList?.size!!>0){
                    Alert.createYesNoAlert(this,"Save Message","Attachment Cannot be Saved in draft",object :
                        Alert.OnAlertClickListener{
                        override fun onPositive(dialog: DialogInterface) {
                            mViewModel?.doCallDraftComposeMessageAPI(Constants.SAVE_TYPE)
                            dialog.dismiss()
                        }

                        override fun onNegative(dialog: DialogInterface) {
                            dialog.dismiss()
                        }

                    })
                }else{
                    mViewModel?.doCallDraftComposeMessageAPI(Constants.SAVE_TYPE)
                }


            }
        }
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        navigateTo(this, clazz, null)
    }

    override fun selectedIndices(indices: MutableList<Int>?) {
        sendToList.clear()
        indices?.forEach {
            Log.d("indices", "" + it)
            selectSpinnerItems(it)
        }

    }

    private fun selectSpinnerItems(it: Int) {
        when (it) {
            1 -> {
                sendToList.add(Constants.PARENT_TYPE)
            }
            2 -> {
                sendToList.add(Constants.TEACHER_TYPE)
            }
            3 -> {
                sendToList.add(Constants.ADMIN_TYPE)
            }
        }
    }

    private fun setSpinnerItems(sendTo: List<String>?) {
        val list=ArrayList<Int>()
        sendTo?.forEach {
            if(it==Constants.PARENT_TYPE){
                list.add(1)
            }else if(it==Constants.TEACHER_TYPE){
                list.add(2)
            } else if(it==Constants.ADMIN_TYPE){
                list.add(3)
            }
            getViewDataBinding().spinner.setSelection(list.toIntArray())
        }
    }


    override fun selectedStrings(strings: MutableList<String>?) {
        //putToast("" + getViewDataBinding().spinner.selectedItemsAsString)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var fileSize=0
        mViewModel?.selectedFilePathList?.clear()
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                1001 ->
                    // Checking whether data is null or not
                    if (data != null) {
                        fileSize=0
                        // Checking for selection multiple files or single.
                        if (data.clipData != null) {
                            // Getting the length of data and logging up the logs using index
                            for (index in 0 until data.clipData!!.itemCount) {
                                // Getting the URIs of the selected files and logging them into logcat at debug level
                                val uri = data.clipData!!.getItemAt(index).uri
                                //needToCheckUri
                                mViewModel?.selectedFileUriList?.add(uri)
                                mViewModel?.selectedFilePathList?.add(PathUtil.getPath(this,uri))

                                Log.d("filesUri: ", ""+PathUtil.getPath(this,uri))
                                val file = File(mViewModel?.selectedFilePathList?.get(index)!!)
                                fileSize = fileSize+Integer.parseInt((file.length() / 1024).toString())
                                Log.d("filesPath: ", ""+file)
                                mViewModel?.file?.add(file)
                            }
                            mViewModel?.isAttachmentAdded?.set(true)
                            val attachmentText="Attachment("+ mViewModel?.selectedFilePathList?.size+")"
                            getViewDataBinding().attachmentLayout.attachment_count_tv.text = attachmentText
                            getViewDataBinding().attachmentLayout.size_tv.text = getString(R.string.kb, fileSize,fileSize)

                        } else {
                            // Getting the URI of the selected file and logging into logcat at debug level
                            val uri = data.data
                            uri?.let { mViewModel?.selectedFileUriList?.add(it) }
                            mViewModel?.selectedFilePathList?.add(PathUtil.getPath(this,uri))
                            val attachmentText="Attachment("+ mViewModel?.selectedFilePathList?.size+")"
                            getViewDataBinding().attachmentLayout.attachment_count_tv.text = attachmentText
                            mViewModel?.isAttachmentAdded?.set(true)
                            val file = File(mViewModel?.selectedFilePathList?.get(0)!!)
                            fileSize = Integer.parseInt((file.length() / 1024).toString())
                            mViewModel?.file?.add(file)
                            getViewDataBinding().attachmentLayout.size_tv.text = getString(R.string.kb, fileSize,fileSize)
                            Log.d("filesPath: ", ""+file)
                            Log.d("filesUri else: ", ""+PathUtil.getPath(this,uri))
                        }
                    }
            }
        }
    }


    override fun selectedUserList(userList: ArrayList<String>) {
        mViewModel?.email?.set(userList.joinToString(separator =";"))
    }



}
