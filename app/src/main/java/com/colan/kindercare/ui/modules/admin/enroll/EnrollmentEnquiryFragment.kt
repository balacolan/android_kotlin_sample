package com.colan.kindercare.ui.modules.admin.enroll


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.adapter.BaseRecyclerViewAdapter
import com.colan.kindercare.adapter.OnDataBindCallback
import com.colan.kindercare.data.remote.data.Status
import com.colan.kindercare.data.remote.data.response.enrollment.EnrollDetail
import com.colan.kindercare.databinding.EnquiryEnrollmentListItemsBinding
import com.colan.kindercare.databinding.FragmentEnrollmentEnquiryBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.dashboard.IToolbar
import com.colan.kindercare.ui.modules.admin.enroll.addEnquiry.AddEnquiryActivity
import com.colan.kindercare.ui.modules.admin.enroll.details.EnrollmentDetailsActivity
import com.colan.kindercare.utils.*

/**
 * A simple [Fragment] subclass.
 */
class EnrollmentEnquiryFragment :
    BaseFragment<FragmentEnrollmentEnquiryBinding, EnrollmentEnquiryVM>(),
    BaseNavigator, OnDataBindCallback<EnquiryEnrollmentListItemsBinding>, SchoolSelectionListner {

    private var baseRecyclerAdapter: BaseRecyclerViewAdapter<EnrollDetail, EnquiryEnrollmentListItemsBinding>? =
        null
    private var childDetailList: List<EnrollDetail>? = null
    private lateinit var mToobarManagner: IToolbar

    override val bindingVariable: Int
        get() = BR.enrollmentEnquiryVM
    override val layoutId: Int
        get() = R.layout.fragment_enrollment_enquiry
    override val viewModel: EnrollmentEnquiryVM
        get() = ViewModelProviders.of(this).get(EnrollmentEnquiryVM::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        setUpRecyclerview()
        observeResponse()
        viewModel.getEnrolmentList(null)
        SchoolSelectionBroadCast.schoolLister = this
        viewModel.mShowProgressBar.observe(this, Observer {
            when {
                it -> showLoadingIndicator()
                else -> dismissLoadingIndicator()
            }
        }

        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mToobarManagner.changeToolbarTitle(getString(R.string.enrollment_enquiry))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Singleton.isDashBoardFragmentVisible = true
        mToobarManagner = context as IToolbar
    }


    private fun observeResponse() {
        viewModel.enrollmentModel.observe(this, Observer {
            viewDataBinding?.enrollmentRv?.adapter = baseRecyclerAdapter
            it?.let {
                when (it.status) {
                    Status.SUCCESS -> {
                        viewModel.mShowProgressBar.value = false
                        it.data?.data?.data.let { detail ->
                            baseRecyclerAdapter?.cleatDataSet()
                            baseRecyclerAdapter?.addDataSet(detail!!)
                            childDetailList = detail!!
                            baseRecyclerAdapter?.notifyDataSetChanged()
                        }
                        viewModel.childTotalCount.set(it.data?.data?.totalCount.toString())
                        viewModel.childAccepted.set(it.data?.data?.acceptedCount.toString())
                        viewModel.childRejected.set(it.data?.data?.rejectedCount.toString())
                    }
                    Status.FAILURE -> {
                        putToast(it.message)
                        viewModel.mShowProgressBar.value = false
                    }
                    Status.ERROR -> {
                        putToast(it.message)
                        viewModel.mShowProgressBar.value = false
                    }
                    else -> {
                        putToast(it.message)
                    }
                }
            }

        })
    }

    private fun setUpRecyclerview() {
        baseRecyclerAdapter = BaseRecyclerViewAdapter(
            R.layout.enquiry_enrollment_list_items,
            BR.enquiryItem,
            ArrayList(),
            null,
            this
        )
    }


    override fun onClickView(v: View?) {
        when (v?.id) {
            R.id.add_new_enquiry_iv -> {
                startActivityForResult(
                    Intent(context, AddEnquiryActivity::class.java).putExtras(
                        bundle
                    ), 200
                )
                // goTo(AddEnquiryActivity::class.java, null)
            }
            R.id.date_previous_iv -> {
                val date = viewModel.selectedDate.get()?.let { StringToDateConvert(it) }
                val decreamentDate = date?.let { decrementDateByOne(it) }
                val dateInString = decreamentDate?.toString("dd MMMM yyyy")
                viewModel.selectedDate.set(dateInString)
                viewModel.getEnrolmentList(dateToStringCustom(viewModel.selectedDate.get()!!))
            }
            R.id.date_next_iv -> {
                val date = viewModel.selectedDate.get()?.let { StringToDateConvert(it) }
                val incrementDate = date?.let { incrementDateByOne(it) }
                val dateInString = incrementDate?.toString("dd MMMM yyyy")
                viewModel.selectedDate.set(dateInString)
                viewModel.getEnrolmentList(viewModel.selectedDate.get()!!)
            }
        }
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        navigateTo(context!!, clazz, mExtras)
    }

    override fun onItemClick(view: View, position: Int, v: EnquiryEnrollmentListItemsBinding) {
        when (view) {
            v.rootLayout -> {
                bundle.clear()
                bundle.putString(
                    Constants.CHILD_AGE,
                    childDetailList?.get(position)?.age.toString()
                )
                bundle.putString(
                    Constants.CHILD_NAME,
                    childDetailList?.get(position)?.studentName.toString()
                )
                bundle.putString(
                    Constants.CHILD_PARENT_EMAIL,
                    childDetailList?.get(position)?.email.toString()
                )
                bundle.putString(
                    Constants.CHILD_PARENT_MOBILE,
                    childDetailList?.get(position)?.contact.toString()
                )
                bundle.putString(
                    Constants.CHILD_FATHER_NAME,
                    childDetailList?.get(position)?.fatherName.toString()
                )
                bundle.putString(
                    Constants.CHILD_STATUS,
                    childDetailList?.get(position)?.getStatusString()
                )
                bundle.putString(
                    Constants.CHILD_MOTHER_NAME,
                    childDetailList?.get(position)?.motherName.toString()
                )
                bundle.putString(Constants.CHILD_CLASS, childDetailList?.get(position)?._class)
                bundle.putString(Constants.CHILD_DOB, childDetailList?.get(position)?.dob)
                bundle.putString(Constants.CHILD_ID, childDetailList?.get(position)?.id.toString())
                bundle.putString(Constants.CHILD_PURPOSE, childDetailList?.get(position)?.purpose)

                startActivityForResult(Intent(context, EnrollmentDetailsActivity::class.java).putExtras(bundle), 200)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Constants.ENROLLCALLBACK -> {
                viewModel.getEnrolmentList(null)
            }
        }
    }

    override fun onDataBind(
        v: EnquiryEnrollmentListItemsBinding,
        onClickListener: View.OnClickListener
    ) {
        v.rootLayout.setOnClickListener(onClickListener)
    }


    override fun onTriggerSchoolSelection() {
        baseRecyclerAdapter?.cleatDataSet()
        baseRecyclerAdapter?.notifyDataSetChanged()
        viewModel.getEnrolmentList(viewModel.selectedDate.get()!!)
    }


}
