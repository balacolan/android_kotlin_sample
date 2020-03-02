package com.colan.kindercare.ui.modules.common.message.inbox


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.adapter.BaseRecyclerViewAdapter
import com.colan.kindercare.adapter.OnDataBindCallback
import com.colan.kindercare.data.model.MessageModel
import com.colan.kindercare.data.remote.data.Status
import com.colan.kindercare.data.remote.data.response.message.MessageListResponse
import com.colan.kindercare.databinding.FragmentInboxBinding
import com.colan.kindercare.databinding.MessageListItemsBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.dashboard.IToolbar
import com.colan.kindercare.ui.modules.common.message.MessageVM
import com.colan.kindercare.ui.modules.common.message.details.MessageDetailActivity
import com.colan.kindercare.ui.modules.parent.payment.IRadioListener
import com.colan.kindercare.utils.Constants
import com.colan.kindercare.utils.Singleton
import com.colan.kindercare.utils.Singleton.isInboxClicked
import com.colan.kindercare.utils.Singleton.isSentClicked
import com.colan.kindercare.utils.bundle
import com.colan.kindercare.utils.navigateTo

/**
 * A simple [Fragment] subclass.
 */
class InboxFragment(private val msgCount:GetMessageCountListner) : BaseFragment<FragmentInboxBinding, MessageVM>(), IRadioListener,
    OnDataBindCallback<MessageListItemsBinding> {

    private var baseRecyclerAdapter: BaseRecyclerViewAdapter<MessageListResponse, MessageListItemsBinding>? = null

    private lateinit var mToobarManagner: IToolbar

    override val bindingVariable: Int
        get() = BR.messageVM
    override val layoutId: Int
        get() = R.layout.fragment_inbox
    override val viewModel: MessageVM
        get() = ViewModelProviders.of(this).get(MessageVM::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        mToobarManagner.changeToolbarTitle(getString(R.string.message_title))
        setUpUser()
        viewModel.loadMessageList(Constants.LIST_BY_ALL,Constants.INBOX)
        observeResponse()
    }

    private fun setUpUser() {
        if(viewModel.isteacher.get()!!){
            viewDataBinding?.radioGb?.weightSum=3f
            viewDataBinding?.allMessageRb?.visibility=View.GONE
        }
    }

    private fun observeResponse() {

        viewModel.mShowProgressBar.observeEvent(this, Observer {
            when {
                it -> showLoadingIndicator()
                else -> dismissLoadingIndicator()
            }
        })

        viewModel.messageListResponse.observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        viewModel.mShowProgressBar.value = true
                    }
                    Status.SUCCESS -> {
                        viewModel.mShowProgressBar.value = false
                        try {
                            viewModel.getMessageListApiJob?.cancel()
                            it.data?.data?.let { it1 -> viewModel.messageList.addAll(it1) }
                            viewModel.unReadMsgCount.set(it.data?.data?.filter { f->f.read_status==0 }?.size.toString())
                            msgCount.getMessageCount(it.data?.data?.filter { f->f.read_status==0 }?.size.toString())
                            setUpRecyclerview(viewModel.messageList)
                        } catch (e: Exception) {
                            Log.i("catch Error", "" + e.message)
                        }
                    }
                    Status.ERROR, Status.FAILURE -> {
                        viewModel.mShowProgressBar.value = false
                    }
                }

            }
        })


    }


    private fun setUpRecyclerview(messageList: ArrayList<MessageListResponse>) {
        baseRecyclerAdapter = BaseRecyclerViewAdapter(
            R.layout.message_list_items,
            BR.messageItem,
            messageList,
            null,
            this
        )

        viewDataBinding?.messageRv?.adapter=baseRecyclerAdapter
        baseRecyclerAdapter?.notifyDataSetChanged()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Singleton.isDashBoardFragmentVisible =true
        mToobarManagner = context as IToolbar
    }

    override fun onItemClick(view: View, position: Int, v: MessageListItemsBinding) {
        when(view){
            v.rootLayout->{
                isSentClicked=false
                isInboxClicked=true
                bundle.clear()
                bundle.putString(Constants.MESSAGE_ID,viewModel.messageList[position].msgId.toString())
                bundle.putString(Constants.PROFILE_IMAGE,viewModel.messageList[position].profile.toString())
                val intent = Intent(context, MessageDetailActivity::class.java)
                intent.putExtras(bundle)
                startActivityForResult(intent, 200)
            }
        }
    }

    override fun onDataBind(v: MessageListItemsBinding, onClickListener: View.OnClickListener) {
        v.rootLayout.setOnClickListener(onClickListener)
    }

    override fun onRadioButtonClick(id: Int) {
        when(id){
            R.id.admin_rb->{
                viewModel.messageList.clear()
                viewModel.loadMessageList(Constants.LIST_BY_ADMIN,Constants.INBOX)
            }
            R.id.teacher_rb->{
                viewModel.messageList.clear()
                viewModel.loadMessageList(Constants.LIST_BY_TEACHER,Constants.INBOX)
            }
            R.id.super_admin_rb->{
                viewModel.messageList.clear()
                viewModel.loadMessageList(Constants.LIST_BY_SUPER_ADMIN,Constants.INBOX)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(resultCode){
            Constants.ENROLLCALLBACK->{
                viewModel.messageList.clear()
                viewModel.loadMessageList(Constants.LIST_BY_ALL,Constants.INBOX)
            }
        }
    }

    interface GetMessageCountListner{
       fun getMessageCount(count:String?)
    }

    override fun onClickView(v: View?) {
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        navigateTo(context!!,clazz, mExtras)
    }

}
