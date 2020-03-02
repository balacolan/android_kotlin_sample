package com.colan.kindercare.ui.modules.teacher.dailyActivity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.adapter.BaseRecyclerViewAdapter
import com.colan.kindercare.adapter.OnDataBindCallback
import com.colan.kindercare.data.model.ChooseActivityModel
import com.colan.kindercare.databinding.ActivityActivtyChoosingBinding
import com.colan.kindercare.databinding.ChooseActivityItemBinding
import com.colan.kindercare.ui.base.BaseActivity
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.modules.teacher.dailyActivity.AllActivity.AllDailyCommonActivty
import com.colan.kindercare.utils.Constants
import com.colan.kindercare.utils.bundle
import com.colan.kindercare.utils.navigateTo
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class ActivtyChoosingActivity : BaseActivity<ActivityActivtyChoosingBinding, DailyActivityVM>(),
    BaseNavigator,OnDataBindCallback<ChooseActivityItemBinding> {

    private var baseRecyclerAdapter: BaseRecyclerViewAdapter<ChooseActivityModel, ChooseActivityItemBinding>? =
        null


    override fun getBindingVariable(): Int {
        return BR.dailyActivityVM
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_activty_choosing
    }

    override fun getViewModel(): DailyActivityVM {
        return ViewModelProviders.of(this).get(DailyActivityVM::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel?.setNavigator(this)
        getViewDataBinding().toolbar.title_toolbar.text=getString(R.string.choose_activty)
        getViewDataBinding().toolbar.nav_icon_iv.setOnClickListener { onBackPressed() }
        setUpRecyclerview()
        observeResponse()
    }

    private fun observeResponse() {
        mViewModel?.activityModel?.observe(this, Observer {
            getViewDataBinding().chooseActivityRv?.adapter = baseRecyclerAdapter
            baseRecyclerAdapter?.addDataSet(it)
            baseRecyclerAdapter?.notifyDataSetChanged()
        })
    }

    private fun setUpRecyclerview() {
        baseRecyclerAdapter = BaseRecyclerViewAdapter(
            R.layout.choose_activity_item,
            BR.activityItem,
            ArrayList(),
            null,
            this
        )
    }

    override fun onItemClick(view: View, position: Int, v: ChooseActivityItemBinding) {
        when(view){
            v.rootLayout->{
                if(position!=1){
                    bundle.clear()
                    bundle.putString(Constants.ACTIVITY_TYPE,baseRecyclerAdapter!!.getItems()[position].Name)
                    goTo(AllDailyCommonActivty::class.java, bundle)
                }
            }
        }
    }

    override fun onDataBind(v: ChooseActivityItemBinding, onClickListener: View.OnClickListener) {
        v.rootLayout.setOnClickListener(onClickListener)
    }


    override fun onClickView(v: View?) {
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        navigateTo(this,clazz,mExtras)
    }
}
