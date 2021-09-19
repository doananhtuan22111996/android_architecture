package vn.geekup.app.model.user

import vn.geekup.app.R
import vn.geekup.app.base.BaseViewItem

data class UserEventModelV(var isEnableIndicator: Boolean = true) : BaseViewItem {

    override fun getViewType(): Int = R.layout.item_user_event

    override fun getItemId(): String = "user_event_id"
}