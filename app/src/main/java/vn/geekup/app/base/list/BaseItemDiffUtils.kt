package vn.geekup.app.base.list

import androidx.recyclerview.widget.DiffUtil
import vn.geekup.app.base.BaseViewItem

abstract class BaseItemDiffUtils : DiffUtil.ItemCallback<BaseViewItem>() {
  
  override fun areItemsTheSame(oldItem: BaseViewItem, newItem: BaseViewItem): Boolean {
    return oldItem.getItemId() == newItem.getItemId() && oldItem.getViewType() == newItem.getViewType()
  }
  
  override fun getChangePayload(oldItem: BaseViewItem, newItem: BaseViewItem): Any? {
    return newItem
  }
}