package com.colan.kindercare.ui.dashboard

import android.app.AlertDialog
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.adapter.BaseRecyclerViewAdapter
import com.colan.kindercare.adapter.OnDataBindCallback
import com.colan.kindercare.data.model.SelectChildModel
import com.colan.kindercare.data.remote.data.Status
import com.colan.kindercare.data.remote.data.response.SchoolListResponse
import com.colan.kindercare.databinding.ActivityDashboardBinding
import com.colan.kindercare.databinding.ChildProfileSpinnerItemsBinding
import com.colan.kindercare.databinding.SchoolProfileSpinnerItemsBinding
import com.colan.kindercare.ui.base.BaseActivity
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.dashboard.fragments.DashboardFragment
import com.colan.kindercare.ui.dashboard.fragments.add_child.AddChildActitvity
import com.colan.kindercare.ui.dashboard.menu.DrawerAdapter
import com.colan.kindercare.ui.dashboard.menu.DrawerItem
import com.colan.kindercare.ui.dashboard.menu.SimpleItem
import com.colan.kindercare.ui.dashboard.menu.SpaceItem
import com.colan.kindercare.ui.modules.admin.AdminDashboardFragment
import com.colan.kindercare.ui.modules.admin.attendance.AdminAttendanceFragmet
import com.colan.kindercare.ui.modules.admin.enroll.EnrollmentEnquiryFragment
import com.colan.kindercare.ui.modules.admin.pickupApproval.PickupPersonApproval
import com.colan.kindercare.ui.modules.common.changepassword.ChangePasswordActivity
import com.colan.kindercare.ui.modules.common.dailyactivity.DailyActivityFragment
import com.colan.kindercare.ui.modules.common.leaveapproval.LeaveApprovalFragment
import com.colan.kindercare.ui.modules.common.leavestatus.LeaveStatusFragment
import com.colan.kindercare.ui.modules.common.message.MessageFragment
import com.colan.kindercare.ui.modules.common.notification.NotificationActivity
import com.colan.kindercare.ui.modules.common.profile.ProfileActivity
import com.colan.kindercare.ui.modules.common.report.ReportsFragment
import com.colan.kindercare.ui.modules.common.weeklyMenu.WeeklyMenuFragment
import com.colan.kindercare.ui.modules.parent.calender.CalenderFragment
import com.colan.kindercare.ui.modules.parent.familyinformation.FamilyInformationFragment
import com.colan.kindercare.ui.modules.parent.payment.PaymentFragment
import com.colan.kindercare.ui.modules.superAdmin.SuperAdminDashboardFragment
import com.colan.kindercare.ui.modules.teacher.TeacherDashboardFragment
import com.colan.kindercare.ui.modules.teacher.attendance.AttendanceFragment
import com.colan.kindercare.utils.*
import com.colan.kindercare.utils.SchoolSelectionBroadCast.Companion.schoolLister
import com.yarolegovich.slidingrootnav.SlidingRootNav
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.menu_left_drawer.*

