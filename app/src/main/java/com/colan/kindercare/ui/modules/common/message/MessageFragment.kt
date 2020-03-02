package com.colan.kindercare.ui.modules.common.message


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.databinding.FragmentMessageBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.dashboard.IToolbar
import com.colan.kindercare.ui.modules.common.message.compose.ComposeActivity
import com.colan.kindercare.ui.modules.common.message.draft.DraftFragment
import com.colan.kindercare.ui.modules.common.message.inbox.InboxFragment
import com.colan.kindercare.ui.modules.common.message.sent.SentMsgFragment
import com.colan.kindercare.ui.modules.common.message.trash.TrashFragment
import com.colan.kindercare.ui.modules.parent.payment.IRadioListener
import com.colan.kindercare.utils.Singleton
import com.colan.kindercare.utils.Singleton.isBackPressFromMessage
import com.colan.kindercare.utils.bundle
import com.colan.kindercare.utils.navigateTo

/**
 * A simple [Fragment] subclass.
 */
class MessageFragment : BaseFragment<FragmentMessageBinding, MessageVM>(), IRadioListener,
    InboxFragment.GetMessageCountListner {


    private lateinit var mToobarManagner: IToolbar

    override val bindingVariable: Int
        get() = BR.messageVM
    override val layoutId: Int
        get() = R.layout.fragment_message
    override val viewModel: MessageVM
        get() = ViewModelProviders.of(this).get(MessageVM::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        isBackPressFromMessage=true
        showInboxFragment()
    }

    fun showInboxFragment() {
        replaceFragment(R.id.msg_container,InboxFragment(this),"Inbox","Inbox")
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Singleton.isDashBoardFragmentVisible =true
        mToobarManagner = context as IToolbar
    }

    override fun onRadioButtonClick(id: Int) {
    }

    override fun onClickView(v: View?) {
        when(v?.id){
            R.id.inbox_layout->{
                showInboxFragment()
            }
            R.id.draft_layout->{
                replaceFragment(R.id.msg_container,DraftFragment(),"Draft","Draft")
            }
            R.id.sent_layout->{
                replaceFragment(R.id.msg_container,SentMsgFragment(),"Sent","Sent")
            }
            R.id.trash_layout->{
                replaceFragment(R.id.msg_container,TrashFragment(),"Trash","Trash")
            }
            R.id.compose_layout->{
                bundle.clear()
                goTo(ComposeActivity::class.java,bundle)
            }
        }
    }

    override fun getMessageCount(count: String?) {
        when {
            count!="null" -> viewModel.unReadMsgCount.set(count)
            else -> viewModel.unReadMsgCount.set("0")
        }

    }


    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        navigateTo(context!!,clazz, null)
    }

    override fun onDetach() {
        super.onDetach()
        isBackPressFromMessage=false
    }
}
