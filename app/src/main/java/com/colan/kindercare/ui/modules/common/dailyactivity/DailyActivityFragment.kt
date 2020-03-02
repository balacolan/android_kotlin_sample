package com.colan.kindercare.ui.modules.common.dailyactivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.colan.kindercare.BR

import com.colan.kindercare.R
import com.colan.kindercare.adapter.BaseRecyclerViewAdapter
import com.colan.kindercare.adapter.OnDataBindCallback
import com.colan.kindercare.data.model.DailyActivityModel
import com.colan.kindercare.databinding.DailyActivityListItemsBinding
import com.colan.kindercare.databinding.FragmentDailyActivityBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.dashboard.IToolbar
import com.colan.kindercare.ui.modules.common.dailyactivity.details.DailyActivityDetail
import com.colan.kindercare.ui.modules.teacher.dailyActivity.ActivtyChoosingActivity
import com.colan.kindercare.utils.*
import com.facebook.shimmer.ShimmerFrameLayout



class DailyActivityFragment : BaseFragment<FragmentDailyActivityBinding,DailyActivityVM>(),BaseNavigator{

    private lateinit var mToolbarManager: IToolbar

    var mShimmerViewContainer: ShimmerFrameLayout? = null

    private var adapterLeaveRquest: BaseRecyclerViewAdapter<DailyActivityModel, DailyActivityListItemsBinding>? =
        null

    override val bindingVariable: Int
        get() = BR.dailyActivityVM
    override val layoutId: Int
        get() = R.layout.fragment_daily_activity
    override val viewModel: DailyActivityVM
        get() = ViewModelProviders.of(this).get(DailyActivityVM::class.java)



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        viewModel.setNavigator(this)
        mToolbarManager.changeToolbarTitle(getString(R.string.daily_activity_tittle))


        if(viewModel.isAdmin.get()!!){
            viewDataBinding?.addNewActivityIv?.visibility=View.GONE
        }else{
            viewDataBinding?.addNewActivityIv?.visibility=View.VISIBLE
        }

        viewModel.listAdvancedQuoteResult.observe(this, Observer {
            if (it != null) {
                adapterLeaveRquest?.cleatDataSet()
                adapterLeaveRquest?.addDataSet(it)
            }
        })

        viewDataBinding?.dailyActivityRv?.layoutManager = LinearLayoutManager(activity)

        adapterLeaveRquest =
            BaseRecyclerViewAdapter(R.layout.daily_activity_list_items, BR.dailyActivityItem, ArrayList(),
                null, object : OnDataBindCallback<DailyActivityListItemsBinding> {
                    override fun onDataBind(v: DailyActivityListItemsBinding, onClickListener: View.OnClickListener) {
                    }

                    override fun onItemClick(view: View, position: Int, v: DailyActivityListItemsBinding) {
                        when (view.id) {
                            R.id.card_daily_item -> {
                                val intent = Intent(activity, DailyActivityDetail::class.java)
                                intent.putExtra("FragmentDetail",v.dailyActivityTv.text)
                                startActivity(intent)
                            }
                        }
                    }
                })

        viewDataBinding?.dailyActivityRv?.adapter = adapterLeaveRquest

        return viewDataBinding?.root

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Singleton.isDashBoardFragmentVisible =true
        mToolbarManager = context as IToolbar
    }
    override fun onClickView(v: View?) {
        when(v?.id){
            R.id.add_new_activity_iv->{
                goTo(ActivtyChoosingActivity::class.java, bundle)
            }
            R.id.date_previous_iv->{
                val date = viewModel.currentDate.get()?.let { StringToDateConvert(it) }
                val decreamentDate= date?.let { decrementDateByOne(it) }
                val dateInString = decreamentDate?.toString("dd MMMM yyyy")
                viewModel.currentDate.set(dateInString)
            }
            R.id.date_next_iv->{
                val date = viewModel.currentDate.get()?.let { StringToDateConvert(it) }
                val incrementDate= date?.let { incrementDateByOne(it) }
                val dateInString = incrementDate?.toString("dd MMMM yyyy")
                viewModel.currentDate.set(dateInString)
            }

        }
    }
    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        navigateTo(context!!,clazz, mExtras)
    }

    /*override fun onDestroy() {
        super.onDestroy()
        viewModel?.activitiesNavigationIndex?.value=0
    }*/

    /*private fun setUpFragmentObserver() {
        viewModel?.fragmentNavigationIndex?.observe(this, Observer {
            when(it){
                1 -> {

                    //replaceFragment(R.id.activity_container, MealWeeklyDetailFragment(),"MealWeeklyDetail","MealWeeklyDetail")
                    viewModel?.isReject?.set(true)
                }
            }
        } )
    }*/

}
