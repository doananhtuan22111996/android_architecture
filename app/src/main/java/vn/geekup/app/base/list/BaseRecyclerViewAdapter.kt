package vn.geekup.app.base.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.geekup.app.R
import vn.geekup.app.base.BaseViewItem
import vn.geekup.app.databinding.ItemPagingStateBinding
import vn.geekup.app.model.PagingState
import androidx.recyclerview.widget.DiffUtil

abstract class BaseRecyclerViewAdapter<out DU : BaseItemDiffUtils>() :
    RecyclerView.Adapter<BaseRecyclerHolder<BaseViewItem>>() {

    private var viewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()
//    private val differ: AsyncListDiffer<BaseViewItem> = AsyncListDiffer(this, provideDiffUtils())
    private var networkState: PagingState? = null
    private var currentList = mutableListOf<BaseViewItem>()

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) R.layout.item_paging_state
        else getItemAt(position).getViewType()
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(true)
    }

    abstract fun initialViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerHolder<out BaseViewItem>

    abstract fun provideDiffUtils(mOldList: MutableList<BaseViewItem>, mNewList : MutableList<BaseViewItem>): BaseCallbackDiffUtils

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerHolder<BaseViewItem> {
        viewPool.setMaxRecycledViews(viewType, 5)
        return when (viewType) {
            R.layout.item_paging_state -> {
                val viewBinding = ItemPagingStateBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                PagingStateViewModel(viewBinding)
            }
            else -> initialViewHolder(parent, viewType) as BaseRecyclerHolder<BaseViewItem>
        }
    }

    override fun onBindViewHolder(holder: BaseRecyclerHolder<BaseViewItem>, position: Int) {
        holder.bindData(getItemAt(position), position)
    }

    override fun onBindViewHolder(
        holder: BaseRecyclerHolder<BaseViewItem>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
            return
        }
        holder.bindData(payloads[0] as BaseViewItem, position)
    }

    fun addAllItemsWithDiffUtils(newList: List<BaseViewItem>) {
        val diffCallback = provideDiffUtils(this.currentList, newList.toMutableList())
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.currentList.clear()
        this.currentList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setItemPositionChanged(item : BaseViewItem, position: Int) {
        currentList[position] = item
        notifyItemChanged(position)
    }

    fun setNetworkState(newNetworkState: PagingState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(itemCount)
            } else {
                notifyItemInserted(itemCount)
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    private fun getItemAt(position: Int): BaseViewItem {
        return currentList[position]
    }

    private fun hasExtraRow() = networkState != null && networkState == PagingState.LoadingMore

}