package vn.geekup.app.base.list

import android.view.View
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import vn.geekup.app.base.BaseViewItem

interface OnItemListener {
    fun onItemClickerListener(action: Int, item: BaseViewItem, view: View, position: Int): Boolean
}

abstract class BaseRecyclerHolder<T : BaseViewItem>(
    protected open val viewBinding: ViewDataBinding,
) : RecyclerView.ViewHolder(viewBinding.root) {

    protected lateinit var data: T
    private var itemPosition: Int = 0

    @CallSuper
    open fun bindData(data: T, position: Int) {
        this.data = data
        this.itemPosition = position
    }

//    protected fun invokeAction(action: Int): Boolean {
//        return listener?.invoke(action, data, itemView, itemPosition) ?: false
//    }
}