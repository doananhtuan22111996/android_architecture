package vn.geekup.app.module.moment.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.ponnamkarthik.richlinkpreview.MetaData
import vn.geekup.app.R
import vn.geekup.app.base.BaseViewItem
import vn.geekup.app.base.list.BaseCallbackDiffUtils
import vn.geekup.app.base.list.BaseItemDiffUtils
import vn.geekup.app.base.list.BaseRecyclerHolder
import vn.geekup.app.base.list.BaseRecyclerViewAdapter
import vn.geekup.app.databinding.*
import vn.geekup.app.model.moment.MomentActionV
import vn.geekup.app.model.moment.MomentModelV
import vn.geekup.app.module.moment.executingMomentLinkPreview

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
            viewBinding.linkPreview = MetaData()
            executingMomentLinkPreview(data) {
                viewBinding.linkPreview = it
            }
            viewBinding.moment = data
            viewBinding.root.setOnClickListener {
                listener?.invoke(data, position, MomentActionV.MomentDetail)
            }
            viewBinding.tvContent.setOnClickListener {
                listener?.invoke(data, position, MomentActionV.MomentDetail)
            }
            viewBinding.layoutLinkPreview.root.setOnClickListener {
                onClickLinkListener?.invoke(viewBinding.linkPreview?.url ?: "")
            }
            viewBinding.layoutMomentFooter.btnLikes.setOnClickListener {
                listener?.invoke(data, position, MomentActionV.MomentLike)
            }
            viewBinding.layoutMomentFooter.btnShares.setOnClickListener {
                listener?.invoke(data, position, MomentActionV.MomentShare)
            }
            viewBinding.layoutMomentFooter.btnComments.setOnClickListener {
                listener?.invoke(data, position, MomentActionV.MomentDetail)
            }
            viewBinding.layoutMomentOneImage.root.setOnClickListener {
                listener?.invoke(data, position, MomentActionV.MomentPreview)
            }
        }
    }

    inner class MomentFeed2ImagesViewHolder(override val viewBinding: ItemMomentFeed2ImagesBinding) :
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
            viewBinding.layoutMomentFooter.btnLikes.setOnClickListener {
                listener?.invoke(data, position, MomentActionV.MomentLike)
            }
            viewBinding.layoutMomentFooter.btnShares.setOnClickListener {
                listener?.invoke(data, position, MomentActionV.MomentShare)
            }
            viewBinding.layoutMoment2Images.root.setOnClickListener {
                listener?.invoke(data, position, MomentActionV.MomentPreview)
            }
        }
    }

    inner class MomentFeed3ImagesViewHolder(override val viewBinding: ItemMomentFeed3ImagesBinding) :
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
            viewBinding.layoutMomentFooter.btnLikes.setOnClickListener {
                listener?.invoke(data, position, MomentActionV.MomentLike)
            }
            viewBinding.layoutMomentFooter.btnShares.setOnClickListener {
                listener?.invoke(data, position, MomentActionV.MomentShare)
            }
            viewBinding.layoutMoment3Images.root.setOnClickListener {
                listener?.invoke(data, position, MomentActionV.MomentPreview)
            }
        }
    }

    inner class MomentFeedMoreImagesViewHolder(override val viewBinding: ItemMomentFeedMoreImagesBinding) :
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
            viewBinding.layoutMomentFooter.btnLikes.setOnClickListener {
                listener?.invoke(data, position, MomentActionV.MomentLike)
            }
            viewBinding.layoutMomentFooter.btnShares.setOnClickListener {
                listener?.invoke(data, position, MomentActionV.MomentShare)
            }
            viewBinding.layoutMomentMoreImages.root.setOnClickListener {
                listener?.invoke(data, position, MomentActionV.MomentPreview)
            }
        }
    }

    override fun initialViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerHolder<out BaseViewItem> {
        return when (viewType) {
            R.layout.item_moment_feed_2_images -> {
                val viewBinding = ItemMomentFeed2ImagesBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                MomentFeed2ImagesViewHolder(viewBinding)
            }
            R.layout.item_moment_feed_3_images -> {
                val viewBinding = ItemMomentFeed3ImagesBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                MomentFeed3ImagesViewHolder(viewBinding)
            }
            R.layout.item_moment_feed_more_images -> {
                val viewBinding = ItemMomentFeedMoreImagesBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                MomentFeedMoreImagesViewHolder(viewBinding)
            }
            else -> {
                val viewBinding = ItemMomentFeed1ImageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                MomentFeed1ImageViewHolder(viewBinding)
            }
        }
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
