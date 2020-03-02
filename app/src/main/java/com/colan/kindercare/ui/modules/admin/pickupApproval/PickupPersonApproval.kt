package com.colan.kindercare.ui.modules.admin.pickupApproval


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
import com.colan.kindercare.data.remote.data.response.pickupPerson.PickupPersonListResponse
import com.colan.kindercare.databinding.FragmentPickupPersonApprovalBinding
import com.colan.kindercare.databinding.PersonalPickupApprovalItemBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.dashboard.IToolbar
import com.colan.kindercare.ui.modules.admin.pickupApproval.details.PickupPersonDetails
import com.colan.kindercare.utils.*

/**
 * A simple [Fragment] subclass.
 */
class PickupPersonApproval : BaseFragment<FragmentPickupPersonApprovalBinding,PickupPersonVM>(),BaseNavigator,
    OnDataBindCallback<PersonalPickupApprovalItemBinding>, SchoolSelectionListner {


    private var baseRecyclerAdapter: BaseRecyclerViewAdapter<PickupPersonListResponse, PersonalPickupApprovalItemBinding>? = null
    private lateinit var mToobarManagner: IToolbar

    override val bindingVariable: Int
        get() = BR.pickUpVM
    override val layoutId: Int
        get() = R.layout.fragment_pickup_person_approval
    override val viewModel: PickupPersonVM
        get() =  ViewModelProviders.of(this).get(PickupPersonVM::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        SchoolSelectionBroadCast.schoolLister =this
        observeResponse()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mToobarManagner.changeToolbarTitle(getString(R.string.pickup_person_approval))
    }

    private fun observeResponse() {
        viewModel.mShowProgressBar.observeEvent(this, Observer {
            when {
                it -> showLoadingIndicator()
                else -> dismissLoadingIndicator()
            }
        })

        viewModel.pickupListResponse.observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        viewModel.mShowProgressBar.value=true
                    }
                    Status.SUCCESS -> {
                        viewModel.mShowProgressBar.value=false
                        try {
                            viewModel.getPickupPersonListApiJob?.cancel()
                            it.data?.data?.let { it1 -> viewModel.pickupApprovalList.addAll(it1) }
                            setUpRecyclerview(viewModel.pickupApprovalList)
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



        viewModel.statusResponse.observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        viewModel.mShowProgressBar.value=true
                    }
                    Status.SUCCESS -> {
                        viewModel.mShowProgressBar.value=false
                        try {
                            viewModel.pickupApprovalList.clear()
                            viewModel.loadPickupApprovalData()
                        }catch (e: Exception) {
                            Log.i("catch Error", ""+e.message)
                        }
                    }
                    Status.ERROR, Status.FAILURE -> {
                        viewModel.mShowProgressBar.value=false
                        putToast("Error")
                    }
                }

            }
        })

    }

    private fun setUpRecyclerview(pickupApprovalList: ArrayList<PickupPersonListResponse>) {
        baseRecyclerAdapter = BaseRecyclerViewAdapter(
            R.layout.personal_pickup_approval_item,
            BR.approvalItem,
            pickupApprovalList,
            null,
            this
        )

        viewDataBinding?.pickutpApprovalRv?.adapter=baseRecyclerAdapter
        baseRecyclerAdapter?.notifyDataSetChanged()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Singleton.isDashBoardFragmentVisible =true
        mToobarManagner = context as IToolbar
    }

    override fun onClickView(v: View?) {
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        navigateTo(context!!, clazz, null)
    }

    override fun onItemClick(view: View, position: Int, v: PersonalPickupApprovalItemBinding) {
        when(view){
            v.rootLayout->{
                bundle.clear()
                viewModel.itemId.set(viewModel.pickupApprovalList[position].id.toString())
                bundle.putString(Constants.ITEM_ID,viewModel.pickupApprovalList[position].id.toString())
                bundle.putString(Constants.APPROVED_STATUS,viewModel.pickupApprovalList[position].approvalStatus)
                bundle.putString(Constants.STUDENT_NAME,viewModel.pickupApprovalList[position].name)
                bundle.putString(Constants.STUDENT_CLASS,viewModel.pickupApprovalList[position]._class+" "+viewModel.pickupApprovalList[position].section)
                bundle.putString(Constants.FATHERS_NAME,viewModel.pickupApprovalList[position].fathername)
                bundle.putString(Constants.MOTHERS_NAME,viewModel.pickupApprovalList[position].mothername)
                bundle.putString(Constants.PICKUP_PERSON_NAME,viewModel.pickupApprovalList[position].pickupPersonName)
                bundle.putString(Constants.RELATIONSHIP,viewModel.pickupApprovalList[position].relationType)
                bundle.putString(Constants.CONTACTS,viewModel.pickupApprovalList[position].contactNumber)
                goTo(PickupPersonDetails::class.java, bundle)
            }

            v.approvedBtn->{
                viewModel.itemId.set(viewModel.pickupApprovalList[position].id.toString())
                viewModel.doCallApproveRejectApi(Constants.APPROVE)
            }

            v.rejectedBtn->{
                viewModel.itemId.set(viewModel.pickupApprovalList[position].id.toString())
                viewModel.doCallApproveRejectApi(Constants.REJECT)
            }
        }
    }

    override fun onDataBind(
        v: PersonalPickupApprovalItemBinding,
        onClickListener: View.OnClickListener
    ) {
        v.rootLayout.setOnClickListener(onClickListener)
        v.approvedBtn.setOnClickListener(onClickListener)
        v.rejectedBtn.setOnClickListener(onClickListener)
    }

    override fun onTriggerSchoolSelection() {
        viewModel.pickupApprovalList.clear()
        viewModel.loadPickupApprovalData()
    }



}
