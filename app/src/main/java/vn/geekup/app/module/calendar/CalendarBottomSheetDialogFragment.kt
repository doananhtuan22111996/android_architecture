package vn.geekup.app.module.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import vn.geekup.app.R
import vn.geekup.app.databinding.FragmentBottomSheetCalendarBinding
import java.util.*

class CalendarBottomSheetDialogFragment(
    private val onDateSelectedListener: ((day: Int, month: Int, year: Int) -> Unit)? = null,
    private val onResetSelectedListener: (() -> Unit)? = null
) : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "CalendarBottomSheetDialogFragment"
    }

    private var fragmentBinding: FragmentBottomSheetCalendarBinding? = null
    private var calendar = Calendar.getInstance()

    override fun getTheme(): Int {
        return R.style.Theme_MaterialComponents_DayNight_BottomSheetDialog_App
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentBinding = FragmentBottomSheetCalendarBinding.inflate(inflater, container, false)
        return fragmentBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentBinding?.fragment = this
        initCalendar()
    }

    fun onClickClose() {
        dismissAllowingStateLoss()
    }

    fun onClickReset() {
        setNewCalendar(
            day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
            month = Calendar.getInstance().get(Calendar.MONTH),
            year = Calendar.getInstance().get(Calendar.YEAR)
        )
        fragmentBinding?.vCalendar?.date = calendar.timeInMillis
        onResetSelectedListener?.invoke()
        dismissAllowingStateLoss()
    }

    private fun initCalendar() {
        fragmentBinding?.vCalendar?.date = calendar.timeInMillis
        fragmentBinding?.vCalendar?.setOnDateChangeListener { _, year, month, day ->
            setNewCalendar(day, month, year)
            fragmentBinding?.vCalendar?.date = calendar.timeInMillis
            onDateSelectedListener?.invoke(day, month + 1, year)
            this.dismissAllowingStateLoss()
        }
    }

    private fun setNewCalendar(day: Int, month: Int, year: Int) {
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.YEAR, year)
    }

}
