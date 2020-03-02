package com.colan.kindercare.ui.dashboard.fragments.all_activity.meal


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.adapter.BaseRecyclerViewAdapter
import com.colan.kindercare.adapter.OnDataBindCallback
import com.colan.kindercare.data.model.MealModel
import com.colan.kindercare.databinding.FragmentMealBinding
import com.colan.kindercare.databinding.MealListItemsBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.dashboard.fragments.all_activity.AllActivityViewModel

/**
 * A simple [Fragment] subclass.
 */
class MealFragment : BaseFragment<FragmentMealBinding, AllActivityViewModel>(), BaseNavigator,
    OnDataBindCallback<MealListItemsBinding> {


    private var baseRecyclerAdapter: BaseRecyclerViewAdapter<MealModel, MealListItemsBinding>? = null

    override val bindingVariable: Int
        get() = BR.allActivityVM
    override val layoutId: Int
        get() = R.layout.fragment_meal
    override val viewModel: AllActivityViewModel
        get() = ViewModelProviders.of(this).get(AllActivityViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        setUpRecyclerview()
        observeResponse()
    }

    private fun observeResponse() {
        viewModel.mealModel.observe(this, Observer {
            viewDataBinding?.mealRv?.adapter=baseRecyclerAdapter
            baseRecyclerAdapter?.addDataSet(it)
            baseRecyclerAdapter?.notifyDataSetChanged()
        })
    }

    private fun setUpRecyclerview() {
        baseRecyclerAdapter = BaseRecyclerViewAdapter(
            R.layout.meal_list_items,
            BR.mealItem,
            ArrayList(),
            null,
            this
        )
    }


    override fun onItemClick(view: View, position: Int, v: MealListItemsBinding) {
    }

    override fun onDataBind(v: MealListItemsBinding, onClickListener: View.OnClickListener) {
    }


    override fun onClickView(v: View?) {
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
    }


}
