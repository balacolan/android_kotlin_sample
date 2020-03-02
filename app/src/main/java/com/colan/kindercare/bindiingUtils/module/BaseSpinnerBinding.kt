package com.colan.kindercare.bindiingUtils.module

import android.content.Context
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.Spinner
import android.widget.TextView
import com.colan.kindercare.R

import com.colan.kindercare.bindiingUtils.implementaion.ISpinnerBinding

class BaseSpinnerBinding : ISpinnerBinding {


    override fun setAdapter(spinner: Spinner, customLayout: Int, customViewId: Int,
                            customDropDownLayout: Int, customDropDownTextViewId: Int,
                            customHint: String?, dataItems: ArrayList<String>?, customSelectedItemPosition: Int?) {

        if (dataItems != null) {

            if (customHint != null && !dataItems.contains(customHint)) {
                dataItems.add(0, customHint)
            }

            val inflater = LayoutInflater.from(spinner.context)
            spinner.adapter = object : ArrayAdapter<String>(spinner.context, customLayout, dataItems) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent)
                    val textView = view.findViewById<TextView>(customViewId)
                    textView.text = dataItems[position]
                    if (customHint != null && position == 0) {
                        textView.setTextColor(ContextCompat.getColor(spinner.context, R.color.colorGray))
                    }
                    return textView
                }

                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                    super.getView(position, convertView, parent)
                    val view = inflater.inflate(customDropDownLayout, parent, false)
                    val textView = view.findViewById<TextView>(customDropDownTextViewId)
                    textView.text = dataItems[position]
                    return view
                }
            }

            if (customSelectedItemPosition != null && spinner.selectedItemPosition != customSelectedItemPosition) {
                spinner.isSelected = false
                spinner.setSelection(customSelectedItemPosition, true)
            }
        }
    }

    override fun setEnable(spinner: Spinner, enable: Boolean) {
        spinner.isEnabled = enable
    }

    private class Adapter(val context: Context, @LayoutRes val customLayout: Int, @IdRes val customTextViewId: Int,
                          @IdRes val imgItemImageViewId: Int,
                          @LayoutRes val customDropDownLayout: Int, @IdRes val customDropDownTextViewId: Int,
                          val dataItems: ArrayList<String>?,
                          @IdRes val customDropDownImageViewId: Int) : BaseAdapter() {


        val mInflater: LayoutInflater = LayoutInflater.from(context)

        override fun getCount(): Int {
            return dataItems?.size!!
        }

        override fun getItem(position: Int): Any? {
            return null
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view: View
            val vh: ItemRowHolder
            if (convertView == null) {
                view = mInflater.inflate(customLayout, parent, false)
                vh = ItemRowHolder(view, customTextViewId, imgItemImageViewId)
                view.tag = vh
            } else {
                view = convertView
                vh = view.tag as ItemRowHolder
            }
            vh.label.text = dataItems!![position]

            return view
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view: View
            val vh: ItemRowHolder
            if (convertView == null) {
                view = mInflater.inflate(customDropDownLayout, parent, false)
                vh = ItemRowHolder(view, customDropDownTextViewId, customDropDownImageViewId)
                view.tag = vh
            } else {
                view = convertView
                vh = view.tag as ItemRowHolder
            }
            vh.label.text = dataItems!![position]

            return view
        }

        private class ItemRowHolder(row: View?, customDropDownTextViewId: Int, customDropDownImageViewId: Int) {
            val label = row?.findViewById(customDropDownTextViewId) as TextView
        }
    }

}