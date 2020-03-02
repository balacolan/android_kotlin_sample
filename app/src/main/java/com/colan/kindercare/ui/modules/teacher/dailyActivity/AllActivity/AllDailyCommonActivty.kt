package com.colan.kindercare.ui.modules.teacher.dailyActivity.AllActivity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.databinding.ActivityAllDailyCommonActivtyBinding
import com.colan.kindercare.ui.base.BaseActivity
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.modules.teacher.dailyActivity.AllActivity.fragment.PhotoActivityFragment
import com.colan.kindercare.ui.modules.teacher.dailyActivity.AllActivity.fragment.bathroom.BathRoomDetailFragment
import com.colan.kindercare.ui.modules.teacher.dailyActivity.AllActivity.fragment.bathroom.BathRoomPreviewFragment
import com.colan.kindercare.ui.modules.teacher.dailyActivity.AllActivity.fragment.bathroom.BathRoomStudentFragment
import com.colan.kindercare.ui.modules.teacher.dailyActivity.AllActivity.fragment.classroom.ClassRoomDetailFragment
import com.colan.kindercare.ui.modules.teacher.dailyActivity.AllActivity.fragment.classroom.ClassRoomPreviewFragment
import com.colan.kindercare.ui.modules.teacher.dailyActivity.AllActivity.fragment.classroom.ClassRoomStudentFragment
import com.colan.kindercare.ui.modules.teacher.dailyActivity.AllActivity.fragment.incident.IncidentDetailFragment
import com.colan.kindercare.ui.modules.teacher.dailyActivity.AllActivity.fragment.incident.IncidentPreviewFragment
import com.colan.kindercare.ui.modules.teacher.dailyActivity.AllActivity.fragment.incident.IncidentStudentFragment
import com.colan.kindercare.ui.modules.teacher.dailyActivity.AllActivity.fragment.meal.MealActivityDetailFragment
import com.colan.kindercare.ui.modules.teacher.dailyActivity.AllActivity.fragment.meal.MealActivityFragment
import com.colan.kindercare.ui.modules.teacher.dailyActivity.AllActivity.fragment.meal.MealPreviewFragment
import com.colan.kindercare.ui.modules.teacher.dailyActivity.AllActivity.fragment.medicine.MedicineDetailFragment
import com.colan.kindercare.ui.modules.teacher.dailyActivity.AllActivity.fragment.medicine.MedicinePreviewFragment
import com.colan.kindercare.ui.modules.teacher.dailyActivity.AllActivity.fragment.medicine.MedicineStudentFragment
import com.colan.kindercare.ui.modules.teacher.dailyActivity.AllActivity.fragment.nap.NapActivityDetailFragment
import com.colan.kindercare.ui.modules.teacher.dailyActivity.AllActivity.fragment.nap.NapActivityFragment
import com.colan.kindercare.ui.modules.teacher.dailyActivity.AllActivity.fragment.nap.NapPreviewFragment
import com.colan.kindercare.ui.modules.teacher.dailyActivity.DailyActivityVM
import com.colan.kindercare.utils.Constants
import kotlinx.android.synthetic.main.back_next_layout.view.*


class AllDailyCommonActivty : BaseActivity<ActivityAllDailyCommonActivtyBinding, DailyActivityVM>(),BaseNavigator {



    override fun getBindingVariable(): Int {
      return BR.dailyActivityVM
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_all_daily_common_activty
    }

