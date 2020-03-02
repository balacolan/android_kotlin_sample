package com.colan.kindercare.ui.modules.admin.attendance.teacher


import android.app.TimePickerDialog
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
import com.colan.kindercare.data.local.sharedPreference.ISharedPreferenceService
import com.colan.kindercare.data.remote.data.Status
import com.colan.kindercare.data.remote.data.response.attendance.AttendanceResponse
import com.colan.kindercare.databinding.AttendanceListItemsBinding
import com.colan.kindercare.databinding.FragmentAdminTeacherAttendanceBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.modules.admin.attendance.AdminAttendanceVM
import com.colan.kindercare.ui.modules.parent.payment.IRadioListener
import com.colan.kindercare.utils.Constants
import com.colan.kindercare.utils.SchoolSelectionBroadCast
import com.colan.kindercare.utils.SchoolSelectionListner
import com.colan.kindercare.utils.Singleton.mIntentFilter
import kotlinx.android.synthetic.main.mark_absent_layout.view.*
import kotlinx.android.synthetic.main.signin_signout_absent_layout.view.*
import org.koin.android.ext.android.inject
import org.koin.core.inject
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class AdminTeacherAttendanceFragment :
    BaseFragment<FragmentAdminTeacherAttendanceBinding, AdminAttendanceVM>(),
    IRadioListener,
    OnDataBindCallback<AttendanceListItemsBinding>, SchoolSelectionListner {

    private var baseRecyclerAdapter: BaseRecyclerViewAdapter<AttendanceResponse, AttendanceListItemsBinding>? =
        null

    val iSharedPreferenceService: ISharedPreferenceService by inject()

    override val bindingVariable: Int
        get() = BR.teacherAttendanceVM
    override val layoutId: Int
        get() = R.layout.fragment_admin_teacher_attendance
    override val viewModel: AdminAttendanceVM
        get() = ViewModelProviders.of(this).get(AdminAttendanceVM::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        viewModel.loadAttedanceData(Constants.TEACHER_TYPE)
        observeResponse()
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
                            if (iSharedPreferenceService.getStringValue(Constants.USER_TYPE_ID) == "3") {
                                if (viewModel.iSharedPreferenceService.getStringValue(Constants.EDIT_STAFF_ATTENDANCE) == "0") {
                                    viewModel.attendanceList.forEach {
                                        it.mEDIT_STAFF_ATTENDANCE = true
                                    }
                                    viewDataBinding?.bottomLayout?.visibility = View.VISIBLE
                                    viewDataBinding?.bottomLayout2?.visibility = View.VISIBLE

                                } else if (viewModel.iSharedPreferenceService.getStringValue(
                                        Constants.EDIT_STAFF_ATTENDANCE
                                    ) == "1"
                                ) {
                                    viewModel.attendanceList.forEach {
                                        it.mEDIT_STAFF_ATTENDANCE = false
                                    }
                                    viewDataBinding?.bottomLayout?.visibility = View.GONE
                                    viewDataBinding?.bottomLayout2?.visibility = View.GONE
                                }
                            }
                            setUpRecyclerview(viewModel.attendanceList)
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

        viewModel.addTeacherAttendanceResponse.observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        viewModel.mShowProgressBar.value = true
                    }
                    Status.SUCCESS -> {
                        viewModel.mShowProgressBar.value = false
                        viewModel.attendanceList.clear()
                        viewModel.loadAttedanceData(Constants.TEACHER_TYPE)
                        putToast("Success")
                    }
                    Status.ERROR, Status.FAILURE -> {
                        viewModel.mShowProgressBar.value = false
                        putToast("" + it.data?.errors)
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
        viewDataBinding?.attendanceTeacherRv?.adapter = baseRecyclerAdapter
        baseRecyclerAdapter?.notifyDataSetChanged()
    }

    private fun setUpPresentAbsentClick() {
        viewDataBinding?.bottomLayout?.present_btn?.setOnClickListener {
            val selectedUserIdList = ArrayList<String>()
            for (Data in viewModel.attendanceList.filter { it.isSelected }) {
                selectedUserIdList.add(Data.userId.toString())
            }

            when {
                selectedUserIdList.size > 0 -> viewModel.doCallAddTeacherAttendanceAPI(
                    Constants.TEACHER_TYPE,
                    viewModel.iSharedPreferenceService.getStringValue(Constants.SCHOOL_ID),
                    Constants.LOG_PRESENT,
                    selectedUserIdList
                )
                else -> putToast(getString(R.string.please_select_checkbox))
            }


        }
        viewDataBinding?.bottomLayout?.mark_absent_btn?.setOnClickListener {
            val selectedUserIdList = ArrayList<String>()
            for (Data in viewModel.attendanceList.filter { it.isSelected }) {
                selectedUserIdList.add(Data.userId.toString())
            }

            when {
                selectedUserIdList.size > 0 -> viewModel.doCallAddTeacherAttendanceAPI(
                    Constants.TEACHER_TYPE,
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
            mTimePicker = TimePickerDialog(
                this.context,
                TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                    viewModel.signInTime.set(
                        "$selectedHour:$selectedMinute"
                    )
                    val selectedUserIdList = ArrayList<String>()
                    for (Data in viewModel.attendanceList.filter { it.isSelected }) {
                        selectedUserIdList.add(Data.userId.toString())
                    }

                    when {
                        selectedUserIdList.size > 0 -> viewModel.doCallAddTeacherAttendanceAPI(
                            Constants.TEACHER_TYPE,
                            viewModel.iSharedPreferenceService.getStringValue(Constants.SCHOOL_ID),
                            Constants.LOG_SIGN_IN,
                            selectedUserIdList
                        )

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
            mTimePicker = TimePickerDialog(
                this.context,
                TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                    viewModel.signInTime.set(
                        "$selectedHour:$selectedMinute"
                    )
                    val selectedUserIdList = ArrayList<String>()
                    for (Data in viewModel.attendanceList.filter { it.isSelected }) {
                        selectedUserIdList.add(Data.userId.toString())
                    }


                    when {
                        selectedUserIdList.size > 0 -> viewModel.doCallAddTeacherAttendanceAPI(
                            Constants.TEACHER_TYPE,
                            viewModel.iSharedPreferenceService.getStringValue(Constants.SCHOOL_ID),
                            Constants.LOG_SIGN_OUT,
                            selectedUserIdList
                        )


                        else -> putToast(getString(R.string.please_select_checkbox))
                    }

                }, hour, minute, true
            )//Yes 24 hour time
            mTimePicker.setTitle("Select Time")
            mTimePicker.show()
        }

        viewDataBinding?.bottomLayout2?.absent_btn_2?.setOnClickListener {
            val selectedUserIdList = ArrayList<String>()
            for (Data in viewModel.attendanceList.filter { it.isSelected }) {
                selectedUserIdList.add(Data.userId.toString())
            }

            when {
                selectedUserIdList.size > 0 -> viewModel.doCallAddTeacherAttendanceAPI(
                    Constants.TEACHER_TYPE,
                    viewModel.iSharedPreferenceService.getStringValue(Constants.SCHOOL_ID),
                    Constants.LOG_ABSCENT,
                    selectedUserIdList
                )

                else -> putToast(getString(R.string.please_select_checkbox))
            }
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpPresentAbsentClick()
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
        viewModel.loadAttedanceData(Constants.TEACHER_TYPE)
    }

    override fun onResume() {
        super.onResume()
        SchoolSelectionBroadCast.schoolLister = this
        context!!.registerReceiver(SchoolSelectionBroadCast(), mIntentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        context!!.unregisterReceiver(SchoolSelectionBroadCast())
    }


}
