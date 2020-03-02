package com.colan.kindercare.ui.modules.common.profile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.data.local.sharedPreference.ISharedPreferenceService
import com.colan.kindercare.data.remote.data.Status
import com.colan.kindercare.databinding.ActivityEditProfileBinding
import com.colan.kindercare.ui.base.BaseActivity
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.before_login.resetPassword.PasswordResetActivity
import com.colan.kindercare.utils.Constants
import com.colan.kindercare.utils.createpicdirectory
import com.colan.kindercare.utils.getUniqueImageFilename
import com.colan.kindercare.utils.showPermissionDialogBox
import com.google.android.material.snackbar.Snackbar
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_request_leave.*
import kotlinx.android.synthetic.main.custom_toolbar_edit_profile.view.*
import org.apache.commons.io.FileUtils
import org.koin.android.ext.android.inject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class EditProfileActivity : BaseActivity<ActivityEditProfileBinding,ProfileVM>(),BaseNavigator {
    private val sharedPreferences: ISharedPreferenceService by inject()

    override fun getBindingVariable(): Int {
        return BR.profileVM
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_edit_profile
    }

    override fun getViewModel(): ProfileVM {
        return ViewModelProviders.of(this).get(ProfileVM::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel?.setNavigator(this)
        mViewModel?.setUpUserCredentials()
        getViewDataBinding().toolbar.titleToolbar.text=getString(R.string.edit_profile)
        getViewDataBinding().toolbar.navIconIv.setOnClickListener { onBackPressed()}
        getViewDataBinding().toolbar.cameraIv.setOnClickListener { choosePrfilePicture() }
        observeResponse()
        setUpProfileImage()
    }

    private fun setUpProfileImage() {
        when {
            sharedPreferences.getStringValue(Constants.PROFILE_IMAGE).isNotEmpty() -> Glide.with(this)
                .asBitmap().load(sharedPreferences.getStringValue(Constants.PROFILE_IMAGE))
                .placeholder(R.drawable.ic_profile_pic)
                .into(getViewDataBinding().toolbar.profileChildIv)
        }

    }

    private fun observeResponse() {
        mViewModel?.mShowProgressBar?.observeEvent(this, androidx.lifecycle.Observer {
            when {
                it -> showLoadingIndicator()
                else -> dismissLoadingIndicator()
            }
        })

        mViewModel?.updateProfileResponse?.observe(this,androidx.lifecycle.Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        mViewModel?.mShowProgressBar?.value = true
                    }
                    Status.SUCCESS -> {
                        try {
                            if (it.data!=null) {
                                mViewModel?.mShowProgressBar?.value = false
                                Toast.makeText(this, "Profile Picture Updated", Toast.LENGTH_SHORT).show()
                            }else {
                                mViewModel?.mShowProgressBar?.value = false
                                Toast.makeText(this, "Error getting User Information", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } catch (e: java.lang.Exception) {
                            mViewModel?.mShowProgressBar?.value = false
                            Toast.makeText(this, "" + e.message, Toast.LENGTH_SHORT).show()
                        }

                    }
                    Status.ERROR, Status.FAILURE -> {
                        mViewModel?.mShowProgressBar?.value = false
                        putToast(""+it.message)
                    }
                }

            }
        })

        mViewModel?.updateProfileImageResponse?.observe(this,androidx.lifecycle.Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        mViewModel?.mShowProgressBar?.value = true
                    }
                    Status.SUCCESS -> {
                        try {
                            if (it.data!=null) {
                                mViewModel?.mShowProgressBar?.value = false
                                Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()
                                //finish()
                            }else {
                                mViewModel?.mShowProgressBar?.value = false
                                Toast.makeText(this, "Error getting User Information", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } catch (e: java.lang.Exception) {
                            mViewModel?.mShowProgressBar?.value = false
                            Toast.makeText(this, "" + e.message, Toast.LENGTH_SHORT).show()
                        }

                    }
                    Status.ERROR, Status.FAILURE -> {
                        mViewModel?.mShowProgressBar?.value = false
                        putToast(""+it.message)
                    }
                }

            }
        })
    }


    var mYear: Int = 0
    var mMonth: Int = 0
    var mDay: Int = 0

    fun datepicker(){

        // Get Current Date
        val c = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                c.set(year,monthOfYear,dayOfMonth)
                val format =  SimpleDateFormat("yyyy-MM-dd");
                val strDate = format.format(c.getTime());
                reave_for_leave_et.setText(
                    strDate
                )
            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()

    }


    override fun onClickView(v: View?) {
        when(v?.id) {
            R.id.reave_for_leave_et -> {
                datepicker()
            }
            R.id.submit_btn->{
                mViewModel?.doCallUpdateProfileAPI()
            }
            R.id.cancel_btn -> {
                onBackPressed()
            }


        }
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
    }

    @Suppress("DEPRECATED_IDENTITY_EQUALS")
    fun choosePrfilePicture() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) !== PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) !== PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) || ActivityCompat.shouldShowRequestPermissionRationale(
                        this, Manifest.permission.CAMERA
                    )
                ) {
                    Snackbar.make(
                        this.findViewById(android.R.id.content),
                        "Please grant the Camera Permission",
                        Snackbar.LENGTH_LONG
                    ).setAction(
                        "OK"
                    ) {
                        this.requestPermissions(
                            arrayOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                            ),
                           PERMISSIONS_WRITE_EXTERNAL_STORAGE
                        )
                    }.show()
                } else {
                    this.requestPermissions(
                        arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                        ),
                        PERMISSIONS_WRITE_EXTERNAL_STORAGE
                    )
                }
            } else
                openImageIntent()
        } else {
            openImageIntent()
        }
    }


    @SuppressLint("IntentReset")
    private fun openImageIntent() {
        val fname = getUniqueImageFilename()
        mViewModel?.sdImageMainDirectory = File(createpicdirectory(), fname)
        try {
            mViewModel?.sdImageMainDirectory!!.createNewFile()
        } catch (e: IOException) {
            Log.d("printStackTrace", e.message, e)
        }
        mViewModel?.outputFileUri = Uri.fromFile(mViewModel?.sdImageMainDirectory)
        val cameraIntents = ArrayList<Intent>()
        val captureIntent = Intent("android.media.action.IMAGE_CAPTURE")
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mViewModel?.outputFileUri)
        val packageManager = this.packageManager
        val listCam = packageManager.queryIntentActivities(captureIntent, 0)
        for (res in listCam) {
            val packageName = res.activityInfo.packageName
            val intent = Intent(captureIntent)
            intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
            intent.setPackage(packageName)
            intent.putExtra("android.intent.extras.CAMERA_FACING", 1)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mViewModel?.outputFileUri)
            cameraIntents.add(intent)
        }
        @SuppressLint("IntentReset") val galleryIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "image/*"
        val chooserIntent = Intent.createChooser(galleryIntent, "Select Source")
        chooserIntent.putExtra(
            Intent.EXTRA_INITIAL_INTENTS,
            cameraIntents.toTypedArray<Parcelable>()
        )
        this.startActivityForResult(
            chooserIntent,
            CAMERA_REQUEST_CODE
        )
    }

    private fun performCrop(selectedImageUri: Uri) {
        CropImage.activity(selectedImageUri)
            .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
            .setAspectRatio(1, 1)
            .setFixAspectRatio(true)
            .start(this)
    }


    override fun onActivityResult(requestCode: Int, resuleCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resuleCode, data)
        var selectedImageUri: Uri?
        if (resuleCode != Activity.RESULT_CANCELED) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                if (resuleCode == Activity.RESULT_OK) {
                    val isCamera: Boolean
                    isCamera = if (data == null) {
                        true
                    } else {
                        val action = data.action
                        if (action == null) {
                            false
                        } else {
                            action == MediaStore.ACTION_IMAGE_CAPTURE
                        }
                    }
                    selectedImageUri = if (isCamera) {
                        mViewModel?.outputFileUri
                    } else {
                        data!!.data
                    }
                    if (selectedImageUri != null) {
                        performCrop(selectedImageUri)
                    } else {
                        selectedImageUri = Uri.fromFile(mViewModel?.sdImageMainDirectory)
                        performCrop(selectedImageUri)
                    }
                }
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resuleCode == Activity.RESULT_OK) {
                mViewModel?.resultUri = result.uri
                // get the cropped bitmap
                val cropImageDirectory = File(mViewModel?.resultUri!!.path)
                try {
                    cropImageDirectory.createNewFile()
                } catch (e: IOException) {
                    Log.d("printStackTrace", e.message, e)
                }

                val root = File(
                    (Environment.getExternalStorageDirectory()).toString() + File.separator + Constants.DIRECTORY_NAME +
                            File.separator
                )
                if (root.isDirectory) {
                    try {
                        FileUtils.copyFileToDirectory(cropImageDirectory, root)
                    } catch (e: IOException) {
                        Log.d("printStackTrace", e.message, e)
                    }

                    if (File(root.toString() + File.separator + cropImageDirectory.name).isFile) {
                        mViewModel?.outputFileUri =
                            Uri.fromFile(File(root.toString() + File.separator + cropImageDirectory.name))
                        mViewModel?.file =
                            File(root.toString() + File.separator + cropImageDirectory.name)
                    }
                    if (File(mViewModel?.outputFileUri?.path).isFile) {
                        mViewModel?.currentPicture = File(mViewModel?.outputFileUri?.path).name

                        Log.d(
                            "SysOutPrintln",
                            "created sucessfully" + File(mViewModel?.outputFileUri?.path).isFile
                        )
                    }

                    mViewModel?.profilePicture?.set(mViewModel?.resultUri.toString())
                    var selectedImageBitmap: Bitmap? = null
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            val source: ImageDecoder.Source = ImageDecoder.createSource(this.getContentResolver(), mViewModel?.resultUri!!)
                            selectedImageBitmap = ImageDecoder.decodeBitmap(source);
                        }else{
                            selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                                this.contentResolver,
                                mViewModel?.resultUri
                            )
                        }

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    selectedImageBitmap?.compress(
                        Bitmap.CompressFormat.JPEG,
                        100,
                        byteArrayOutputStream
                    )
                    val byteArrayImage: ByteArray = byteArrayOutputStream.toByteArray()

                    Glide.with(this)
                        .asBitmap().load(mViewModel?.resultUri.toString())
                        .placeholder(R.drawable.ic_profile_pic)
                        .into(getViewDataBinding().toolbar.profileChildIv)

                    mViewModel?.doCallUpdateProfilePictureAPI()
                }
            }/* else if (resuleCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //Exception error = result.getError();
            }*/
            else if (resuleCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        if (requestCode == CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CropImage.startPickImageActivity(this)
            } else {
                Toast.makeText(
                    this,
                    "Cancelling, required permissions are not granted",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        if (requestCode == PERMISSIONS_WRITE_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                val cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED
                val readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (cameraPermission && readExternalFile) {
                    openImageIntent()
                }
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED) {
                var showRationale = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    showRationale =
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                }
                if (!showRationale) {
                    showPermissionDialogBox(
                        this,
                        "Permission",
                        "You need to Grant the Camera Permissions"
                    )
                } else {
                    Snackbar.make(
                        this.findViewById(android.R.id.content),
                        "You need to Grant the Camera Permissions",
                        Snackbar.LENGTH_LONG
                    ).setAction(
                        "OK"
                    ) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            this.requestPermissions(
                                arrayOf(
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA
                                ),
                               PERMISSIONS_WRITE_EXTERNAL_STORAGE
                            )
                        }
                    }.show()
                }
            }
        }
    }

    companion object {
        const val PERMISSIONS_WRITE_EXTERNAL_STORAGE = 100
        const val CAMERA_REQUEST_CODE = 0
    }

}