package com.colan.kindercare.ui.modules.admin.attendance.admin


import android.app.TimePickerDialog
import android.content.IntentFilter
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
import com.colan.kindercare.data.remote.data.response.attendance.AttendanceResponse
import com.colan.kindercare.databinding.AttendanceListItemsBinding
import com.colan.kindercare.databinding.FragmentAttendanceAdminBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.modules.admin.attendance.AdminAttendanceVM
import com.colan.kindercare.ui.modules.parent.payment.IRadioListener
import com.colan.kindercare.utils.Constants
import com.colan.kindercare.utils.SchoolSelectionBroadCast
import com.colan.kindercare.utils.SchoolSelectionListner
import com.colan.kindercare.utils.Singleton.mIntentFilter
import kotlinx.android.synthetic.main.mark_absent_layout.view.*
import kotlinx.android.synthetic.main.signin_signout_absent_layout.view.*
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class AttendanceAdminFragment :  BaseFragment<FragmentAttendanceAdminBinding, AdminAttendanceVM>(),
    IRadioListener,
    OnDataBindCallback<AttendanceListItemsBinding>, SchoolSelectionListner {


    private var baseRecyclerAdapter: BaseRecyclerViewAdapter<AttendanceResponse, AttendanceListItemsBinding>? = null

    override val bindingVariable: Int
        get() = BR.adminAttendanceVM
    override val layoutId: Int
        get() = R.layout.fragment_attendance_admin
    override val viewModel: AdminAttendanceVM
        get() =  ViewModelProviders.of(this).get(AdminAttendanceVM::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        observeResponse()
        viewModel.loadAttedanceData(Constants.ADMIN_TYPE)
    }



    private fun setUpPresentAbsentClick() {
        viewDataBinding?.bottomLayout?.present_btn?.setOnClickListener {
            val selectedUserIdList=ArrayList<String>()
            for (Data in viewModel.attendanceList.filter { it.isSelected }){
                selectedUserIdList.add(Data.userId.toString())
            }
            when {
                selectedUserIdList.size>0 -> viewModel.doCallAddAdminAttendanceAPI(Constants.ADMIN_TYPE,viewModel.iSharedPreferenceService.getStringValue(Constants.SCHOOL_ID),Constants.LOG_PRESENT,selectedUserIdList)
                else -> putToast(getString(R.string.please_select_checkbox))
            }

        }
        viewDataBinding?.bottomLayout?.mark_absent_btn?.setOnClickListener {
            val selectedUserIdList=ArrayList<String>()
            for (Data in viewModel.attendanceList.filter { it.isSelected }){
                selectedUserIdList.add(Data.userId.toString())
            }
            when {
                selectedUserIdList.size>0 -> viewModel.doCallAddAdminAttendanceAPI(
                    Constants.ADMIN_TYPE,
                    viewModel.iSharedPreferenceService.getStringValue(Constants.SCHOOL_ID),
                    Constants.LOG_ABSCENT,
                    selectedUserIdList
                )
                else -> putToast(getString(R.string.please_select_checkbox))
            }
            }

        viewDataBinding?.bottomLayout2?.sign_in_btn?.setOnClickListener {
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(this.context,
                TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                    viewModel.signInTime.set(
                        "$selectedHour:$selectedMinute"
                    )
                    val selectedUserIdList=ArrayList<String>()
                    for (Data in viewModel.attendanceList.filter { it.isSelected }){
                        selectedUserIdList.add(Data.userId.toString())
                    }
                    when{
                        selectedUserIdList.size>0 ->viewModel.doCallAddAdminAttendanceAPI(Constants.ADMIN_TYPE,viewModel.iSharedPreferenceService.getStringValue(Constants.SCHOOL_ID),Constants.LOG_SIGN_IN,selectedUserIdList)
                        else -> putToast(getString(R.string.please_select_checkbox))
                    }

                }, hour, minute, true
            )//Yes 24 hour time
            mTimePicker.setTitle("Select Time")
            mTimePicker.show()
        }


        viewDataBinding?.bottomLayout2?.sign_out_btn?.setOnClickListener {
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(this.context,
                TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                    viewModel.signInTime.set(
                        "$selectedHour:$selectedMinute"
                    )
                    val selectedUserIdList=ArrayList<String>()
                    for (Data in viewModel.attendanceList.filter { it.isSelected }){
                        selectedUserIdList.add(Data.userId.toString())
                    }

                    when{
                        selectedUserIdList.size>0 ->viewModel.doCallAddAdminAttendanceAPI(Constants.ADMIN_TYPE,viewModel.iSharedPreferenceService.getStringValue(Constants.SCHOOL_ID),Constants.LOG_SIGN_OUT,selectedUserIdList)
                        else -> putToast(getString(R.string.please_select_checkbox))
                    }

                }, hour, minute, true
            )//Yes 24 hour time
            mTimePicker.setTitle("Select Time")
            mTimePicker.show()
        }

        viewDataBinding?.bottomLayout2?.absent_btn_2?.setOnClickListener {
            val selectedUserIdList=ArrayList<String>()
            for (Data in viewModel.attendanceList.filter { it.isSelected }){
                selectedUserIdList.add(Data.userId.toString())
            }

            when{
                selectedUserIdList.size>0 -> viewModel.doCallAddAdminAttendanceAPI(Constants.ADMIN_TYPE,viewModel.iSharedPreferenceService.getStringValue(Constants.SCHOOL_ID),Constants.LOG_ABSCENT,selectedUserIdList)

                else -> putToast(getString(R.string.please_select_checkbox))
            }

        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpPresentAbsentClick()
    }


    private fun observeResponse() {

        viewModel.mShowProgressBar.observeEvent(this, Observer {
            when {
                it -> showLoadingIndicator()
                else -> dismissLoadingIndicator()
            }
        })

        viewModel.getAttendanceListResponse.observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        viewModel.mShowProgressBar.value = true
                    }
                    Status.SUCCESS -> {
                        viewModel.mShowProgressBar.value = false
                        try {
                            it.data?.data?.let { it1 -> viewModel.attendanceList.addAll(it1) }
                            setUpRecyclerview(viewModel.attendanceList)
                        }catch (e: Exception) {
                            Log.i("catch Error", ""+e.message)
                        }


                    }
                    Status.ERROR, Status.FAILURE -> {
                        viewModel.mShowProgressBar.value = false
                    }
                }

            }
        })

        viewModel.addAdminAttendanceResponse.observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        viewModel.mShowProgressBar.value = true
                    }
                    Status.SUCCESS -> {
                        viewModel.mShowProgressBar.value = false
                        viewModel.attendanceList.clear()
                        viewModel.loadAttedanceData(Constants.ADMIN_TYPE)
                        putToast("Success")
                    }
                    Status.ERROR, Status.FAILURE -> {
                        viewModel.mShowProgressBar.value = false
                        putToast(""+it.data?.errors)
                    }
                }

            }
        })
    }

    private fun setUpRecyclerview(attendanceList: ArrayList<AttendanceResponse>) {
        baseRecyclerAdapter = BaseRecyclerViewAdapter(
            R.layout.attendance_list_items,
            BR.attendanceItem,
            attendanceList,
            null,
            this
        )
        viewDataBinding?.attendanceAdminRv?.adapter=baseRecyclerAdapter
        baseRecyclerAdapter?.notifyDataSetChanged()
    }


    override fun onItemClick(view: View, position: Int, v: AttendanceListItemsBinding) {
        if (view == v.checkBox) {
            viewModel.attendanceList[position].isSelected = v.checkBox.isChecked
        }
    }

    override fun onDataBind(v: AttendanceListItemsBinding, onClickListener: View.OnClickListener) {
        v.checkBox.setOnClickListener(onClickListener)
    }

    override fun onClickView(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRadioButtonClick(id: Int) {
    }


    override fun onTriggerSchoolSelection() {
        viewModel.attendanceList.clear()
        viewModel.loadAttedanceData(Constants.ADMIN_TYPE)
    }

    override fun onResume() {
        super.onResume()
        SchoolSelectionBroadCast.schoolLister =this
        context!!.registerReceiver(SchoolSelectionBroadCast(),mIntentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        context!!.unregisterReceiver(SchoolSelectionBroadCast())
    }


}
