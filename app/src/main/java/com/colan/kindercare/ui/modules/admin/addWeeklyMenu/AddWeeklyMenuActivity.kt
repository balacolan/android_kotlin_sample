package com.colan.kindercare.ui.modules.admin.addWeeklyMenu

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.databinding.ActivityAddWeeklyMenuBinding
import com.colan.kindercare.ui.base.BaseActivity
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.modules.teacher.dailyActivity.AllActivity.fragment.meal.MealWeeklyDetailFragment
import com.colan.kindercare.ui.modules.teacher.dailyActivity.AllActivity.fragment.meal.MealWeeklyFragment
import com.colan.kindercare.ui.modules.teacher.dailyActivity.AllActivity.fragment.meal.MealWeeklyPreviewFragment
import kotlinx.android.synthetic.main.back_next_layout.view.*
import kotlinx.android.synthetic.main.fragment_meal_weekly_detail.*
import java.util.*

class AddWeeklyMenuActivity : BaseActivity< ActivityAddWeeklyMenuBinding,AddWeeklyMenuVM>(),BaseNavigator {

    override fun onClickView(v: View?) {
        when(v?.id) {
            R.id.date_et -> {
                datepicker()
                Log.e("fff","ggg")
            }
        }
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
    }

    override fun getBindingVariable(): Int {
            return BR.addWeeklyMenuVM
    }

    override fun getLayoutId(): Int {
            return R.layout.activity_add_weekly_menu
    }

    override fun getViewModel(): AddWeeklyMenuVM {
        return ViewModelProviders.of(this).get(AddWeeklyMenuVM::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            mViewModel?.setNavigator(this)
            setUpFragmentObserver()
            handleActivitiesNextPreviousAction()
    }

    private fun setUpFragmentObserver() {
        mViewModel?.fragmentNavigationIndex?.observe(this, Observer {
            when(it){
                1 -> {
                    replaceFragment(R.id.activity_container, MealWeeklyDetailFragment(),"MealWeeklyDetail","MealWeeklyDetail")
                    mViewModel?.isFinish?.set(false)
                    mViewModel?.isBack?.set(false)
                }
                2 -> {
                    replaceFragment(R.id.activity_container, MealWeeklyFragment(),"MealWeekly","MealWeekly")
                    mViewModel?.isFinish?.set(false)
                    mViewModel?.isBack?.set(true)
                }
                3 -> {
                    replaceFragment(R.id.activity_container, MealWeeklyPreviewFragment(),"MealWeeklyPreview","MealWeeklyPreview")
                    mViewModel?.isFinish?.set(true)
                    mViewModel?.isBack?.set(true)
                }
            }
        } )
    }

    private fun handleActivitiesNextPreviousAction() {
        getViewDataBinding().bottomLayout.nextBtn.setOnClickListener {
            if(!(mViewModel?.fragmentNavigationIndex?.value!!>3)){
                mViewModel?.fragmentNavigationIndex?.value=mViewModel?.fragmentNavigationIndex?.value!!+1
            }
        }
        getViewDataBinding().bottomLayout.backBtn.setOnClickListener {
            handleOnBack()

        }
    }

    fun handleOnBack() {
        if(mViewModel?.fragmentNavigationIndex?.value!! >= 1){
            if(mViewModel?.fragmentNavigationIndex?.value == 1) {
                finish()
            } else {
                mViewModel?.fragmentNavigationIndex?.value=mViewModel?.fragmentNavigationIndex?.value!!-1
            }
        }
    }


    override fun onBackPressed() {
        handleOnBack()
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
                date_et.setText(
                    dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                )
            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()

    }


}