    override fun getViewModel(): DailyActivityVM {
        return ViewModelProviders.of(this).get(DailyActivityVM::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpActivitiesLayout()
        handleActivitiesNextPreviousAction()
    }

    private fun handleActivitiesNextPreviousAction() {
        getViewDataBinding().bottomLayout.next_btn.setOnClickListener {
            if(!(mViewModel?.activitiesNavigationIndex?.value!!>3)){
                mViewModel?.activitiesNavigationIndex?.value=mViewModel?.activitiesNavigationIndex?.value!!+1
            }

        }
        getViewDataBinding().bottomLayout.back_btn.setOnClickListener {
            if(!(mViewModel?.activitiesNavigationIndex?.value!!<1)){
                mViewModel?.activitiesNavigationIndex?.value=mViewModel?.activitiesNavigationIndex?.value!!-1
            }
        }

        mViewModel?.isLast?.observe(this, Observer {
            if(it){
                getViewDataBinding().bottomLayout.next_btn.text = getString(R.string.save)
            }else{
                getViewDataBinding().bottomLayout.next_btn.text = getString(R.string.next)
            }
        })
    }

    private fun setUpActivitiesLayout() {
        val `in` = intent
        val bundle=`in`.getExtras()
        val activityType=bundle?.get(Constants.ACTIVITY_TYPE)
        mViewModel?.activitiesType?.set(activityType.toString())
        when(activityType){
            "Photo"->{
                    replaceFragment(R.id.activity_container, PhotoActivityFragment(),"photos","photos")
                mViewModel?.isFinish?.set(true)
            }
            "Meal"->{
                    replaceFragment(R.id.activity_container, MealActivityFragment(),"Meal","Meal")
            }
            "Nap"->{
                replaceFragment(R.id.activity_container, NapActivityFragment(),"Meal","Meal")
            }
            "Class room"->{
                replaceFragment(R.id.activity_container, ClassRoomDetailFragment(),"Classroom","Classroom")
            }
            "Bathroom"->{
                replaceFragment(R.id.activity_container, BathRoomStudentFragment(),"Bathroom","Bathroom")
            }
            "Medicine"->{
                replaceFragment(R.id.activity_container, MedicineStudentFragment(),"Medicine","Medicine")
            }
            "Incident"->{
                replaceFragment(R.id.activity_container, IncidentStudentFragment(),"Incident","Incident")
            }

        }

        mViewModel?.activitiesNavigationIndex?.observe(this, Observer {
            when(mViewModel?.activitiesType?.get()){
                "Photo"->{
                    if(mViewModel?.activitiesNavigationIndex?.value==1){
                        replaceFragment(R.id.activity_container, PhotoActivityFragment(),"photos","photos")
                    }
                }
                "Meal"->{
                    if(mViewModel?.activitiesNavigationIndex?.value==1){
                        replaceFragment(R.id.activity_container, MealActivityFragment(),"Meal","Meal")
                        mViewModel?.isLast?.value=false
                    }
                    else if(mViewModel?.activitiesNavigationIndex?.value==2){
                        replaceFragment(R.id.activity_container, MealActivityDetailFragment(),"Meal","Meal")
                        mViewModel?.isLast?.value=false
                    }
                    else if(mViewModel?.activitiesNavigationIndex?.value==3){
                        replaceFragment(R.id.activity_container, MealPreviewFragment(),"Meal","Meal")
                        mViewModel?.isLast?.value=true
                    }
                }
                "Nap"->{
                    if(mViewModel?.activitiesNavigationIndex?.value==1){
                        replaceFragment(R.id.activity_container, NapActivityFragment(),"Nap","Nap")
                    }
                    else if(mViewModel?.activitiesNavigationIndex?.value==2){
                        replaceFragment(R.id.activity_container, NapActivityDetailFragment(),"Nap","Nap")
                    }
                    else if(mViewModel?.activitiesNavigationIndex?.value==3){
                        replaceFragment(R.id.activity_container, NapPreviewFragment(),"Nap","Nap")
                    }
                }
                "Class room"->{
                    if(mViewModel?.activitiesNavigationIndex?.value==1){
                        replaceFragment(R.id.activity_container, ClassRoomDetailFragment(),"Classroom","Classroom")
                    }
                    else if(mViewModel?.activitiesNavigationIndex?.value==2){
                        replaceFragment(R.id.activity_container, ClassRoomStudentFragment(),"Classroom","Classroom")
                    }
                    else if(mViewModel?.activitiesNavigationIndex?.value==3){
                        replaceFragment(R.id.activity_container, ClassRoomPreviewFragment(),"Classroom","Classroom")
                    }
                }
                "Bathroom"->{
                    if(mViewModel?.activitiesNavigationIndex?.value==1){
                        replaceFragment(R.id.activity_container, BathRoomStudentFragment(),"Bathroom","Bathroom")
                    }
                    else if(mViewModel?.activitiesNavigationIndex?.value==2){
                        replaceFragment(R.id.activity_container, BathRoomDetailFragment(),"Bathroom","Bathroom")
                    }
                    else if(mViewModel?.activitiesNavigationIndex?.value==3){
                        replaceFragment(R.id.activity_container, BathRoomPreviewFragment(),"Bathroom","Bathroom")
                    }
                }

                "Medicine"->{
                    if(mViewModel?.activitiesNavigationIndex?.value==1){
                        replaceFragment(R.id.activity_container, MedicineStudentFragment(),"Medicine","Medicine")
                    }
                    else if(mViewModel?.activitiesNavigationIndex?.value==2){
                        replaceFragment(R.id.activity_container, MedicineDetailFragment(),"Medicine","Medicine")
                    }
                    else if(mViewModel?.activitiesNavigationIndex?.value==3){
                        replaceFragment(R.id.activity_container, MedicinePreviewFragment(),"Medicine","Medicine")
                    }
                }
                "Incident"->{
                    if(mViewModel?.activitiesNavigationIndex?.value==1){
                        replaceFragment(R.id.activity_container, IncidentStudentFragment(),"Incident","Incident")
                    }
                    else if(mViewModel?.activitiesNavigationIndex?.value==2){
                        replaceFragment(R.id.activity_container, IncidentDetailFragment(),"Incident","Incident")
                    }
                    else if(mViewModel?.activitiesNavigationIndex?.value==3){
                        replaceFragment(R.id.activity_container, IncidentPreviewFragment(),"Incident","Incident")
                    }
                }
            }
        })
    }


    override fun onClickView(v: View?) {
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
    }


    override fun onBackPressed() {

    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel?.activitiesNavigationIndex?.value=0
    }

}
