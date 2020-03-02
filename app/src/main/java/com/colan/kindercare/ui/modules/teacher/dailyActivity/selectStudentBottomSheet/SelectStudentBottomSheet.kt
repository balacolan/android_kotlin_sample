package com.colan.kindercare.ui.modules.teacher.dailyActivity.selectStudentBottomSheet


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.adapter.BaseRecyclerViewAdapter
import com.colan.kindercare.adapter.OnDataBindCallback
import com.colan.kindercare.data.local.sharedPreference.ISharedPreferenceService
import com.colan.kindercare.data.model.SelectStudentModel
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.Status
import com.colan.kindercare.data.remote.data.response.message.UserListResponse
import com.colan.kindercare.data.repository.message.IMessageControllerRepository
import com.colan.kindercare.databinding.SelectStudentBtmItemBinding
import com.colan.kindercare.utils.Constants
import com.colan.kindercare.utils.SingleLiveData
import com.colan.kindercare.utils.Singleton.sendToList
import com.colan.kindercare.utils.Singleton.userListEmail
import com.colan.kindercare.utils.Singleton.userListId
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_select_student_bottom_sheet.*
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import org.koin.core.inject


/**
 * A simple [Fragment] subclass.
 */
class SelectStudentBottomSheet(val clickListener: OnUserSlectedListener) : BottomSheetDialogFragment(),OnDataBindCallback<SelectStudentBtmItemBinding>,KoinComponent {


    private var baseRecyclerAdapter: BaseRecyclerViewAdapter<UserListResponse, SelectStudentBtmItemBinding>? = null

    private var studentList = ArrayList<SelectStudentModel>()

    val iSharedPreferenceService: ISharedPreferenceService by inject()

    private val iMessageControllerRepository: IMessageControllerRepository by inject()

    var userList = ArrayList<UserListResponse>()

    val userListResponse = MutableLiveData<Resource<BaseResponse<List<UserListResponse>>>>()

    var getUserListApiJob: Job? = null

    val mShowProgressBar = SingleLiveData<Boolean>()

    private var sheetBehavior: BottomSheetBehavior<*>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style. AppBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v= inflater.inflate(R.layout.fragment_select_student_bottom_sheet, container, false)
        val cancel=v.findViewById<AppCompatImageView>(R.id.close_label)
        //sheetBehavior = BottomSheetBehavior.from(bottom_sheet_layout);
        //sheetBehavior.setPeekHeight(R.dimen.peek_height);//put this ub dimens.xml (300dp)
        isCancelable = false
        cancel.setOnClickListener {
            dismiss()
        }
        return v
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerview(userList)
        observeResponse()
    }


    private fun observeResponse() {

        seacrch_student_et.doAfterTextChanged {
            Log.d("search_text",""+seacrch_student_et.text.toString())
            Log.d("send_to",""+sendToList.size)
            GlobalScope.launch {
                getUserListApiJob = async(Dispatchers.IO) {
                    iMessageControllerRepository.getUserList(iSharedPreferenceService.getStringValue(Constants.SCHOOL_ID),
                        seacrch_student_et.text.toString(),sendToList,userListResponse
                    )
                }
            }
        }


       /* mShowProgressBar.observeEvent(this, Observer {
            when {
                it -> showLoadingIndicator()
                else -> dismissLoadingIndicator()
            }
        })*/


        userListResponse.observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                       mShowProgressBar.value=true
                    }
                    Status.SUCCESS -> {
                       mShowProgressBar.value=false
                        try {
                            getUserListApiJob?.cancel()
                            userList.clear()
                            sendToList.forEach {it1->
                                Log.d("forEach",""+it1)
                                userList.add(UserListResponse(getHeaderById(it1)))
                                it.data?.data?.forEach {
                                    Log.d("detail",""+it.detail?.substringAfterLast("-"))
                                      if(it.detail?.substringAfterLast("-")==getHeaderById(it1)){
                                          userList.add(UserListResponse(it.id,it.detail?.substringBefore("-"),it.isSelected))
                                      }


                                }
                            }
                            //it.data?.data?.let { it1 -> userList.addAll(it1) }
                            setUpRecyclerview(userList)
                        }catch (e: Exception) {
                            Log.i("catch Error", ""+e.message)
                        }
                    }
                    Status.ERROR, Status.FAILURE -> {
                        userList.clear()
                       mShowProgressBar.value=false
                    }
                }

            }
        })

        select_all_tv.setOnClickListener {
         userList.forEach {
                it.isSelected=true
                it.detail?.let { it1 -> userListEmail.add(it1) }
            }
            setUpRecyclerview(userList)
        }

        btnSubmit.setOnClickListener {
            userListId.clear()
            userListEmail.clear()
            userList.filter { f->f.isSelected==true }.forEach {
                userListId.add(it.id.toString())
                it.detail?.let { it1 -> userListEmail.add(it1) }
            }
            clickListener.selectedUserList(userListEmail)
            dismiss()
        }
    }

    private fun getHeaderById(it: String): String {
        when (it) {
            "3" -> {
                return Constants.ADMIN
            }
            "4" -> {
                return Constants.TEACHER
            }
            "6" -> {
                return Constants.PARENT
            }
        }

        return "SUPER ADMIN"
    }

    private fun setUpRecyclerview(userList: ArrayList<UserListResponse>) {
        Log.d("userList",""+userList.size)
        baseRecyclerAdapter = BaseRecyclerViewAdapter(
            R.layout.select_student_btm_item,
            BR.selectStudentItem,
            userList,
            null,
            this
        )
        selected_students_rv.adapter= baseRecyclerAdapter
       // baseRecyclerAdapter?.addDataSet(userList)
        baseRecyclerAdapter?.notifyDataSetChanged()
    }

    override fun onItemClick(view: View, position: Int, v: SelectStudentBtmItemBinding) {
        if (view == v.selectStudentCb) {
            userList[position].isSelected = v.selectStudentCb.isChecked
        }
    }

    override fun onDataBind(v: SelectStudentBtmItemBinding, onClickListener: View.OnClickListener) {
        v.selectStudentCb.setOnClickListener(onClickListener)
    }

    interface OnUserSlectedListener{
        fun selectedUserList(userList: ArrayList<String>)
    }
}
