package com.colan.kindercare.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.colan.kindercare.KinderApplication
import com.colan.kindercare.R
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


const val EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
private val PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})"
//private val POSTCODE="^[1-9][0-9]{3}\$"
private val POSTCODE="^(0[289][0-9]{2})|([1-9][0-9]{3})\$"
private val MOBILE="((\\+*)((0[ -]+)*|(91 )*)(\\d{12}+|\\d{10}+))|\\d{5}([- ]*)\\d{6}"

fun validateEmail(email: String): Boolean {
    val pattern: Pattern = Pattern.compile(EMAIL_PATTERN)
    val matcher: Matcher
    matcher = pattern.matcher(email)
    return matcher.matches()
}

fun validateMobile(mobile: String): Boolean {
    val pattern: Pattern = Pattern.compile(MOBILE)
    val matcher: Matcher
    matcher = pattern.matcher(mobile)
    return matcher.matches()
}

fun validatePostalCode(code: String): Boolean {
    val pattern: Pattern = Pattern.compile(POSTCODE)
    val matcher: Matcher
    matcher = pattern.matcher(code)
    return matcher.matches()
}

fun getUniqueImageFilename(): String {
    return "img_" + System.currentTimeMillis() + ".jpg"
}

fun createpicdirectory(): File {
    val root =
        File(Environment.getExternalStorageDirectory().toString() + File.separator + Constants.DIRECTORY_NAME + File.separator)
    root.mkdirs()
    return root

}

fun showPermissionDialogBox(context: Context,title: String, message: String) {
    val builder = AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setPositiveButton("OK",
        { _, _ -> context.startActivity(Intent(Settings.ACTION_SETTINGS)) })
    builder.setNegativeButton("Cancel",
         { dialog, _ -> dialog.dismiss() })
    builder.show()
}

fun capitalize(line: String): String {
    return Character.toUpperCase(line[0]) + line.substring(1)
}
fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun View.ShowSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

fun Activity.logd(message: String) {
    //if (BuildConfig.DEBUG)
    Log.d(this::class.java.simpleName, message)
}

fun getAge(year: Int, month: Int, day: Int): String {
    val dob = Calendar.getInstance()
    val today = Calendar.getInstance()
    dob.set(year, month, day)
    var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
    if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
        age--
    }
    val ageInt = age
    return ageInt.toString()
}

var bundle = Bundle()

fun navigateTo(context: Context, clazz: Class<*>, mExtras: Bundle?) {
    val intent = Intent(context, clazz)
    if (mExtras != null) {
        intent.putExtras(mExtras)
    }
    context.startActivity(intent)
}


fun navigateWithClearTop(context: Context, clazz: Class<*>, mExtras: Bundle) {
    val intent = Intent(context, clazz)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
    intent.putExtras(mExtras)
    context.startActivity(intent)
    ActivityCompat.finishAffinity(context as Activity)
}


fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}

/**
 * Get next date from current selected date
 *
 * @param date date
 */
fun incrementDateByOne(date: Date): Date {
    val c = Calendar.getInstance()
    c.time = date
    c.add(Calendar.DATE, 1)
    return c.time
}

/**
 * Get previous date from current selected date
 *
 * @param date date
 */
fun decrementDateByOne(date: Date): Date {
    val c = Calendar.getInstance()
    c.time = date
    c.add(Calendar.DATE, -1)
    return c.time
}

fun dateToStringCustom(dateString: String):String{
    val src = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val dest = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    var date: Date? = null
    try {
        date = src.parse(dateString)
    } catch (e: ParseException) {
        Log.d("Exception", ""+e.message)
    }

    return dest.format(date);
}



fun StringToDateConvert(dateString:String):Date{
    val dateFormat = SimpleDateFormat("dd MMMM yyyy")
    var convertedDate: Date? = Date()
    try {
        convertedDate = dateFormat.parse(dateString)
    } catch (e: ParseException) {
        // TODO Auto-generated catch block
        e.printStackTrace()
    }
return convertedDate!!
}


fun dateCustom(dateString: String,pattern:String):String{
    val src = SimpleDateFormat(pattern, Locale.getDefault())
    val dest = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    var date: Date? = null
    try {
        date = src.parse(dateString)
    } catch (e: ParseException) {
        Log.d("Exception", ""+e.message)
    }

    return dest.format(date)
}


fun dateToTimeStringCustom(dateString: String):String{
    val src = SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.getDefault())
    val dest = SimpleDateFormat("hh:mm a", Locale.getDefault())
    var date: Date? = null
    try {
        date = src.parse(dateString)
    } catch (e: ParseException) {
        Log.d("Exception", ""+e.message)
    }

    return dest.format(date);
}

fun dateToTimeStringCustom2(dateString: String):String{
    val src = SimpleDateFormat("hh:mm a dd MMM yyyy", Locale.getDefault())
    val dest = SimpleDateFormat("hh:mm a dd", Locale.getDefault())
    var date: Date? = null
    try {
        date = src.parse(dateString)
    } catch (e: ParseException) {
        Log.d("Exception", ""+e.message)
    }

    return dest.format(date);
}

fun dateToFromDateStringCustom(dateString: String):String{
    //val src = SimpleDateFormat("hh:mm a dd MMM yyyy", Locale.getDefault())
    val src = SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.getDefault())
    val dest = SimpleDateFormat("dd MMM", Locale.getDefault())
    var date: Date? = null
    try {
        date = src.parse(dateString)
    } catch (e: ParseException) {
        Log.d("Exception", ""+e.message)
    }

    return dest.format(date);
}
fun dateYearDateStringCustom(dateString: String):String{
    //val src = SimpleDateFormat("hh:mm a dd MMM yyyy", Locale.getDefault())
    val src = SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.getDefault())
    val dest = SimpleDateFormat("yyyy", Locale.getDefault())
    var date: Date? = null
    try {
        date = src.parse(dateString)
    } catch (e: ParseException) {
        Log.d("Exception", ""+e.message)
    }

    return dest.format(date);
}
fun dateFromToStringCustom(dateString: String):String{
    //val src = SimpleDateFormat("hh:mm a dd MMM yyyy", Locale.getDefault())
    val src = SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.getDefault())
    val dest = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    var date: Date? = null
    try {
        date = src.parse(dateString)
    } catch (e: ParseException) {
        Log.d("Exception", ""+e.message)
    }

    return dest.format(date);
}

fun getStatusTypeByName(statusType: String): String? {
    val applicaton : KinderApplication? = null
    KinderApplication.appContext
    when (statusType) {
        Constants.PENDING_LEAVE_STATUS -> {
            return  KinderApplication.appContext.resources?.getString(R.string.pending)
        }
        Constants.APPROVED_LEAVE_STATUS -> {
            return KinderApplication.appContext.resources?.getString(R.string.approved)
        }
        Constants.REJECTED_LEAVE_STATUS -> {
            return KinderApplication.appContext.resources?.getString(R.string.rejected)
        }
    }
    return ""
}


fun getRealPathFromURI(context:Context,contentURI: Uri): String {
    val result: String
    val cursor = context.getContentResolver().query(contentURI, null, null, null, null)
    if (cursor == null) { // Source is Dropbox or other similar local file path
        result = contentURI.getPath()!!
    } else {
        cursor.moveToFirst()
        val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        result = cursor.getString(idx)
        cursor.close()
    }
    return result
}


fun getClass(data:Int):String{
    var className=""
    when(data){

        1->{
            className="LKG"
        }
        2->{
            className="UKG"
        }
        3->{
            className="PRE KG"
        }
    }
    return  className
}