package com.colan.kindercare.ui.cusome_ui

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colan.kindercare.R
import com.colan.kindercare.ui.cusome_ui.models.Day
import com.colan.kindercare.ui.cusome_ui.models.YearMonth
import java.util.ArrayList


class PersianDatePicker : LinearLayout, View.OnClickListener {

    private var leftArrow: ImageView? = null
    private var rightArrow: ImageView? = null
    private var mYearMonthText: TextView? = null
    private var mDaysRecyclerView: RecyclerView? = null
    private var mYearMonths: ArrayList<YearMonth>? = null
    private var mOnDaySelectListener: OnDaySelectListener? = null
    private var typeface: Typeface? = null
    private var selectedPosition = -1
    //private var elevation = 0.0f
    var radius = 0f
    private var selectedItemBackgroundColor = R.drawable.ic_weekly_bg
    private var selectedItemBackground = 0
    private var selectedItemTextColor =  R.drawable.ic_weekly_bg
    private var defaultItemBackgroundColor =  R.drawable.ic_weekly_bg
    private var defaultItemTextColor =  R.drawable.ic_weekly_bg
    private var mYearMonthIndex = 0
    private var hasAnimation = false



    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    private fun init(context: Context, attributeSet: AttributeSet?) {
        View.inflate(getContext(), R.layout.horizontal_date_picker, this)

        this.leftArrow = findViewById(R.id.date_previous_iv)
        this.rightArrow = findViewById(R.id.date_next_iv)
        this.mYearMonthText = findViewById(R.id.date_tv)
        this.mDaysRecyclerView = findViewById(R.id.days_recycler_view)

        leftArrow!!.setOnClickListener(this)
        rightArrow!!.setOnClickListener(this)
    }

    fun setYearMonths(yearMonths: ArrayList<YearMonth>): PersianDatePicker {
        this.mYearMonths = yearMonths
        return this
    }

    fun setTypeFace(typeface: Typeface): PersianDatePicker {
        this.mYearMonthText!!.typeface = typeface
        this.typeface = typeface
        return this
    }

    fun setTitleTextColor(@ColorRes color: Int): PersianDatePicker {
        this.mYearMonthText!!.setTextColor(resources.getColor(color))
        return this
    }

    fun setTitleTextSize(size: Int): PersianDatePicker {
        this.mYearMonthText!!.textSize = size.toFloat()
        return this
    }

    fun setArrowDrawable(@DrawableRes drawable: Int): PersianDatePicker {
        this.leftArrow!!.setImageDrawable(resources.getDrawable(drawable))
        this.rightArrow!!.setImageDrawable(resources.getDrawable(drawable))
        return this
    }

    fun setSelectedItemBackgroundColor(@ColorRes color: Int): PersianDatePicker {
        this.selectedItemBackgroundColor = color
        return this
    }

    fun setSelectedItemBackground(@DrawableRes background: Int): PersianDatePicker {
        this.selectedItemBackground = background
        return this
    }

    fun setSelectedItemTextColor(@ColorRes color: Int): PersianDatePicker {
        this.selectedItemTextColor = color
        return this
    }

    fun setDefaultItemBackgroundColor(@ColorRes color: Int): PersianDatePicker {
        this.defaultItemBackgroundColor = color
        return this
    }

    fun setDefaultItemTextColor(@ColorRes color: Int): PersianDatePicker {
        this.defaultItemTextColor = color
        return this
    }

    fun setListener(onDaySelectListener: OnDaySelectListener): PersianDatePicker {
        mOnDaySelectListener = onDaySelectListener
        return this
    }

    fun setItemElevation(elevation: Float): PersianDatePicker {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.elevation = elevation
        }
        return this
    }

    fun setItemRadius(radius: Float): PersianDatePicker {
        this.radius = radius
        return this
    }

    fun hasAnimation(hasAnimation: Boolean): PersianDatePicker {
        this.hasAnimation = hasAnimation
        return this
    }

    fun load() {
        if (this.mYearMonths!!.size == 0)
            return
        setupView(mYearMonthIndex)
    }

    private fun setupView(index: Int) {

        val yearMonth = this.mYearMonths!![index]
        mYearMonthText!!.text = getTitle(yearMonth)
        val adapter = DaysRecyclerViewAdapter(context, yearMonth.days, this.mOnDaySelectListener)
        adapter.setSelectedItemBackgroundColor(this.selectedItemBackgroundColor)
        adapter.setSelectedItemBackground(this.selectedItemBackground)
        adapter.setSelectedItemTextColor(this.selectedItemTextColor)
        adapter.setDefaultItemBackgroundColor(this.defaultItemBackgroundColor)
        adapter.setDefaultItemTextColor(this.defaultItemTextColor)
        this.typeface?.let { adapter.setTypeface(it) }
        adapter.setYearMonth(yearMonth)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            adapter.setElevation(this.elevation)
        }
        adapter.setRadius(this.radius)
        adapter.setAnimation(this.hasAnimation)
        adapter.setSelectionPosition(selectedPosition)
        this.mDaysRecyclerView!!.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        this.mDaysRecyclerView!!.layoutManager = linearLayoutManager
    }

    private fun getTitle(yearMonth: YearMonth): String {
        return yearMonth.year.toString() + " " + yearMonth.month
    }

    fun setItemSelected(date: String): Int {
        val strings = date.split("-".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        if (strings.size != 3)
            throw IllegalArgumentException("date must divided by -")
        val year = Integer.valueOf(strings[0])
        val day = Integer.valueOf(strings[2])
        val index = findYearMonthIndex(year, strings[1])
        if (index != -1) {
            mYearMonthIndex = index
            selectedPosition = findDayIndex(mYearMonths!![index], day)
            setupView(mYearMonthIndex)
        }
        return selectedPosition
    }

    fun scrollToPosition(pos: Int) {
        mDaysRecyclerView!!.scrollToPosition(pos)
    }

    private fun findYearMonthIndex(year: Int, month: String): Int {
        for (i in mYearMonths!!.indices) {
            if (mYearMonths!![i].year == year && mYearMonths!![i].monthNumber == month)
                return i
        }
        return -1
    }

    private fun findDayIndex(yearMonth: YearMonth, day: Int): Int {
        for (i in 0 until yearMonth.days.size) {
            if (yearMonth.days[i].number == day)
                return i
        }
        return -1
    }

    override fun onClick(v: View) {
        val i = v.id
        if (i == R.id.date_previous_iv) {
            gotoNextMonth()

        } else if (i == R.id.date_next_iv) {
            gotoPreviousMonth()

        }
    }

    private fun gotoNextMonth() {
        if (mYearMonthIndex == 0)
            return
        setupView(--mYearMonthIndex)
    }

    private fun gotoPreviousMonth() {
        if (mYearMonthIndex == mYearMonths!!.size - 1)
            return
        setupView(++mYearMonthIndex)
    }

    interface OnDaySelectListener {
        fun onDaySelect(yearMonth: YearMonth, day: Day)
    }


}
