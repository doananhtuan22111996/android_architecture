package vn.geekup.app.module.moment.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import vn.geekup.app.base.BaseViewItem
import vn.geekup.app.base.list.BaseCallbackDiffUtils
import vn.geekup.app.base.list.BaseItemDiffUtils
import vn.geekup.app.base.list.BaseRecyclerHolder
import vn.geekup.app.base.list.BaseRecyclerViewAdapter
import vn.geekup.app.databinding.ItemMomentCommentBinding
import vn.geekup.app.model.moment.MomentCommentModelV

class MomentCommentAdapter : BaseRecyclerViewAdapter<BaseItemDiffUtils>() {

    inner class MomentCommentViewHolder(override val viewBinding: ItemMomentCommentBinding) :
        BaseRecyclerHolder<MomentCommentModelV>(viewBinding) {

        override fun bindData(data: MomentCommentModelV, position: Int) {
            super.bindData(data, position)
            viewBinding.momentComment = data
        }
    }

    override fun initialViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerHolder<out BaseViewItem> {
        val viewBinding = ItemMomentCommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MomentCommentViewHolder(viewBinding)
    }

    override fun provideDiffUtils(
        mOldList: MutableList<BaseViewItem>,
        mNewList: MutableList<BaseViewItem>
    ): BaseCallbackDiffUtils = object : BaseCallbackDiffUtils(mOldList, mNewList) {

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldModel = mOldList[oldItemPosition] as? MomentCommentModelV
            val newModel = mNewList[newItemPosition] as? MomentCommentModelV
            return oldModel?.content == newModel?.content &&
                    oldModel?.createdAt == newModel?.createdAt
        }

    }
}