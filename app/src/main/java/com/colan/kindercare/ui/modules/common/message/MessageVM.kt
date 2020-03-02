package com.colan.kindercare.ui.modules.common.message

import android.app.Application
import android.net.Uri
import android.view.View
import android.widget.RadioGroup
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.R
import com.colan.kindercare.data.local.sharedPreference.ISharedPreferenceService
import com.colan.kindercare.data.model.MessageModel
import com.colan.kindercare.data.model.PhotosModel
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.message.MessageListResponse
import com.colan.kindercare.data.remote.data.response.message.ViewMessageResponse
import com.colan.kindercare.data.repository.message.IMessageControllerRepository
import com.colan.kindercare.ui.base.BaseViewModel
import com.colan.kindercare.ui.modules.parent.payment.IRadioListener
import com.colan.kindercare.utils.Constants
import com.colan.kindercare.utils.SingleLiveData
import com.colan.kindercare.utils.Singleton
import com.colan.kindercare.utils.Singleton.sendToList
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.File

class MessageVM(application: Application) : BaseViewModel<IRadioListener>(application),
    KoinComponent {

    val mAllMsgRb = ObservableField(false)
    var selectedFileUriList = ArrayList<Uri>()
    var selectedFilePathList = ArrayList<String?>()
    var messageInboxModel = MutableLiveData<ArrayList<MessageModel>>()
    var messageSentModel = MutableLiveData<ArrayList<MessageModel>>()
    var messageInboxList = ArrayList<MessageModel>()
    var messageSentList = ArrayList<MessageModel>()
    var msgTpe = ObservableField("")
    var subject = ObservableField("")
    var composeMessage = ObservableField("")
    var email = ObservableField("")
    var name = ObservableField("")
    var type = ObservableField("")
    var date = ObservableField("")
    var fromInboxMessage = ObservableField(false)
    var isAttachmentAdded = ObservableField(false)
    var isInoboxClicked = ObservableField(false)
    var isSentClicked = ObservableField(false)
    var isDraftClicked = ObservableField(false)
    var isTrashClicked = ObservableField(false)
    var isComposeClicked = ObservableField(false)
    var fromDraftMessage = MutableLiveData(false)
    var isListEmpty = ObservableField(false)
    var phtosdModel = MutableLiveData<ArrayList<PhotosModel>>()
    private var photosList = ArrayList<PhotosModel>()
    var file = ArrayList<File?>()
    val mShowProgressBar = SingleLiveData<Boolean>()
    val iSharedPreferenceService: ISharedPreferenceService by inject()
    private val iMessageControllerRepository: IMessageControllerRepository by inject()

    var messageList = ArrayList<MessageListResponse>()
    val messageListResponse = MutableLiveData<Resource<BaseResponse<List<MessageListResponse>>>>()
    val viewMessageResponse = MutableLiveData<Resource<BaseResponse<List<ViewMessageResponse>>>>()
    val deleteMessageResponse = MutableLiveData<Resource<BaseResponse<Nothing>>>()
    val composeMessageResponse = MutableLiveData<Resource<BaseResponse<Nothing>>>()
    var getMessageListApiJob: Job? = null
    var isParent = ObservableField(false)
    var isteacher = ObservableField(false)
    var isAdmin = ObservableField(false)
    var isSuperAdmin = ObservableField(true)
    var unReadMsgCount = ObservableField("0")
    var profile = ObservableField("")
    init {

        setUpUserType()
        messageInboxModel.value = loadInboxMessages()
        messageSentModel.value = loadSentMessages()
        phtosdModel.value = loadPhotosData()
        mAllMsgRb.set(true)
        isInoboxClicked.set(true)
    }

    fun loadMessageList(listBy: String, msgTpe: String) {
        GlobalScope.launch {
            getMessageListApiJob = async(Dispatchers.IO) {
                iMessageControllerRepository.getMessageList(
                    iSharedPreferenceService.getStringValue(
                        Constants.SCHOOL_ID
                    ), msgTpe, listBy, messageListResponse
                )
            }
        }
    }

    private fun loadInboxMessages(): ArrayList<MessageModel>? {
        val model1 = MessageModel(
            1,
            0,
            "Chrish Hemsworth",
            "Super Admin",
            "10.25AM",
            "Re: Class Room Activities",
            true,
            false
        )
        val model2 = MessageModel(
            2,
            0,
            "Hemsworth",
            "Super Admin",
            "Yesterday",
            "Re: Class Room Activities",
            false,
            true
        )
        val model3 = MessageModel(
            1,
            0,
            "Chrish",
            "Super Admin",
            "Yesterday",
            "Re: Class Room Activities",
            false,
            false
        )
        val model4 = MessageModel(
            1,
            0,
            "Monoj",
            "Super Admin",
            "Yesterday",
            "Re: Class Room Activities",
            false,
            true
        )
        val model5 = MessageModel(
            1,
            0,
            "Chrish",
            "Super Admin",
            "Yesterday",
            "Re: Class Room Activities",
            false,
            true
        )
        val model6 = MessageModel(
            1,
            0,
            "Imran",
            "Super Admin",
            "Yesterday",
            "Re: Class Room Activities",
            false,
            true
        )
        val model7 = MessageModel(
            1,
            0,
            "Chrish Hemsworth",
            "Super Admin",
            "Yesterday",
            "Re: Class Room Activities",
            false,
            true
        )
        val model8 = MessageModel(
            1,
            0,
            "Chrish Hemsworth",
            "Super Admin",
            "Yesterday",
            "Re: Class Room Activities",
            false,
            true
        )

        messageInboxList.add(model1)
        messageInboxList.add(model2)
        messageInboxList.add(model3)
        messageInboxList.add(model4)
        messageInboxList.add(model5)
        messageInboxList.add(model6)
        messageInboxList.add(model7)
        messageInboxList.add(model8)
        return messageInboxList
    }

    private fun loadSentMessages(): ArrayList<MessageModel>? {
        val model1 = MessageModel(
            1,
            0,
            "Chrish Hemsworth",
            "Super Admin",
            "10.25AM",
            "Re: Class Room Activities",
            true,
            true
        )
        val model2 = MessageModel(
            2,
            0,
            "Hemsworth",
            "Super Admin",
            "Yesterday",
            "Re: Class Room Activities",
            false,
            true
        )
        val model3 = MessageModel(
            1,
            0,
            "Chrish",
            "Super Admin",
            "Yesterday",
            "Re: Class Room Activities",
            false,
            true
        )
        val model4 = MessageModel(
            1,
            0,
            "Monoj",
            "Super Admin",
            "Yesterday",
            "Re: Class Room Activities",
            false,
            true
        )
        val model5 = MessageModel(
            1,
            0,
            "Chrish",
            "Super Admin",
            "Yesterday",
            "Re: Class Room Activities",
            false,
            true
        )
        val model6 = MessageModel(
            1,
            0,
            "Imran",
            "Super Admin",
            "Yesterday",
            "Re: Class Room Activities",
            false,
            true
        )
        val model7 = MessageModel(
            1,
            0,
            "Chrish Hemsworth",
            "Super Admin",
            "Yesterday",
            "Re: Class Room Activities",
            false,
            true
        )
        val model8 = MessageModel(
            1,
            0,
            "Chrish Hemsworth",
            "Super Admin",
            "Yesterday",
            "Re: Class Room Activities",
            false,
            true
        )

        messageSentList.add(model1)
        messageSentList.add(model2)
        messageSentList.add(model3)
        messageSentList.add(model4)
        messageSentList.add(model5)
        messageSentList.add(model6)
        messageSentList.add(model7)
        messageSentList.add(model8)
        return messageSentList
    }


    private fun loadPhotosData(): ArrayList<PhotosModel>? {
        val model1 = PhotosModel(1, R.drawable.test_pic1)
        val model2 = PhotosModel(2, R.drawable.test_pic2)
        val model3 = PhotosModel(3, R.drawable.test_pic3)
        photosList.add(model1)
        photosList.add(model2)
        photosList.add(model3)
        return photosList
    }


    fun onClickAction(view: View?) {
        getNavigator().onClickView(view)
    }

    fun onRbChanged(button: RadioGroup, id: Int) {
        getNavigator().onRadioButtonClick(id)
    }

    fun onClickInbox(view: View?) {
        isInoboxClicked.set(true)
        isSentClicked.set(false)
        isDraftClicked.set(false)
        isTrashClicked.set(false)
        isComposeClicked.set(false)
        getNavigator().onClickView(view)
    }

    fun onClickSent(view: View?) {
        isInoboxClicked.set(false)
        isSentClicked.set(true)
        isDraftClicked.set(false)
        isTrashClicked.set(false)
        isComposeClicked.set(false)
        getNavigator().onClickView(view)
    }

    fun onClickDraft(view: View?) {
        isInoboxClicked.set(false)
        isSentClicked.set(false)
        isDraftClicked.set(true)
        isTrashClicked.set(false)
        isComposeClicked.set(false)
        getNavigator().onClickView(view)
    }

    fun onClickTrash(view: View?) {
        isInoboxClicked.set(false)
        isSentClicked.set(false)
        isDraftClicked.set(false)
        isTrashClicked.set(true)
        isComposeClicked.set(false)
        getNavigator().onClickView(view)
    }

    fun onClickCompose(view: View?) {
        /*isInoboxClicked.set(false)
        isSentClicked.set(false)
        isDraftClicked.set(false)
        isTrashClicked.set(false)
        isComposeClicked.set(true)*/
        getNavigator().onClickView(view)
    }

    fun doCallComposeMessageAPI(msgAction: String) {
        if (validateCredentials()) {
            iMessageControllerRepository.composeMessage(
                sendToList, file, subject.get().toString(), composeMessage.get().toString(),
                iSharedPreferenceService.getStringValue(Constants.SCHOOL_ID),
                Singleton.userListId, msgAction, "0", composeMessageResponse
            )
        }
    }

    fun doCallDraftComposeMessageAPI(msgAction: String) {
        if (validateCredentials()) {
            iMessageControllerRepository.composeMessage(
                sendToList, null, subject.get().toString(), composeMessage.get().toString(),
                iSharedPreferenceService.getStringValue(Constants.SCHOOL_ID),
                Singleton.userListId, msgAction, "0", composeMessageResponse
            )
        }
    }


    private fun validateCredentials(): Boolean {
        when {
            sendToList.isEmpty() -> putToast("Please select the recipient")
            subject.get()!!.isEmpty() -> putToast("Please enter the subject")
            composeMessage.get()!!.isEmpty() -> putToast("please write the message")
            Singleton.userListId.isEmpty() -> putToast("please Add the user")
            else -> return true
        }
        return false
    }

    private fun setUpUserType() {
        when {
            iSharedPreferenceService.getStringValue(Constants.USER_TYPE_ID) == "2" -> {
                isSuperAdmin = ObservableField(true)
                isAdmin = ObservableField(false)
                isteacher = ObservableField(false)
                isParent = ObservableField(false)
            }
            iSharedPreferenceService.getStringValue(Constants.USER_TYPE_ID) == "3" -> {
                isSuperAdmin = ObservableField(false)
                isAdmin = ObservableField(true)
                isteacher = ObservableField(false)
                isParent = ObservableField(false)
            }
            iSharedPreferenceService.getStringValue(Constants.USER_TYPE_ID) == "4" -> {
                isSuperAdmin = ObservableField(false)
                isAdmin = ObservableField(false)
                isteacher = ObservableField(true)
                isParent = ObservableField(false)
            }
            iSharedPreferenceService.getStringValue(Constants.USER_TYPE_ID) == "5" -> {
                isSuperAdmin = ObservableField(false)
                isAdmin = ObservableField(false)
                isteacher = ObservableField(false)
                isParent = ObservableField(true)
            }
        }
    }

    fun doCallViewMessageAPI(msgId: String) {
        GlobalScope.launch {
            getMessageListApiJob = async(Dispatchers.IO) {
                iMessageControllerRepository.viewMessage(
                    iSharedPreferenceService.getStringValue(
                        Constants.SCHOOL_ID
                    ), msgId, viewMessageResponse
                )
            }
        }
    }

    fun doCallDeleteMessageAPI(msgId: String) {
        GlobalScope.launch {
            getMessageListApiJob = async(Dispatchers.IO) {
                iMessageControllerRepository.deleteMessage(
                    msgId,
                    msgTpe.get().toString(),
                    deleteMessageResponse
                )
            }
        }

    }


}