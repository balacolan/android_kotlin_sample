package com.colan.kindercare.ui.base

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.colan.kindercare.R
import com.colan.kindercare.utils.LoadingIndicatorView
import com.colan.kindercare.utils.SchoolSelectionBroadCast.Companion.schoolLister

import java.io.File
import java.text.NumberFormat

abstract class BaseFragment<T : ViewDataBinding, out V : BaseViewModel<*>> : androidx.fragment.app.Fragment() {
    var baseActivity: BaseActivity<*, *>? = null
        private set
    var mRootView: View? = null
    private var mRootViewGrp: ViewGroup? = null
    var viewDataBinding: T? = null
        private set
    private var mViewModel: V? = null

    private var progressBar: ProgressBar? = null
    private var mToast:Toast ?=null
    private var loadingIndicatorView: LoadingIndicatorView? = null
    var primaryColor = ObservableField(Color.parseColor("#20B8AB"))
    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract val bindingVariable: Int

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutId: Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract val viewModel: V

    val isNetworkConnected: Boolean
        get() = baseActivity != null && baseActivity!!.isNetworkConnected()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            val activity = context as BaseActivity<*, *>?
            this.baseActivity = activity
            //activity!!.onFragmentAttached()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = viewModel
        setHasOptionsMenu(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        mRootView = viewDataBinding!!.root
        mRootViewGrp = container
        initLoadingIndicator()
        return mRootView
    }

    override fun onDetach() {
        hideKeyboard()
        //dismissProgressBar()
        baseActivity = null
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding!!.setVariable(bindingVariable, mViewModel)
        viewDataBinding!!.executePendingBindings()
    }

    fun hideKeyboard() {
        if (baseActivity != null) {
            baseActivity!!.hideKeyboard()
        }
    }

    fun openActivityOnTokenExpire() {
        if (baseActivity != null) {
            //mActivity.openActivityOnTokenExpire();
        }
    }

    interface Callback {

        fun onFragmentAttached()

        fun onFragmentDetached(tag: String)
    }

    /**
     * common toast show for all screens
     *
     */
    fun putToast(message: String?) {
        if (mToast != null) mToast?.cancel();
        mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        mToast?.show();
    }



    /*function:  Loading Indicator*/
    fun initLoadingIndicator() {
        try {
            loadingIndicatorView = LoadingIndicatorView(context!!)
            loadingIndicatorView?.setColor(
                String.format(
                    "#%06X",
                    0xFFFFFF and primaryColor.get()!!
                )
            )
        } catch (e: Exception) {
        }
    }

    fun showLoadingIndicator() {
        try {
            if (!loadingIndicatorView?.isShowing!!) {
                loadingIndicatorView?.show()
            }
        } catch (e: Exception) {
        }
    }

    fun dismissLoadingIndicator() {
        try {
            if (loadingIndicatorView?.isShowing!!) {
                loadingIndicatorView?.dismiss()
            }
        } catch (e: Exception) {
        }
    }

    /**
     * common show progress bar for all screens
     *
     * @param parent layout of screen
     */
    fun showProgressBar(parent: ViewGroup) {
        baseActivity!!.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        progressBar = ProgressBar(baseActivity, null, android.R.attr.progressBarStyleLarge)
        progressBar?.indeterminateDrawable?.setColorFilter(ContextCompat.getColor(baseActivity!!, R.color.colorAccent),
                android.graphics.PorterDuff.Mode.SRC_ATOP)
        when (parent) {
            is RelativeLayout -> {
                val params = RelativeLayout.LayoutParams(130, 130)
                params.addRule(RelativeLayout.CENTER_IN_PARENT)
                parent.addView(progressBar, params)
            }
            is FrameLayout -> {
                val params = FrameLayout.LayoutParams(130, 130)
                params.gravity = Gravity.CENTER
                parent.addView(progressBar, params)
            }
            is androidx.coordinatorlayout.widget.CoordinatorLayout -> {
                val params = androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams(130, 130)
                params.gravity = Gravity.CENTER
                parent.addView(progressBar, params)
            }
            is ConstraintLayout -> {
                val params = ConstraintLayout.LayoutParams(130, 130)
                params.topToTop = ConstraintSet.PARENT_ID
                params.startToStart = ConstraintSet.PARENT_ID
                params.endToEnd = ConstraintSet.PARENT_ID
                params.bottomToBottom = ConstraintSet.PARENT_ID
                parent.addView(progressBar, params)
            }
        }
        progressBar?.visibility = View.VISIBLE  //To show ProgressBar
    }

    fun numberFormatPriceFloat(total: Float?): String {
        return if (total != null) {
            val number = NumberFormat.getInstance()
            number.minimumFractionDigits = 2
            number.maximumFractionDigits = 2
            number.format(total)
        } else {
            "0.00"
        }
    }

    fun numberFormatFloat(total: Float?): String {
        return if (total != null) {
            val number = NumberFormat.getInstance()
            number.minimumFractionDigits = 0
            number.maximumFractionDigits = 0
            number.format(total)
        } else {
            "0"
        }
    }

    fun numberFormatPriceDouble(total: Double?): String {
        return if (total != null) {
            val number = NumberFormat.getInstance()
            number.minimumFractionDigits = 2
            number.maximumFractionDigits = 2
            number.format(total)
        } else {
            "0.00"
        }
    }

    fun numberFormatPriceString(total: String?): String {
        return if (total != null) {
            val number = NumberFormat.getInstance()
            number.minimumFractionDigits = 2
            number.maximumFractionDigits = 2
            number.format(total.toDouble())

        } else {
            "0.00"
        }
    }

    /**
     * common dismiss progress bar for all screens
     *
     */
    fun dismissProgressBar() {
        progressBar?.visibility = View.GONE     // To Hide ProgressBar
        baseActivity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }


    override fun onResume() {
        super.onResume()

    }


    protected fun replaceFragment(
        @IdRes containerViewId: Int,
        @NonNull fragment: Fragment,
        @NonNull fragmentTag: String,
        @Nullable backStackStateName: String
    ) {
        activity!!.getSupportFragmentManager()
            .beginTransaction()
            .replace(containerViewId, fragment, fragmentTag)
            .addToBackStack(backStackStateName)
            .commit()
    }

    fun getUniqueImageFilename(): String {
        return "img_" + System.currentTimeMillis() + ".jpg"
    }

    /**
     * This will creates the public directory for the App in the externalStorage
     *
     * @return The folder path
     */
    fun createpicdirectory(): File {
        val root =
            File(Environment.getExternalStorageDirectory().toString() + File.separator + "EveryMans" + File.separator)
        root.mkdirs()
        return root

    }


}