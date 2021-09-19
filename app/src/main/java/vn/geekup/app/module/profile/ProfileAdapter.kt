package vn.geekup.app.module.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import vn.geekup.app.R
import vn.geekup.app.base.BaseViewItem
import vn.geekup.app.base.list.BaseCallbackDiffUtils
import vn.geekup.app.base.list.BaseItemDiffUtils
import vn.geekup.app.base.list.BaseRecyclerHolder
import vn.geekup.app.base.list.BaseRecyclerViewAdapter
import vn.geekup.app.databinding.ItemUserEngagementBinding
import vn.geekup.app.databinding.ItemUserEventBinding
import vn.geekup.app.databinding.ItemUserIndicatorBinding
import vn.geekup.app.databinding.ItemUserInfoBinding
import vn.geekup.app.model.user.UserEngagementModelV
import vn.geekup.app.model.user.UserEventModelV
import vn.geekup.app.model.user.UserIndicatorModelV
import vn.geekup.app.model.user.UserInfoModelV

class ProfileAdapter(private val onUserItemListener: ((data: BaseViewItem, position: Int, action: ProfileActions) -> Unit)? = null) :
    BaseRecyclerViewAdapter<BaseItemDiffUtils>() {

    inner class UserInfoViewHolder(override val viewBinding: ItemUserInfoBinding) :
        BaseRecyclerHolder<UserInfoModelV>(viewBinding) {
        override fun bindData(data: UserInfoModelV, position: Int) {
            super.bindData(data, position)
            viewBinding.userInfo = data
            viewBinding.tvLogout.setOnClickListener {
                onUserItemListener?.invoke(data, position, ProfileActions.Logout)
            }
        }
    }

    inner class UserIndicatorViewHolder(override val viewBinding: ItemUserIndicatorBinding) :
        BaseRecyclerHolder<UserIndicatorModelV>(viewBinding) {
        override fun bindData(data: UserIndicatorModelV, position: Int) {
            super.bindData(data, position)
            viewBinding.userIndicator = data
            viewBinding.tvSparrowGiveNow.setOnClickListener {
                onUserItemListener?.invoke(data, position, ProfileActions.GiveNow)
            }
            viewBinding.tvAliaLearnMore.setOnClickListener {
                onUserItemListener?.invoke(data, position, ProfileActions.LearMore)
            }
        }
    }

    inner class UserEventViewHolder(override val viewBinding: ItemUserEventBinding) :
        BaseRecyclerHolder<UserEventModelV>(viewBinding) {

        override fun bindData(data: UserEventModelV, position: Int) {
            super.bindData(data, position)
            viewBinding.userEvent = data
            viewBinding.tvHideIndicator.setOnClickListener {
                onUserItemListener?.invoke(data, position, ProfileActions.IndicatorActive)
            }
        }
    }

    inner class UserEngagementViewHolder(override val viewBinding: ItemUserEngagementBinding) :
        BaseRecyclerHolder<UserEngagementModelV>(viewBinding) {
        override fun bindData(data: UserEngagementModelV, position: Int) {
            super.bindData(data, position)
            viewBinding.userEngagement = data
            viewBinding.root.setOnClickListener {
                onUserItemListener?.invoke(data, position, ProfileActions.MomentDetail)
            }
        }
    }

    override fun initialViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerHolder<out BaseViewItem> {
        return when (viewType) {
            R.layout.item_user_info -> {
                val viewBinding = ItemUserInfoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                UserInfoViewHolder(viewBinding)
            }
            R.layout.item_user_indicator -> {
                val viewBinding = ItemUserIndicatorBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                UserIndicatorViewHolder(viewBinding)
            }
            R.layout.item_user_event -> {
                val viewBinding = ItemUserEventBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                UserEventViewHolder(viewBinding)
            }
            else -> {
                val viewBinding = ItemUserEngagementBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                UserEngagementViewHolder(viewBinding)
            }
        }

    }

    override fun provideDiffUtils(
        mOldList: MutableList<BaseViewItem>,
        mNewList: MutableList<BaseViewItem>
    ): BaseCallbackDiffUtils = object : BaseCallbackDiffUtils(mOldList, mNewList) {

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldUserModel = mOldList[oldItemPosition]
            val newUserModel = mNewList[newItemPosition]
            return when {
                oldUserModel is UserInfoModelV && newUserModel is UserInfoModelV -> {
                    oldUserModel == newUserModel
                }
                oldUserModel is UserIndicatorModelV && newUserModel is UserIndicatorModelV -> {
                    oldUserModel.sparrowPoints == newUserModel.sparrowPoints &&
                            oldUserModel.rankingPoints == newUserModel.rankingPoints &&
                            oldUserModel.aliaPoints == newUserModel.aliaPoints
                }
                oldUserModel is UserEngagementModelV && newUserModel is UserEngagementModelV -> {
                    oldUserModel.aliaPoint == newUserModel.aliaPoint &&
                            oldUserModel.createdAt == newUserModel.createdAt &&
                            oldUserModel.momentDescription == newUserModel.momentDescription
                }
                oldUserModel is UserEventModelV && newUserModel is UserEventModelV -> {
                    oldUserModel.isEnableIndicator == newUserModel.isEnableIndicator
                }
                else -> oldUserModel == newUserModel
            }

        }

    }
}