package vn.geekup.app.pages.moment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import vn.geekup.app.databinding.ItemMomentFeed1ImageBinding
import vn.geekup.domain.model.moment.MomentModel

class MomentFeedPagingAdapter :
    PagingDataAdapter<MomentModel, MomentFeedPagingAdapter.MomentFeedPagingViewHolder>(
        POST_COMPARATOR
    ) {

    companion object {
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<MomentModel>() {
            override fun areContentsTheSame(oldItem: MomentModel, newItem: MomentModel): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: MomentModel, newItem: MomentModel): Boolean =
                oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MomentFeedPagingViewHolder {
        val viewBinding = ItemMomentFeed1ImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MomentFeedPagingViewHolder(viewBinding)
    }

    inner class MomentFeedPagingViewHolder(val viewBinding: ItemMomentFeed1ImageBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bindData(data: MomentModel?) {
            viewBinding.moment = data
        }
    }

    override fun onBindViewHolder(holder: MomentFeedPagingViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }
}