package vn.geekup.app.base

interface BaseViewItem {
  fun getViewType(): Int
  fun getItemId(): String
}