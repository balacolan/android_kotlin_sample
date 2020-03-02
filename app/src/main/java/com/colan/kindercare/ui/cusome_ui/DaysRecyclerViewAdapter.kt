package com.colan.kindercare.ui.cusome_ui

import android.content.Context
import android.graphics.Typeface

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.colan.kindercare.R
import com.colan.kindercare.ui.cusome_ui.models.Day
import com.colan.kindercare.ui.cusome_ui.models.YearMonth

import java.util.ArrayList


class DaysRecyclerViewAdapter(private val mContext: Context, private val mDays: ArrayList<Day>, private val mOnDaySelectListener: PersianDatePicker.OnDaySelectListener?) : RecyclerView.Adapter<DaysRecyclerViewAdapter.ViewHolder>() {
    private var yearMonth: YearMonth? = null
    private var selectedPosition = -1
    private var selectedItemBackgroundColor: Int = 0
    private var selectedItemBackground = 0
    private var selectedItemTextColor: Int = 0
    private var defaultItemBackgroundColor: Int = 0
    private var defaultItemTextColor: Int = 0
    private var typeface: Typeface? = null
    private var elevation: Float = 0.toFloat()
    private var radius: Float = 0.toFloat()
    private var hasAnimation: Boolean = false

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var mDay: TextView
        var mWeekDay: TextView
        var mRoot: LinearLayout

        init {
            itemView.setOnClickListener(this)
            mDay = itemView.findViewById(R.id.day)
            mWeekDay = itemView.findViewById(R.id.week_day)
            mRoot = itemView.findViewById(R.id.root)

            //mRoot.cardElevation = elevation
            //mRoot.radius = radius

            if (typeface != null) {
                mDay.typeface = typeface
                mWeekDay.typeface = typeface
            }
        }

        fun setItem(day: Day) {
            bind(day)
        }

        private fun bind(day: Day) {
            mDay.text = day.number.toString()
            mWeekDay.text = day.day

            if (adapterPosition == selectedPosition) {

                mDay.setTextColor(mContext.resources.getColor(selectedItemTextColor))
                mWeekDay.setTextColor(mContext.resources.getColor(selectedItemTextColor))
                if (selectedItemBackground != 0)
                    mRoot.setBackgroundResource(R.drawable.ic_weekly_bg)
                //mRoot.background = mContext.resources.getDrawable(selectedItemBackground)
                else
                    mRoot.setBackgroundResource(R.drawable.ic_weekly_bg)
                    //mRoot.setBackgroundColor(mContext.resources.getColor(selectedItemBackgroundColor))

            } else {

                //                if (selectedItemBackground == 0)
                mRoot.setBackgroundColor(mContext.resources.getColor(defaultItemBackgroundColor))
                //                else
                //                    mRoot.setCardBackgroundColor( mContext.getResources().getColor( selectedItemBackgroundColor ) );

                mDay.setTextColor(mContext.resources.getColor(defaultItemTextColor))
                mWeekDay.setTextColor(mContext.resources.getColor(defaultItemTextColor))

            }
        }

        override fun onClick(v: View) {
            val prevSelectPosition = selectedPosition
            selectedPosition = adapterPosition
            val day = mDays[selectedPosition]
            if (hasAnimation) {
                notifyItemChanged(selectedPosition)
                notifyItemChanged(prevSelectPosition)
            } else
                notifyDataSetChanged()
            mOnDaySelectListener?.onDaySelect(yearMonth!!, day)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.day_item_layout, parent, false)
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val day = mDays[position]
        holder.setItem(day)

    }

    override fun getItemCount(): Int {
        return mDays.size
    }

    fun setYearMonth(yearMonth: YearMonth) {
        this.yearMonth = yearMonth
    }

    fun setSelectedItemBackgroundImage(selectedItemBackgroundImage: Int) {
        this.selectedItemBackgroundColor = selectedItemBackgroundImage
    }

    fun setSelectedItemBackgroundColor(selectedItemBackgroundColor: Int) {
        this.selectedItemBackgroundColor = selectedItemBackgroundColor
    }

    fun setSelectedItemBackground(selectedItemBackground: Int) {
        this.selectedItemBackground = selectedItemBackground
    }

    fun setSelectedItemTextColor(selectedItemTextColor: Int) {
        this.selectedItemTextColor = selectedItemTextColor
    }

    fun setTypeface(typeface: Typeface) {
        this.typeface = typeface
    }

    fun setDefaultItemBackgroundColor(defaultItemBackgroundColor: Int) {
        this.defaultItemBackgroundColor = defaultItemBackgroundColor
    }

    fun setDefaultItemTextColor(defaultItemTextColor: Int) {
        this.defaultItemTextColor = defaultItemTextColor
    }

    fun setElevation(elevation: Float) {
        this.elevation = elevation
    }

    fun setRadius(radius: Float) {
        this.radius = radius
    }

    fun setAnimation(animation: Boolean) {
        this.hasAnimation = animation
    }

    fun setSelectionPosition(selectedPosition: Int) {
        this.selectedPosition = selectedPosition
    }
}