@Suppress("INACCESSIBLE_TYPE")
class DashboardActivity : BaseActivity<ActivityDashboardBinding, DashboardVM>(), BaseNavigator,
    DrawerAdapter.OnItemSelectedListener, IToolbar, SimpleItem.OnItemTitleSelectedListener,
    SchoolSelectionListner {

    private var screenTitles: Array<String>? = null

    private var screenIcons: Array<Drawable?>? = null

    private var slidingRootNav: SlidingRootNav? = null

    private var fragmentManager: FragmentManager? = null

    private var baseRecyclerAdapter: BaseRecyclerViewAdapter<SelectChildModel, ChildProfileSpinnerItemsBinding>? =
        null

    private var baseSchoolRecyclerAdapter: BaseRecyclerViewAdapter<SchoolListResponse, SchoolProfileSpinnerItemsBinding>? =
        null

    private var childList = ArrayList<SelectChildModel>()


    private var mIntentFilter : IntentFilter?=null

    private val reciever=SchoolSelectionBroadCast()

    override fun getBindingVariable(): Int {
        return BR.dashboardVM
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_dashboard
    }

    override fun getViewModel(): DashboardVM {
        return ViewModelProviders.of(this).get(DashboardVM::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel?.setNavigator(this)
        fragmentManager = supportFragmentManager
        addIntentFiltersForReceiver()
        setupSliderNavigation(savedInstanceState)
        setUpAdaper()
        observeResponse()
        setUpFragment()
    }

    private fun setUpFragment() {
        when {
            mViewModel?.isParent?.get()!! -> showFragment(DashboardFragment())
            mViewModel?.isAdmin?.get()!! -> {
                guideline_hor_right_20_per.setGuidelinePercent(.14f)
                showFragment(AdminDashboardFragment())
            }
            mViewModel?.isteacher?.get()!! -> {
                guideline_hor_right_20_per.setGuidelinePercent(.14f)
                showFragment(TeacherDashboardFragment())
            }
            mViewModel?.isSuperAdmin?.get()!! -> showFragment(SuperAdminDashboardFragment())
        }
    }

    private fun observeResponse() {
        mViewModel?.selectChildModel?.observe(this, Observer {
            if (it != null && it.isNotEmpty()) {
                childList.addAll(it)
                setUpRecyclerview(childList)
            }
        })

        mViewModel?.schoolListResponse?.observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {

                    }
                    Status.SUCCESS -> {
                        try {
                            mViewModel?.getSchoolListApiJob?.cancel()
                            it.data?.data?.let { it1 -> mViewModel?.schoolList?.addAll(it1) }
                            mViewModel?.iSharedPreferenceService?.setStringValue(Constants.SCHOOL_ID,
                                it.data?.data!![0].id.toString()
                            )
                            mViewModel?.iSharedPreferenceService?.setStringValue(Constants.CLASS_ID,
                                it.data?.data!![0].id.toString()
                            )
                            mViewModel?.iSharedPreferenceService?.setStringValue(Constants.SECTION_ID,
                                it.data?.data!![0].id.toString()
                            )
                            mViewModel?.schoolName?.set(it.data?.data!![0].schoolName)
                            mViewModel?.schoolBranch?.set(it.data?.data!![0].location+" branch")
                            mViewModel?.schoolList?.get(0)!!.isSelected=true
                            mViewModel?.schoolList?.let { it1 -> setUpSchoolRecyclerview(it1) }
                        }catch (e: Exception) {
                            Log.i("catch Error", ""+e.message)
                        }


                    }
                    Status.ERROR, Status.FAILURE -> {

                    }
                }

            }
        })
    }

    private fun setUpSchoolRecyclerview(schoolList: ArrayList<SchoolListResponse>) {
        baseSchoolRecyclerAdapter = BaseRecyclerViewAdapter(
            R.layout.school_profile_spinner_items,
            BR.schoolItem,
            schoolList,
            null,
            schoolListCallBack
        )
        getViewDataBinding().chooseSchool.schoolRv.adapter = baseSchoolRecyclerAdapter
    }


    private fun setUpRecyclerview(childList: ArrayList<SelectChildModel>) {
        baseRecyclerAdapter = BaseRecyclerViewAdapter(
            R.layout.child_profile_spinner_items,
            BR.childItem,
            childList,
            null,
            childListCallBack
        )
        getViewDataBinding().chooseChild.childerRv.adapter = baseRecyclerAdapter

    }

    private fun setupSliderNavigation(savedInstanceState: Bundle?) {
        slidingRootNav = SlidingRootNavBuilder(this)
            .withMenuOpened(false)
            .withContentClickableWhenMenuOpened(false)
            .withSavedState(savedInstanceState)
            .withMenuLayout(R.layout.menu_left_drawer)
            .inject()
    }


    private fun setUpAdaper() {
        val adapter: DrawerAdapter?
        when {
            mViewModel?.isParent?.get()!! -> {
                screenTitles = loadScreenTitles()
                screenIcons = loadScreenIcons()
                adapter = DrawerAdapter(
                    mutableListOf(
                        createItemFor(POS_DASHBOARD).setChecked(true),
                        createItemFor(POS_FAMILY_INFORMATION),
                        createItemFor(POS_PAYMENT),
                        createItemFor(POS_MESSAGES),
                        createItemFor(POS_LEAVE_APPLICATION),
                        createItemFor(POS_WEEKLY_MENU),
                        createItemFor(POS_CALENDER),
                        createItemFor(POS_CHANGE_PASSWORD),
                        createItemFor(POS_LOGOUT),
                        SpaceItem(48)
                    )
                )

            }
            mViewModel?.isAdmin?.get()!! -> {
                screenTitles = loadAdminScreenTitles()
                screenIcons = loadAdminScreenIcons()
                adapter = DrawerAdapter(
                    mutableListOf(
                        createItemFor(POS_DASHBOARD).setChecked(true),
                        createItemFor(POS_ADMIN_REPORT),
                        createItemFor(POS_ADMIN_ATTENDANCE),
                        createItemFor(POS_ADMIN_MESSAGES),
                        createItemFor(POS_ENROLLMENT_ENQUIRY),
                        createItemFor(POS_PICKUP_PERSONAL_APPROVAL),
                        createItemFor(POS_ADMIN_TEACHER_LEAVE_APPLICATION),
                        createItemFor(POS_ADMIN_LEAVE_APPROVAL),
                        createItemFor(POS_ADMIN_WEEKLY_MENU),
                        createItemFor(POS_ADMIN_DAILY_ACTIVITY),
                        createItemFor(POS_ADMIN_LOGOUT),
                        SpaceItem(48)
                    )
                )
            }
            mViewModel?.isSuperAdmin?.get()!! -> {
                screenTitles = loadSuperAdminScreenTitles()
                screenIcons = loadSuperAdminScreenIcons()
                adapter = DrawerAdapter(
                    mutableListOf(
                        createItemFor(POS_DASHBOARD).setChecked(true),
                        createItemFor(POS_SUPER_ADMIN_REPORT),
                        createItemFor(POS_SUPER_ADMIN_ATTENDANCE),
                        createItemFor(POS_SUPER_ADMIN_MESSAGES),
                        createItemFor(POS_SUPER_ADMIN_ENROLLMENT_ENQUIRY),
                        createItemFor(POS_SUPER_ADMIN_PICKUP_PERSONAL_APPROVAL),
                        createItemFor(POS_SUPER_ADMIN_LEAVE_APPROVAL),
                        createItemFor(POS_SUPER_ADMIN_LOGOUT),
                        SpaceItem(48)
                    )
                )
            }
            else -> {
                screenTitles = loadTeacherScreenTitles()
                screenIcons = loadTeacherScreenIcons()
                adapter = DrawerAdapter(
                    mutableListOf(
                        createItemFor(POS_DASHBOARD).setChecked(true),
                        createItemFor(POS_ATTENDANCE),
                        createItemFor(POS_TEACHER_MESSAGES),
                        createItemFor(POS_TEACHER_LEAVE_APPLICATION),
                        createItemFor(POS_LEAVE_APPROVAL),
                        createItemFor(POS_WEEKLY_MENU),
                        createItemFor(POS_DAILY_ACTIVITY),
                        createItemFor(POS_TEACHER_LOGOUT),
                        SpaceItem(48)
                    )
                )
            }
        }

        adapter.setListener(this)
        val list = findViewById<RecyclerView>(R.id.list)
        list.isNestedScrollingEnabled = false
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter
        adapter.setSelected(POS_DASHBOARD)

    }

    private fun loadTeacherScreenIcons(): Array<Drawable?>? {
        val ta = resources.obtainTypedArray(R.array.teacher_activityScreenIcons)
        val icons = arrayOfNulls<Drawable>(ta.length())
        for (i in 0 until ta.length()) {
            val id = ta.getResourceId(i, 0)
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id)
            }
        }
        ta.recycle()
        return icons
    }


    @Suppress("UNREACHABLE_CODE")
    private fun loadTeacherScreenTitles(): Array<String>? {
        setUpNavigationTopProfile()
        return resources.getStringArray(R.array.teacher_activityScreenTitles)

    }

    private fun loadScreenIcons(): Array<Drawable?> {
        val ta = resources.obtainTypedArray(R.array.ld_activityScreenIcons)
        val icons = arrayOfNulls<Drawable>(ta.length())
        for (i in 0 until ta.length()) {
            val id = ta.getResourceId(i, 0)
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id)
            }
        }
        ta.recycle()
        return icons
    }

    @Suppress("UNREACHABLE_CODE")
    private fun loadAdminScreenTitles(): Array<String>? {
        setUpNavigationTopProfile()
        return resources.getStringArray(R.array.admin_activityScreenTitles)

    }

    private fun loadAdminScreenIcons(): Array<Drawable?>? {
        val ta = resources.obtainTypedArray(R.array.admin_activityScreenIcons)
        val icons = arrayOfNulls<Drawable>(ta.length())
        for (i in 0 until ta.length()) {
            val id = ta.getResourceId(i, 0)
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id)
            }
        }
        ta.recycle()
        return icons
    }


    @Suppress("UNREACHABLE_CODE")
    private fun loadSuperAdminScreenTitles(): Array<String>? {
        setUpNavigationTopProfile()
        return resources.getStringArray(R.array.super_admin_activityScreenTitles)
    }


    private fun setUpNavigationTopProfile(){
        userName_tv.text=mViewModel?.iSharedPreferenceService?.getStringValue(Constants.USER_NAME)
        userType_tv.text=mViewModel?.iSharedPreferenceService?.getStringValue(Constants.USER_TYPE)
        Glide.with(this)
            .asBitmap().load(mViewModel?.iSharedPreferenceService?.getStringValue(Constants.PROFILE_IMAGE))
            .placeholder(R.drawable.ic_profile_pic)
            .into(profile_iv)
        profile_iv.setOnClickListener {
            goTo(ProfileActivity::class.java, bundle)
        }

    }

    private fun loadSuperAdminScreenIcons(): Array<Drawable?>? {
        val ta = resources.obtainTypedArray(R.array.super_admin_activityScreenIcons)
        val icons = arrayOfNulls<Drawable>(ta.length())
        for (i in 0 until ta.length()) {
            val id = ta.getResourceId(i, 0)
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id)
            }
        }
        ta.recycle()
        return icons
    }

    override fun onBackPressed() {

    }

    private fun createItemFor(position: Int): DrawerItem<*> {
        return SimpleItem(this.screenIcons!![position], screenTitles!![position])
            .setListener(this)
            .withIconTint(color(R.color.white))
            .withTextTint(color(R.color.white))
            .withSelectedIconTint(color(R.color.colorAccent))
            .withSelectedTextTint(color(R.color.colorAccent))
    }

    private fun loadScreenTitles(): Array<String> {
        val profileIv = findViewById<CircleImageView>(R.id.profile_iv)
        profileIv.setOnClickListener {
            goTo(ProfileActivity::class.java, bundle)
        }
        return resources.getStringArray(R.array.ld_activityScreenTitles)
    }

    @ColorInt
    private fun color(@ColorRes res: Int): Int {
        return ContextCompat.getColor(this, res)
    }

    override fun onItemSelectedTitle(title: String?): String {
        slidingRootNav!!.closeMenu()
        when (title) {
            Constants.DASHBOARD -> {
                when {
                    mViewModel?.isParent?.get()!! -> {
                        guideline_hor_right_20_per.setGuidelinePercent(.18f)
                        getViewDataBinding().container.visibility = View.VISIBLE
                        getViewDataBinding().reportContainer.visibility = View.GONE
                        showFragment(DashboardFragment())
                    }
                    mViewModel?.isAdmin?.get()!! -> {
                        getViewDataBinding().container.visibility = View.VISIBLE
                        getViewDataBinding().reportContainer.visibility = View.GONE
                        guideline_hor_right_20_per.setGuidelinePercent(.14f)
                        showFragment(AdminDashboardFragment())
                    }
                    mViewModel?.isSuperAdmin?.get()!! -> showFragment(SuperAdminDashboardFragment())
                    else -> {
                        guideline_hor_right_20_per.setGuidelinePercent(.12f)
                        showFragment(TeacherDashboardFragment())
                    }
                }
            }
            Constants.PAYMENT -> {
                showFragment(PaymentFragment())
            }

            Constants.FAMILY_INFORMATION -> {
                if (mViewModel?.isParent?.get()!!) {
                    guideline_hor_right_20_per.setGuidelinePercent(.18f)
                    showFragment(FamilyInformationFragment())
                }
            }
            Constants.LEAVE_APPLICATION -> {
                if (mViewModel?.isParent?.get()!!) {
                    guideline_hor_right_20_per.setGuidelinePercent(.18f)
                    getViewDataBinding().container.visibility = View.VISIBLE
                    getViewDataBinding().reportContainer.visibility = View.GONE
                    showFragment(LeaveStatusFragment())
                } else {
                    guideline_hor_right_20_per.setGuidelinePercent(.14f)
                    getViewDataBinding().container.visibility = View.VISIBLE
                    getViewDataBinding().reportContainer.visibility = View.GONE
                    showFragment(LeaveStatusFragment())
                }

            }
            Constants.MESSAGES -> {
                when {
                    mViewModel?.isAdmin?.get()!! -> {
                        getViewDataBinding().container.visibility = View.GONE
                        getViewDataBinding().reportContainer.visibility = View.VISIBLE
                        guideline_hor_right_20_per.setGuidelinePercent(.14f)
                        showInToolbarFragment(MessageFragment())
                    }
                    mViewModel?.isSuperAdmin?.get()!! -> showFragment(MessageFragment())
                    else -> showFragment(MessageFragment())
                }
            }
            Constants.WEEKLY_MENU -> {
                when {
                    mViewModel?.isParent?.get()!! -> {
                        guideline_hor_right_20_per.setGuidelinePercent(.18f)
                        getViewDataBinding().container.visibility = View.VISIBLE
                        getViewDataBinding().reportContainer.visibility = View.GONE
                        showFragment(WeeklyMenuFragment())
                    }
                    mViewModel?.isteacher?.get()!! -> {
                        guideline_hor_right_20_per.setGuidelinePercent(.14f)
                        getViewDataBinding().container.visibility = View.VISIBLE
                        getViewDataBinding().reportContainer.visibility = View.GONE
                        showFragment(WeeklyMenuFragment())
                    }
                    else -> {
                        guideline_hor_right_20_per.setGuidelinePercent(.18f)
                        getViewDataBinding().container.visibility = View.GONE
                        getViewDataBinding().reportContainer.visibility = View.VISIBLE
                        showInToolbarFragment(WeeklyMenuFragment())
                    }
                }

            }
            Constants.CALENDER -> {
                showFragment(CalenderFragment())
            }

            Constants.CHANGE_PASSWORD -> {
                //putToast("Need to discuss.Not yet started")
                goTo(ChangePasswordActivity::class.java, bundle)
            }
            Constants.ATTENDANCE -> {
                when {
                    mViewModel?.isAdmin?.get()!! -> {
                        getViewDataBinding().container.visibility = View.GONE
                        getViewDataBinding().reportContainer.visibility = View.VISIBLE
                        guideline_hor_right_20_per.setGuidelinePercent(.20f)
                        showInToolbarFragment(AdminAttendanceFragmet())
                    }
                    mViewModel?.isSuperAdmin?.get()!! ->{
                        showFragment(AdminAttendanceFragmet())
                    }
                    else -> showFragment(AttendanceFragment())
                }

            }
            Constants.LOGOUT -> {
                showAlertDialogueToLogout()
            }
            Constants.LEAVE_APPROVAL -> {
                when {
                    mViewModel?.isAdmin?.get()!! -> {
                        getViewDataBinding().container.visibility = View.VISIBLE
                        getViewDataBinding().reportContainer.visibility = View.GONE
                        guideline_hor_right_20_per.setGuidelinePercent(.14f)
                        showFragment(LeaveApprovalFragment())
                    }
                    else -> {
                        showFragment(LeaveApprovalFragment())
                    }
                }

            }
            Constants.DAILY_ACTIVITY -> {
                guideline_hor_right_20_per.setGuidelinePercent(.14f)
                getViewDataBinding().container.visibility = View.VISIBLE
                getViewDataBinding().reportContainer.visibility = View.GONE
                showFragment(DailyActivityFragment())
            }
            Constants.REPORT -> {
                when {
                    mViewModel?.isSuperAdmin?.get()!! -> {
                       /* getViewDataBinding().container.visibility = View.VISIBLE
                        getViewDataBinding().reportContainer.visibility = View.GONE
                        guideline_hor_right_20_per.setGuidelinePercent(.14f)*/
                        showFragment(ReportsFragment())
                    }
                    mViewModel?.isAdmin?.get()!! -> {
                        getViewDataBinding().container.visibility = View.GONE
                        getViewDataBinding().reportContainer.visibility = View.VISIBLE
                        guideline_hor_right_20_per.setGuidelinePercent(.18f)
                        showInToolbarFragment(ReportsFragment())
                    }
                    else -> {
                        getViewDataBinding().container.visibility = View.GONE
                        getViewDataBinding().reportContainer.visibility = View.VISIBLE
                        guideline_hor_right_20_per.setGuidelinePercent(.17f)
                        showInToolbarFragment(ReportsFragment())
                    }
                }

            }
            Constants.ENROLLMENT_ENQUIRY -> {
                getViewDataBinding().container.visibility = View.VISIBLE
                getViewDataBinding().reportContainer.visibility = View.GONE
                guideline_hor_right_20_per.setGuidelinePercent(.17f)
                showFragment(EnrollmentEnquiryFragment())
            }

            Constants.PICKUP_PERSON_APPROVAL -> {
                getViewDataBinding().container.visibility = View.VISIBLE
                getViewDataBinding().reportContainer.visibility = View.GONE
                guideline_hor_right_20_per.setGuidelinePercent(.17f)
                showFragment(PickupPersonApproval())
            }
        }

        return title!!
    }

    private fun showInToolbarFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.report_container, fragment)
            .commit()
    }

    override fun onItemSelected(position: Int) {

    }


    private fun showAlertDialogueToLogout() {
        AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Log out?")
            .setMessage("Are you sure you want to Log out?")
            .setPositiveButton("Yes") { _, _ ->
                mViewModel?.iSharedPreferenceService?.clearAllValues()
                mViewModel?.iSharedPreferenceService?.setBooleanValue(Constants.ALREADY_LOGED_IN,false)
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }


    override fun onClickView(v: View?) {
        when (v?.id) {
            R.id.drop_down_iv -> {
                mViewModel?.isShowingChooseChldlayout?.set(!mViewModel!!.isShowingChooseChldlayout.get()!!)
            }
            R.id.drop_down_school_iv -> {
                mViewModel?.isShowingSchoollayout?.set(!mViewModel!!.isShowingSchoollayout.get()!!)
            }
            R.id.nav_icon_iv -> {
                when {
                    slidingRootNav!!.isMenuClosed -> slidingRootNav!!.openMenu()
                    slidingRootNav!!.isMenuOpened -> slidingRootNav!!.closeMenu()
                }
            }
            R.id.add_more_child_tv -> {
                goTo(AddChildActitvity::class.java, bundle)
            }
            R.id.notification_iv -> {
                goTo(NotificationActivity::class.java, null)
            }


        }
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        navigateTo(this, clazz, null)
    }


    override fun changeNavigationIcon(drawableIcon: Int) {
        getViewDataBinding().navIconIv.setImageResource(drawableIcon)
    }

    override fun changeToolbarTitle(title: String) {
        getViewDataBinding().titleToolbar.text = title
    }

    private var childListCallBack = object : OnDataBindCallback<ChildProfileSpinnerItemsBinding> {
        override fun onItemClick(view: View, position: Int, v: ChildProfileSpinnerItemsBinding) {
            when (view) {
                v.selectionIv -> {
                    baseRecyclerAdapter?.getItems()?.filter { it.isSelected }?.forEach {
                        it.isSelected = false
                    }
                    childList[position].isSelected = !childList[position].isSelected
                    mViewModel?.childName?.set(childList[position].chileName)
                    mViewModel?.childSection?.set(childList[position].chileSection)
                    baseRecyclerAdapter?.notifyDataSetChanged()
                }
            }
        }

        override fun onDataBind(
            v: ChildProfileSpinnerItemsBinding,
            onClickListener: View.OnClickListener
        ) {
            v.selectionIv.setOnClickListener(onClickListener)
        }

    }


    private var schoolListCallBack = object : OnDataBindCallback<SchoolProfileSpinnerItemsBinding> {
        override fun onItemClick(view: View, position: Int, v: SchoolProfileSpinnerItemsBinding) {
            when (view) {
                v.selectionIv -> {
                    baseSchoolRecyclerAdapter?.getItems()?.filter { it.isSelected }?.forEach {
                        it.isSelected = false
                    }
                    mViewModel!!.schoolList[position].isSelected = !mViewModel!!.schoolList[position].isSelected
                    mViewModel?.iSharedPreferenceService?.setStringValue(Constants.SCHOOL_ID,mViewModel!!.schoolList[position].id.toString())
                    mViewModel?.iSharedPreferenceService?.setStringValue(Constants.SECTION_ID,mViewModel!!.schoolList[position].id.toString())
                    mViewModel?.iSharedPreferenceService?.setStringValue(Constants.CLASS_ID,mViewModel!!.schoolList[position].id.toString())
                    mViewModel?.schoolName?.set(mViewModel!!.schoolList[position].schoolName)
                    mViewModel?.schoolBranch?.set(mViewModel!!.schoolList[position].location+" branch")
                    baseSchoolRecyclerAdapter?.notifyDataSetChanged()

                    val intent = Intent()
                    intent.action = Constants.SCHOOL_LISTNER
                    sendBroadcast(intent)
                }
            }
        }

        override fun onDataBind(
            v: SchoolProfileSpinnerItemsBinding,
            onClickListener: View.OnClickListener
        ) {

            v.selectionIv.setOnClickListener(onClickListener)
        }

    }


    companion object {
        private const val POS_DASHBOARD = 0
        private const val POS_FAMILY_INFORMATION = 1
        private const val POS_PAYMENT = 2
        private const val POS_MESSAGES = 3
        private const val POS_LEAVE_APPLICATION = 4
        private const val POS_WEEKLY_MENU = 5
        private const val POS_CALENDER = 6
        private const val POS_CHANGE_PASSWORD = 7
        private const val POS_LOGOUT = 8

        //TEACHER NAVIGATION MENU
        private const val POS_ATTENDANCE = 1
        private const val POS_TEACHER_MESSAGES = 2
        private const val POS_TEACHER_LEAVE_APPLICATION = 3
        private const val POS_LEAVE_APPROVAL = 4
        private const val POS_DAILY_ACTIVITY = 6
        private const val POS_TEACHER_LOGOUT = 7


        //ADMIN NAVIGATION MENU
        private const val POS_ADMIN_REPORT = 1
        private const val POS_ADMIN_ATTENDANCE = 2
        private const val POS_ADMIN_MESSAGES = 3
        private const val POS_ENROLLMENT_ENQUIRY = 4
        private const val POS_PICKUP_PERSONAL_APPROVAL = 5
        private const val POS_ADMIN_TEACHER_LEAVE_APPLICATION = 6
        private const val POS_ADMIN_LEAVE_APPROVAL = 7
        private const val POS_ADMIN_WEEKLY_MENU = 8
        private const val POS_ADMIN_DAILY_ACTIVITY = 9
        private const val POS_ADMIN_LOGOUT = 10


        //SUPER- ADMIN NAVIGATION MENU
        private const val POS_SUPER_ADMIN_REPORT = 1
        private const val POS_SUPER_ADMIN_ATTENDANCE = 2
        private const val POS_SUPER_ADMIN_MESSAGES = 3
        private const val POS_SUPER_ADMIN_ENROLLMENT_ENQUIRY = 4
        private const val POS_SUPER_ADMIN_PICKUP_PERSONAL_APPROVAL = 5
        private const val POS_SUPER_ADMIN_LEAVE_APPROVAL = 6
        private const val POS_SUPER_ADMIN_LOGOUT = 7
    }

    private fun addIntentFiltersForReceiver() {
        mIntentFilter = IntentFilter()
        mIntentFilter!!.addAction(Constants.SCHOOL_LISTNER)
    }

    override fun onResume() {
        super.onResume()
        setUpNavigationTopProfile()
        schoolLister=this
        registerReceiver(reciever,mIntentFilter)
    }


    override fun onPause() {
        super.onPause()
        unregisterReceiver(reciever)
    }


    override fun onTriggerSchoolSelection() {
        //putToast("Trigger")
    }


}
