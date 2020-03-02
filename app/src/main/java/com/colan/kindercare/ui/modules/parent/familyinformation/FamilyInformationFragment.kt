package com.colan.kindercare.ui.modules.parent.familyinformation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR

import com.colan.kindercare.R
import com.colan.kindercare.databinding.FragmentFamilyInformationBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.dashboard.IToolbar
import com.colan.kindercare.utils.Singleton


class FamilyInformationFragment :
    BaseFragment<FragmentFamilyInformationBinding, FamilyInformationVM>(), BaseNavigator {

    private lateinit var mToobarManagner: IToolbar

    override val bindingVariable: Int
        get() = BR.familyInformationVM
    override val layoutId: Int
        get() = R.layout.fragment_family_information
    override val viewModel: FamilyInformationVM
        get() = activity?.run {
            ViewModelProviders.of(this)[FamilyInformationVM::class.java]
        } ?: throw Exception("Invalid Activity")

    override fun onClickView(v: View?) {
        when (v!!.id) {
            R.id.tvAuthorizedPerson -> {
                goTo(AuthorizedPickupActivity::class.java, null)
            }
        }
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        val intent = Intent(activity, clazz)
        startActivity(intent)
        activity!!.overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        viewModel.setNavigator(this)
        mToobarManagner.changeToolbarTitle(getString(R.string.family_info))
        return viewDataBinding?.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Singleton.isDashBoardFragmentVisible =true
        mToobarManagner = context as IToolbar
    }

}
