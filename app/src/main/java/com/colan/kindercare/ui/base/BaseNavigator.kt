package com.colan.kindercare.ui.base

import android.os.Bundle
import android.view.View

/**
 *
 */
interface BaseNavigator {
    fun onClickView(v: View?)
    fun goTo(clazz: Class<*>, mExtras: Bundle?)
}