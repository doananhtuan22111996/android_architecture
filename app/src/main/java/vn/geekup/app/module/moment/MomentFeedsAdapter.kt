package vn.geekup.app.module.moment

import android.view.LayoutInflater
import android.view.ViewGroup
import vn.geekup.app.base.BaseViewItem
import vn.geekup.app.base.list.BaseCallbackDiffUtils
import vn.geekup.app.base.list.BaseItemDiffUtils
import vn.geekup.app.base.list.BaseRecyclerHolder
import vn.geekup.app.base.list.BaseRecyclerViewAdapter
import vn.geekup.app.databinding.*
import vn.geekup.app.model.moment.MomentActionV
import vn.geekup.app.model.moment.MomentModelV

class MomentFeedsAdapter(
    private val listener: ((data: MomentModelV, position: Int, action: MomentActionV) -> Unit)? = null,
    private val onClickSeeMoreListener: ((MomentModelV?) -> Unit)? = null,
    private val onClickLinkListener: ((url: String) -> Unit)? = null,
) : BaseRecyclerViewAdapter<BaseItemDiffUtils>() {

    inner class MomentFeed1ImageViewHolder(override val viewBinding: ItemMomentFeed1ImageBinding) :
        BaseRecyclerHolder<MomentModelV>(viewBinding) {

        override fun bindData(data: MomentModelV, position: Int) {
            super.bindData(data, position)
            data.onClickSeeMoreListener = onClickSeeMoreListener
            data.onClickLinkListener = onClickLinkListener
            viewBinding.moment = data
            viewBinding.root.setOnClickListener {
                listener?.invoke(data, position, MomentActionV.MomentDetail)
            }
            viewBinding.tvContent.setOnClickListener {
                listener?.invoke(data, position, MomentActionV.MomentDetail)
            }
            viewBinding.layoutMomentOneImage.root.setOnClickListener {
                listener?.invoke(data, position, MomentActionV.MomentPreview)
            }
        }
    }

    override fun initialViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerHolder<out BaseViewItem> {

        val viewBinding = ItemMomentFeed1ImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MomentFeed1ImageViewHolder(viewBinding)
    }

    override fun provideDiffUtils(
        mOldList: MutableList<BaseViewItem>,
        mNewList: MutableList<BaseViewItem>
    ): BaseCallbackDiffUtils = object : BaseCallbackDiffUtils(mOldList, mNewList) {
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldMoment = mOldList[oldItemPosition] as? MomentModelV
            val newMoment = mNewList[newItemPosition] as? MomentModelV
            return oldMoment?.content == newMoment?.content &&
                    oldMoment?.createdAt == newMoment?.createdAt &&
                    oldMoment?.imgUrls == newMoment?.imgUrls &&
                    oldMoment?.likeCount == newMoment?.likeCount &&
                    oldMoment?.commentCount == newMoment?.commentCount &&
                    oldMoment?.thisUserLiked == newMoment?.thisUserLiked &&
                    oldMoment?.thisUserCommented == newMoment?.thisUserCommented
        }

    }

}
