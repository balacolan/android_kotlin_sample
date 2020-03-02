package com.colan.kindercare.ui.modules.common.message.trash

import android.content.Context
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
import com.colan.kindercare.data.remote.data.Status
import com.colan.kindercare.data.remote.data.response.message.MessageListResponse
import com.colan.kindercare.databinding.FragmentTrashBinding
import com.colan.kindercare.databinding.MessageListItemsBinding
import com.colan.kindercare.databinding.SentMessageListItemsBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.dashboard.IToolbar
import com.colan.kindercare.ui.modules.common.message.MessageVM
import com.colan.kindercare.ui.modules.parent.payment.IRadioListener
import com.colan.kindercare.utils.Constants
import com.colan.kindercare.utils.Singleton
import com.colan.kindercare.utils.navigateTo

/**
 * A simple [Fragment] subclass.
 */

class TrashFragment : BaseFragment<FragmentTrashBinding, MessageVM>(), IRadioListener,
    OnDataBindCallback<SentMessageListItemsBinding> {

    private lateinit var mToobarManagner: IToolbar
    private var baseRecyclerAdapter: BaseRecyclerViewAdapter<MessageListResponse, SentMessageListItemsBinding>? = null
    override val bindingVariable: Int
        get() = BR.messageVM
    override val layoutId: Int
        get() = R.layout.fragment_trash
    override val viewModel: MessageVM
        get() = ViewModelProviders.of(this).get(MessageVM::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        mToobarManagner.changeToolbarTitle(getString(R.string.message_title))
        viewModel.loadMessageList(Constants.LIST_BY_ALL, Constants.TRASH)
        observeResponse()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Singleton.isDashBoardFragmentVisible =true
        mToobarManagner = context as IToolbar

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
                            it.data?.data?.let {
                                    it1 -> viewModel.messageList.addAll(it1) }
                            viewModel.isListEmpty.set( viewModel.messageList.isNotEmpty())
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
            R.layout.sent_message_list_items,
            BR.messageItem,
            messageList,
            null,
            this
        )

        viewDataBinding?.trashRv?.adapter=baseRecyclerAdapter
        baseRecyclerAdapter?.notifyDataSetChanged()
    }

    override fun onRadioButtonClick(id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClickView(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        navigateTo(context!!,clazz, mExtras)
    }

    override fun onItemClick(view: View, position: Int, v: SentMessageListItemsBinding) {
    }

    override fun onDataBind(v: SentMessageListItemsBinding, onClickListener: View.OnClickListener) {
    }


}
