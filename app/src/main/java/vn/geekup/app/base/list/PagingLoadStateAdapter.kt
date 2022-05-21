package vn.geekup.app.base.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import vn.geekup.app.base.BaseViewItem
import vn.geekup.app.databinding.ItemMomentFeed1ImageBinding
import vn.geekup.app.databinding.ItemPagingStateBinding
import vn.geekup.app.utils.visible

class PagingLoadStateAdapter : LoadStateAdapter<PagingLoadStateAdapter.PagingStateViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PagingStateViewHolder {

        val viewBinding = ItemPagingStateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PagingStateViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: PagingStateViewHolder, loadState: LoadState) {
        holder.bindData()
    }

    inner class PagingStateViewHolder(val viewBinding: ItemPagingStateBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bindData() {
            viewBinding.rlParentProgressbar.visible(true)
        }
    }
}


