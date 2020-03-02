package com.colan.kindercare.ui.modules.common.message.draft


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
import com.colan.kindercare.data.model.MessageModel
import com.colan.kindercare.data.remote.data.Status
import com.colan.kindercare.data.remote.data.response.message.MessageListResponse
import com.colan.kindercare.databinding.DraftListItemsBinding
import com.colan.kindercare.databinding.FragmentDraftBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.dashboard.IToolbar
import com.colan.kindercare.ui.modules.common.message.MessageVM
import com.colan.kindercare.ui.modules.common.message.compose.ComposeActivity
import com.colan.kindercare.ui.modules.parent.payment.IRadioListener
import com.colan.kindercare.utils.Constants
import com.colan.kindercare.utils.Singleton
import com.colan.kindercare.utils.bundle
import com.colan.kindercare.utils.navigateTo

/**
 * A simple [Fragment] subclass.
 */
class DraftFragment : BaseFragment<FragmentDraftBinding,MessageVM>(),IRadioListener,
    OnDataBindCallback<DraftListItemsBinding> {

    private var baseRecyclerAdapter: BaseRecyclerViewAdapter<MessageListResponse, DraftListItemsBinding>? = null

    private lateinit var mToobarManagner: IToolbar

    override val bindingVariable: Int
        get() = BR.draftMessageVM
    override val layoutId: Int
        get() = R.layout.fragment_draft
    override val viewModel: MessageVM
        get() = ViewModelProviders.of(this).get(MessageVM::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        mToobarManagner.changeToolbarTitle(getString(R.string.message_title))
        setUpRecyclerview(viewModel.messageList)
        viewModel.loadMessageList(Constants.LIST_BY_ALL,Constants.DRAFT)
        observeResponse()
    }

    private fun observeResponse() {
       /* viewModel.messageSentModel.observe(this, Observer {
            viewDataBinding?.draftRv?.adapter=baseRecyclerAdapter
            baseRecyclerAdapter?.addDataSet(it)
            baseRecyclerAdapter?.notifyDataSetChanged()
        })*/

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
                        viewModel.mShowProgressBar.value=true
                    }
                    Status.SUCCESS -> {
                        viewModel.mShowProgressBar.value=false
                        try {
                            viewModel.getMessageListApiJob?.cancel()
                            it.data?.data?.let { it1 -> viewModel.messageList.addAll(it1) }
                            setUpRecyclerview(viewModel.messageList)
                        }catch (e: Exception) {
                            Log.i("catch Error", ""+e.message)
                        }
                    }
                    Status.ERROR, Status.FAILURE -> {
                        viewModel.mShowProgressBar.value=false
                    }
                }

            }
        })


    }

    private fun setUpRecyclerview(messageList: ArrayList<MessageListResponse>) {
        baseRecyclerAdapter = BaseRecyclerViewAdapter(
            R.layout.draft_list_items,
            BR.messageItem,
            messageList,
            null,
            this
        )

        viewDataBinding?.draftRv?.adapter=baseRecyclerAdapter
        baseRecyclerAdapter?.notifyDataSetChanged()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Singleton.isDashBoardFragmentVisible =true
        mToobarManagner = context as IToolbar
    }
    override fun onItemClick(view: View, position: Int, v: DraftListItemsBinding) {
        when(view){
            v.rootLayout->{
                bundle.clear()
                bundle.putString(Constants.MESSAGE_ID,viewModel.messageList[position].msgId.toString())
                goTo(ComposeActivity::class.java, bundle)
            }
        }
    }

    override fun onDataBind(v: DraftListItemsBinding, onClickListener: View.OnClickListener) {
        v.rootLayout.setOnClickListener(onClickListener)
    }


    override fun onClickView(v: View?) {
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        navigateTo(context!!,clazz, mExtras)
    }

    override fun onRadioButtonClick(id: Int) {
    }



}
